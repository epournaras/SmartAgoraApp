package smartagora.ethz.ch.roomDatabase.databaseFetcher;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import smartagora.ethz.ch.roomDatabase.entityModels.AnswerEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.QuestionEntity;
import smartagora.ethz.ch.roomDatabase.RoomDatabaseHandler;

/**
 * AnswerThread class is used to fetch Answer table data
 */
public class AnswerThread {

    private final RoomDatabaseHandler databaseHandler;

    /**
     * Instantiates a new Answer thread.
     */
    public AnswerThread(Context context) {
        databaseHandler = RoomDatabaseHandler.getAppDatabase(context);
    }


    /**
     * returns the list of answers entity.
     *
     * @param assignmentName the unique name of assignment
     * @return the answered questions from Assignment
     */
    public List<AnswerEntity> getAnsweredQuestions(String assignmentName){
        return databaseHandler.getAnswerDao().getAnsweredQuestions(assignmentName);
    }


    /**
     * returns the list of questionIds which is answered.
     *
     * @param assignmentName the unique name of assignment
     * @return the answered question ids
     */
    public List<String> getAnsweredQuestionIds(String assignmentName){

        List<Integer> ids = databaseHandler.getAnswerDao().getAnsweredQuestionIds(assignmentName);
        List<String> qIds = new ArrayList<>();

        for (int i = 0; i < ids.size(); i++) {
            qIds.add(ids.get(0).toString());
        }

        return qIds;


    }

    /**
     * returns the list of questions which are answered.
     *
     * @param assignmentName the unique of assignment
     * @param latitude       the latitude
     * @param longitude      the longitude
     * @return the check point answered questions
     */
    public List<QuestionEntity> getCheckPointAnsweredQuestions(String assignmentName, String latitude, String longitude){
        return databaseHandler.getAnswerDao().getCheckPointAnswers(assignmentName, latitude, longitude);
    }


    /**
     * returns the list of questions which is answered.
     *
     * @param assignmentId the unique of assignment
     * @param questionId   the latitude
     * @return the check point answered questions
     */
    public AnswerEntity getAnswerFromQuestion(String assignmentId, String questionId){
        return databaseHandler.getAnswerDao().getAnswerFromQuestion(assignmentId, questionId);
    }


    /**
     * returns the list of answers entity.
     *
     * @param assignmentName the name of assignment
     * @return the answers entity list from assignment
     */
    public List<AnswerEntity> getAnswersFromAssignment(String assignmentName){
        return databaseHandler.getAnswerDao().getAnswersFromAssignment(assignmentName);
    }

    /**
     * returns the list of answers entity.
     *
     * @param assignmentName the name of assignment
     * @return the answers entity list from assignment
     */
    public List<AnswerEntity> getLocalAggregateValues(String assignmentName, String lat, String lng){
        return databaseHandler.getAnswerDao().getCheckPointLocalAggregateAnswers(assignmentName, lat, lng);
    }
}
