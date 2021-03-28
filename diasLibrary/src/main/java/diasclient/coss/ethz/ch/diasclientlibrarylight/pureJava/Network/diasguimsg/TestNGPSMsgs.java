package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.diasguimsg;
// example for using Google's JSON class (GSON)
// taken from http://www.javacreed.com/simple-gson-example/

// more examples:
// https://futurestud.io/tutorials/gson-mapping-of-arrays-and-lists-of-objects
	
// eag - 2016-11-22

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Random;

public class TestNGPSMsgs {

	public static void main(String[] args) throws FileNotFoundException
	{
		// arguments
		final int		nmsgs = 1000;
		
		// output
		String basePath = "C:/Users/Administrator/Documents/java-workspace/DIAS-GUI/integration-gui/msgs/out",
					outputFile = basePath + "/gps.json";
		
		// create output file; PrintWriter will not create it
		File fileOut = new File(outputFile);
		fileOut.getParentFile().mkdirs();
		
		PrintWriter out = new PrintWriter(fileOut);
		
		assert( out != null );
		
		// random number generator
		Random rand = new Random();
					
		// JSON serializer
		Gson 			gson = new Gson();  
		
		// initialise a descriptor about our class
		SensorAgentDescription 		sensorAgent = new SensorAgentDescription( "localhost", 9000 );
		System.out.printf( "sensorAgent : %s\n", sensorAgent.toString() );
		
		// initialise a descriptor about a sensor
		SensorDescription			sensorDescription = new SensorDescription( "SensorA", "gps" );
		System.out.printf( "sensorDescription : %s\n", sensorDescription.toString() );
		 
		// generate a random value for the sensor
		for( int i = 1; i <= nmsgs; i++ )
		{
			LinkedHashMap<String,Object> sensorReading = new LinkedHashMap<String,Object>();
			 
			sensorReading.put( "lat", rand.nextDouble() * 100.0 );
			sensorReading.put( "lon", rand.nextDouble() * 100.0 );
			sensorReading.put( "alt", rand.nextDouble() * 100.0 );
			 
			
			System.out.printf( "sensorReading : %s\n", sensorReading.toString() );
			
			SensorValueMessage			sensorValueMsg = new  SensorValueMessage( sensorAgent, sensorDescription, sensorReading );
			System.out.printf( "sensorValueMsg : %s\n", sensorValueMsg.toString() );
			 
			// serialize to JSON
			String sensorValueMsgJson = gson.toJson(sensorValueMsg);
			System.out.printf( "sensorValueMsgJson (JSON): %s\n", sensorValueMsgJson );
			
			// append to file
			out.printf( "%s\n", sensorValueMsgJson );
		}
		
		// close file
		out.flush();
		out.close();
		System.out.printf( "file closed\n" );
				
		
		
	}// main

}
