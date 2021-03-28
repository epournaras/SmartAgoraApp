package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg;

import java.util.LinkedHashMap;

public class SensorValueMessage extends GUIMessage
{
	final public LinkedHashMap<String,Object> SensorReading;
	
	public SensorValueMessage(SensorAgentDescription 	SensorAgent, SensorDescription Sensor, LinkedHashMap<String,Object> SensorReading )
	{
		// initialise the parent class
		super("SensorValue", SensorAgent, Sensor);
				
		this.SensorReading = SensorReading;
		
	}
	 
	public String toString()
	{
		return super.toString() + "|" + srcSensor.toString() + "|SensorReading=" + SensorReading.toString();
	}
	
}
