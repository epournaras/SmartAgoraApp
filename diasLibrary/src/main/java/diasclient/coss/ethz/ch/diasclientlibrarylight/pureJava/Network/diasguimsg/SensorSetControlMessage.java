package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg;

public class SensorSetControlMessage extends SensorCommandMessage
{

	// 2017-07-13, agreed with Varin 
	// 1. implemented a LinkedHashMap<String,String> -> multiple controls can be sent in the same message, thereby facilitating REQ/REP communication patterns where
	// only 1 response is possible
	
	final public String ControlName,
									ControlValue;		
	
	 
	
	public SensorSetControlMessage(SensorAgentDescription 	SensorAgent, SensorDescription Sensor, String ControlName, String ControlValue )
	{
		// initialise the parent class; SensorSetControl
		super( "Command", SensorAgent, Sensor, "Set");
				
		this.ControlName = ControlName;
		this.ControlValue = ControlValue;
	}
	 
	public String toString()
	{
		return super.toString() + "|ControlName=" + ControlName + "|ControlValue=" + ControlValue;
	}
	
}
