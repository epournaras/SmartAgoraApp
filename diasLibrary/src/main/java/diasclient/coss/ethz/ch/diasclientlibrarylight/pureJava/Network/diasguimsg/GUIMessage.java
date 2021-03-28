package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GUIMessage 
{
	// need a timestamp
	public String timeStamp;
	
	final public String MessageType;
	
	// src: who is sending the message 
	public final SensorAgentDescription 	srcSensorAgent;
	
	public final SensorDescription 			srcSensor;
	
	// target: to whom the message is sent
	public SensorAgentDescription 			targetSensorAgent;
	
	public SensorDescription 				targetSensor;
	
	
	public GUIMessage(String MessageType, SensorAgentDescription 	SensorAgent, SensorDescription Sensor )
	{
		// timestamp - add eag 2017-03-21
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeStamp = sdf.format(date);
		
		
		this.MessageType = MessageType;
		this.srcSensorAgent = SensorAgent;
		this.srcSensor = Sensor;
		
		// default target is everyone
		this.targetSensorAgent = new SensorAgentDescription( "*", 0 );
		this.targetSensor = new SensorDescription( "*", "*" );
		
	}

	public void updteTimeStamp(){
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		timeStamp = sdf.format(date);
	}
	
	public String GetMessageType() { return MessageType; }
	
	public String toString()
	{
		return "MessageType:" + MessageType + "|srcSensorAgent:" + srcSensorAgent.toString() + (srcSensor != null ? "|srcSensor:" + srcSensor.toString() : "" ) + "|targetSensorAgent:" + targetSensorAgent.toString()  + "|targetSensor:" + targetSensor.toString();
	}
	
	
}
