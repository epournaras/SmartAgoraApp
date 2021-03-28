package smartagora.ethz.ch.uiActivities;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
//import com.google.android.gms.games.quest.Quest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.services.concurrency.AsyncTask;
import smartagora.ethz.ch.R;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.AssignmentThread;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.QuestionThread;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.StartAndDestinationThread;
import smartagora.ethz.ch.roomDatabase.entityModels.AssignmentEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.QuestionEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.StartAndDestinationEntity;
import smartagora.ethz.ch.databases.DatabaseHandler;
import smartagora.ethz.ch.models.MainModel;
import smartagora.ethz.ch.models.QuestionDataModel;
import smartagora.ethz.ch.models.StartAndDestinationModel;
import smartagora.ethz.ch.models.StaticModel;
import smartagora.ethz.ch.prefrences.AppPreferences;
import smartagora.ethz.ch.uiActivities.adapter.FilesListAdapter;
import smartagora.ethz.ch.utils.ExceptionalHandling;
import smartagora.ethz.ch.utils.JSONParser;
import smartagora.ethz.ch.utils.Permissions;
import smartagora.ethz.ch.utils.utils;
import smartagora.ethz.ch.viewmodels.CheckBoxQuestionModel;
import smartagora.ethz.ch.viewmodels.RadioButtonViewModel;
import smartagora.ethz.ch.viewmodels.SeekBarViewModel;

import static smartagora.ethz.ch.utils.utils.getDataFromFile;

/**
 * QuestionsActivity is used to load questions from local directory and show list
 * of questions which are loaded locally. When there is no question
 * in the list only choose file button is visible
 * and when questions are loaded a list of
 * question with OK and Cancel button.
 */
public class QuestionsActivity extends AppCompatActivity {

    private final static int FILE_SELECT_CODE = 1; // file selection code
    private final static int READ_PERMISSION_CODE = 2; // read permission code
    private LinearLayout linearLayout;
    private DatabaseHandler databaseHandler;
    private Button ok, cancel;
    private ListView list_files;
    public static String FileFromList = "";
    private static MainModel selectedQuestions;
    public static AssignmentEntity selectedAssignment;
    public static StartAndDestinationEntity startAndDestinationEntity;
    public static List<QuestionEntity> assignmentQuestions;

    private Context mContext;
    private MainModel mQuestions;
    private ProgressDialog mParserProgress;

