package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg;

public class JoinMessage extends GUIMessage
{
	
	public JoinMessage(SensorAgentDescription 	SensorAgent, SensorDescription Sensor )
	{
		// initialise the parent class
		super("Join", SensorAgent, Sensor);
				
	}
	
	
	
}
