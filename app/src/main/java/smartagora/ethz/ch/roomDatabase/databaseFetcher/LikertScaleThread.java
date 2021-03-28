package smartagora.ethz.ch.roomDatabase.databaseFetcher;

import android.content.Context;

import smartagora.ethz.ch.roomDatabase.entityModels.LikertScaleEntity;
import smartagora.ethz.ch.roomDatabase.RoomDatabaseHandler;

/**
 * LikertScaleThread class is used to fetch LikertScale table data
 */
public class LikertScaleThread {


    private final RoomDatabaseHandler databaseHandler;

    /**
     * Instantiates a new Likert scale thread.
     */
    public LikertScaleThread(Context context) {
        databaseHandler = RoomDatabaseHandler.getAppDatabase(context);
    }


    /**
     * returns likerScale object.
     *
     * @param assignmentId the unique id of the assignment
     * @param questionId   the unique id of the question
     * @return the likertScale from specific question in  specific assignment
     */
    public LikertScaleEntity getAllLikertScalesFromQuestionInAssignment(String assignmentId, final String questionId){
        return databaseHandler.getLikertScaleDao().getLikertScalesFromSpecificAssignmentQuestion(assignmentId, questionId);
    }
}
