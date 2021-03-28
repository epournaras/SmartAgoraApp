package smartagora.ethz.ch.roomDatabase.databaseFetcher;

import android.content.Context;

import java.util.List;

import smartagora.ethz.ch.roomDatabase.entityModels.LikertScaleDecisionModeEntity;
import smartagora.ethz.ch.roomDatabase.RoomDatabaseHandler;

/**
 * LikertScaleDecisionModeThread class is used to fetch LikertScaleDecisionMode table data
 */
public class LikertScaleDecisionModeThread {

    private final RoomDatabaseHandler databaseHandler;

    /**
     * Instantiates a new Likert scale decision mode thread.
     */
    public LikertScaleDecisionModeThread(Context context) {
        databaseHandler = RoomDatabaseHandler.getAppDatabase(context);
    }


    /**
     * returns the list of all LikertScaleDecisionMode objects.
     *
     * @param likertScaleId the unique id of likertScale
     * @return all the associate questions of LikertScale
     */
    public List<LikertScaleDecisionModeEntity> getAllAssociateQuestions(String likertScaleId){
        return databaseHandler.getLikertScaleDecisionModeDao().getLikertScaleDecisionModeQuestions(likertScaleId);
    }

    /**
     * returns the LikertScaleDecisionMode object.
     *
     * @param likertScaleId the unique id of likertScale
     * @return the associate question of specific LikertScale value
     */
    public LikertScaleDecisionModeEntity getAssociateQuestion(String likertScaleId){
        return databaseHandler.getLikertScaleDecisionModeDao().getAssociateQuestion(likertScaleId);
    }
}