    /**
     * Called when activity is created by User.
     *
     * @param savedInstanceState not used, standard
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_questions);
            Fabric.with(this, Crashlytics.getInstance());
            ok = findViewById(R.id.ok);
            cancel = findViewById(R.id.cancel);
            Button addFiles = findViewById(R.id.ChooseJsonFile);

            ok.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
            list_files = findViewById(R.id.file_list);
            databaseHandler = new DatabaseHandler(this);
            mContext = this.getApplicationContext();

            if (selectedQuestions == null)
                selectedQuestions = new MainModel();
            if (databaseHandler.getFilesName().size() > 0) {
                addFiles.setText(R.string.add_more_files);
            } else {
                addFiles.setText(R.string.add_files);
            }

            // loading files list
            FileListsNew();

            // get read permission for marshmallow and above
            getReadPermission();

            //initialize UI handlers and databaseHandler
            initialize();

            list_files.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view,
                                        int position, long id) {
                    FileFromList = list_files.getItemAtPosition(position).toString();
                    if (FileFromList.contains("*")) {
                        Toast.makeText(mContext, "Assignment is already completed!", Toast.LENGTH_LONG).show();
                    } else {
                        utils.assignmentSubmissionStatus = false;
                        utils.fileName = FileFromList;
                        utils.loadedFileName = FileFromList;
                        getDataFromRoomDB();
                    }
                }
            });


        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    /**
     * Initializing handlers and views.
     */
    private void initialize() {
        try {
            databaseHandler = new DatabaseHandler(this);
            linearLayout = findViewById(R.id.linearLayout);
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    /**
     * Getting File Read permissions.
     */
    @TargetApi(23)
    private void getReadPermission() {
        try {
            if (!Permissions.hasStorageReadPermissions(this))
                Permissions.getStorageReadPermissions(this, READ_PERMISSION_CODE);
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }


    public void chooseJsonFile(View view) {
        try {
            utils.showFileChooser(this, FILE_SELECT_CODE);
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (resultCode == Activity.RESULT_OK) {
                switch (requestCode) {
                    case FILE_SELECT_CODE:
                        if (databaseHandler.getFilesName().contains(utils.fileName)) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage("File Already Existed")
                                    .setCancelable(false)
                                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                        public void onClick(final DialogInterface dialog, final int id) {
                                            dialog.cancel();
                                        }
                                    });
                            builder.create().show();
                        } else {
                            new ProgressTask().execute(data);
                        }
                        break;
                    case READ_PERMISSION_CODE:
                        if (Permissions.hasStorageReadPermissions(this))
                            break;
                }
            }
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }


    private void setJsonToClass(String dataFromFile) {
        try {
            JSONParser jsonParser = new JSONParser();
            JSONObject obj = new JSONObject(dataFromFile);

            JSONObject startAndDestinationModelJson = null;
            JSONArray sampleDataModelsJson = null;

            if (!obj.isNull("StartAndDestinationModel"))
                startAndDestinationModelJson = obj.getJSONObject("StartAndDestinationModel");

            if (obj.optJSONArray("SampleDataModel") != null)
                sampleDataModelsJson = obj.optJSONArray("SampleDataModel");

            StartAndDestinationModel startAndDestinationModel = null;
            List<QuestionDataModel> sampleDataModelList = null;
            if (startAndDestinationModelJson != null)
                startAndDestinationModel = jsonParser.parseStartAndDestinationModel(startAndDestinationModelJson);
            if (sampleDataModelsJson != null)
                sampleDataModelList = jsonParser.parseQuestionDataModel(sampleDataModelsJson);

            mQuestions = new MainModel();
            mQuestions.StartAndDestinationModel = startAndDestinationModel;
            mQuestions.SampleDataModel = sampleDataModelList;
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }

    }

    private void showQuestions() {
        try {
            populateQuestions(mQuestions);

            ok.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);

            ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        saveDataToDB(mQuestions);
                        StaticModel.XMLLoaded = true;

                        QuestionsActivity.selectedQuestions = mQuestions;
                        Intent intent = new Intent(QuestionsActivity.this, HomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    } catch (Exception ex) {
                        StaticModel.XMLLoaded = false;
                        ExceptionalHandling.logException(ex);
                    }
                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    try {
                        StaticModel.XMLLoaded = false;
                        QuestionsActivity.super.onBackPressed();
                        mQuestions = null;
                    } catch (Exception ex) {
                        ExceptionalHandling.logException(ex);
                    }
                }
            });
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    /**
     * Storing questions data to database
     *
     * @param model the questions to save
     */
    private void saveDataToDB(MainModel model) {
        try {
            databaseHandler.saveStartAndDestinationData(model.StartAndDestinationModel);
            databaseHandler.saveQuestionDataTransition(model.SampleDataModel);
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    /**
     * Getting questions data from Room database
     */
    private void getDataFromRoomDB() {
        try {
            assignmentQuestions = new ArrayList<>();
            selectedAssignment = new AssignmentEntity();
            startAndDestinationEntity = new StartAndDestinationEntity();

            AssignmentThread assignmentThread = new AssignmentThread(mContext);
            StartAndDestinationThread startAndDestinationThread = new StartAndDestinationThread(mContext);
            QuestionThread questionThread = new QuestionThread(mContext);

            selectedAssignment = assignmentThread.getAssignment(FileFromList);
            startAndDestinationEntity = startAndDestinationThread.getStartAndDestination(FileFromList);

            boolean isnull = assignmentQuestions == null;

            Log.i("DEBUG", "isnull: " + isnull);

            assignmentQuestions = questionThread.getAllQuestionsFromAssignment(FileFromList);

            isnull = assignmentQuestions == null;

            Log.i("DEBUG", "isnull now: " + isnull);

            Intent intent = new Intent(QuestionsActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }


    /**
     * Showing list of questions when reach at specific question position.
     *
     * @param questions the questions to show
     */
    private void populateQuestions(MainModel questions) {
        try {
            boolean isType = false;
            for (int i = 0, j = 0; i < questions.SampleDataModel.size(); i++) {
                if (questions.SampleDataModel.get(i).Type != null) {
                    if (questions.SampleDataModel.get(i).Type.equalsIgnoreCase("radio")) {
                        RadioButtonViewModel radioButtonViewModel = new RadioButtonViewModel(QuestionsActivity.this);
                        radioButtonViewModel.updateView(questions.SampleDataModel.get(i));
                        linearLayout.addView(radioButtonViewModel.getRadioView(), j++);
                    } else if (questions.SampleDataModel.get(i).Type.equalsIgnoreCase("checkbox")) {
                        CheckBoxQuestionModel checkBoxQuestionModel = new CheckBoxQuestionModel(QuestionsActivity.this);
                        checkBoxQuestionModel.updateView(questions.SampleDataModel.get(i));
                        linearLayout.addView(checkBoxQuestionModel.getCheckBoxQuestionView(), j++);
                    } else {
                        SeekBarViewModel seekbarViewModel = new SeekBarViewModel(QuestionsActivity.this);
                        seekbarViewModel.updateView(questions.SampleDataModel.get(i));
                        linearLayout.addView(seekbarViewModel.getSeekBarView(), j++);
                    }
                    isType = true;
                }
            }
            if (!isType) {
                Toast.makeText(this, "All questions loaded. Press OK to continue", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }

    }

    /**
     * Called when activity is fully closed by user.
     */
    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }


    /**
     * Getting list of files and placing list with adapter.
     */
    private void FileListsNew() {
        try {
            String selectedProjectId = AppPreferences.getProjectId(this);
            AssignmentThread assignmentThread = new AssignmentThread(mContext);
            List<AssignmentEntity> assignmentEntities = assignmentThread.getAllAssignments();
            List<String> assignmentNames = new ArrayList<>();

            for (int i = 0; i < assignmentEntities.size(); i++) {
                if (assignmentEntities.get(i).getId().indexOf(selectedProjectId) == 0)
                    if (assignmentEntities.get(i).getState().equalsIgnoreCase("unfinished")) {
                        assignmentNames.add(assignmentEntities.get(i).getName());
                    } else {
                        assignmentNames.add(assignmentEntities.get(i).getName() + "*");
                    }
            }

            FilesListAdapter adapter = new FilesListAdapter(QuestionsActivity.this, assignmentNames);
            list_files.setAdapter(adapter);
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }


    class ProgressTask extends AsyncTask<Intent, Void, Void> {
        @Override
        protected void onPreExecute() {
            try {
                mParserProgress = new ProgressDialog(QuestionsActivity.this);
                mParserProgress.setTitle("Please Wait.");
                mParserProgress.setMessage("Loading XML...");
                mParserProgress.setIndeterminate(false);
                mParserProgress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mParserProgress.show();
            } catch (Exception ex) {
                ExceptionalHandling.logException(ex);
            }
        }

        @Override
        protected Void doInBackground(Intent... arg0) {
            try {
                if (arg0.length > 0)
                    setJsonToClass(getDataFromFile(QuestionsActivity.this, arg0[0]));
            } catch (Exception ex) {
                ExceptionalHandling.logException(ex);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                mParserProgress.dismiss();
                if (mQuestions != null) {
                    showQuestions();
                }
            } catch (Exception ex) {
                ExceptionalHandling.logException(ex);
            }
        }
    }


}
