package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg;

public class LoginMessage extends GUIMessage
{
	
	public LoginMessage(SensorAgentDescription 	SensorAgent, SensorDescription Sensor )
	{
		// initialise the parent class
		super("Login", SensorAgent, Sensor);
				
	}
	
	
	
}
