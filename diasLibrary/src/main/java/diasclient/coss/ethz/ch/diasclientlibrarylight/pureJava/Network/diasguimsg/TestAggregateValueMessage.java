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
import java.util.ArrayList;

public class TestAggregateValueMessage {

	public static void main(String[] args)
	{
		
		//String BasePath = "C:/Users/Administrator/Documents/java-workspace/DIAS-GUI/integration-gui/msgs";
		String BasePath = "/Users/edward/Documents/workspace/DIAS-GUI/integration-gui/msgs";
		
		// initialise a descriptor about our class
		SensorAgentDescription 		SensorAgent = new SensorAgentDescription( "localhost", 9000 );
		System.out.printf( "SensorAgent : %s\n", SensorAgent.toString() );
		
	
		// create messages
		ArrayList<Object> data = new ArrayList<Object>();
		data.add(5.0);
		data.add(9.0);
		data.add(1.0);
		
		
		AggregateValueMessage					dataMessage = new AggregateValueMessage( SensorAgent, "Zurich Mood", "avg", "Second", data );
		System.out.printf( "dataMessage : %s\n", dataMessage.toString() );

		
		// serialiaze to JSON
		Gson 			gson = new Gson();  
		
		String dataMessageJson = gson.toJson(dataMessage);
		
		System.out.printf( "dataMessageJson (JSON): %s\n", dataMessageJson );
		
		// save to file
		String dataMessageFileName = BasePath + "/dataMessage.json";
		
		try 
		{
			PrintWriter out = new PrintWriter(dataMessageFileName);
			out.printf( "%s\n", dataMessageJson );
			out.close();
			System.out.printf( "File written : %s\n", dataMessageFileName );
			
		
		} catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
		}
		
		
		// 2. Deserialize
		try
		 {
			 FileInputStream stream = new FileInputStream(dataMessageFileName);
			 
			 Reader reader = new InputStreamReader( stream, "UTF-8");
			 
			 //Gson gsonbuilder = new GsonBuilder().create();
			 AggregateValueMessage				dataMessageIn = gson.fromJson(reader, AggregateValueMessage.class);
					 			
			 System.out.printf("dataMessageIn : %s\n", dataMessageIn);
			 
			 // show data
			 System.out.printf("\ndata\n");
			 for( Object d : dataMessageIn.data )
			 {
				 System.out.printf("%s\n", d.toString() );
			 }
			 
	     }
		 catch( Exception e)
		 {
			 System.out.printf( "Exception : %s\n", e.getMessage() );
		 }
		
		
	
		
	}// main

}
