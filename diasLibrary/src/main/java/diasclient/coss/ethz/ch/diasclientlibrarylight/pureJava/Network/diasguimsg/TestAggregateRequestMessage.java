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

public class TestAggregateRequestMessage {

	public static void main(String[] args)
	{
		
		//String BasePath = "C:/Users/Administrator/Documents/java-workspace/DIAS-GUI/integration-gui/msgs";
		String BasePath = "/Users/edward/Documents/workspace/DIAS-GUI/integration-gui/msgs";
		
		
		// initialise a descriptor about our class
		SensorAgentDescription 		SensorAgent = new SensorAgentDescription( "localhost", 9000 );
		System.out.printf( "SensorAgent : %s\n", SensorAgent.toString() );
		
	
		// create messages
		AggregateRequestMessage					reqMessage1 = new AggregateRequestMessage( SensorAgent, "Zurich Mood" ),
												reqMessage2 = new AggregateRequestMessage( SensorAgent, "Zurich Mood", 10, "Day" );
		
		System.out.printf( "reqMessage1 : %s\n", reqMessage1.toString() );
		System.out.printf( "reqMessage2 : %s\n", reqMessage2.toString() );
		
		// 1. Serialiaze
		// convert to JSON
		Gson 			gson = new Gson();  
		
		String reqMessage1Json = gson.toJson(reqMessage1),
						reqMessage2Json = gson.toJson(reqMessage2);
		
		System.out.printf( "reqMessage1Json (JSON): %s\n", reqMessage1Json );
		System.out.printf( "reqMessage2Json (JSON): %s\n", reqMessage2Json );
		
		
		// save to file
		String reqMessage1FileName = BasePath + "/reqMessage1.json",
						reqMessage2FileName = BasePath + "/reqMessage2.json";
				
		
		
		try 
		{
			PrintWriter out = new PrintWriter(reqMessage1FileName);
			out.printf( "%s\n", reqMessage1Json );
			out.close();
			System.out.printf( "File written : %s\n", reqMessage1FileName );
			
			out = new PrintWriter(reqMessage2FileName);
			out.printf( "%s\n", reqMessage2Json );
			out.close();
			System.out.printf( "File written : %s\n", reqMessage2FileName );
			
		} catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
		}
		
		
		// 2. Deserialize
		try
		 {
			 FileInputStream stream1 = new FileInputStream(reqMessage1FileName),
					 			stream2 = new FileInputStream(reqMessage2FileName);
			 
			 Reader reader1 = new InputStreamReader( stream1, "UTF-8"),
					 			reader2 = new InputStreamReader( stream2, "UTF-8");
			 
			 //Gson gsonbuilder = new GsonBuilder().create();
			 AggregateRequestMessage			ackMessage1In = gson.fromJson(reader1, AggregateRequestMessage.class),
					 							ackMessage2In = gson.fromJson(reader2, AggregateRequestMessage.class);
					 			
			 System.out.printf("ackMessage1In : %s\n", ackMessage1In);
			 System.out.printf("ackMessage2In : %s\n", ackMessage2In);
	     }
		 catch( Exception e)
		 {
			 System.out.printf( "Exception : %s\n", e.getMessage() );
		 }
		
		
	
		
	}// main

}
