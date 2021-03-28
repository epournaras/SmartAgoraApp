package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg;

public class SensorDestinationMessage extends SensorCommandMessage
{
	
	// specify destinations: the sensor agent, and the sensor
	final public	SensorAgentDescription 			destinationSensorAgent;
	
	final public	SensorDescription				destinationSensor;
									
	
	public SensorDestinationMessage(SensorAgentDescription 	SensorAgent, SensorDescription Sensor, SensorAgentDescription destinationSensorAgent, SensorDescription destinationSensor )
	{
		// initialise the parent class; SensorDestination
		super( "Command", SensorAgent, Sensor, "Dest");
				
		this.destinationSensorAgent = destinationSensorAgent;
		this.destinationSensor = destinationSensor;
	}
	 
	public String toString()
	{
		return super.toString() + "|destinationSensorAgent=" + destinationSensorAgent + "|destinationSensor=" + destinationSensor;
	}
	
}
