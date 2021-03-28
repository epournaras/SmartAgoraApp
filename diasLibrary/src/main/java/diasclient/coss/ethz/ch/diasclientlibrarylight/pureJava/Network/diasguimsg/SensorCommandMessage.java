package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg;

public class SensorCommandMessage extends GUIMessage
{
	
	final public String Command;
	
	public SensorCommandMessage(String MessageType, SensorAgentDescription 	SensorAgent, SensorDescription Sensor, String Command )
	{
		// initialise the parent class
		super(MessageType, SensorAgent, Sensor);
				
		this.Command = Command;
	
	}
	 
	public String toString()
	{
		return super.toString() + "|" + srcSensor.toString() + "|Command=" + Command;
	}
	
}
