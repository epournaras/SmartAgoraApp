package smartagora.ethz.ch.modes;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import smartagora.ethz.ch.networkRequests.NetworkCallbacks;
import smartagora.ethz.ch.R;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.AnswerThread;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.CombinationThread;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.LikertScaleThread;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.RadioButtonThread;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.TextBoxThread;
import smartagora.ethz.ch.roomDatabase.entityModels.AnswerEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.CombinationEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.LikertScaleEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.RadioButtonEntity;
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

import com.google.gson.Gson;
import com.kofigyan.stateprogressbar.StateProgressBar;

/**
 * SimpleMode Class has responsibility to initialize the simpleMode Assignment and simpleMode PopUp method for displaying questions.
 */
public class SimpleMode{

    private final Context context;
    private DeviceUuidFactory mDeviceId = null;
    private final QuestionHandling questionHandling;
    private final ModeFunctions modeFunctions;
    private final SensorsInfo sensorsInfo;
    private int answeredQuestions;
    private final HomeActivity homeActivity;
    private final StateProgressBar stateProgressBar;

    /**
     * Instantiates a SimpleMode.
     *
     * @param context                     the context
     * @param answeredQuestions           the answered questions
     * @param stateProgressBar            the state progress bar
     * @param questionHandling            the question handling
     * @param sensorsInfo                 the sensorsInfo object
     */
    public SimpleMode(Context context, int answeredQuestions, StateProgressBar stateProgressBar, QuestionHandling questionHandling, SensorsInfo sensorsInfo) {

        this.context = context;
        this.sensorsInfo = sensorsInfo;
        this.answeredQuestions = answeredQuestions;
        this.stateProgressBar = stateProgressBar;
        homeActivity = new HomeActivity();
        homeActivity.answersList = new ArrayList<>();
        if (mDeviceId == null) {
            mDeviceId = new DeviceUuidFactory(context);
        }

        this.questionHandling = questionHandling;

        modeFunctions = new ModeFunctions(context, questionHandling, this.answeredQuestions, homeActivity.answersList, stateProgressBar, HomeActivity.diasCheckpointQuestions, sensorsInfo);
    }

