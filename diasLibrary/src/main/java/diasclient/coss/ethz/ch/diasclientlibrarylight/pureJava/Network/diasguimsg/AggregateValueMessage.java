package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg;

import java.util.ArrayList;

public class AggregateValueMessage extends GUIMessage
{
	
	public String topicName,					// topic of the data, as was specified in the AggregateRequestMessage message
						aggregateType;				// the DIAS aggregate that was requested: avg, sum, min, max, sumsqr, count, stdev
	
	public String dataFrequency = "Second";	// frequency of the data, as was specified in the AggregateRequestMessage message
	
	public ArrayList<Object> data;			// the data
	
	
	public AggregateValueMessage(SensorAgentDescription 	SensorAgent, String topicName )
	{
		// initialise the parent class
		super("AggregateValueMessage", SensorAgent, null);
		
		this.topicName = topicName;
				
	}
	
	public AggregateValueMessage(SensorAgentDescription 	SensorAgent, SensorDescription sensor, String topicName, String aggregateType )
	{
		// initialise the parent class
		super("AggregateValueMessage", SensorAgent, sensor);
		
		this.topicName = topicName;
		this.aggregateType = aggregateType;
				
	}
	
	public AggregateValueMessage(SensorAgentDescription 	SensorAgent, String topicName, String aggregateType, String dataFrequency, ArrayList<Object> data  )
	{
		// initialise the parent class
		super("AggregateValueMessage", SensorAgent, null);
		
		this.topicName = topicName;
		this.aggregateType = aggregateType;
		this.dataFrequency = dataFrequency;

		this.data = (ArrayList<Object>)data.clone();
				
	}
	
	
	@Override
	public String toString()
	{
		return super.toString() + "|topic:" + this.topicName + "(" + this.aggregateType + ")," + this.data.size() + " items)";
	}
	
	
}
