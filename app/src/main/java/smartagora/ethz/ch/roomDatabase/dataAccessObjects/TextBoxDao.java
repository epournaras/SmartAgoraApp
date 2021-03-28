package smartagora.ethz.ch.roomDatabase.dataAccessObjects;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import smartagora.ethz.ch.roomDatabase.entityModels.TextBoxEntity;

/**
 * @Dao Creates a Data Access Object in the database using an TextBoxDao Interface Class
 */
@Dao
public interface TextBoxDao {

    /**
     * @Insert parameter insert the TextBox entity as a object into the Database Table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TextBoxEntity textBox);

    /**
     * Gets textBox from specific question in Assignment.
     *
     * @return the textBox from specific question in Assignment from TextBox Table
     */
    @SuppressWarnings("SpellCheckingInspection")
    @Query("SELECT * FROM TextBox WHERE assignmentId=:assignmentId AND questionId=:questionId")
    TextBoxEntity getTextBoxeFromSpecificAssignmentQuestion(final String assignmentId, final String questionId);

    /**
     * Gets last inserted row from TextBox Table.
     *
     * @return the last inserted row of TextBox Entity
     */
    @Query("select * from TextBox order by id desc limit 1;")
    TextBoxEntity getLastInsertedRow();


}
