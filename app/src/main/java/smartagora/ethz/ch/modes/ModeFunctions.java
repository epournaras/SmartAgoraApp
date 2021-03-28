package smartagora.ethz.ch.modes;

import android.annotation.SuppressLint;
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
import com.kofigyan.stateprogressbar.StateProgressBar;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import smartagora.ethz.ch.R;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.AnswerThread;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.RadioButtonThread;
import smartagora.ethz.ch.roomDatabase.entityModels.AnswerEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.CombinationEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.QuestionEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.RadioButtonEntity;
import smartagora.ethz.ch.roomDatabase.RoomDatabaseHandler;
import smartagora.ethz.ch.models.StaticModel;
import smartagora.ethz.ch.uiActivities.HomeActivity;
import smartagora.ethz.ch.uiActivities.QuestionsActivity;
import smartagora.ethz.ch.utils.ExceptionalHandling;
import smartagora.ethz.ch.utils.QuestionHandling;
import smartagora.ethz.ch.utils.utils;
import smartagora.ethz.ch.viewmodels.CheckBoxQuestionModel;
import smartagora.ethz.ch.viewmodels.RadioButtonViewModel;


/**
 * ModeFunctions class have a generic methods for all modes.
 * ModeFunctions class is used in all types of modes because this class has methods
 * which is same for all Modes.
 */
class ModeFunctions {

    private final Context context;
    private final QuestionHandling questionHandling;
    private final StateProgressBar stateProgressBar;
    private final SensorsInfo sensorsInfo;
    private int answeredQuestions;
    private final HomeActivity homeActivity;
    private final List<Integer> diasCheckpointQuestions;
    private final List<AnswerEntity> answersList;

    /**
     * Instantiates a ModeFunctions.
     *
     * @param context                     the context
     * @param questionHandling            the question handling
     * @param answeredQuestions           the answered questions
     * @param answersList                 the answers list
     * @param stateProgressBar            the state progress bar
     * @param diasCheckpointQuestions     the dias checkpoint questions
     * @param sensorsInfo                 the sensors info
     */
    ModeFunctions(Context context, QuestionHandling questionHandling, int answeredQuestions, List<AnswerEntity> answersList, StateProgressBar stateProgressBar, List<Integer> diasCheckpointQuestions, SensorsInfo sensorsInfo) {
        this.context = context;
        this.answeredQuestions = answeredQuestions;
        this.sensorsInfo = sensorsInfo;
        this.stateProgressBar = stateProgressBar;
        this.diasCheckpointQuestions = diasCheckpointQuestions;
        this.answersList = answersList;
        homeActivity = new HomeActivity();
        this.questionHandling = questionHandling;
    }

