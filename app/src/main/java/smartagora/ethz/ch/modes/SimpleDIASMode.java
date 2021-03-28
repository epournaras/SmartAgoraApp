

package smartagora.ethz.ch.modes;

import android.annotation.SuppressLint;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import diasclient.coss.ethz.ch.diasclientlibrarylight.AndroidDependent.apiinterface.MappingWrapper;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Enums.AggregationType;
import diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.Network.AggregateResult;
import smartagora.ethz.ch.networkRequests.NetworkCallbacks;
import smartagora.ethz.ch.R;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.AnswerThread;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.LikertScaleThread;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.QuestionThread;
import smartagora.ethz.ch.roomDatabase.entityModels.AnswerEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.LikertScaleEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.QuestionEntity;

import com.google.android.gms.maps.model.LatLng;
import com.kofigyan.stateprogressbar.StateProgressBar;

import smartagora.ethz.ch.models.StaticModel;
import smartagora.ethz.ch.prefrences.AppPreferences;
import smartagora.ethz.ch.uiActivities.HomeActivity;
import smartagora.ethz.ch.uiActivities.QuestionsActivity;
import smartagora.ethz.ch.utils.DeviceUuidFactory;
import smartagora.ethz.ch.utils.ExceptionalHandling;
import smartagora.ethz.ch.utils.QuestionHandling;
import smartagora.ethz.ch.utils.utils;
import smartagora.ethz.ch.viewmodels.SeekBarViewModel;

import static smartagora.ethz.ch.uiActivities.HomeActivity.isLeaveDiasCheckPoint;
import static smartagora.ethz.ch.uiActivities.HomeActivity.localAnswers;

/**
 * SimpleDIASMode Class has responsibility to initialize the simpleDIASMode Assignment and simpleMode PopUp method for displaying questions.
 */
public class SimpleDIASMode{

    private final Context context;
    private final QuestionHandling questionHandling;
    private final List<AnswerEntity> answersList;
    private DeviceUuidFactory mDeviceId;
    private final ModeFunctions modeFunctions;
    private final SensorsInfo sensorsInfo;
    private int answeredQuestions;
    private final HomeActivity homeActivity;
    private final StateProgressBar stateProgressBar;
    private MappingWrapper mappingWrapper;
    private final List<Integer> diasCheckpointQuestions;
    static int questionNumber;
    private final HashMap<String, Double> diasSimpleLikertScaleAnswers;
    private final TextView diasPersonTv;
    private final TextView diasGroupTv;
    private static int diasUniqueCheckpoint;
    static Double personAvg = 0.0;
    private Double diasResult = 0.0;
    private final String gatewayIP = "78.46.75.138:4987";

    private String currentQuestionLatitude;
    private String currentQuestionLongitude;

    private List<QuestionEntity> checkPointQuestions = new ArrayList<>();
    private List<QuestionEntity> checkPointAnsweredQuestions = new ArrayList<>();
    //initialize the question and answer worker threads
    private final QuestionThread questionThread;
    private final AnswerThread answerThread;
    private final ImageView diasShowGroup, diasShow;


    /**
     * Instantiates a SimpleDIASMode
     *
     * @param context                      the context
     * @param stateProgressBar             the state progress bar
     * @param questionHandling             the question handling
     * @param answeredQuestions            the answered questions
     * @param diasCheckpointQuestions      the dias checkpoint questions
     * @param diasSimpleLikertScaleAnswers the dias simple likert scale answers
     * @param answersList                  the answers list
     * @param sensorsInfo                  the sensors info
     * @param diasGroupTv   the dias group tv
     * @param diasPersonTv  the dias person tv
     * @param diasShowGroup the dias show group
     * @param diasShow      the dias show
     */
    public SimpleDIASMode(Context context, StateProgressBar stateProgressBar, QuestionHandling questionHandling, int answeredQuestions, List<Integer> diasCheckpointQuestions, HashMap<String, Double> diasSimpleLikertScaleAnswers, List<AnswerEntity> answersList, SensorsInfo sensorsInfo, TextView diasGroupTv, TextView diasPersonTv, ImageView diasShowGroup, ImageView diasShow) {


        this.context = context;
        questionThread = new QuestionThread(context);
        answerThread = new AnswerThread(context);
        this.answersList = answersList;
        this.sensorsInfo = sensorsInfo;
        this.answeredQuestions = answeredQuestions;
        this.stateProgressBar = stateProgressBar;

        this.mappingWrapper = new MappingWrapper(context, gatewayIP);
        this.mappingWrapper.reset();

        this.diasCheckpointQuestions = diasCheckpointQuestions;

        homeActivity = new HomeActivity();
        this.questionHandling = questionHandling;
        homeActivity.answersList = new ArrayList<>();
        this.diasSimpleLikertScaleAnswers = diasSimpleLikertScaleAnswers;

        if (mDeviceId == null) {
            mDeviceId = new DeviceUuidFactory(context);
        }

        this.diasPersonTv = diasPersonTv;
        this.diasGroupTv = diasGroupTv;
        this.diasShowGroup = diasShowGroup;
        this.diasShow = diasShow;

        modeFunctions = new ModeFunctions(context, this.questionHandling, this.answeredQuestions, answersList, stateProgressBar, diasCheckpointQuestions, sensorsInfo);
    }

