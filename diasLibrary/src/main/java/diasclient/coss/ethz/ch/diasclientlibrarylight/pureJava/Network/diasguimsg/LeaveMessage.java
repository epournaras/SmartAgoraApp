package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg;

public class LeaveMessage extends GUIMessage
{
	
	public LeaveMessage(SensorAgentDescription 	SensorAgent, SensorDescription Sensor )
	{
		// initialise the parent class
		super("Leave", SensorAgent, Sensor);
				
	}
	
	
	
}
