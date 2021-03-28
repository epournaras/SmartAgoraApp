package smartagora.ethz.ch.utils;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import smartagora.ethz.ch.roomDatabase.databaseFetcher.AssignmentThread;
import smartagora.ethz.ch.roomDatabase.databaseFetcher.QuestionThread;
import smartagora.ethz.ch.roomDatabase.entityModels.AssignmentEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.QuestionEntity;
import smartagora.ethz.ch.databases.DatabaseHandler;
import smartagora.ethz.ch.models.MainModel;
import smartagora.ethz.ch.models.QuestionDataModel;
import smartagora.ethz.ch.uiActivities.HomeActivity;
import smartagora.ethz.ch.uiActivities.QuestionsActivity;

import static smartagora.ethz.ch.uiActivities.HomeActivity.isLeaveDiasCheckPoint;
import static smartagora.ethz.ch.uiActivities.QuestionsActivity.FileFromList;

public class QuestionHandling {

    private List<QuestionDataModel> visitedPoints;
    private List<QuestionEntity> visitedPointsNew;
    private final MainModel questions = new MainModel();
    private final MainModel selectedQuestions = new MainModel();
    private DatabaseHandler databaseHandler;
    private final Context mContext;
    private Ellipse mEllipse;
    private static int questionNumber;
    private int shownQuestion;
    private List<QuestionDataModel> allQuestions;
    private List<QuestionDataModel> routeQuestions;
    private List<QuestionDataModel> notificationQuestions;


    public int getQuestionNumber() {
        return questionNumber;
    }

    private void setQuestionNumber(int questionNumber) {
        QuestionHandling.questionNumber = questionNumber;
    }

    public void setShownQuestion(int shownQuestion) {
        this.shownQuestion = shownQuestion;
    }

    public List<QuestionDataModel> getRouteQuestions() {
        return routeQuestions;
    }

    public Ellipse getmEllipse() {
        return mEllipse;
    }

    public QuestionHandling(Context context) {
        mContext = context;
        setQuestionNumber(0);
        setShownQuestion(0);
        initValue();
    }

    private void initValue() {
        if (mEllipse == null)
            mEllipse = new Ellipse();
        if (databaseHandler == null)
            databaseHandler = new DatabaseHandler(mContext);
        if (allQuestions == null)
            allQuestions = new ArrayList<>();
        if (routeQuestions == null)
            routeQuestions = new ArrayList<>();
        if (notificationQuestions == null)
            notificationQuestions = new ArrayList<>();
        if (visitedPoints == null)
            visitedPoints = new ArrayList<>();
        if (visitedPointsNew == null)
            visitedPointsNew = new ArrayList<>();
    }
    //methods


