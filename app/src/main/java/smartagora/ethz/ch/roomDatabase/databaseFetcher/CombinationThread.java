package smartagora.ethz.ch.roomDatabase.databaseFetcher;

import android.content.Context;

import java.util.List;

import smartagora.ethz.ch.roomDatabase.entityModels.CombinationEntity;

import smartagora.ethz.ch.roomDatabase.RoomDatabaseHandler;

/**
 * CombinationThread class is used to fetch combination table data
 */
public class CombinationThread {

    private final RoomDatabaseHandler databaseHandler;

    /**
     * Instantiates a new Combination thread.
     */
    public CombinationThread(Context context) {
        databaseHandler = RoomDatabaseHandler.getAppDatabase(context);
    }

    /**
     * returns the list of combinations objects.
     *
     * @param assignmentId the unique id of assignment
     * @param questionId   the unique id of question
     * @return the list of checkBox combinations from specific question in  specific assignment
     */
    public List<CombinationEntity> getCombinationFromQuestion(String assignmentId, final String questionId){
        return databaseHandler.getCombinationDao().getCombinationFromQuestion(assignmentId, questionId);
    }
}
