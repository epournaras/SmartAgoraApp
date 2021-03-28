package smartagora.ethz.ch.roomDatabase.dataAccessObjects;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import smartagora.ethz.ch.roomDatabase.entityModels.QuestionEntity;

/**
 * @Dao Creates a Data Access Object in the database using an QuestionDao Interface Class
 */
@Dao
public interface QuestionDao {

    /**
     * @Insert parameter insert the Question entity as a object into the Database Table
     */
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(QuestionEntity question);

    /**
     * Gets all questions from assignment.
     *
     * @return all questions from assignment as a List of Questions Entity
     */
    @Query("SELECT Question.id,Question.assignmentId,Question.frequency,Question.latitude,Question.longitude,Question.mandatory,Question.question,Question.sequence,Question.time,Question.type,Question.vicinity,Question.visibility FROM Question join Assignment on (Question.assignmentId = Assignment.id) WHERE Assignment.name=:assignmentName")
    List<QuestionEntity> getAllQuestionsFromAssignment(final String assignmentName);


    /**
     * Gets sequences of all Questions from Assignment.
     *
     * @return the sequence list of all Questions in Assignment
     */
    @Query("SELECT Question.sequence FROM Question join Assignment on (Question.assignmentId = Assignment.id) WHERE Assignment.name=:assignmentName")
    List<String> getSequenceList(final String assignmentName);


    /**
     * Gets checkpoint Questions from assignment in specific latitude and longitude.
     *
     * @return the checkpoint questions from assignment in specific latitude and longitude location from Question and Assignment Tables through AssignmentName, longitude and latitude
     */
    @Query("SELECT  Question.id, Question.assignmentId,Question.longitude,Question.latitude,Question.question,Question.visibility,Question.vicinity,Question.type,Question.time,Question.sequence,Question.mandatory,Question.frequency FROM Question join Assignment on Question.assignmentId = Assignment.id WHERE Assignment.name =:assignmentName AND Question.latitude=:latitude AND Question.longitude=:longitude")
    List<QuestionEntity> getCheckPointQuestions(final String assignmentName, final String latitude, final String longitude);


    /**
     * Gets specific question from Assignment.
     *
     * @return the specific question from Question Table through QuestionId and AssignmentId
     */
    @Query("SELECT * FROM Question WHERE assignmentId=:assignmentId AND id=:questionId")
    QuestionEntity getQuestion(final String assignmentId, final String questionId);


}
