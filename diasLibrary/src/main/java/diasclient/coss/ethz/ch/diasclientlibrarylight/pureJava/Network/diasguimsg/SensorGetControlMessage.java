package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg;

public class SensorGetControlMessage extends SensorCommandMessage
{
	
	final public String ControlName;
	
	public SensorGetControlMessage(SensorAgentDescription 	SensorAgent, SensorDescription Sensor, String ControlName )
	{
		// initialise the parent class; SensorGetControl
		super( "Command", SensorAgent, Sensor, "Get");
				
		this.ControlName = ControlName;
	}
	 
	public String toString()
	{
		return super.toString() + "|ControlName=" + ControlName;
	}
	
}
