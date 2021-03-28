package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg;
// example for using Google's JSON class (GSON)
// taken from http://www.javacreed.com/simple-gson-example/

// more examples:
// https://futurestud.io/tutorials/gson-mapping-of-arrays-and-lists-of-objects
	
// eag - 2016-11-22

import com.google.gson.Gson;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;

public class TestPingMessage {

	public static void main(String[] args)
	{
		
		//String BasePath = "C:/Users/Administrator/Documents/java-workspace/DIAS-GUI/integration-gui/msgs";
		String BasePath = "/Users/edward/Documents/workspace/DIAS-GUI/integration-gui/msgs";
		
		// initialise a descriptor about our class
		SensorAgentDescription 		SensorAgent = new SensorAgentDescription( "localhost", 9000 );
		System.out.printf( "SensorAgent : %s\n", SensorAgent.toString() );
		
	
		// AckMessage
		// add eag 2017-07-21
		PingMessage					pingMessage = new PingMessage( SensorAgent );
									
		System.out.printf( "pingMessage : %s\n", pingMessage.toString() );
		
		// 1. Serialiaze
		// convert to JSON
		Gson 			gson = new Gson();  
		
		String pingMessageJson = gson.toJson(pingMessage);
		
		System.out.printf( "pingMessageJson (JSON): %s\n", pingMessageJson );
		
		
		// save to file
		String pingMessageFileName = BasePath + "/PingMessage.json";
		
		
		try 
		{
			PrintWriter out = new PrintWriter(pingMessageFileName);
			out.printf( "%s\n", pingMessageJson );
			out.close();
			System.out.printf( "File written : %s\n", pingMessageFileName );
			
			
		} catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
		}
		
		
		// 2. Deserialize
		try
		 {
			 FileInputStream stream = new FileInputStream(pingMessageFileName);
			 
			 Reader reader = new InputStreamReader( stream, "UTF-8");
			 
			 //Gson gsonbuilder = new GsonBuilder().create();
			 PingMessage		pingMessage1In = gson.fromJson(reader, PingMessage.class);
					 			
			 System.out.printf("pingMessage1In : %s\n", pingMessage1In);
	     }
		 catch( Exception e)
		 {
			 System.out.printf( "Exception : %s\n", e.getMessage() );
		 }
		
		
	
		
	}// main

}
