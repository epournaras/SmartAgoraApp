package smartagora.ethz.ch.roomDatabase.databaseFetcher;

import android.content.Context;

import smartagora.ethz.ch.roomDatabase.entityModels.TextBoxEntity;
import smartagora.ethz.ch.roomDatabase.RoomDatabaseHandler;

/**
 * TextBoxThread class is used to fetch TextBox table data
 */
public class TextBoxThread {

    private final RoomDatabaseHandler databaseHandler;

    /**
     * Instantiates a new Text box thread.
     */
    public TextBoxThread(Context context) {
        databaseHandler = RoomDatabaseHandler.getAppDatabase(context);
    }


    /**
     * return
     * textBox object.
     *
     * @param assignmentId the unique id of Assignment
     * @param questionId   the id of the Question
     * @return the textBox from specific question in  specific assignment
     */
    public TextBoxEntity getAllTextBoxesFromQuestionInAssignment(String assignmentId, final String questionId) {
        return databaseHandler.getTextBoxDao().getTextBoxeFromSpecificAssignmentQuestion(assignmentId, questionId);
    }// get All TExt Boxes From Question in Assignment
}