    /**
     * This method is used to execute a SimpleDIASMode Assignment
     */
    @SuppressLint("SetTextI18n")
    public void call() {
        diasGroupTv.setVisibility(View.VISIBLE);
        diasPersonTv.setVisibility(View.VISIBLE);
        diasShowGroup.setVisibility(View.VISIBLE);
        diasShow.setVisibility(View.VISIBLE);

        try {


            // check if question is in ellipse or nearest the question location then return the question number
            if (!homeActivity.isDiasQuestionShowing && questionHandling.QuestionInEllipseNew(QuestionsActivity.selectedAssignment.getMode()) >= 0) {

                if (mappingWrapper == null) {
                    mappingWrapper = new MappingWrapper(context, gatewayIP);
                    this.mappingWrapper.reset();
                }

                //get the question number
                //set question number to static variable
                questionNumber = questionHandling.getQuestionNumber();

                if (questionNumber >= 0) {
                    QuestionEntity question = questionHandling.getCurrentQuestionNew();
                    diasUniqueCheckpoint = utils.getUniqueCheckpointId(question.getLatitude(), question.getLongitude());
                }

                // Get Ellipse Question Id , and its latitude and longitude points
                // questionHandling.setQuestionNumber(q);
                //get question Id from current question number
                int currentQuestionId = QuestionsActivity.assignmentQuestions.get(questionNumber).getId();
                String currentAssignmentId = QuestionsActivity.assignmentQuestions.get(questionNumber).getAssignmentId();
                //get latitude from from current question number
                currentQuestionLatitude = QuestionsActivity.assignmentQuestions.get(currentQuestionId - 1).getLatitude();
                //get longitude from from current question number
                currentQuestionLongitude = QuestionsActivity.assignmentQuestions.get(currentQuestionId - 1).getLongitude();


                //get All checkpoint Question on specific Location in Assignment
                checkPointQuestions = questionThread.getCheckPointQuestions(utils.loadedFileName, currentQuestionLatitude, currentQuestionLongitude);
                //get All checkpoint answered questions on specific location in Assignment
                checkPointAnsweredQuestions = answerThread.getCheckPointAnsweredQuestions(utils.loadedFileName, currentQuestionLatitude, currentQuestionLongitude);

                //get All group point local values from database
                List<AnswerEntity> localValues = answerThread.getLocalAggregateValues(utils.loadedFileName, currentQuestionLatitude, currentQuestionLongitude);

                List<String> answeredQuestionIds = new ArrayList<>();
                List<String> answeredAssignmentIds = new ArrayList<>();

                localAnswers.clear();
                for (int c = 0; c < checkPointAnsweredQuestions.size(); c++) {
                    answeredQuestionIds.add(String.valueOf(checkPointAnsweredQuestions.get(c).getId()));
                    answeredAssignmentIds.add(checkPointAnsweredQuestions.get(c).getAssignmentId());

                    localAnswers.add(Double.valueOf(localValues.get(c).getAnswer()));
                }

                AggregateResult ar1 = mappingWrapper.getAggregate(String.valueOf(diasUniqueCheckpoint), AggregationType.avg);
                diasResult = ar1.get(3000);
                if (diasResult != null)
                    diasGroupTv.setText(String.valueOf(diasResult));//here value from preDIASAggregatedValue will be set.


                //if checkpoint questions not equal to checkpoint answers then display the question popup on checkpoint
                if (checkPointQuestions.size() != checkPointAnsweredQuestions.size()) {

                    // Check if question is already answered then add question to the diasCheckpointQuestions which are answered
                    // Technically,We check that current question id and assignment Id exists in this checkpoint answered questions list
                    //If Exist so this question is answered
                    if (answeredQuestionIds.contains(currentQuestionId + "") && answeredAssignmentIds.contains(currentAssignmentId)) {
                        diasCheckpointQuestions.add(questionNumber);
                    }else {
                        StaticModel.questionsQueue.add(questionNumber);
                        QuestionEntity question = questionHandling.getCurrentQuestionNew();
                        LatLng diasCheckpoint = new LatLng(Double.parseDouble(question.getLatitude()), Double.parseDouble(question.getLongitude()));
                        utils.getDiasCheckPointLeaveStatus(diasCheckpoint, HomeActivity.myLocation);
                        showSimpleDiasModePopUp();
                    }

                    homeActivity.isDiasQuestionShowing = true;
                    questionHandling.addQuestionInVisitedPointsNew(questionNumber);

                }

                modeFunctions.updateStateBar(context, "simple");
                homeActivity.VisitedCheckPoint++;
                homeActivity.visitedPointsHeading.add("" + homeActivity.VisitedCheckPoint);
                for (int i = 0; i < homeActivity.visitedPointsHeading.size(); i++) {
                    if (homeActivity.visitedPointsHeading.size() > 1) {
                        homeActivity.visitedPointsHeading.remove(0);
                    }
                }
            }

            //get All group point answered questions from Room Database
            checkPointAnsweredQuestions = answerThread.getCheckPointAnsweredQuestions(utils.loadedFileName, currentQuestionLatitude, currentQuestionLongitude);
            // get all answers from selected assignment for sending to the dashboard
            List<AnswerEntity> answers = answerThread.getAnswersFromAssignment(utils.loadedFileName);

            //check if Current Assignment answers not equal to Database Assignment Answers then
            // assign current answers list to database answers list
            // in case if assignment is not completed and want to complete later
            if (homeActivity.answersList.size() != answers.size()) {
                homeActivity.answersList = answers;
            }

            // check if user completes all answers in given checkpoint then send the checkpoint answers to the dias library and clear the checkpoint answers array
            // Technically, We check that current checkpoint questions size == to the checkpoint answered questions size which we get from the database
            if (checkPointQuestions.size() != checkPointAnsweredQuestions.size()) {
                // if user not complete all answers in checkpoint then we add the answer in array
                for (int i = 0; i < checkPointAnsweredQuestions.size(); i++) {
                    int questionId = checkPointAnsweredQuestions.get(i).getId();
                    String assignmentId = checkPointAnsweredQuestions.get(i).getAssignmentId();
                    AnswerEntity answerEntity = answerThread.getAnswerFromQuestion(assignmentId, String.valueOf(questionId));
                    diasSimpleLikertScaleAnswers.put(checkPointAnsweredQuestions.get(i).getQuestion(), Double.valueOf(answerEntity.getAnswer()));
                }
            } else {
                personAvg = getCalculatedLocalAverageFromLocalAnswers();
                diasPersonTv.setText(String.valueOf(personAvg));
            }

            //check if current checkpoint answers equal to checkpoint questions the clear the checkpoint answers list
            if (diasSimpleLikertScaleAnswers.size() == checkPointQuestions.size()) {
                diasSimpleLikertScaleAnswers.clear();
                localAnswers.clear();
                personAvg = 0.0;
                diasPersonTv.setText(personAvg.toString());
                diasGroupTv.setText(String.valueOf(diasResult));

            }

            //calling leave method
            if (isLeaveDiasCheckPoint) {
                if (mappingWrapper != null) {
                    mappingWrapper.leave(String.valueOf(diasUniqueCheckpoint));
                }
                isLeaveDiasCheckPoint = false;
            }

            // check if current Assignment answers equal to the Assignment Questions then assignment will be completed
            if (homeActivity.answersList.size() == QuestionsActivity.assignmentQuestions.size()) {
                stateProgressBar.setAllStatesCompleted(true);
                if (utils.FileWritingStatus) {
                    utils.FileWritingStatus = false;
                    homeActivity.handlerCheck = false;
                    homeActivity.handlerForQuestions = null;
                    String projectId = AppPreferences.getProjectId(context);
                    // send Assignment Data to the Hive Server
                    NetworkCallbacks.AssignmentSubmission(context, projectId, mDeviceId.getDeviceUuid().toString(), QuestionsActivity.selectedAssignment.getId(), answersList);
                    // show all answers to user when user complete survey
                    modeFunctions.showAllAnswers();
                    mappingWrapper.cleanclose();
                    localAnswers.clear();
                }
            }
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    /**
     * This method is used to display a popUp for every question in SimpleDIASMode and saves answer.
     */
    private void showSimpleDiasModePopUp() {

        try {

            final Dialog qstDialog = new Dialog(context, R.style.actionSheetTheme);
            qstDialog.setContentView(R.layout.popup_layout);
            Window window = qstDialog.getWindow();

            if(window == null){
                Toast.makeText(context, "error when trying to show popup", Toast.LENGTH_SHORT).show();
                Log.e("SimpleDIASMode:showSim*","window was null");
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

            final SeekBarViewModel seekBarViewModel = new SeekBarViewModel(context);

            linearLayout.removeAllViews();

            TextView textView = new TextView(context);
            textView.setText(R.string.stop_to_answer);
            textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            textView.setTypeface(null, Typeface.BOLD);


            if (QuestionsActivity.assignmentQuestions.get(StaticModel.questionsQueue.get(0)).getType().equalsIgnoreCase("likertScale")) {
                seekBarViewModel.updateViewNew(QuestionsActivity.assignmentQuestions.get(StaticModel.questionsQueue.get(0)));
                textView.setBackgroundResource(R.drawable.bottom_line);
                linearLayout.addView(textView, 0);
                linearLayout.addView(seekBarViewModel.getSeekBarView(), 1);
            }

            StaticModel.shownQuestionsQueue.add(StaticModel.questionsQueue.get(0));
            StaticModel.questionsQueue.remove(0);

            save.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onClick(View view) {
                    try {
                        int questionCredit = 0;
                        //check if current question type is likert scale then create seek bar and add it into the linear layout
                        if (QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getType().equalsIgnoreCase("likertScale")) {
                            //get the seekBar answer
                            String result = seekBarViewModel.getValue();

                            homeActivity.isDiasQuestionShowing = false;

                            diasSimpleLikertScaleAnswers.put(QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getQuestion(), Double.parseDouble(result));
                            // added code

                            localAnswers.add(Double.valueOf(result));
                            personAvg = getCalculatedLocalAverageFromLocalAnswers();
                            diasPersonTv.setText(personAvg.toString());

                            if (result != null) {
                                // get pop up question Id
                                int qid = QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getId();

                                //get pop up question's Assignment Id
                                String aId = QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getAssignmentId();

                                //initialize the radiobutton worker thread
                                LikertScaleThread likertScaleThread = new LikertScaleThread(context);

                                // get likertScale data against pop up question id and Assignment id from Room Database
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
                                //change the marker into black if all checkpoint questions are visited
                                homeActivity.placeMarkers(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1));
                                StaticModel.questionsSensorsQueue.add(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1));
                                //save sensors reading into the Room Database
                                sensorsInfo.saveSensorsValue(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1), result);
                                StaticModel.shownQuestionsQueue.remove(StaticModel.shownQuestionsQueue.size() - 1);

                                answeredQuestions++;
                                qstDialog.dismiss();

                                // get all answered questions of group point from database
                                checkPointAnsweredQuestions = answerThread.getCheckPointAnsweredQuestions(utils.loadedFileName, currentQuestionLatitude, currentQuestionLongitude);

                                if (checkPointQuestions.size() == checkPointAnsweredQuestions.size()) {
                                    sendGroupPointAnswersToDiasLibrary();
                                }
                            }//likertScale value is not null
                        }//likertScale type
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

    // calculate the local average value from assignment answers
    private Double getCalculatedLocalAverageFromLocalAnswers() {
        Double sum = 0.0;
        if(HomeActivity.localAnswers.size()==0)
            return 0.0;
        for (int i = 0; i < HomeActivity.localAnswers.size(); i++) {
            sum += HomeActivity.localAnswers.get(i);
        }
        return sum / HomeActivity.localAnswers.size();

    }

    @SuppressLint("SetTextI18n")
    private void sendGroupPointAnswersToDiasLibrary() {
        try {
            if (mappingWrapper == null) {
                mappingWrapper = new MappingWrapper(context, gatewayIP);
                mappingWrapper.reset();
            }

            if (diasSimpleLikertScaleAnswers != null && diasSimpleLikertScaleAnswers.size() != 0) {
                // Send likertScale Answers to the
                mappingWrapper.setPossibleStatesRange(String.valueOf(diasUniqueCheckpoint), 1, 10); // set possible likertScale values range, please always call this method on changes of value range and at least once in the beginning
                mappingWrapper.setReading(String.valueOf(diasUniqueCheckpoint), diasSimpleLikertScaleAnswers);
                AggregateResult ar = mappingWrapper.getAggregate(String.valueOf(diasUniqueCheckpoint), AggregationType.avg);

                diasResult = ar.get(3000);

                if (diasResult != null) {
                    diasGroupTv.setText(String.valueOf(diasResult));//here value from preDIASAggregatedValue will be set.
                }
            }
            if (diasResult != null)
                diasGroupTv.setText(String.valueOf(diasResult));//here value from preDIASAggregatedValue will be set.

            personAvg = getCalculatedLocalAverageFromLocalAnswers();
            diasPersonTv.setText(personAvg.toString());

            if(diasSimpleLikertScaleAnswers != null)
                diasSimpleLikertScaleAnswers.clear();

            localAnswers.clear();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}






