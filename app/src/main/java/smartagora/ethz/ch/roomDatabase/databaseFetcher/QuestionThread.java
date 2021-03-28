package smartagora.ethz.ch.roomDatabase.databaseFetcher;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import smartagora.ethz.ch.roomDatabase.entityModels.QuestionEntity;
import smartagora.ethz.ch.roomDatabase.RoomDatabaseHandler;

/**
 * QuestionThread class is used to fetch Question table data
 */
public class QuestionThread {


    private final RoomDatabaseHandler databaseHandler;

    /**
     * Instantiates a new Question thread.
     */
    public QuestionThread(Context context) {
        databaseHandler = RoomDatabaseHandler.getAppDatabase(context);
    }


    /**
     * returns the list of Questions.
     *
     * @param assignmentName the name of assignment
     * @return the all questions from assignment
     */
    public List<QuestionEntity> getAllQuestionsFromAssignment(String assignmentName) {
        return databaseHandler.getQuestionDao().getAllQuestionsFromAssignment(assignmentName);
    }


    /**
     * returns the list of sequences of Questions.
     *
     * @param assignmentName the name of assignment
     * @return the sequence list of questions
     */
    public ArrayList<Integer> getSequenceList(String assignmentName) {

        List<String> list = databaseHandler.getQuestionDao().getSequenceList(assignmentName);

        ArrayList<Integer> sequenceList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            if (list.get(i) != null) {
                sequenceList.add(Integer.parseInt(list.get(i)));
            }
        }
        return sequenceList;
    }

    /**
     * returns the list of Assignment Entities.
     *
     * @param assignmentName the assignment name
     * @param latitude       the latitude
     * @param longitude      the longitude
     * @return the list of check point questions
     */
    public List<QuestionEntity> getCheckPointQuestions(String assignmentName, String latitude, String longitude){
        return databaseHandler.getQuestionDao().getCheckPointQuestions(assignmentName, latitude, longitude);
    }
}
