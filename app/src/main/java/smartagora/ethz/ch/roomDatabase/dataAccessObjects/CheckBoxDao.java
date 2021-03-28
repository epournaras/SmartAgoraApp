package smartagora.ethz.ch.roomDatabase.dataAccessObjects;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import smartagora.ethz.ch.roomDatabase.entityModels.CheckBoxEntity;

/**
 * @Dao Creates a Data Access Object in the database using an CheckBoxDao Interface Class
 */
@Dao
public interface CheckBoxDao {

    /**
     * @Insert parameter insert the checkbox entity as a object into the Database Table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CheckBoxEntity checkBox);

    /**
     * Gets all checkboxes from specific question in Assignment.
     *
     * @return the all checkboxes from specific question in Assignment from Checkbox Table
     */
    @Query("SELECT * FROM CheckBox WHERE assignmentId=:assignmentId AND questionId=:questionId")
    List<CheckBoxEntity> getAllCheckBoxesFromSpecificAssignmentQuestion(final String assignmentId, final String questionId);


}
