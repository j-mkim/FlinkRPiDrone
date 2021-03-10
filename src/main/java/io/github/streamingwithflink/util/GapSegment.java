package io.github.streamingwithflink.util;
import javax.swing.text.Segment;
import java.util.ArrayList;
import java.util.Iterator;

public class GapSegment extends Segment
{
    public GapSegment(Iterable<SensorReading> events)
    {
        ArrayList<SensorReading> list = new ArrayList<SensorReading>();
        for(Iterator<SensorReading> iterator = events.iterator(); iterator.hasNext();)
        {
            SensorReading event = iterator.next();
            list.add(event);
        }
    }
}
