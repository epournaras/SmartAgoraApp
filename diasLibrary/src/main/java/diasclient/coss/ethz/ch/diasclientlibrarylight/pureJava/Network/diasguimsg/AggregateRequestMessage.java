package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg;

public class AggregateRequestMessage extends GUIMessage
{
	public String topicName,					// topic for which data is requested
						aggregateType = "avg";		// the DIAS aggregate being requested (avg, sum, min, max, sumsqr, count, stdev)
	
	public int 			numberDataPoints = 1;		// number of data points to return; >= 1
	
	public String dataFrequency = "Second";	// frequency of the data to be returned; supported values: Second, Minute, Hour, Day
	
	
	public AggregateRequestMessage(SensorAgentDescription 	SensorAgent, String topicName )
	{
		// initialise the parent class
		super("AggregateRequestMessage", SensorAgent, null);
		
		this.topicName = topicName;
				
	}
	
	public AggregateRequestMessage(SensorAgentDescription 	SensorAgent, SensorDescription sensor, String topicName, String aggregateType)
	{
		// initialise the parent class
		super("AggregateRequestMessage", SensorAgent, sensor);
		
		this.topicName = topicName;
		this.aggregateType = aggregateType;
				
	}
	
	public AggregateRequestMessage(SensorAgentDescription 	SensorAgent, String topicName, int numberDataPoints, String dataFrequency )
	{
		// initialise the parent class
		super("AggregateRequestMessage", SensorAgent, null);
		
		this.topicName = topicName;
		this.numberDataPoints = numberDataPoints;
		this.dataFrequency = dataFrequency;
				
	}
	
	
	@Override
	public String toString()
	{
		return super.toString() + "|topic:" + this.topicName + "(" + this.aggregateType + ")," + this.numberDataPoints + " x " + this.dataFrequency + ")";
	}
	
	
}