    /**
     * `
     * Get Questions Data based on selecting from Load Trip list.
     */
    public void getDataFromSavedFile() {
        try {
            selectedQuestions.SampleDataModel = databaseHandler.getFilesData();
            questions.SampleDataModel = new ArrayList<>(selectedQuestions.SampleDataModel);
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }


    /**
     * Return Questions data from Room database.
     */
    public void getDataFromRoomDB() {
        try {
            QuestionsActivity.assignmentQuestions = new ArrayList<>();
            QuestionsActivity.selectedAssignment = new AssignmentEntity();

            AssignmentThread assignmentThread = new AssignmentThread(mContext);
            QuestionThread questionThread = new QuestionThread(mContext);

            QuestionsActivity.selectedAssignment = assignmentThread.getAssignment(FileFromList);
            QuestionsActivity.assignmentQuestions = questionThread.getAllQuestionsFromAssignment(FileFromList);
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
    }

    /**
     * nearest question
     *
     * @return nearest questions.
     */

    public int QuestionInEllipseNew(String AssignmentMode) {
        try {
            int qus = questionNumber;
            boolean inEllipse = false;
            List<QuestionEntity> quesInEllipse = new ArrayList<>();
            List<Integer> quesNo = new ArrayList<>();

            if (AssignmentMode.equalsIgnoreCase("sequence")) {

                outerFor: for (int i = 0; i < QuestionsActivity.assignmentQuestions.size(); i++) {
                    LatLng questionPoint = new LatLng(Double.parseDouble(QuestionsActivity.assignmentQuestions.get(i).getLatitude()), Double.parseDouble(QuestionsActivity.assignmentQuestions.get(i).getLongitude()));
                    if (mEllipse.IsInEllipse(questionPoint)) {
                        inEllipse = true;
                        for (int j = 0; j < visitedPointsNew.size(); j++) {
                            if ((Objects.equals(visitedPointsNew.get(j).getLatitude(), QuestionsActivity.assignmentQuestions.get(i).getLatitude()) &&
                                    Objects.equals(visitedPointsNew.get(j).getLongitude(), QuestionsActivity.assignmentQuestions.get(i).getLongitude())))
                                continue outerFor;
                        }
                        // list of all questions enclosed in ellipse
                        quesInEllipse.add(QuestionsActivity.assignmentQuestions.get(i));
                        quesNo.add(i);
                    }
                }

                int seq = 99999;
                for (int j = 0; j < quesInEllipse.size(); j++) {
                    if (Integer.parseInt(quesInEllipse.get(j).getSequence()) <= seq) {
                        seq = quesNo.get(j);
                    }
                }
                if (seq != 99999) {
                    setQuestionNumber(seq);
                }

                //avoiding from displaying same question again at same location if user answered that question
                if (inEllipse && (questionNumber != qus || visitedPointsNew.size() == 0))
                    return questionNumber;

                return -1;
            } else if (AssignmentMode.equalsIgnoreCase("decision")) {
                for (int i = 0; i < QuestionsActivity.assignmentQuestions.size(); i++) {
                    LatLng questionPoint = new LatLng(Double.parseDouble(QuestionsActivity.assignmentQuestions.get(i).getLatitude()), Double.parseDouble(QuestionsActivity.assignmentQuestions.get(i).getLongitude()));
                    if (mEllipse.IsInEllipse(questionPoint) && i == shownQuestion && !isDoneNew(i)) {
//                        avoiding from displaying same question again at same location if user answered that question
                        setQuestionNumber(shownQuestion);
                        return shownQuestion;
                    }
                }

            } else if (AssignmentMode.equalsIgnoreCase("simple")) {

                for (int i = 0; i < QuestionsActivity.assignmentQuestions.size(); i++) {
                    LatLng questionPoint = new LatLng(Double.parseDouble(QuestionsActivity.assignmentQuestions.get(i).getLatitude()), Double.parseDouble(QuestionsActivity.assignmentQuestions.get(i).getLongitude()));
                    if (mEllipse.IsInEllipse(questionPoint) && !isDoneNew(i)) {
                        setQuestionNumber(i);
                        return i;
                    }
                }
            } else if (AssignmentMode.equalsIgnoreCase("DIAS_Simple")) {
                for (int i = 0; i < QuestionsActivity.assignmentQuestions.size(); i++) {
                    LatLng questionPoint = new LatLng(Double.parseDouble(QuestionsActivity.assignmentQuestions.get(i).getLatitude()), Double.parseDouble(QuestionsActivity.assignmentQuestions.get(i).getLongitude()));
                    if (mEllipse.IsInEllipse(questionPoint) && !HomeActivity.diasCheckpointQuestions.contains(i)) {
                        setQuestionNumber(i);
                        return i;
                    } else {
                        isLeaveDiasCheckPoint = true;
                    }
                }
            }
            return -1;
        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
            return -1;
        }

    }

    public QuestionEntity getCurrentQuestionNew() {
        return QuestionsActivity.assignmentQuestions.get(questionNumber);
    }

    public void addQuestionInVisitedPointsNew(int qNumber) {
        visitedPointsNew.add(QuestionsActivity.assignmentQuestions.get(qNumber));
    }

    public boolean isDoneNew(int point) {
        try {
            for (int i = 0; i < visitedPointsNew.size(); i++)
                if (Objects.equals(visitedPointsNew.get(i).getLatitude(), QuestionsActivity.assignmentQuestions.get(point).getLatitude()) &&
                        Objects.equals(visitedPointsNew.get(i).getLongitude(), QuestionsActivity.assignmentQuestions.get(point).getLongitude()))
                    return true;

        } catch (Exception ex) {
            ExceptionalHandling.logException(ex);
        }
        return false;
    }


}
