package smartagora.ethz.ch.roomDatabase.dataAccessObjects;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import smartagora.ethz.ch.roomDatabase.entityModels.LikertScaleDecisionModeEntity;


/**
 * @Dao Creates a Data Access Object in the database using an LikertScaleDecisionModeDao Interface Class
 */
@Dao
public interface LikertScaleDecisionModeDao {

    /**
     * @Insert parameter insert the LikertScaleDecisionMode entity as a object into the Database Table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LikertScaleDecisionModeEntity likertScaleDecisionModeEntity);


    /**
     * Gets likertScale associate question from LikertScaleDecisionMode Table by likertScaleId
     *
     * @return the likert scale associate question as a LikertScaleDecisionMode Table
     */
    @Query("SELECT * FROM LikertScaleDecisionMode WHERE likertScaleId=:likertScaleId")
    List<LikertScaleDecisionModeEntity> getLikertScaleDecisionModeQuestions(final String likertScaleId);

    @Query("SELECT * FROM LikertScaleDecisionMode WHERE id=:id")
    LikertScaleDecisionModeEntity getAssociateQuestion(final String id);


}
