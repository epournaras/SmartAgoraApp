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
import java.util.LinkedHashMap;
import java.util.Random;

public class TestStateMsgs {

	public static void main(String[] args)
	{
		
		String BasePath = "C:/Users/Administrator/Documents/java-workspace/DIAS-GUI/integration-gui/msgs";
		
		// initialise a descriptor about our class
		SensorAgentDescription 		sensorAgent = new SensorAgentDescription( "localhost", 9000 );
		System.out.printf( "sensorAgent : %s\n", sensorAgent.toString() );
		
		// initialise a descriptor about a sensor
		SensorDescription			sensor = new SensorDescription( "SensorA", "Test" );
		System.out.printf( "sensor : %s\n", sensor.toString() );
		
		// JSON serializer
		Gson 			gson = new Gson();  
		
		// create possible states
		final int					numPossibleStates = 9,
									numDimensions = 1;		// number of scalar values output for 1 reading of the sensor; e.g 3 for position (x,y,z)

		final Random random = new Random();

		ArrayList<SensorState> PossibleStates = new ArrayList<SensorState>(numPossibleStates);		// this sets the capacity, not the size
	
		// ---------------
		// possible states
		// ---------------
		
		System.out.printf( "\nCreating states:\n" );
		for( int i = 0; i <= numPossibleStates - 1; i++ )
		{
			// create the means that represent the cluster
			LinkedHashMap<String,Object> cluster_means = new LinkedHashMap<String,Object>();
			
			for (int j = 1; j <= numDimensions; j++ )
				cluster_means.put( "x." + j, random.nextDouble() );
				
			// create the state object
			SensorState							state = new SensorState( new Integer(i), cluster_means );
			
			// finally, add to the list of possible states (add to the end)
			PossibleStates.add( state );
			
			System.out.printf( "#%d : %s\n", i, state.toString() );
			
		}
		
		// create possible states msg
		PossibleStatesMessage		possibleStatesMsg = new PossibleStatesMessage( PossibleStates, sensorAgent, sensor );
		

		String possibleStatesMsgJson = gson.toJson(possibleStatesMsg);
		System.out.printf( "possibleStatesMsgJson (JSON): %s\n", possibleStatesMsgJson );
		
		// save to file
		String possibleStatesMsgFileName = BasePath + "/possibleStatesMsg.json";
		
		
		try 
		{
			PrintWriter out = new PrintWriter(possibleStatesMsgFileName);
			out.printf( "%s\n", possibleStatesMsgJson );
			out.close();
			
		} catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
		}
		System.out.printf( "File written : %s\n", possibleStatesMsgFileName );
		
		// Deserialize
		try
		 {
			 FileInputStream stream = new FileInputStream(possibleStatesMsgFileName);
			 
			 Reader reader = new InputStreamReader( stream, "UTF-8");
			 
			 PossibleStatesMessage		possibleStatesMsgIn = gson.fromJson(reader, PossibleStatesMessage.class);
			 System.out.printf("possibleStatesMsgIn : %s\n", possibleStatesMsgIn);
			 
			
	     }
		 catch( Exception e)
		 {
			 System.out.printf( "Exception : %s\n", e.getMessage() );
		 }
		// --------------
		// selected state
		// --------------
		
		// select a state at random from possible states
		Random rand = new Random();
		
		final	int		selectedStateID = rand.nextInt(numPossibleStates);
		System.out.printf("selectedStateID : %d\n", selectedStateID);
		
		SensorState							selectedState = PossibleStates.get(selectedStateID);
		System.out.printf("selectedState : %s\n", selectedState);
		
		// create a message with the cluster means
		SelectedStateMessage				selectedStateMsg = new SelectedStateMessage( selectedState, sensorAgent,sensor );
		System.out.printf("selectedState : %s\n", selectedStateMsg);
		
		// convert to JSON
		String selectedStateMsgJson = gson.toJson(selectedStateMsg);
		System.out.printf( "selectedStateMsgJson (JSON): %s\n", selectedStateMsgJson );
		
		// save to file
		String selectedStateMsgJsonFileName = BasePath + "/selectedStateMsgJson.json";
		
		
		try 
		{
			PrintWriter out = new PrintWriter(selectedStateMsgJsonFileName);
			out.printf( "%s\n", selectedStateMsgJson );
			out.close();
			
		} catch (FileNotFoundException e1)
		{
			e1.printStackTrace();
		}
		System.out.printf( "File written : %s\n", possibleStatesMsgFileName );
		
		// TODO: Deserialize without the cluster means
		
		// TODO: Deserialize with the cluster means
	
		
	}// main

}
