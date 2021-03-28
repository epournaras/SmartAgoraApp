package smartagora.ethz.ch.roomDatabase.databaseFetcher;

import android.content.Context;

import smartagora.ethz.ch.roomDatabase.entityModels.RadioDecisionModeEntity;
import smartagora.ethz.ch.roomDatabase.RoomDatabaseHandler;

/**
 * RadioDecisionModeThread class is used to fetch RadioDecisionMode table data in a separate thread by using the AsyncTask Class.
 */
public class RadioDecisionModeThread {


    private final RoomDatabaseHandler databaseHandler;

    /**
     * Instantiates a new Radio decision mode thread.
     */
    public RadioDecisionModeThread(Context context) {
        databaseHandler = RoomDatabaseHandler.getAppDatabase(context);
    }


    /**
     * Execute the AsyncTask class by passing the radioId and return
     * the RadioDecisionMode object.
     *
     * @param radioId the unique id of radio
     * @return the associate question of radioButton
     */
    public RadioDecisionModeEntity getRadioDecisionModeQuestion(final String radioId){
        return databaseHandler.getRadioDecisionModeDao().getRadioDecisionModeQuestion(radioId);
    }
}
