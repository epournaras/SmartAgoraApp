package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg;

public class HeartbeatMessage extends GUIMessage
{
	
	public HeartbeatMessage(SensorAgentDescription 	SensorAgent, SensorDescription Sensor )
	{
		// initialise the parent class
		super("Heartbeat", SensorAgent, Sensor);
				
	}
	
	
	
}
