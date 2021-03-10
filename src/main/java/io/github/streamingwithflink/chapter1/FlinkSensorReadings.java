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
package io.github.streamingwithflink.chapter1;

import io.github.streamingwithflink.util.GapSegment;
import io.github.streamingwithflink.util.SensorReading;
//import io.github.streamingwithflink.util.SensorTimeAssigner;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.io.TextInputFormat;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.source.FileProcessingMode;
import org.apache.flink.streaming.api.functions.windowing.WindowFunction;
import org.apache.flink.streaming.api.windowing.assigners.EventTimeSessionWindows;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;
import org.apache.flink.api.java.tuple.Tuple;

public class FlinkSensorReadings {

    /**
     * main() defines and executes the DataStream program.
     *
     * @param args program arguments
     * @throws Exception
     */

    public static void main(String[] args) throws Exception
    {
        /**
         * Define the .csv file's location and reads via Flink
         */
        String path = "/home/pi/Navio2/Python/output/AccelGyroMag.csv";
        TextInputFormat format = new TextInputFormat(new org.apache.flink.core.fs.Path(path));

        /**
         * Set up streaming execution environment
         */
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime); //Deprecated so we don't need?

        /**
         * Read .csv file continuously
         */
        DataStream<String> sensorData = env.readFile(format, path, FileProcessingMode.PROCESS_CONTINUOUSLY, 100);

        /**
         * Find segments
         */
        DataStream<SensorReading> events = sensorData
                .map((MapFunction<String, SensorReading>) line -> SensorReading.fromString(line));
                //.assignTimestampsAndWatermarks(new SensorTimeAssigner());
        events.keyBy("accelID")
                .window(EventTimeSessionWindows.withGap(Time.seconds(1)))
                .apply(new CreateGapSegment())
                .print();
        /**
         * Execute
         */
        env.execute("Accelerometer, Gyroscope, Magnetometer Sensor Values");
    }

    public static class CreateGapSegment implements WindowFunction<SensorReading, GapSegment, Tuple, TimeWindow>
    {
        @Override
        public void apply(Tuple key, TimeWindow window, Iterable<SensorReading> events, Collector<GapSegment> out)
        {
            out.collect(new GapSegment(events));
        }
    }
}

