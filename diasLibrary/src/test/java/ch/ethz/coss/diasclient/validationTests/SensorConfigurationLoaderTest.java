package ch.ethz.coss.diasclient.validationTests;

import com.google.gson.Gson;

import org.junit.Test;
import org.mockito.Mock;

import java.lang.reflect.Array;
import java.util.ArrayList;

import ch.ethz.coss.diasclient.pureJava.configuration.SensorConfiguration;
import ch.ethz.coss.diasclient.pureJava.configuration.SensorConfigurationLoader;

/**
 * Created by jubatyrn on 01.02.18.
 */

public class SensorConfigurationLoaderTest {

    @Mock
    Gson gson = new Gson();

    @Test
    public void testParsingJSONConfigurationFiles() {
        SensorConfigurationLoader loader;
        ArrayList<SensorConfiguration> sensorConfigurations;

        // Gets a loader singleton.
        try {

            //loader = SensorConfigurationLoader.getInstance();
            //sensorConfigurations = loader.getSensorConfigurations();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
