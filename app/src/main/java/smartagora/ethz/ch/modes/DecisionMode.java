package smartagora.ethz.ch.modes;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.gson.Gson;
import com.google.maps.android.PolyUtil;
import com.kofigyan.stateprogressbar.StateProgressBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


import smartagora.ethz.ch.networkRequests.NetworkCallbacks;
import smartagora.ethz.ch.R;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.AnswerThread;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.CheckBoxDecisionModeThread;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.CombinationThread;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.LikertScaleDecisionModeThread;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.LikertScaleThread;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.RadioButtonThread;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.RadioDecisionModeThread;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.TextBoxDecisionModeThread;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.TextBoxThread;
import smartagora.ethz.ch.roomDatabase.entityModels.AnswerEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.CheckBoxDecisionModeEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.CombinationEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.LikertScaleDecisionModeEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.LikertScaleEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.QuestionEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.RadioButtonEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.RadioDecisionModeEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.TextBoxDecisionModeEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.TextBoxEntity;
import smartagora.ethz.ch.models.StaticModel;
import smartagora.ethz.ch.prefrences.AppPreferences;
import smartagora.ethz.ch.uiActivities.HomeActivity;
import smartagora.ethz.ch.uiActivities.QuestionsActivity;
import smartagora.ethz.ch.utils.DeviceUuidFactory;
import smartagora.ethz.ch.utils.ExceptionalHandling;
import smartagora.ethz.ch.utils.QuestionHandling;
import smartagora.ethz.ch.utils.utils;
import smartagora.ethz.ch.viewmodels.CheckBoxQuestionModel;
import smartagora.ethz.ch.viewmodels.EditTextViewModel;
import smartagora.ethz.ch.viewmodels.RadioButtonViewModel;
import smartagora.ethz.ch.viewmodels.SeekBarViewModel;


/**
 * DecisionMode Class has responsibility to initialize the decisionMode Assignment and decisionMode PopUp method for displaying questions.
 */
public class DecisionMode{

    private final Context context;
    private final QuestionHandling questionHandling;
    private List<AnswerEntity> answersList;
    private final DeviceUuidFactory mDeviceId;
    private final ModeFunctions modeFunctions;
    private final SensorsInfo sensorsInfo;
    private final Polyline line;
    private int answeredQuestions;
    private final HomeActivity homeActivity;

    /**
     * Instantiates a DecisionMode.
     *
     * @param context                     the context
     * @param line                        the line
     * @param answeredQuestions           the answered questions
     * @param stateProgressBar            the state progress bar
     * @param questionHandling            the question handling
     * @param answersList                 the answers list
     * @param sensorsInfo                 the sensors info
     */
    public DecisionMode(Context context, Polyline line, int answeredQuestions, StateProgressBar stateProgressBar, QuestionHandling questionHandling, List<AnswerEntity> answersList, SensorsInfo sensorsInfo) {
        this.context = context;

        this.answersList = answersList;
        this.sensorsInfo = sensorsInfo;
        this.answeredQuestions = answeredQuestions;
        this.line = line;

        homeActivity = new HomeActivity();
        homeActivity.missedPath = new ArrayList<>();
        homeActivity.answersList = new ArrayList<>();

        mDeviceId = new DeviceUuidFactory(context);

        this.questionHandling = questionHandling;
        modeFunctions = new ModeFunctions(context, questionHandling, this.answeredQuestions, answersList, stateProgressBar, HomeActivity.diasCheckpointQuestions, sensorsInfo);
    }

