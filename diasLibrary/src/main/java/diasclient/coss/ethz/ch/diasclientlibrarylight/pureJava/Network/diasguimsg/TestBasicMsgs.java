package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg;
// example for using Google's JSON class (GSON)
// taken from http://www.javacreed.com/simple-gson-example/

// more examples:
// https://futurestud.io/tutorials/gson-mapping-of-arrays-and-lists-of-objects
	
// eag - 2016-11-22

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;

public class TestBasicMsgs {

	public static void main(String[] args)
	{
		
		String BasePath = "C:/Users/Administrator/Documents/java-workspace/DIAS-GUI/integration-gui/msgs";
		
		// initialise a descriptor about our class
		SensorAgentDescription 		SensorAgent = new SensorAgentDescription( "localhost", 9000 );
		System.out.printf( "SensorAgent : %s\n", SensorAgent.toString() );
		
		// initialise a descriptor about a sensor
		SensorDescription			AllSensors = new SensorDescription( "*", "Test" ),
									Sensor = new SensorDescription( "SensorA", "Test" );
		System.out.printf( "AllSensors : %s\n", AllSensors.toString() );
		System.out.printf( "Sensor : %s\n", Sensor.toString() );
		
		// HeartbeatMessage
		HeartbeatMessage			HbtMsg = new HeartbeatMessage(SensorAgent, AllSensors );
		System.out.printf( "HbtMsg : %s\n", HbtMsg.toString() );
		
		// AckMessage
		// add eag 2017-07-21
		AckMessage					ackMessage1 = new AckMessage( SensorAgent, "OK", "" ),
									ackMessage2 = new AckMessage( SensorAgent, "NOK", "You forgot to turn on your computer!" );
		
		System.out.printf( "ackMessage1 : %s\n", ackMessage1.toString() );
		System.out.printf( "ackMessage2 : %s\n", ackMessage2.toString() );
		
		// 1. Serialiaze
		// convert to JSON
		Gson 			gson = new Gson();  
		
		String HbtMsgJson = gson.toJson(HbtMsg);
		System.out.printf( "HbtMsgJson (JSON): %s\n", HbtMsgJson );
		
		String ackMessage1Json = gson.toJson(ackMessage1),
						ackMessage2Json = gson.toJson(ackMessage2);
		
		System.out.printf( "ackMessage1Json (JSON): %s\n", ackMessage1Json );
		System.out.printf( "ackMessage2Json (JSON): %s\n", ackMessage2Json );
		
		
		// save to file
		String HbtMsgFileName = BasePath + "/HbtMsg.json";
		
		
		try 
		{
			PrintWriter out = new PrintWriter(HbtMsgFileName);
			out.printf( "%s\n", HbtMsgJson );
			out.close();
			
		} catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
		}
		System.out.printf( "File written : %s\n", HbtMsgFileName );
		
		// 2. Deserialize
		try
		 {
			 FileInputStream stream = new FileInputStream(HbtMsgFileName);
			 
			 Reader reader = new InputStreamReader( stream, "UTF-8");
			 
			 //Gson gsonbuilder = new GsonBuilder().create();
			 HeartbeatMessage	HbtMsgIn = gson.fromJson(reader, HeartbeatMessage.class);
			 System.out.printf("HbtMsgIn : %s\n", HbtMsgIn);
	     }
		 catch( Exception e)
		 {
			 System.out.printf( "Exception : %s\n", e.getMessage() );
		 }
		
		// 3. SensorStatusMsg 
		SensorStatusMessage			SensorStatusMsg = new SensorStatusMessage(SensorAgent, Sensor, "RefreshRate", "100" );
		System.out.printf( "SensorStatusMsg : %s\n", SensorStatusMsg.toString() );
		
		String SensorStatusMsgJson = gson.toJson(SensorStatusMsg);
		System.out.printf( "SensorStatusMsg (JSON): %s\n", SensorStatusMsgJson );
		
