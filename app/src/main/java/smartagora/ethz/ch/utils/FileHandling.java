package smartagora.ethz.ch.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.AsyncTask;
import android.util.Log;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


public class FileHandling {
    private final SavingDataToFileAsync savingDataToFileAsync;

    public FileHandling(){
        savingDataToFileAsync = new SavingDataToFileAsync();
    }

    static class SavingDataToFileAsync extends AsyncTask<Void, Void, String> {

        private WeakReference<Context> wr;
        private List<String> question_sensorInfo;
        private String questionId;

        String getAnswerValue() {
            return answerValue;
        }

        void setAnswerValue(String answerValue) {
            this.answerValue = answerValue;
        }

        private String answerValue;

        @Override
        protected String doInBackground(Void... params) {
            String status = "Data is not written.";
            Context context = wr.get();
            try {
                //opening file if file not exist then created new one
                String filename =  utils.fileName+".json";
                ContextWrapper contextWrapper = new ContextWrapper(context);
                File sensorInfoFolder = contextWrapper.getExternalFilesDir("Sensor Information"); //Creating an internal dir;
                File sensorInfoFile = null;

                if(sensorInfoFolder == null)
                    throw new Exception("sensorInfoFolder was null");

                File [] files= sensorInfoFolder.listFiles();
                boolean sts = true;
                if(files.length>0){
                    for (File file : files) {
                        if (file.getName().equals(utils.fileName + ".json")) {
                            sts = false;
                            sensorInfoFile = file;
                        }
                    }
                }
                StringBuilder str= new StringBuilder();
                if(sts) {
                    sensorInfoFile = new File(sensorInfoFolder, filename); //Getting a file within the dir.
                    str = new StringBuilder("[");
                }
                FileWriter fileWriter = new FileWriter(sensorInfoFile,true);
                BufferedWriter bufferWriter = new BufferedWriter(fileWriter);

                if(str.toString().equals("["))
                    str.append("{'").append(questionId).append("':[");
                else
                    str.append(",{'").append(questionId).append("':[");
                for(int i=0;i<question_sensorInfo.size();i++) {
                    if (i == 0) {
                        String val = question_sensorInfo.get(i).replace("}",",\"Answer Value\":'"+getAnswerValue()+"'}");
                        str.append(val);
                    }
                    else {
                        String val = question_sensorInfo.get(i).replace("}",",\"Answer Value\":'"+getAnswerValue()+"'}");
                        str.append(",").append(val);
                    }
                }
                str.append("]}");
                bufferWriter.append(str.toString());

                bufferWriter.close();

                status = "Done";

            } catch (Exception e) {
                e.printStackTrace();
            }
            return status;
        }

        @Override
        protected void onPreExecute() {
            Log.d("TESTING", "OnPreExecute() FileHandling");
            utils.FileWritingStatus = false;

        }

        @Override
        protected void onPostExecute(String s) {
            Log.d("TESTING", "OnPostExecute()FileHandling");
            utils.FileWritingStatus = true;

        }

        void setContext(Context context) {
            wr = new WeakReference<>(context);
        }


        void setQuestionSensorInfo(List<String> sensorData) {
            this.question_sensorInfo = new ArrayList<>();
            this.question_sensorInfo.addAll(sensorData);
        }

        void setQuestionId(String questionId){
            this.questionId = questionId;
        }
    }


    public void saveDataToFile(Context context, List<String> questionSensorData, String qId, String answerValue){
        savingDataToFileAsync.setContext(context);
        savingDataToFileAsync.setQuestionSensorInfo(questionSensorData);
        savingDataToFileAsync.setQuestionId(qId);
        savingDataToFileAsync.setAnswerValue(answerValue);
        try {
            savingDataToFileAsync.execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


}
