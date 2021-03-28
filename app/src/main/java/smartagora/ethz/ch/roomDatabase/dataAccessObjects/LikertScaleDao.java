package smartagora.ethz.ch.roomDatabase.dataAccessObjects;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import smartagora.ethz.ch.roomDatabase.entityModels.LikertScaleEntity;

/**
 * @Dao Creates a Data Access Object in the database using an LikertScaleDao Interface Class
 */
@Dao
public interface LikertScaleDao {


    /**
     * @Insert parameter insert the LikertScale entity as a object into the Database Table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LikertScaleEntity likertScale);

    /**
     * Gets likertScale from specific question in Assignment.
     *
     * @return the likertScale from specific question in Assignment from LikertScale Table
     */
    @Query("SELECT * FROM LikertScale WHERE assignmentId=:assignmentId AND questionId=:questionId")
    LikertScaleEntity getLikertScalesFromSpecificAssignmentQuestion(final String assignmentId, final String questionId);

    /**
     * Gets last inserted row from LikertScale Table.
     *
     * @return the last inserted row of LikertScale Entity
     */
    @Query("select * from LikertScale order by id desc limit 1;")
    LikertScaleEntity getLastInsertedRow();


}