		// save to file
		String SensorStatusMsgFileName = BasePath + "/SensorStatusMsg.json";
		
		
		try 
		{
			PrintWriter out = new PrintWriter(SensorStatusMsgFileName);
			out.printf( "%s\n", SensorStatusMsgJson );
			out.close();
			
		} catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
		}
		System.out.printf( "File written : %s\n", SensorStatusMsgFileName );
	
		// 4. Test casting the message to a GUIMessage to determine the type, and then cast to the exact type after
		
		String JsonToRead = BasePath + "/HbtMsg.json";
		//String				JsonToRead = BasePath + "/SensorStatusMsg.json";
		//String				JsonToRead = BasePath + "/SensorValueMessage.json";
		
		
		System.out.printf( "JsonToRead : %s\n", JsonToRead );
		
		FileInputStream stream2 = null;
		Reader reader2 = null;
		
		try 
		{
			stream2 = new FileInputStream(JsonToRead);
			reader2 = new InputStreamReader( stream2, "UTF-8");
			
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		 
		 					
		 
		 if( reader2 != null )
		 {
			 GUIMessage				MsgInMtype = gson.fromJson(reader2, GUIMessage.class);
			 System.out.printf("MsgInMtype : %s\n", MsgInMtype);
			 
			 // re-open file, it's a stream so cursor is at the end
			 	FileInputStream stream3 = null;
				Reader reader3 = null;
				
				try 
				{
					stream3 = new FileInputStream(JsonToRead);
					reader3 = new InputStreamReader( stream3, "UTF-8");
					
				
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				}
				
				System.out.printf("File re-opened\n");
				 
			 if( MsgInMtype.GetMessageType().equals("Heartbeat") )
			 {
				 HeartbeatMessage				MsgIn = gson.fromJson(reader3, HeartbeatMessage.class);
				 System.out.printf("HeartbeatMessage : %s\n", MsgIn);
			 }
			 else if( MsgInMtype.GetMessageType().equals("Login") )
			 {
				 LoginMessage				MsgIn = gson.fromJson(reader3, LoginMessage.class);
				 System.out.printf("LoginMessage : %s\n", MsgIn);
			 }
			 else if( MsgInMtype.GetMessageType().equals("SensorStatus") )
			 {
				 SensorStatusMessage				MsgIn = gson.fromJson(reader3, SensorStatusMessage.class);
				 System.out.printf("SensorStatusMessage : %s\n", MsgIn);
			 }
			 else if( MsgInMtype.GetMessageType().equals("SensorValue") )
			 {
				 SensorValueMessage				MsgIn = gson.fromJson(reader3, SensorValueMessage.class);
				 System.out.printf("SensorValueMessage : %s\n", MsgIn);
			 }
			 else
			 {
				 System.out.printf( "Message type not supported : %s\n", MsgInMtype.GetMessageType() );
			 }
		 }
		 
		 // Value of the sensor
		 // simulate 
		 LinkedHashMap<String,Object> SensorReading = new LinkedHashMap<String,Object>();
		 
		 SensorReading.put( "Accel.X", 1.23 );
		 SensorReading.put( "Accel.Y", 4.56 );
		 SensorReading.put( "Accel.Z", 7.89 );
		 

		 System.out.printf( "SensorReading : %s\n", SensorReading.toString() );

		 SensorValueMessage			SensorValueMsg = new  SensorValueMessage( SensorAgent, Sensor, SensorReading );
		 System.out.printf( "SensorValueMsg : %s\n", SensorValueMsg.toString() );
	
		 
		 // serialize and write to file
		String SensorValueMsgJson = gson.toJson(SensorValueMsg);
		System.out.printf( "SensorValueMessage (JSON): %s\n", SensorValueMsgJson );
		
		// save to file
		String SensorValueMessageFileName = BasePath + "/SensorValueMessage.json";
		
		
		try 
		{
			PrintWriter out = new PrintWriter(SensorValueMessageFileName);
			out.printf( "%s\n", SensorValueMsgJson );
			out.close();
			
		} catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
		}
		System.out.printf( "File written : %s\n", SensorValueMessageFileName );
		
		// ---------------
		// --- FACTORY ---
		// ---------------
		System.out.printf( "\n--- Factory ---\n" );
		
		//String				JsonToRead2 = BasePath + "/HbtMsg.json";
		//String				JsonToRead2 = BasePath + "/SensorStatusMsg.json";
		String JsonToRead2 = BasePath + "/SensorValueMessage.json";
		
		System.out.printf( "JsonToRead2 : %s\n", JsonToRead2 );
		
		// read file into String
		String contents = null;
		/*try
		{
			contents = new String(Files.readAllBytes(Paths.get(JsonToRead2)));
		} catch (IOException e)
		{
			e.printStackTrace();
		}*/
		System.out.printf( "contents : %s\n", contents );
		
		MessageFactory			Factory = new MessageFactory();
		
		GUIMessage 				guiMessage = Factory.CrackMessage(contents);
		System.out.printf( "guiMessage : %s\n", guiMessage );
		
		if( guiMessage.MessageType.equals( "Heartbeat") )
		{
			HeartbeatMessage		hbtmsg2 = (HeartbeatMessage)Factory.CastMessage(HeartbeatMessage.class);
			
			System.out.printf( "hbtmsg2 : %s\n", hbtmsg2 );
		}
		else if( guiMessage.MessageType.equals( "SensorStatus") )
		{
			SensorStatusMessage		sensorstatusmsg2 = (SensorStatusMessage)Factory.CastMessage(SensorStatusMessage.class);
			
			System.out.printf( "sensorstatusmsg2 : %s\n", sensorstatusmsg2 );
		}
		else if( guiMessage.MessageType.equals( "SensorValue") )
		{
			SensorValueMessage		sensorvaluemsg2 = (SensorValueMessage)Factory.CastMessage(SensorValueMessage.class);
			
			System.out.printf( "sensorvaluemsg2 : %s\n", sensorvaluemsg2 );
			
			// test reading a value from the map
			System.out.printf( "Accel.X : %s\n", sensorvaluemsg2.SensorReading.get("Accel.X") );
		}
		else
			System.out.printf( "unhandled message type : %s\n", guiMessage.MessageType );
		
		
		// ------------------------
		// SensorDestinationMessage
		// ------------------------
		
		SensorAgentDescription 	destinationSensorAgent = new SensorAgentDescription( "localhost", 9002 );
		
		SensorDescription 		destinationSensor = new SensorDescription( "*", "Test" );
		
		SensorDestinationMessage destinatonMsg = new SensorDestinationMessage( 	SensorAgent
																				,AllSensors
																				,destinationSensorAgent
																				,destinationSensor
																				);
		
		// serialize and write to file
		String destinatonMsgJson = gson.toJson(destinatonMsg);
		System.out.printf( "destinatonMsg (JSON): %s\n", destinatonMsgJson );
		
		// save to file
		String destinatonMsgFileName = BasePath + "/SensorDestinationMessage.json";
		
		
		try 
		{
			PrintWriter out = new PrintWriter(destinatonMsgFileName);
			out.printf( "%s\n", destinatonMsgJson );
			out.close();
			
		} catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
		}
		System.out.printf( "File written : %s\n", destinatonMsgFileName );
		
		
		
		
		
	}// main

}
