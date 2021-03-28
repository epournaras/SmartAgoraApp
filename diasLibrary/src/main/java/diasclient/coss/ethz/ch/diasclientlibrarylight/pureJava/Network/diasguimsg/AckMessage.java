package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg;

public class AckMessage extends GUIMessage
{
	
	public String ackText;		// either OK or NOK
	
	public String errorText;		// optional error text; should be specified if ackText is NOK
	
	public String acknowledgedMessageType;		// optional: the message type of the message that generated this AckMessage response

	public int 			errorCode = 0; //initialised with 0 (no Error)

	public AckMessage(SensorAgentDescription 	sensorAgent, String ackText, String errorText )
	{
		// initialise the parent class
		super("Ack", sensorAgent, null);
		
		this.ackText = ackText;
		this.errorText = errorText;
				
	}
	
		
	public AckMessage(SensorAgentDescription 	sensorAgent, SensorDescription sensor, String acknowledgedMessageType, String ackText, String errorText )
	{
		// initialise the parent class
		super("Ack", sensorAgent, sensor);
		
		this.ackText = ackText;
		this.errorText = errorText;
		this.acknowledgedMessageType = acknowledgedMessageType;
				
	}

    public AckMessage(SensorAgentDescription	sensorAgent, SensorDescription sensor, String acknowledgedMessageType, String ackText, String errorText,
                      int errorCode )
    {
        //use of constructor above
        this(sensorAgent, sensor, acknowledgedMessageType, ackText, errorText);
        this.errorCode = errorCode;
    }
	
	
	@Override
	public String toString()
	{
		return super.toString() + "|ack:" + this.ackText + "(" + this.errorText + ")";
	}
	
	
}