    /**
     * This method is used to execute a SimpleMode Assignment
     */
    public void call() {
        try {

            // check if question is in ellipse or nearest the question location then return the question number
            if (questionHandling.QuestionInEllipseNew(QuestionsActivity.selectedAssignment.getMode()) >= 0) {

                //initialize the Answers worker thread
                AnswerThread answerThread = new AnswerThread(context);
                //get all answers of this Assignment from Room Database
                List<AnswerEntity> answeredQuestions = answerThread.getAnsweredQuestions(utils.loadedFileName);

                List<String> answeredQuestionIds = new ArrayList<>();
                List<String> answeredAssignmentIds = new ArrayList<>();

                //iterate through all the answers of Assignment
                for (int i = 0; i < answeredQuestions.size(); i++) {
                    //get Assignment Id from each answer and put it into the answeredAssignmentIDs array
                    answeredAssignmentIds.add(answeredQuestions.get(i).getAssignmentId());
                    //get Question Id from each answer and put it into the answeredQuestionIDs array
                    answeredQuestionIds.add(String.valueOf(answeredQuestions.get(i).getQuestionId()));
                }

                //get current question number
                int questionNumber = questionHandling.getQuestionNumber();

                //check if current question id and assignment id is in answered array lists
                if (!answeredQuestionIds.contains(QuestionsActivity.assignmentQuestions.get(questionNumber).getId() + "") || !answeredAssignmentIds.contains(QuestionsActivity.assignmentQuestions.get(questionNumber).getAssignmentId())) {
                    StaticModel.questionsQueue.add(questionHandling.getQuestionNumber());
                    showSimpleModePopUp();
                }

                //add question in visited points array. i-e this question is visited and answered
                questionHandling.addQuestionInVisitedPointsNew(questionHandling.getQuestionNumber());
                modeFunctions.updateStateBar(context, "simple");
                homeActivity.VisitedCheckPoint++;
                homeActivity.visitedPointsHeading.add("" + homeActivity.VisitedCheckPoint);
                for (int i = 0; i < homeActivity.visitedPointsHeading.size(); i++) {
                    if (homeActivity.visitedPointsHeading.size() > 1) {
                        homeActivity.visitedPointsHeading.remove(0);
                    }
                }
            }

            AnswerThread answerThread = new AnswerThread(context);
            // get all answers from assignment
            List<AnswerEntity> answers = answerThread.getAnswersFromAssignment(utils.loadedFileName);

            //check if Current Assignment answers not equal to Database Assignment Answers then
            // assign current answers list to database answers list
            // in case if assignment is not completed and want to complete later
            if (homeActivity.answersList.size() != answers.size()) {
                homeActivity.answersList = answers;
            }

            // check if current Assignment answers equal to the Assignment Questions and database assignment answers equal to Assignment Questions
            //then assignment will be completed
            if (homeActivity.answersList.size() == QuestionsActivity.assignmentQuestions.size()) {
                stateProgressBar.setAllStatesCompleted(true);
                if (utils.FileWritingStatus) {
                    utils.FileWritingStatus = false;
                    homeActivity.handlerCheck = false;
                    homeActivity.handlerForQuestions = null;
                    String projectId = AppPreferences.getProjectId(context);
                    // send Assignment Data to the Hive Server
                    NetworkCallbacks.AssignmentSubmission(context, projectId, mDeviceId.getDeviceUuid().toString(), QuestionsActivity.selectedAssignment.getId(), homeActivity.answersList);
                    // show all answers to user when user complete survey
                    modeFunctions.showAllAnswers();
                }
            }

        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }


    /**
     * This method is used to display a popUp for every question in SimpleMode and saves answer.
     */
    private void showSimpleModePopUp() {

        try {
            final Dialog qstDialog = new Dialog(context, R.style.actionSheetTheme);
            qstDialog.setContentView(R.layout.popup_layout);
            Window window = qstDialog.getWindow();

            if(window == null){
                Toast.makeText(context, "error when trying to show popup", Toast.LENGTH_SHORT).show();
                Log.e("SimpleMode:showSim*","window was null");
                return;
            }

            WindowManager.LayoutParams wlp = window.getAttributes();

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
                        //check if pop up question type is radio then save the Radio button answer into the Room database
                        if (QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getType().equalsIgnoreCase("radio")) {
                            String result = radioButtonViewModel.getValue();

                            if (result != null) {

                                // get pop up question Id
                                int qid = QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getId();

                                //get pop up question's Assignment Id
                                String aId = QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getAssignmentId();

                                //initialize the radiobutton worker thread
                                RadioButtonThread radioButtonThread = new RadioButtonThread(context);

                                // get radio button options against pop up question id and Assignment id from Room Database
                                List<RadioButtonEntity> radioButtonEntityList = radioButtonThread.getAllRadioButtonsFromQuestionInAssignment(aId, qid);


                                if (radioButtonEntityList.get(radioButtonViewModel.getSelectedId()).getCredits() != null) {

                                    //get selected radio button option credits
                                    String customCredit = radioButtonEntityList.get(radioButtonViewModel.getSelectedId()).getCredits();

                                    //if empty then set credits to default which is 5
                                    if (TextUtils.isEmpty(customCredit)) {

                                        homeActivity.mCredits += Integer.parseInt(QuestionsActivity.selectedAssignment.getDefaultCredit());
                                        questionCredit = Integer.parseInt(QuestionsActivity.selectedAssignment.getDefaultCredit());

                                    } else {

                                        homeActivity.mCredits += Integer.parseInt(customCredit);
                                        questionCredit = Integer.parseInt(customCredit);

                                    }

                                }// credits not null


                                // save credit, option value against question data
                                modeFunctions.saveAnswer(questionCredit, result, QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)));
                                //change the marker into black that is visited question
                                homeActivity.placeMarkers(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1));
                                StaticModel.questionsSensorsQueue.add(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1));
                                //save sensors reading into the Database
                                sensorsInfo.saveSensorsValue(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1), result);
                                StaticModel.shownQuestionsQueue.remove(StaticModel.shownQuestionsQueue.size() - 1);
                                answeredQuestions++;
                                qstDialog.dismiss();

                            }//radio value is not null
                        }// type radio


                        //check if pop up question type is LikertScale then save the likertScale answer into the Room database
                        else if (QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getType().equalsIgnoreCase("likertScale")) {
                            String result = seekBarViewModel.getValue();

                            if (result != null) {

                                //get  popup question Id
                                int qid = QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getId();
                                //get pop up question's Assignment Id
                                String aId = QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getAssignmentId();
                                //initialize the likertScale worker thread
                                LikertScaleThread likertScaleThread = new LikertScaleThread(context);
                                //get LikertScale data against pop up question id and Assignment id from Room Database
                                LikertScaleEntity likertScaleEntity = likertScaleThread.getAllLikertScalesFromQuestionInAssignment(aId, qid + "");


                                //get the likertScale credits if null then set credits to default which is 5
                                if (likertScaleEntity.getCredits() != null) {
                                    String customCredit = likertScaleEntity.getCredits();
                                    if (TextUtils.isEmpty(customCredit)) {
                                        homeActivity.mCredits += Integer.parseInt(QuestionsActivity.selectedAssignment.getDefaultCredit());
                                        questionCredit = Integer.parseInt(QuestionsActivity.selectedAssignment.getDefaultCredit());

                                    } else {
                                        homeActivity.mCredits += Integer.parseInt(customCredit);
                                        questionCredit = Integer.parseInt(customCredit);

                                    }
                                }

                                //save LikertScale Answer into the Room database
                                modeFunctions.saveAnswer(questionCredit, result, QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)));
                                //change the marker into black that is visited question
                                homeActivity.placeMarkers(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1));
                                StaticModel.questionsSensorsQueue.add(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1));
                                //save sensors reading into the Room Database
                                sensorsInfo.saveSensorsValue(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1), result);
                                StaticModel.shownQuestionsQueue.remove(StaticModel.shownQuestionsQueue.size() - 1);
                                answeredQuestions++;
                                qstDialog.dismiss();


                            }//likertScale value is not null
                        }//likertScale type


                        //check if pop up question type is text box then save the text box answer into the Room database
                        else if (QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getType().equalsIgnoreCase("textBox")) {
                            String result = editTextViewModel.getValue();

                            if (result != null) {

                                //get pop up question Id
                                int qid = QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getId();
                                //get pop up question's Assignment Id
                                String aId = QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getAssignmentId();

                                //initialize the TextBox worker thread
                                TextBoxThread textBoxThread = new TextBoxThread(context);
                                //get TextBox data against pop up question id and Assignment id from Room Database
                                TextBoxEntity textBoxEntity = textBoxThread.getAllTextBoxesFromQuestionInAssignment(aId, qid + "");

                                //get the textBox credits if null then set credits to default which is 5
                                if (textBoxEntity.getCredits() != null) {
                                    String customCredit = textBoxEntity.getCredits();
                                    if (TextUtils.isEmpty(customCredit)) {
                                        homeActivity.mCredits += Integer.parseInt(QuestionsActivity.selectedAssignment.getDefaultCredit());
                                        questionCredit = Integer.parseInt(QuestionsActivity.selectedAssignment.getDefaultCredit());

                                    } else {
                                        homeActivity.mCredits += Integer.parseInt(customCredit);
                                        questionCredit = Integer.parseInt(customCredit);
                                    }
                                }

                                //save textBox Answer into the Room database
                                modeFunctions.saveAnswer(questionCredit, result, QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)));
                                //change the marker into black that is visited question
                                homeActivity.placeMarkers(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1));
                                StaticModel.questionsSensorsQueue.add(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1));
                                //save sensors reading into the Room Database
                                sensorsInfo.saveSensorsValue(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1), result);
                                StaticModel.shownQuestionsQueue.remove(StaticModel.shownQuestionsQueue.size() - 1);
                                answeredQuestions++;
                                qstDialog.dismiss();


                            }// textBox result
                        }//textBox


                        //else question type is checkbox then save the checkbox answer into the Room database
                        else {

                            //get Checkbox options from pop up
                            HashMap<String, String> result = checkBoxQuestionModel.getResult();
                            if (result != null) {

                                Gson gson = new Gson();
                                // set selected checkbox options into JSON as a string
                                String answer = gson.toJson(result);

                                List<String> idList = new ArrayList<>();

                                //iterate through all selected checkbox options

                                for (Map.Entry<String, String> stringStringEntry : result.entrySet()) {
                                    idList.add(((HashMap.Entry) stringStringEntry).getKey().toString());
                                }


                                //get pop up question Id
                                int qId = QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getId();
                                //get pop up question's Assignment Id
                                String aId = QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getAssignmentId();

                                //initialize the combination worker thread
                                CombinationThread combinationThread = new CombinationThread(context);

                                //get All combinations of checkboxes against pop up question id and Assignment id from Room Database
                                List<CombinationEntity> combinations = combinationThread.getCombinationFromQuestion(aId, String.valueOf(qId));
                                //get selected combination order Id
                                int combinationId = modeFunctions.getCombinationId(idList, combinations);

                                //check combination id >= 0 then get the combination credits if empty then set credits to default which is 5
                                if (combinationId >= 0) {
                                    if (TextUtils.isEmpty(combinations.get(combinationId).getCredits())) {
                                        homeActivity.mCredits += Integer.parseInt(QuestionsActivity.selectedAssignment.getDefaultCredit());
                                        questionCredit = Integer.parseInt(QuestionsActivity.selectedAssignment.getDefaultCredit());
                                    } else {
                                        homeActivity.mCredits += Integer.parseInt(combinations.get(combinationId).getCredits());
                                        questionCredit = Integer.parseInt(combinations.get(combinationId).getCredits());
                                    }

                                }

                                //save checkbox Answer into the Room database
                                modeFunctions.saveAnswer(questionCredit, answer, QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)));
                                //change the marker into black that is visited question
                                homeActivity.placeMarkers(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1));
                                //save sensors reading into the Room Database
                                sensorsInfo.saveSensorsValue(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1), answer);
                                StaticModel.questionsSensorsQueue.add(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1));
                                StaticModel.shownQuestionsQueue.remove(StaticModel.shownQuestionsQueue.size() - 1);
                                answeredQuestions++;
                                qstDialog.dismiss();


                            }//checkbox Result
                        }//checkbox
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
