package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg;

public class PeerHelloMessage extends GUIMessage
{
	
	final public Integer peerId;
	
	public PeerHelloMessage(SensorAgentDescription 	SensorAgent, Integer peerId )
	{
		// initialise the parent class
		super("PeerHello", SensorAgent, null);
		
		this.peerId = peerId;
				
	}
	
	
	
}
