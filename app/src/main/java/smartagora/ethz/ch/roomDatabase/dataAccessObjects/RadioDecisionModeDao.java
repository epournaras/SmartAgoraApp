package smartagora.ethz.ch.roomDatabase.dataAccessObjects;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import smartagora.ethz.ch.roomDatabase.entityModels.RadioDecisionModeEntity;

/**
 * @Dao Creates a Data Access Object in the database using an RadioDecisionModeDao Interface Class
 */
@Dao
public interface RadioDecisionModeDao {

    /**
     * @Insert parameter insert the RadioDecisionMode entity as a object into the Database Table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RadioDecisionModeEntity radioAssociateQuestion);

    /**
     * Gets radio associate question from RadioDecisionMode Table by radioId.
     *
     * @return the radio associate question as a RadioDecisionMode Entity
     */
    @Query("SELECT * FROM RadioDecisionMode WHERE radioId=:radioId")
    RadioDecisionModeEntity getRadioDecisionModeQuestion(final String radioId);

}
