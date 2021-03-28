package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg;

public class PeerAddressRequestMessage extends GUIMessage
{
	
	public PeerAddressRequestMessage(SensorAgentDescription 	SensorAgent , SensorDescription Sensor)
	{
		// initialise the parent class
		super("PeerAddressRequest", SensorAgent, Sensor);
				
	}
	
	
	
}
