package smartagora.ethz.ch.roomDatabase.databaseFetcher;

import android.content.Context;
import smartagora.ethz.ch.roomDatabase.entityModels.TextBoxDecisionModeEntity;
import smartagora.ethz.ch.roomDatabase.RoomDatabaseHandler;

/**
 * TextBoxDecisionModeThread class is used to fetch TextBoxDecisionMode table data in a separate thread by using the AsyncTask Class.
 */
public class TextBoxDecisionModeThread {


    private final RoomDatabaseHandler databaseHandler;

    /**
     * Instantiates a new Text box decision mode thread.
     */
    public TextBoxDecisionModeThread(Context context) {
        databaseHandler = RoomDatabaseHandler.getAppDatabase(context);
    }

    /**
     * Execute the AsyncTask class by passing the textBoxId and return
     * the TextBoxDecisionMode object.
     *
     * @param textBoxId the unique id of textBox
     * @return the associate question of TextBox
     */
    public TextBoxDecisionModeEntity getAllAssociateQuestions(String textBoxId) {
        return databaseHandler.getTextBoxDecisionModeDao().getTextBoxDecisionModeQuestion(textBoxId);

    }
}
