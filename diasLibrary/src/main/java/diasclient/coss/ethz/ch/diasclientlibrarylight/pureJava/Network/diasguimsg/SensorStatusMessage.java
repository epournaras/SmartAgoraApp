package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg;

public class SensorStatusMessage extends GUIMessage
{
	
	final public String ControlName,
									ControlValue;
	
	public SensorStatusMessage(SensorAgentDescription 	SensorAgent, SensorDescription Sensor, String ControlName, String ControlValue )
	{
		// initialise the parent class
		super("SensorStatus", SensorAgent, Sensor);
				
		this.ControlName = ControlName;
		this.ControlValue = ControlValue;
	}
	 
	public String toString()
	{
		return super.toString() + "|" + srcSensor.toString() + "|ControlName=" + ControlName + "|ControlValue=" + ControlValue;
	}
	
}
