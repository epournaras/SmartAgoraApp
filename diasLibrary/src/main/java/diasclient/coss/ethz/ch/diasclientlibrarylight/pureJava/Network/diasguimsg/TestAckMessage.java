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

public class TestAckMessage {

	public static void main(String[] args)
	{
		
		String BasePath = "C:/Users/Administrator/Documents/java-workspace/DIAS-GUI/integration-gui/msgs";
		
		// initialise a descriptor about our class
		SensorAgentDescription 		SensorAgent = new SensorAgentDescription( "localhost", 9000 );
		System.out.printf( "SensorAgent : %s\n", SensorAgent.toString() );
		
	
		// AckMessage
		// add eag 2017-07-21
		AckMessage					ackMessage1 = new AckMessage( SensorAgent, "OK", "" ),
									ackMessage2 = new AckMessage( SensorAgent, "NOK", "You forgot to turn on your computer!" );
		
		System.out.printf( "ackMessage1 : %s\n", ackMessage1.toString() );
		System.out.printf( "ackMessage2 : %s\n", ackMessage2.toString() );
		
		// 1. Serialiaze
		// convert to JSON
		Gson 			gson = new Gson();  
		
		String ackMessage1Json = gson.toJson(ackMessage1),
						ackMessage2Json = gson.toJson(ackMessage2);
		
		System.out.printf( "ackMessage1Json (JSON): %s\n", ackMessage1Json );
		System.out.printf( "ackMessage2Json (JSON): %s\n", ackMessage2Json );
		
		
		// save to file
		String ackMessage1FileName = BasePath + "/ackMessage1.json",
						ackMessage2FileName = BasePath + "/ackMessage2.json";
				
		
		
		try 
		{
			PrintWriter out = new PrintWriter(ackMessage1FileName);
			out.printf( "%s\n", ackMessage1Json );
			out.close();
			System.out.printf( "File written : %s\n", ackMessage1FileName );
			
			out = new PrintWriter(ackMessage2FileName);
			out.printf( "%s\n", ackMessage2Json );
			out.close();
			System.out.printf( "File written : %s\n", ackMessage2FileName );
			
		} catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
		}
		
		
		// 2. Deserialize
		try
		 {
			 FileInputStream stream1 = new FileInputStream(ackMessage1FileName),
					 			stream2 = new FileInputStream(ackMessage1FileName);
			 
			 Reader reader1 = new InputStreamReader( stream1, "UTF-8"),
					 			reader2 = new InputStreamReader( stream2, "UTF-8");
			 
			 //Gson gsonbuilder = new GsonBuilder().create();
			 AckMessage			ackMessage1In = gson.fromJson(reader1, AckMessage.class),
					 			ackMessage2In = gson.fromJson(reader2, AckMessage.class);
					 			
			 System.out.printf("ackMessage1In : %s\n", ackMessage1In);
			 System.out.printf("ackMessage2In : %s\n", ackMessage2In);
	     }
		 catch( Exception e)
		 {
			 System.out.printf( "Exception : %s\n", e.getMessage() );
		 }
		
		
	
		
	}// main

}
