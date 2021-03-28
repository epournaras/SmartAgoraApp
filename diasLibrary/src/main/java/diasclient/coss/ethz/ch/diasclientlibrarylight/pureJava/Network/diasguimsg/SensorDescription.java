package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg;

public class SensorDescription 
{

	final public String sensorType;		// e.g gps, accel, gyro, etc
	
	final public String SensorName;		// unique identifier of the sensor within the scope of the SensorAgent
		
	public SensorDescription(String SensorName, String sensorType )
	{
		this.SensorName = SensorName;
		this.sensorType = sensorType;
				
	}

	public String toString()
	{
		return SensorName;
	}
}
