package smartagora.ethz.ch.roomDatabase.dataAccessObjects;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import smartagora.ethz.ch.roomDatabase.entityModels.CheckBoxDecisionModeEntity;

/**
 * @Dao Creates a Data Access Object in the database using an CheckBoxDecisionModeDao Interface Class
 */
@Dao
public interface CheckBoxDecisionModeDao {

    /**
     * @Insert parameter insert the CheckBoxDecisionMode entity as a object into the Database Table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CheckBoxDecisionModeEntity checkBoxAssociateQuestion);


    /**
     * Gets check box associate question from CheckboxDecisionMode Table by combinationId.
     *
     * @return the check box associate question as a CheckboxDecisionMode Entity
     */
    @Query("SELECT * FROM CheckBoxDecisionMode WHERE combinationId=:id")
    CheckBoxDecisionModeEntity getCheckBoxAssociateQuestion(final String id);


}
