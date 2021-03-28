package smartagora.ethz.ch.utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.methods.HttpPost;
import cz.msebera.android.httpclient.impl.client.DefaultHttpClient;
import smartagora.ethz.ch.models.OptionModel;
import smartagora.ethz.ch.models.QuestionDataModel;
import smartagora.ethz.ch.models.SensorModel;
import smartagora.ethz.ch.models.StartAndDestinationModel;

/**
 * Created on on 6/9/2016.
 */
public class JSONParser {

    private static InputStream is = null;
    private static String json = "";

    public String getJSONFromUrl(String url) {

        // Making HTTP request
        try {
            // defaultHttpClient
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            @SuppressWarnings("CharsetObjectCanBeUsed") BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }

            json = sb.toString();
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;

    }

    public StartAndDestinationModel parseStartAndDestinationModel(JSONObject jsonModel) {
        StartAndDestinationModel model = new StartAndDestinationModel();
        try {
            if (!jsonModel.isNull("DefaultCredit"))
                model.setDefaultCredit(jsonModel.getString("DefaultCredit"));
            if (!jsonModel.isNull("DestinationLatitude"))
                model.setDestinationLatitude(jsonModel.getString("DestinationLatitude"));
            if (!jsonModel.isNull("DestinationLongitude"))
                model.setDestinationLongitude(jsonModel.getString("DestinationLongitude"));
            if (!jsonModel.isNull("Mode"))
                model.setMode(jsonModel.getString("Mode"));
            if (!jsonModel.isNull("StartLatitude"))
                model.setStartLatitude(jsonModel.getString("StartLatitude"));
            if (!jsonModel.isNull("StartLongitude"))
                model.setStartLongitude(jsonModel.getString("StartLongitude"));
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
        return model;
    }

    public List<QuestionDataModel> parseQuestionDataModel(JSONArray sampleDataModels) {
        List<QuestionDataModel> questionDataModelList = new ArrayList<>();
        try {
            for (int i = 0; i < sampleDataModels.length(); i++) {
                QuestionDataModel questionDataModel = new QuestionDataModel();
                questionDataModel.setId(Integer.toString(i + 1));

                JSONObject current = sampleDataModels.getJSONObject(i);

                if (!current.isNull("Question"))
                    questionDataModel.setQuestion(current.get("Question").toString());
                if (!current.isNull("Latitude"))
                    questionDataModel.setLatitude(current.get("Latitude").toString());
                if (!current.isNull("Longitude"))
                    questionDataModel.setLongitude(current.get("Longitude").toString());
                if (current.optJSONArray("Sensor") != null) {
                    List<SensorModel> sensorModelList = new ArrayList<>();
                    for (int j = 0; j < current.optJSONArray("Sensor").length(); j++) {
                        if (!current.optJSONArray("Sensor").getJSONObject(j).isNull("Name")) {
                            SensorModel sensorModel = new SensorModel(current.optJSONArray("Sensor").getJSONObject(j).get("Name").toString());
                            sensorModelList.add(sensorModel);
                        }
                    }
                    questionDataModel.setSensorsList(sensorModelList);
                }
                if (!current.isNull("Time"))
                    questionDataModel.setTime(current.get("Time").toString());
                if (!current.isNull("Frequency"))
                    questionDataModel.setFrequency(current.get("Frequency").toString());
                if (!current.isNull("Sequence") && !current.get("Sequence").toString().equalsIgnoreCase("disable"))
                    questionDataModel.setSequence(current.get("Sequence").toString());
                if (!current.isNull("Type"))
                    questionDataModel.setType(current.get("Type").toString());
                if (!current.isNull("Visibility"))
                    questionDataModel.setVisibility(current.get("Visibility").toString());
                if (!current.isNull("Mandatory"))
                    questionDataModel.setMandatory(current.get("Mandatory").toString());
                if (current.optJSONArray("Option") != null) {
                    List<OptionModel> optionModelList = new ArrayList<>();
                    for (int j = 0; j < current.getJSONArray("Option").length(); j++) {
                        if (!current.getJSONArray("Option").getJSONObject(j).isNull("Name"))
                            optionModelList.add(new OptionModel(current.getJSONArray("Option").getJSONObject(j).get("Name").toString()));
                        else
                            optionModelList.add(new OptionModel());

                    }
                    questionDataModel.setOption(optionModelList);
                }
                if (!current.isNull("Vicinity"))
                    questionDataModel.setVicinity(current.get("Vicinity").toString());

                questionDataModelList.add(questionDataModel);
            }
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
        if (questionDataModelList.size() == 0)
            return null;
        else
            return questionDataModelList;
    }


}