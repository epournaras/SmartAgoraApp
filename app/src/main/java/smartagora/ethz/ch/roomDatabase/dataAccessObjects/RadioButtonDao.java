package smartagora.ethz.ch.roomDatabase.dataAccessObjects;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import smartagora.ethz.ch.roomDatabase.entityModels.RadioButtonEntity;

/**
 * @Dao Creates a Data Access Object in the database using an CheckBoxDao Interface Class
 */
@Dao
public interface RadioButtonDao {

    /**
     * @Insert parameter insert the radioButton entity as a object into the Database Table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(RadioButtonEntity radioButton);

    /**
     * Gets all radioButtons from specific question in Assignment.
     *
     * @return the all radioButtons from specific question in Assignment from RadioButton Table
     */
    @Query("SELECT * FROM RadioButton WHERE assignmentId=:assignmentId AND questionId=:questionId")
    List<RadioButtonEntity> getAllRadioButtonsFromSpecificAssignmentQuestion(final String assignmentId, final int questionId);

    /**
     * Gets last inserted row from RadioButton Table.
     *
     * @return the last inserted row of RadioButton Entity
     */
    @Query("select * from RadioButton order by id desc limit 1;")
    RadioButtonEntity getLastInsertedRow();


}
