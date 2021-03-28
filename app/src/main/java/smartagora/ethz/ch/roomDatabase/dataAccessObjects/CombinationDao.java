package smartagora.ethz.ch.roomDatabase.dataAccessObjects;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import smartagora.ethz.ch.roomDatabase.entityModels.CombinationEntity;

/**
 * @Dao Creates a Data Access Object in the database using an CombinationDao Interface Class
 */
@Dao
public interface CombinationDao {

    /**
     * @Insert parameter insert the Combination entity as a object into the Database Table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CombinationEntity combinationEntity);

    /**
     * Gets all combinations of specific question in Assignment
     *
     * @return the list of combinations from specific question in Assignment from Combinations Table
     */
    @Query("SELECT Combination.id, Combination.questionId,Combination.assignmentId,Combination.`order`,Combination.credits FROM Combination where questionId=:questionId and assignmentId=:assignmentId")
    List<CombinationEntity> getCombinationFromQuestion(final String assignmentId, final String questionId);


    /**
     * Gets last inserted row from Combination Table.
     *
     * @return the last inserted row of Combination Entity
     */
    @Query("select * from Combination order by id desc limit 1;")
    CombinationEntity getLastInsertedRow();


}
