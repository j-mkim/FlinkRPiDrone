/*
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.streamingwithflink.util;

/**
 * POJO to hold sensor reading data.
 */
public class SensorReading {
    public String accelID, gyroID, magID;
    public long timestamp;
    public double accel1, accel2, accel3, gyro1, gyro2, gyro3, mag1, mag2, mag3;


    /**
     * Empty default constructor to satisfy Flink's POJO requirements.
     */
    public SensorReading() { }

    /**
     * .csv file outputs the sensor's name and its three values
     *  Accel: x, y, z
     *  Gyro : x, y, z
     *  Mag  : x, y, z
     * @return the sensor readings
     */
    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(accelID).append(":");
        sb.append(accel1).append(",");
        sb.append(accel2).append(",");
        sb.append(accel3).append(",");
        sb.append(gyroID).append(":");
        sb.append(gyro1).append(",");
        sb.append(gyro2).append(",");
        sb.append(gyro3).append(",");
        sb.append(magID).append(":");
        sb.append(mag1).append(",");
        sb.append(mag2).append(",");
        sb.append(mag3).append(",");

        return sb.toString();
    }

    /**
     * regex (,|;) capturing group
     *      matches comma (,) and semicolon (;)
     * regex \s* matches any whitespace character
     * regex * matches the previous token between zero
     *      and unlimited times, as many times as possible,
     *      giving back as needed
     * @param sb sensor readings from .csv file
     * @return the values read from .csv file
     */
    public static SensorReading fromString(String sb)
    {
        String[] tokens = sb.split("(,|;)\\s*");
        if(tokens.length != 23)
        {
            throw new RuntimeException("Invalid record: " + sb);
        }

        SensorReading event = new SensorReading();

        try
        {
            event.accelID = tokens[0];
            event.accel1 = tokens[1].length() > 0 ? Float.parseFloat(tokens[1]) : 0.0f;
            event.accel2 = tokens[2].length() > 0 ? Float.parseFloat(tokens[2]) : 0.0f;
            event.accel3 = tokens[3].length() > 0 ? Float.parseFloat(tokens[3]) : 0.0f;
            event.gyroID = tokens[4];
            event.gyro1 = tokens[5].length() > 0 ? Float.parseFloat(tokens[1]) : 0.0f;
            event.gyro2 = tokens[6].length() > 0 ? Float.parseFloat(tokens[2]) : 0.0f;
            event.gyro3 = tokens[7].length() > 0 ? Float.parseFloat(tokens[3]) : 0.0f;
            event.magID = tokens[8];
            event.mag1 = tokens[9].length() > 0 ? Float.parseFloat(tokens[1]) : 0.0f;
            event.mag2 = tokens[10].length() > 0 ? Float.parseFloat(tokens[2]) : 0.0f;
            event.mag3 = tokens[11].length() > 0 ? Float.parseFloat(tokens[3]) : 0.0f;
        }catch (NumberFormatException nfe) {
            throw new RuntimeException("Invalid fields: " + sb, nfe);
        }
        return event;
    }
}
