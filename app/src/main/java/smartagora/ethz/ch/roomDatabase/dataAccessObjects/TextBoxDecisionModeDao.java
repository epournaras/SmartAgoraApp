package smartagora.ethz.ch.roomDatabase.dataAccessObjects;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import smartagora.ethz.ch.roomDatabase.entityModels.TextBoxDecisionModeEntity;

/**
 * @Dao Creates a Data Access Object in the database using an TextBoxDecisionModeDao Interface Class
 */
@Dao
public interface TextBoxDecisionModeDao {

    /**
     * @Insert parameter insert the TextBoxDecisionMode entity as a object into the Database Table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TextBoxDecisionModeEntity textBoxDecisionModeEntity);

    /**
     * Gets textBox associate question from TextBoxDecisionMode Table by textBoxId.
     *
     * @return the textBox associate question as a TextBoxDecisionMode Entity
     */
    @Query("SELECT * FROM TextBoxDecisionMode WHERE textBoxId=:textBoxId")
    TextBoxDecisionModeEntity getTextBoxDecisionModeQuestion(final String textBoxId);

}
