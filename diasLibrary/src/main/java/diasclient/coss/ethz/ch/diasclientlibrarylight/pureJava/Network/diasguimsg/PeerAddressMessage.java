package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg;

public class PeerAddressMessage extends GUIMessage
{
	public Integer peerId;
	
	public String peerFinger;
	
	
	
	public PeerAddressMessage(SensorAgentDescription 	SensorAgent, SensorDescription Sensor, Integer peerId, String peerFinger )
	{
		// initialise the parent class
		super("PeerAddress", SensorAgent, Sensor);
		
		this.peerId = peerId;
		this.peerFinger = peerFinger;
				
	}
	
	
	
}
