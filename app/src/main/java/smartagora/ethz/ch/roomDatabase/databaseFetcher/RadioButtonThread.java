package smartagora.ethz.ch.roomDatabase.databaseFetcher;

import android.content.Context;

import java.util.List;

import smartagora.ethz.ch.roomDatabase.entityModels.RadioButtonEntity;
import smartagora.ethz.ch.roomDatabase.RoomDatabaseHandler;

/**
 * RadioButtonThread class is used to fetch Radiobutton table data
 */
public class RadioButtonThread {

    private final RoomDatabaseHandler databaseHandler;

    /**
     * Instantiates a new Radio button thread.
     */
    public RadioButtonThread(Context context) {
        databaseHandler = RoomDatabaseHandler.getAppDatabase(context);
    }


    /**
     * returns the list of radioButton objects.
     *
     * @param assignmentId the unique id of the Assignment
     * @param questionId   the unique id of the Question
     * @return the list of radioButtons from specific question in  specific assignment
     */
    public List<RadioButtonEntity> getAllRadioButtonsFromQuestionInAssignment(String assignmentId, final int questionId) {
        return databaseHandler.getRadioButtonDao().getAllRadioButtonsFromSpecificAssignmentQuestion(assignmentId, questionId);
    }
}
