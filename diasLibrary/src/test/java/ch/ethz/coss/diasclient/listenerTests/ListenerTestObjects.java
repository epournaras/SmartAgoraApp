package ch.ethz.coss.diasclient.listenerTests;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lewin on 01.11.17.
 */

public final class ListenerTestObjects {
    public ArrayList<JSONObject> inputs = new ArrayList<JSONObject>();
    public ArrayList<Boolean> results = new ArrayList<Boolean>();


    ListenerTestObjects() throws JSONException {
        inputs.add(new JSONObject("{\n" +
                "      \"sensorID\" : 100,\n" +
                "      \"sensorName\" : \"Smiley\",\n" +
                "      \"timestamp\" : 5678910\n" +
                "      \"parametersNames\" :\n" +
                "      [\n" +
                "        \"Happiness\",\n" +
                "        \"Last meal\",\n" +
                "        \"days till holidays\"\n" +
                "      ],\n" +
                "      \"parametersTypes\" :\n" +
                "      [\n" +
                "        \"double\",\n" +
                "        \"string\",\n" +
                "        \"int\"\n" +
                "      ],\n" +
                "\t \"readings\" :\n" +
                "      [\n" +
                "        0.784,\n" +
                "        \"Pancakes\",\n" +
                "        52\n" +
                "      ]\n" +
                "    },\n"));

        results.add(true);

    }

}