    /**
     * Save answer and credit against specific Question .
     *
     * @param credit         the credit
     * @param answer         the answer
     * @param questionEntity the question entity
     */
    void saveAnswer(int credit, String answer, QuestionEntity questionEntity) {
        try {

            Date d = new Date();
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S");
            String timeAtAnswering = format.format(d);

            AnswerEntity answerEntity = new AnswerEntity();
            answerEntity.setAnswer(answer);
            answerEntity.setAnswerTime(timeAtAnswering);
            answerEntity.setCredits(Integer.toString(credit));
            answerEntity.setQuestionId(questionEntity.getId());
            answerEntity.setAssignmentId(questionEntity.getAssignmentId());

            if (QuestionsActivity.selectedAssignment.getMode().equalsIgnoreCase("DIAS_Simple"))
                answerEntity.setAggregateValue(String.valueOf(SimpleDIASMode.personAvg));


            RoomDatabaseHandler.getAppDatabase(context).getAnswerDao().insert(answerEntity);

            answersList.add(answerEntity);


            if (!diasCheckpointQuestions.contains(SimpleDIASMode.questionNumber))
                diasCheckpointQuestions.add(SimpleDIASMode.questionNumber);

        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    /**
     * Update StateBar for specific mode.
     *
     * @param context the context
     * @param mode    the mode
     */
    void updateStateBar(Context context, String mode) {

        if (!mode.equalsIgnoreCase("sequence") && !mode.equalsIgnoreCase("simple") && !mode.equalsIgnoreCase("decision"))
            return;

        AnswerThread answerThread = new AnswerThread(context);

        List<AnswerEntity> visitedPoints = answerThread.getAnsweredQuestions(utils.fileName);

        if (visitedPoints.size() > 0 && visitedPoints.size() < QuestionsActivity.assignmentQuestions.size() / 4) {
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.ONE);
        } else if (visitedPoints.size() > QuestionsActivity.assignmentQuestions.size() / 4 && visitedPoints.size() <= QuestionsActivity.assignmentQuestions.size() / 2) {
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.TWO);
        } else if (visitedPoints.size() > QuestionsActivity.assignmentQuestions.size() / 2 && visitedPoints.size() <= QuestionsActivity.assignmentQuestions.size() * 3 / 4) {
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.THREE);
        } else if (visitedPoints.size() > QuestionsActivity.assignmentQuestions.size() * 3 / 4) {
            stateProgressBar.setCurrentStateNumber(StateProgressBar.StateNumber.FOUR);
        }

    }

    /**
     * Gets combination id of checkBox.
     *
     * @param idList      the id list
     * @param combination the combination
     * @return the combination id new
     */
    int getCombinationId(List<String> idList, List<CombinationEntity> combination) {
        try {
            boolean found = false;
            int i;
            for (i = 0; i < combination.size(); i++) {

                String[] orders = combination.get(i).getOrder().split(",");

                if (orders.length == idList.size()) {
                    for (int x = 0; x < orders.length; x++) {
                        String order = String.valueOf(Integer.parseInt(orders[x]) - 1);
                        found = order.equals(idList.get(x));
                    }
                }

                if (found)
                    return i;

            }
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }

        return -1;
    }

    /**
     * Show all answers.
     */
    @SuppressLint("SetTextI18n")
    void showAllAnswers() {
        try {
            stateProgressBar.setAllStatesCompleted(true);
            final Dialog qstDialog = new Dialog(context, R.style.actionSheetTheme);
            qstDialog.setContentView(R.layout.see_ans_dialog);
            Button Yes = qstDialog.findViewById(R.id.yes);
            Button No = qstDialog.findViewById(R.id.no);

            AnswerThread answerThread = new AnswerThread(context);

            List<AnswerEntity> answers = answerThread.getAnsweredQuestions(utils.fileName);
            int creditsForAssignment = 0;

            for (AnswerEntity ae: answers)
                creditsForAssignment += Integer.parseInt(ae.getCredits());


            if (creditsForAssignment > homeActivity.mCredits)
                homeActivity.mCredits = creditsForAssignment;


            Toast.makeText(context, "Credits Earned: " + homeActivity.mCredits, Toast.LENGTH_LONG).show();
            if (StaticModel.skippedQuestions.size() > 0) {

                TextView txt = qstDialog.findViewById(R.id.msg);
                txt.setText("You have " + StaticModel.skippedQuestions.size() + " skipped questions. Select Yes to answer them.");

                Yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Collections.sort(StaticModel.skippedQuestions);
                        showSkippedQuestions();
                        qstDialog.dismiss();
                    }
                });
                No.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        qstDialog.cancel();
                    }
                });
            }//skipped
            else {

                List<String> answeredQuestionIDS = answerThread.getAnsweredQuestionIds(utils.fileName);
                if (StaticModel.shownQuestionsQueue.size() > 0) {
                    TextView txt = qstDialog.findViewById(R.id.msg);
                    txt.setText("You have " + StaticModel.shownQuestionsQueue.size() + " unanswered questions. Select Yes to answer them.");
                } else if (((answeredQuestions != QuestionsActivity.assignmentQuestions.size()) || answeredQuestionIDS.size() != QuestionsActivity.assignmentQuestions.size()) && !QuestionsActivity.selectedAssignment.getMode().equalsIgnoreCase("decision")) {
                    int missedPoints = QuestionsActivity.assignmentQuestions.size() - answeredQuestions;
                    int missed2 = QuestionsActivity.assignmentQuestions.size() - answeredQuestionIDS.size();
                    if (missedPoints > 0 && missed2 > 0) {
                        TextView txt = qstDialog.findViewById(R.id.msg);
                        txt.setText("You have missed: " + missedPoints + " questions, please go back to fill them");
                    }
                }

                //listen for clicks on size buttons
                Yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        qstDialog.dismiss();
                    }
                });
                No.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        qstDialog.cancel();
                    }
                });
            }//answered

            qstDialog.show();
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    /**
     * Show skipped questions.
     */
    @SuppressLint("SetTextI18n")
    private void showSkippedQuestions() {
        try {
            if (StaticModel.skippedQuestions.size() > 0) {
                final int number = StaticModel.skippedQuestions.get(0);
                HomeActivity.skippedNextQuestion = number;
                final Dialog qstDialog = new Dialog(context, R.style.actionSheetTheme);
                qstDialog.setContentView(R.layout.popup_layout);
                Window window = qstDialog.getWindow();
                WindowManager.LayoutParams wlp;
                if(window != null)
                    wlp = window.getAttributes();
                else{
                    throw new NullPointerException("The window was null");
                }
                wlp.gravity = Gravity.BOTTOM;
                wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
                window.setAttributes(wlp);

                LinearLayout linearLayout = qstDialog.findViewById(R.id.linearLayout);
                Button save = qstDialog.findViewById(R.id.save);
                Button cancel = qstDialog.findViewById(R.id.cancel);
                final RadioButtonViewModel radioButtonViewModel = new RadioButtonViewModel(context);
                final CheckBoxQuestionModel checkBoxQuestionModel = new CheckBoxQuestionModel(context);
                linearLayout.removeAllViews();

                TextView textView = new TextView(context);
                textView.setText("Please answer the question");
                textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
                textView.setTypeface(null, Typeface.BOLD);


                if (QuestionsActivity.assignmentQuestions.get(number).getType().equalsIgnoreCase("radio")) {
                    radioButtonViewModel.updateViewNew(QuestionsActivity.assignmentQuestions.get(number));
                    linearLayout.addView(textView, 0);
                    linearLayout.addView(radioButtonViewModel.getRadioView(), 1);
                } else {
                    checkBoxQuestionModel.updateViewNew(QuestionsActivity.assignmentQuestions.get(number));
                    linearLayout.addView(textView, 0);
                    linearLayout.addView(checkBoxQuestionModel.getCheckBoxQuestionView(), 1);
                }

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String answerResult = "";
                        int questionCredit = 0;
                        if (QuestionsActivity.assignmentQuestions.get(number).getType().equalsIgnoreCase("radio")) {
                            String result = radioButtonViewModel.getValue();
                            if (result != null) {
                                answerResult = result;

                                int qid = QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getId();
                                String aId = QuestionsActivity.assignmentQuestions.get(StaticModel.shownQuestionsQueue.get(StaticModel.shownQuestionsQueue.size() - 1)).getAssignmentId();

                                RadioButtonThread radioButtonThread = new RadioButtonThread(context);
                                List<RadioButtonEntity> radioButtonEntityList = radioButtonThread.getAllRadioButtonsFromQuestionInAssignment(aId, qid);

                                if (radioButtonEntityList != null && radioButtonEntityList.get(radioButtonViewModel.getSelectedId()).getCredits() != null) {
                                    String customCredit = radioButtonEntityList.get(radioButtonViewModel.getSelectedId()).getCredits();
                                    if (TextUtils.isEmpty(customCredit)) {
                                        homeActivity.mCredits += Integer.parseInt(QuestionsActivity.selectedAssignment.getDefaultCredit());
                                        questionCredit = Integer.parseInt(QuestionsActivity.selectedAssignment.getDefaultCredit());
                                    } else {
                                        homeActivity.mCredits += Integer.parseInt(customCredit);
                                        questionCredit = Integer.parseInt(customCredit);
                                    }
                                }// credits not null


                                saveAnswer(questionCredit, result, QuestionsActivity.assignmentQuestions.get(number));
                                answeredQuestions++;
                                StaticModel.skippedQuestions.remove(0);
                                qstDialog.dismiss();
                                showSkippedQuestions();

                            }
                        } else if (QuestionsActivity.assignmentQuestions.get(number).getType().equalsIgnoreCase("checkbox")) {
                            int questCredit = Integer.parseInt(QuestionsActivity.selectedAssignment.getDefaultCredit());
                            answerResult = "No";

                            saveAnswer(questCredit, answerResult, QuestionsActivity.assignmentQuestions.get(number));
                            answeredQuestions++;
                            StaticModel.skippedQuestions.remove(0);
                            qstDialog.dismiss();
                            showSkippedQuestions();
                        }


                        sensorsInfo.saveSensorsValue(number, answerResult);

                        Toast.makeText(context, "SensorModel Values Inserted", Toast.LENGTH_SHORT).show();
                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (QuestionsActivity.assignmentQuestions.get(number).getMandatory().equalsIgnoreCase("false")) {
                            qstDialog.cancel();
                            StaticModel.skippedQuestions.remove(0);
                            showSkippedQuestions();
                        }
                    }
                });

                //show and wait for user interaction
                qstDialog.show();
            } else {
                showAllAnswers();
            }
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    /**
     * Check survey/Assignment is ended or not.
     *
     * @return the boolean
     */
    boolean checkEndSurvey() {
        try {
            double d = Double.parseDouble(QuestionsActivity.startAndDestinationEntity.getDestinationLongitude());
            double x = Double.parseDouble(QuestionsActivity.startAndDestinationEntity.getDestinationLatitude());

            return questionHandling.getmEllipse().IsInEllipse(new LatLng(x, d));
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
            return false;
        }
    }


}