    /**
     * This method is used to execute a DecisionMode Assignment
     */
    public void call() {
        try {
            // get all answers in assignment from Room Database/
            List<AnswerEntity> answeredQuestions = new AnswerThread(context).getAnsweredQuestions(utils.loadedFileName);

            List<String> answeredQuestionIds = new ArrayList<>();
            List<String> answeredAssignmentIds = new ArrayList<>();

            //iterate through all the answers of Assignment
            for (int i = 0; i < answeredQuestions.size(); i++) {
                AnswerEntity current = answeredQuestions.get(i);
                //get Assignment Id from each answer and put it into the answeredAssignmentIDs array
                answeredAssignmentIds.add(current.getAssignmentId());
                //get Question Id from each answer and put it into the answeredQuestionIDs array
                answeredQuestionIds.add(String.valueOf(current.getQuestionId()));
            }
            // check if question is in ellipse or nearest the question location then return the question number
            if (questionHandling.QuestionInEllipseNew(QuestionsActivity.selectedAssignment.getMode()) >= 0) {

                //get current question number
                int questionNumber = questionHandling.getQuestionNumber();

                // if not answered then display pop up
                if (!answeredQuestionIds.contains(QuestionsActivity.assignmentQuestions.get(questionNumber).getId() + "") || !answeredAssignmentIds.contains(QuestionsActivity.assignmentQuestions.get(questionNumber).getAssignmentId())) {
                    StaticModel.questionsQueue.add(questionHandling.getQuestionNumber());
                    showDecisionModePopup();
                } else {
                    //check if Current Assignment answers not equal to Database Assignment Answers then
                    // assign current answers list to database answers list
                    // in case if assignment is not completed and want to complete later
                    if (answersList.size() != answeredQuestions.size())
                        answersList = answeredQuestions;

                    //iterate through all the database answers
                    for (int k = 0; k < answeredQuestions.size(); k++) {

                        QuestionEntity currentQuestionEntity = QuestionsActivity.assignmentQuestions.get(k);

                        // get questionType of iterating answer
                        String questionType = currentQuestionEntity.getType();
                        //get questionId of iterating answer
                        int questionId = currentQuestionEntity.getId();
                        //get assignmentId of iterating answer
                        String assignmentId = currentQuestionEntity.getAssignmentId();

                        // if question type is textBox then get the associate question against questionId and AssignmentId and place a marker
                        if (questionType.equals("textBox")) {
                            //initialize the textBox and textBoxDecisionMode worker threads

                            //get textBox against questionId and AssignmentId from Room Database
                            TextBoxEntity textBoxEntity = new TextBoxThread(context).getAllTextBoxesFromQuestionInAssignment(assignmentId, String.valueOf(questionId));
                            // get associate question against textBoxId from Room Database
                            TextBoxDecisionModeEntity textBoxOption = new TextBoxDecisionModeThread(context).getAllAssociateQuestions(String.valueOf(textBoxEntity.getId()));
                            // check if textBoxOption have a associate question then place a marker
                            if (textBoxOption.getNextAssignmentId() != null && !textBoxOption.getNextAssignmentId().equals("")) {
                                questionHandling.setShownQuestion(textBoxOption.getNextQuestionId() - 1);
                                // place a Associate question marker
                                homeActivity.placeMarkers(textBoxOption.getNextQuestionId() - 1);
                            } else {
                                Toast.makeText(context, "No Next Question", Toast.LENGTH_SHORT).show();
                                utils.FileWritingStatus = true;
                            }
                        }

                        // if question type is likertScale then get the associate question against questionId and AssignmentId and place a marker
                        if (questionType.equals("likertScale")) {

                            //get likertScale against questionId and AssignmentId from Room Database
                            LikertScaleEntity likertScaleEntity = new LikertScaleThread(context).getAllLikertScalesFromQuestionInAssignment(assignmentId, String.valueOf(questionId));
                            // get all likertScale Options against likertScaleId from Room Database
                            List<LikertScaleDecisionModeEntity> likertScaleOptions = new LikertScaleDecisionModeThread(context).getAllAssociateQuestions(String.valueOf(likertScaleEntity.getId()));

                            //iterate through all the LikertScale Options
                            for (int i = 0; i < likertScaleOptions.size(); i++) {
                                //check if iterating likertScale option value equals to the answered value then check likertScale Option have a associate
                                //question so place a associate question
                                if (likertScaleOptions.get(i).getValue() == (Integer.valueOf(answeredQuestions.get(k).getAnswer().trim()))) {

                                    // check iterating likertScale Option's have a Associate Question
                                    if (likertScaleOptions.get(i).getNextAssignmentId() != null && !likertScaleOptions.get(i).getNextAssignmentId().equals("")) {
                                        questionHandling.setShownQuestion(likertScaleOptions.get(i).getNextQuestionId() - 1);
                                        //place a Associate Question
                                        homeActivity.placeMarkers(likertScaleOptions.get(i).getNextQuestionId() - 1);
                                    } else {
                                        Toast.makeText(context, "No Next Question", Toast.LENGTH_SHORT).show();
                                        utils.FileWritingStatus = true;
                                    }
                                }//if
                                break;
                            }//for

                        }//likert Scale
                        // if question type is radio then get the associate question against questionId and AssignmentId and place a marker
                        if (questionType.equals("radio")) {
                            //initialize the radioButton worker thread
                            //get all radioButton options from Room Database against questionId and assignmentId
                            List<RadioButtonEntity> radioButtonEntityList = new RadioButtonThread(context).getAllRadioButtonsFromQuestionInAssignment(assignmentId, questionId);
                            //iterate through all the radioButton options
                            for (int i = 0; i < radioButtonEntityList.size(); i++) {

                                //check if iterating radioButton option equals to the answered radioButton Option
                                if (radioButtonEntityList.get(i).getName().trim().equalsIgnoreCase(answeredQuestions.get(i).getAnswer().trim())) {
                                    //get the radioButtonId
                                    int radioId = radioButtonEntityList.get(i).getId();
                                    //get the associate question against radioId
                                    RadioDecisionModeEntity associateQuestion = new RadioDecisionModeThread(context).getRadioDecisionModeQuestion(String.valueOf(radioId));

                                    //check if associate question is not null then place a marker for associate question
                                    if (associateQuestion.getNextAssignmentId() != null && !Objects.equals(associateQuestion.getNextAssignmentId(), "")) {
                                        questionHandling.setShownQuestion(associateQuestion.getNextQuestionId() - 1);
                                        //place a associate question marker
                                        homeActivity.placeMarkers(associateQuestion.getNextQuestionId() - 1);
                                    } else {
                                        Toast.makeText(context, "No Next Question", Toast.LENGTH_SHORT).show();
                                        utils.FileWritingStatus = true;
                                    }
                                }//if
                                break;
                            }//for loop
                        }

                        // if question type is checkBox then get the associate question against questionId and AssignmentId and place a marker
                        if (questionType.equals("checkbox")) {
                            //get all checkbox combinations against questionId and AssignmentId
                            List<CombinationEntity> combinations = new CombinationThread(context).getCombinationFromQuestion(assignmentId, String.valueOf(questionId));
                            //initialize the checkboxDecisionMode worker thread
                            CheckBoxDecisionModeThread checkBoxDecisionModeThread = new CheckBoxDecisionModeThread(context);

                            //get json string from database answered checkbox
                            JSONObject object = new JSONObject(answeredQuestions.get(k).getAnswer().trim());
                            //get keys from answer JSON object
                            JSONArray keys = object.names();

                            // get order from checkbox Answered Json
                            String selectedCheckBoxOrder = "";

                            //check if keys length is 1 then increment to key to get a checkbox selected options order
                            if (keys.length() == 1) {
                                selectedCheckBoxOrder = String.valueOf(Integer.valueOf(keys.getString(0)) + 1);
                            } else {
                                //iterate through all the keys
                                for (int j = 0; j < keys.length(); j++) {
                                    //check if selectedCheckBoxOrder is empty the concatenate the  first order
                                    if (Objects.equals(selectedCheckBoxOrder, "")) {
                                        selectedCheckBoxOrder = String.valueOf(Integer.valueOf(keys.getString(j)) + 1);
                                    } else {
                                        //concatenate the selectedCheckBoxOrder order separated by comma e.g order 1,2,3 means option 1 2 3 are selected
                                        selectedCheckBoxOrder = selectedCheckBoxOrder + "," + (Integer.valueOf(keys.getString(j)) + 1); // Here's your key
                                    }
                                }// for loop
                            }//keys size is greater than 1
                            //iterate through all the checkbox combinations
                            for (int i = 0; i < combinations.size(); i++) {
                                //check if combination order equals to the selectedCheckBoxOrder
                                if (combinations.get(i).getOrder().equals(selectedCheckBoxOrder)) {
                                    // get combinationId
                                    int combinationId = combinations.get(i).getId();
                                    // get associate question against combinationId
                                    CheckBoxDecisionModeEntity checkBoxAssociateQuestion = checkBoxDecisionModeThread.getAssociateQuestion(String.valueOf(combinationId));
                                    // if associate question is not null then place a marker for associate question.
                                    if (checkBoxAssociateQuestion.getNextAssignmentId() != null && !Objects.equals(checkBoxAssociateQuestion.getNextAssignmentId(), "")) {
                                        questionHandling.setShownQuestion(checkBoxAssociateQuestion.getNextQuestionId() - 1);
                                        //place a Associate Marker
                                        homeActivity.placeMarkers(checkBoxAssociateQuestion.getNextQuestionId() - 1);
                                    } else {
                                        Toast.makeText(context, "No Next Question", Toast.LENGTH_SHORT).show();
                                        utils.FileWritingStatus = true;
                                    }//else
                                }// if combination order equals
                            }//for loop
                        }//question type checkbox
                    }// answered questions
                }

                //check if question is answered then add question number in visited points array
                if (!(questionHandling.isDoneNew(questionHandling.getQuestionNumber())))
                    questionHandling.addQuestionInVisitedPointsNew(questionHandling.getQuestionNumber());

                modeFunctions.updateStateBar(context, "decision");
                homeActivity.VisitedCheckPoint++;
                homeActivity.visitedPointsHeading.add("" + homeActivity.VisitedCheckPoint);
                for (int i = 0; i < homeActivity.visitedPointsHeading.size(); i++) {
                    if (homeActivity.visitedPointsHeading.size() > 1)
                        homeActivity.visitedPointsHeading.remove(0);
                }
            } else if (utils.haveNetworkConnection(context) && (!PolyUtil.isLocationOnPath(new LatLng(AppPreferences.getLatitude(context), AppPreferences.getLongitude(context)), line.getPoints(), true, homeActivity.accuracyTolerance))) {
                homeActivity.missedPath.add(new LatLng(AppPreferences.getLatitude(context), AppPreferences.getLongitude(context)));
                homeActivity.isMissed = true;
            }
            //check if current Assignment answers equal to the assignment questions
            if (((answersList.size() == QuestionsActivity.assignmentQuestions.size()) || utils.FileWritingStatus) && modeFunctions.checkEndSurvey()) {
                //check if assignment is ended i-e user is at the destination point or not
                homeActivity.handlerCheck = false;
                homeActivity.handlerForQuestions = null;
                String projectId = AppPreferences.getProjectId(context);
                //send Assignment to the Hive Server
                NetworkCallbacks.AssignmentSubmission(context, projectId, mDeviceId.getDeviceUuid().toString(), QuestionsActivity.selectedAssignment.getId(), answersList);
                modeFunctions.showAllAnswers();
            }
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    /**
     * This method is used to display a popUp for every question in DecisionMode and saves answer.
     */
    private void showDecisionModePopup() {
        try {
            final Dialog qstDialog = new Dialog(context, R.style.actionSheetTheme);
            qstDialog.setContentView(R.layout.popup_layout);
            Window window = qstDialog.getWindow();

            WindowManager.LayoutParams wlp;

            if (window != null)
                wlp = window.getAttributes();
             else
                throw new NullPointerException("The window was null");


            wlp.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
            wlp.y = 200;

            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);

            LinearLayout linearLayout = qstDialog.findViewById(R.id.linearLayout);
            Button save = qstDialog.findViewById(R.id.save);
            Button cancel = qstDialog.findViewById(R.id.cancel);

            //check if current question is mandatory so question must be answered that's why cancel button will be invisible in pop up
            if (QuestionsActivity.assignmentQuestions.get(StaticModel.questionsQueue.get(0)).getMandatory().equalsIgnoreCase("true")) {
                cancel.setEnabled(false);
                cancel.setVisibility(View.INVISIBLE);
            } else {
                cancel.setEnabled(true);
                cancel.setVisibility(View.VISIBLE);
            }

            final RadioButtonViewModel radioButtonViewModel = new RadioButtonViewModel(context);
            final CheckBoxQuestionModel checkBoxQuestionModel = new CheckBoxQuestionModel(context);
            final SeekBarViewModel seekBarViewModel = new SeekBarViewModel(context);
            final EditTextViewModel editTextViewModel = new EditTextViewModel(context);
            linearLayout.removeAllViews();

            TextView textView = new TextView(context);
            textView.setText(R.string.stop_to_answer);
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            textView.setTypeface(null, Typeface.BOLD);

            //check if current question type is radio button then create radio buttons and add it into the linear layout
            if (QuestionsActivity.assignmentQuestions.get(StaticModel.questionsQueue.get(0)).getType().equalsIgnoreCase("radio")) {
                // this method creates a radio button question and its options
                radioButtonViewModel.updateViewNew(QuestionsActivity.assignmentQuestions.get(StaticModel.questionsQueue.get(0)));
                textView.setBackgroundResource(R.drawable.bottom_line);
                linearLayout.addView(textView, 0);
                linearLayout.addView(radioButtonViewModel.getRadioView(), 1);
            }
            //check if current question type is check box then create check boxes and add it into the linear layout
            else if (QuestionsActivity.assignmentQuestions.get(StaticModel.questionsQueue.get(0)).getType().equalsIgnoreCase("checkBox")) {
                // this method creates a checkbox question and its options
                checkBoxQuestionModel.updateViewNew(QuestionsActivity.assignmentQuestions.get(StaticModel.questionsQueue.get(0)));
                textView.setBackgroundResource(R.drawable.bottom_line);
                linearLayout.addView(textView, 0);
                linearLayout.addView(checkBoxQuestionModel.getCheckBoxQuestionView(), 1);
            }

            //check if current question type is likert scale then create seek bar and add it into the linear layout
            else if (QuestionsActivity.assignmentQuestions.get(StaticModel.questionsQueue.get(0)).getType().equalsIgnoreCase("likertScale")) {
                // this method creates a likert scale question and its seekBar
                seekBarViewModel.updateViewNew(QuestionsActivity.assignmentQuestions.get(StaticModel.questionsQueue.get(0)));
                textView.setBackgroundResource(R.drawable.bottom_line);
                linearLayout.addView(textView, 0);
                linearLayout.addView(seekBarViewModel.getSeekBarView(), 1);
            }
            //in this block question type is text box so create textBox and add it into the linear layout
            else {
                // this method creates a textBox question and textBox area
                editTextViewModel.updateViewNew(QuestionsActivity.assignmentQuestions.get(StaticModel.questionsQueue.get(0)));
                textView.setBackgroundResource(R.drawable.bottom_line);
                linearLayout.addView(textView, 0);
                linearLayout.addView(editTextViewModel.getEditTextView(), 1);
            }
            StaticModel.shownQuestionsQueue.add(StaticModel.questionsQueue.get(0));
            StaticModel.questionsQueue.remove(0);

            save.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        int questionCredit = 0;
                        //check if pop up question type is radio then get the value from option
                        if (QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getType().equalsIgnoreCase("radio")) {
                            String result = radioButtonViewModel.getValue();
                            if (result != null) {
                                // get pop up question Id
                                int qid = QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getId();
                                //get pop up question's Assignment Id
                                String aId = QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getAssignmentId();
                                //initialize the radiobutton worker thread
                                // get radio button options against pop up question id and Assignment id from Room Database
                                List<RadioButtonEntity> radioButtonEntityList = new RadioButtonThread(context).getAllRadioButtonsFromQuestionInAssignment(aId, qid);
                                if (radioButtonEntityList.get(radioButtonViewModel.getSelectedId()).getCredits() != null) {
                                    //get selected radio button option credits
                                    String customCredit = radioButtonEntityList.get(radioButtonViewModel.getSelectedId()).getCredits();

                                    //if empty then set credits to default which is 5
                                    if (TextUtils.isEmpty(customCredit))
                                        customCredit = QuestionsActivity.selectedAssignment.getDefaultCredit();

                                    int parsed = Integer.parseInt(customCredit);
                                    homeActivity.mCredits += parsed;
                                    questionCredit = parsed;
                                }// credits not null


                                // save credit, option value against question data
                                modeFunctions.saveAnswer(questionCredit, result, QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)));

                                //change the marker into black that is visited question
                                homeActivity.placeMarkers(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1));

                                //iterate through all the radio button options
                                for (int i = 0; i < radioButtonEntityList.size(); i++) {
                                    //check if radio button option is equal to selected radio button option then
                                    //check if selected radio button option have associate question save sensor readings
                                    // and then place a associate marker if not associate question then save the senor readings of selected radio button option
                                    if (radioButtonEntityList.get(i).getName().trim().equalsIgnoreCase(result.trim())) {

                                        //get the radioButton Id from radio button
                                        int radioId = radioButtonEntityList.get(i).getId();

                                        //get the associate question against radioButton Id from room Database
                                        RadioDecisionModeEntity associateQuestion = new RadioDecisionModeThread(context).getRadioDecisionModeQuestion(String.valueOf(radioId));

                                        // check if associate question in not null then place associate question marker else save the answer sensor readings
                                        StaticModel.questionsSensorsQueue.add(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1));
                                        sensorsInfo.saveSensorsValue(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1), result);
                                        StaticModel.shownQuestionsQueue.remove(StaticModel.shownQuestionsQueue.size() - 1);

                                        answeredQuestions++;
                                        qstDialog.dismiss();
                                        if (associateQuestion != null) {
                                            questionHandling.setShownQuestion(associateQuestion.getNextQuestionId() - 1);
                                            //place the associate question marker
                                            homeActivity.placeMarkers(associateQuestion.getNextQuestionId() - 1);
                                        } else
                                            Toast.makeText(context, "No Next Question", Toast.LENGTH_SHORT).show();
                                        break;
                                    }//if
                                }//for loop
                            }//radio value is not null
                        }// type radio

                        //check if pop up question type is LikertScale then get the likertScale answer
                        else if (QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getType().equalsIgnoreCase("likertScale")) {
                            String result = seekBarViewModel.getValue();

                            if (result != null) {
                                //get  popup question Id
                                int qid = QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getId();
                                //get pop up question's Assignment Id
                                String aId = QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getAssignmentId();
                                //get LikertScale data against pop up question id and Assignment id from Room Database
                                LikertScaleEntity likertScaleEntity = new LikertScaleThread(context).getAllLikertScalesFromQuestionInAssignment(aId, qid + "");

                                //get the likertScale credits if null then set credits to default which is 5
                                if (likertScaleEntity.getCredits() != null) {
                                    String customCredit = likertScaleEntity.getCredits();

                                    //if empty then set credits to default which is 5
                                    if (TextUtils.isEmpty(customCredit))
                                        customCredit = QuestionsActivity.selectedAssignment.getDefaultCredit();

                                    int parsed = Integer.parseInt(customCredit);
                                    homeActivity.mCredits += parsed;
                                    questionCredit = parsed;
                                }
                                //save likertScale Answer into the Room database
                                modeFunctions.saveAnswer(questionCredit, result, QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)));
                                //change the marker into black that is visited question
                                homeActivity.placeMarkers(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1));
                                // initialize the LikertScaleDecisionMode worker thread
                                LikertScaleDecisionModeThread l1 = new LikertScaleDecisionModeThread(context);
                                //get all likertScales against likertScaleId Id from room Database
                                List<LikertScaleDecisionModeEntity> likertScaleOptions = new LikertScaleDecisionModeThread(context).getAllAssociateQuestions(likertScaleEntity.getId() + "");

                                // check if likertScaleOptions not equal to 0 then
                                if (likertScaleOptions.size() != 0) {
                                    //iterate through all the likertScaleOptions
                                    for (int i = 0; i < likertScaleOptions.size(); i++) {
                                        //check if iterating likertScale value equal to the answered likertScale value
                                        //then check if answered likertScale Value have a  associate question or not
                                        if (likertScaleOptions.get(i).getValue() == (Integer.valueOf(result))) {
                                            //get likertScaleIf from likertScaleOptions
                                            int likertScaleId = likertScaleOptions.get(i).getId();
                                            //get Associate Question against likertScale id from Room Database
                                            LikertScaleDecisionModeEntity associateQuestion = l1.getAssociateQuestion(String.valueOf(likertScaleId));

                                            StaticModel.questionsSensorsQueue.add(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1));
                                            sensorsInfo.saveSensorsValue(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1), result);
                                            StaticModel.shownQuestionsQueue.remove(StaticModel.shownQuestionsQueue.size() - 1);
                                            answeredQuestions++;
                                            qstDialog.dismiss();

                                            //if associate question not equal to null then save the sensor readings of likert scale while answering and place the associate question
                                            if (associateQuestion != null) {
                                                questionHandling.setShownQuestion(associateQuestion.getNextQuestionId() - 1);
                                                //place the associate question marker
                                                homeActivity.placeMarkers(associateQuestion.getNextQuestionId() - 1);
                                            } else
                                                Toast.makeText(context, "No Next Question", Toast.LENGTH_SHORT).show();

                                        }//if
                                    }//for
                                }
                                // in this block if likertScaleOptions are zero then likert scale not have a associate question
                                //then save the sensor readings
                                else {
                                    StaticModel.questionsSensorsQueue.add(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1));
                                    sensorsInfo.saveSensorsValue(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1), result);
                                    StaticModel.shownQuestionsQueue.remove(StaticModel.shownQuestionsQueue.size() - 1);
                                    answeredQuestions++;
                                    qstDialog.dismiss();
                                    Toast.makeText(context, "No Next Question", Toast.LENGTH_SHORT).show();
                                }
                            }//likertScale value is not null
                        }//likertScale type


                        //check if pop up question type is text box then get the text box answer
                        else if (QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getType().equalsIgnoreCase("textBox")) {
                            String result = editTextViewModel.getValue();

                            if (result != null) {
                                //get pop up question Id
                                int qid = QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getId();
                                //get pop up question's Assignment Id
                                String aId = QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getAssignmentId();

                                //get the tex box against pop up question id and Assignment id from Room Database
                                TextBoxEntity textBoxEntity = new TextBoxThread(context).getAllTextBoxesFromQuestionInAssignment(aId, qid + "");

                                //get the textBox credits if null then set credits to default which is 5
                                if (textBoxEntity.getCredits() != null) {
                                    String customCredit = textBoxEntity.getCredits();
                                    //if empty then set credits to default which is 5
                                    if (TextUtils.isEmpty(customCredit))
                                        customCredit = QuestionsActivity.selectedAssignment.getDefaultCredit();

                                    int parsed = Integer.parseInt(customCredit);
                                    homeActivity.mCredits += parsed;
                                    questionCredit = parsed;
                                }

                                //save textBox Answer into the Room database
                                modeFunctions.saveAnswer(questionCredit, result, QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)));
                                //change the marker into black that is visited question
                                homeActivity.placeMarkers(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1));
                                //initialize the TextBoxDecisionMode worker thread
                                //get associate question against textBox from Room Database
                                TextBoxDecisionModeEntity textBoxDecisionModeEntity = new TextBoxDecisionModeThread(context).getAllAssociateQuestions(textBoxEntity.getId() + "");

                                StaticModel.questionsSensorsQueue.add(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1));
                                sensorsInfo.saveSensorsValue(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1), result);
                                StaticModel.shownQuestionsQueue.remove(StaticModel.shownQuestionsQueue.size() - 1);
                                answeredQuestions++;
                                qstDialog.dismiss();

                                //check if associate question is not null then place Associate marker
                                if (textBoxDecisionModeEntity != null) {
                                    questionHandling.setShownQuestion(textBoxDecisionModeEntity.getNextQuestionId() - 1);
                                    //place the associate question marker
                                    homeActivity.placeMarkers(textBoxDecisionModeEntity.getNextQuestionId() - 1);
                                } else
                                    Toast.makeText(context, "No Next Question", Toast.LENGTH_SHORT).show();
                            }// not null result

                        }

                        //else question type is checkbox then get the checkbox selected options
                        else {
                            //get Checkbox options from pop up
                            HashMap<String, String> result = checkBoxQuestionModel.getResult();

                            if (result != null) {

                                Gson gson = new Gson();
                                // set selected checkbox options into JSON as a string
                                String answer = gson.toJson(result);

                                List<String> idList = new ArrayList<>();

                                //iterate through all selected checkbox options

                                for (Map.Entry<String, String> stringStringEntry : result.entrySet())
                                    idList.add(((HashMap.Entry) stringStringEntry).getKey().toString());


                                //get pop up question Id
                                int qId = QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getId();
                                //get pop up question's Assignment Id
                                String aId = QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getAssignmentId();

                                //get All combinations of checkboxes against pop up question id and Assignment id from Room Database
                                List<CombinationEntity> combination = new CombinationThread(context).getCombinationFromQuestion(aId, String.valueOf(qId));
                                //get selected combination order Id
                                int combinationId = modeFunctions.getCombinationId(idList, combination);

                                //check combination id >= 0 then get the combination credits if empty then set credits to default which is 5
                                if (combinationId >= 0) {
                                    if (TextUtils.isEmpty(combination.get(combinationId).getCredits())) {
                                        homeActivity.mCredits += Integer.parseInt(QuestionsActivity.selectedAssignment.getDefaultCredit());
                                        questionCredit = Integer.parseInt(QuestionsActivity.selectedAssignment.getDefaultCredit());
                                    } else {
                                        homeActivity.mCredits += Integer.parseInt(combination.get(combinationId).getCredits());
                                        questionCredit = Integer.parseInt(combination.get(combinationId).getCredits());
                                    }
                                }
                                //save checkbox Answer into the Room database
                                modeFunctions.saveAnswer(questionCredit, answer, QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)));
                                //change the marker into black that is visited question
                                homeActivity.placeMarkers(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1));

                                if (combinationId >= 0) {
                                    // get associate question against combinationId
                                    CheckBoxDecisionModeEntity c1 = new CheckBoxDecisionModeThread(context).getAssociateQuestion(String.valueOf(combinationId + 1));

                                    sensorsInfo.saveSensorsValue(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1), answer);
                                    StaticModel.questionsSensorsQueue.add(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1));
                                    StaticModel.shownQuestionsQueue.remove(StaticModel.shownQuestionsQueue.size() - 1);
                                    answeredQuestions++;
                                    qstDialog.dismiss();

                                    //check if associate question is not null then place Associate marker
                                    if (c1 != null) {
                                        questionHandling.setShownQuestion(c1.getNextQuestionId() - 1);
                                        //place the associate question marker
                                        homeActivity.placeMarkers(c1.getNextQuestionId() - 1);
                                    } else
                                        Toast.makeText(context, "No Next Question", Toast.LENGTH_SHORT).show();

                                }//combinationId >=0

                            }// result not null
                        }
                    } catch (Exception ex) {
                        ExceptionalHandling.logException(ex);
                    }


                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if (QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getMandatory().equalsIgnoreCase("false")) {
                            StaticModel.skippedQuestions.add(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1));
                            StaticModel.questionsSensorsQueue.add(StaticModel.shownQuestionsQueue.size() - 1);

                            sensorsInfo.saveSensorsValue(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1), "");
                            StaticModel.shownQuestionsQueue.remove(StaticModel.shownQuestionsQueue.size() - 1);

                            qstDialog.cancel();
                            homeActivity.isDiasQuestionShowing = false;
                        }
                    } catch (Exception ex) {
                        ExceptionalHandling.logException(ex);
                    }
                }
            });

            qstDialog.show();
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

}
