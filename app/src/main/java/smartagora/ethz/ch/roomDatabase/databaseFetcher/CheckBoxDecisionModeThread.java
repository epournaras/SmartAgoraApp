package smartagora.ethz.ch.roomDatabase.databaseFetcher;

import android.content.Context;

import smartagora.ethz.ch.roomDatabase.entityModels.CheckBoxDecisionModeEntity;
import smartagora.ethz.ch.roomDatabase.RoomDatabaseHandler;

/**
 * CheckBoxDecisionModeThread class is used to fetch CheckBoxDecisionMode table data
 */
public class CheckBoxDecisionModeThread {


    private final RoomDatabaseHandler databaseHandler;

    /**
     * Instantiates a new CheckBoxDecisionMode thread.
     */
    public CheckBoxDecisionModeThread(Context context) {
        databaseHandler = RoomDatabaseHandler.getAppDatabase(context);
    }


    /**
     * returns the CheckBoxDecisionMode object.
     *
     * @param combinationId the unique id of combination
     * @return the associate question of checkBox
     */
    public CheckBoxDecisionModeEntity getAssociateQuestion(String combinationId){
        return databaseHandler.getCheckBoxDecisionModeDao().getCheckBoxAssociateQuestion(combinationId);
    }


}
