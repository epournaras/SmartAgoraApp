package smartagora.ethz.ch.roomDatabase.databaseFetcher;

import android.content.Context;

import java.util.List;

import smartagora.ethz.ch.roomDatabase.entityModels.CheckBoxEntity;
import smartagora.ethz.ch.roomDatabase.RoomDatabaseHandler;

/**
 * CheckBoxThread class is used to fetch CheckBox table data
 */
public class CheckBoxThread {


    private final RoomDatabaseHandler databaseHandler;

    /**
     * Instantiates a new Check box thread.
     */
    public CheckBoxThread(Context context) {
        databaseHandler = RoomDatabaseHandler.getAppDatabase(context);
    }


    /**
     * returns the list of checkBox objects.
     *
     * @param assignmentId the unique id of the Assignment
     * @param questionId   the unique id of the Question
     * @return the list of checkBoxes from specific question in  specific assignment
     */
    public List<CheckBoxEntity> getAllCheckBoxesFromQuestionInAssignment(String assignmentId, final String questionId){
        return databaseHandler.getCheckBoxDao().getAllCheckBoxesFromSpecificAssignmentQuestion(assignmentId, questionId);
    }
}
