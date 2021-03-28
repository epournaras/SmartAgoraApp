package smartagora.ethz.ch.roomDatabase.databaseFetcher;

import android.content.Context;

import java.util.List;

import smartagora.ethz.ch.roomDatabase.entityModels.SensorEntity;
import smartagora.ethz.ch.roomDatabase.RoomDatabaseHandler;

/**
 * QuestionSensorsThread class is used to fetch QuestionSensors table data
 */
public class QuestionSensorsThread {


    private final RoomDatabaseHandler databaseHandler;


    /**
     * Instantiates a new Question sensors thread.
     */
    public QuestionSensorsThread(Context context) {
        databaseHandler = RoomDatabaseHandler.getAppDatabase(context);
    }


    /**
     * returns the list of sensorEntity objects.
     *
     * @param assignmentId the unique id of  assignment id
     * @param questionId   the unique id of question
     * @return all sensors from specific question in assignment
     */
    public List<SensorEntity> getAllSensorsFromAssignmentQuestion(final String assignmentId, final int questionId){
        return databaseHandler.getQuestionSensorsDao().getAllSensorsFromAssignmentQuestion(assignmentId, questionId);
    }
}
