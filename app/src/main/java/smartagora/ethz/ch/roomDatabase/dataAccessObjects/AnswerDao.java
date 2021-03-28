package smartagora.ethz.ch.roomDatabase.dataAccessObjects;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import smartagora.ethz.ch.roomDatabase.entityModels.AnswerEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.QuestionEntity;

/**
 * @Dao Creates a Data Access Object in the database using an AnswerDao Interface Class
 */
@Dao
public interface AnswerDao {

    /**
     * @Insert parameter insert the answer entity as a object into the Database Table
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AnswerEntity answer);

    /**
     * Gets answered question ids From Assignment.
     *
     * @return the question ids of those questions whose answered by joining the Question,Answer and Assignment Tables
     */
    @Query("SELECT Question.id FROM Answer JOIN Question on (Answer.assignmentId AND Answer.questionId) = (Question.assignmentId AND Question.id) join Assignment on Question.assignmentId = Assignment.id WHERE Assignment.name=:assignmentName ")
    List<Integer> getAnsweredQuestionIds(final String assignmentName);


    /**
     * Gets all answers from assignment.
     *
     * @return the answers list of those questions whose answered from assignment
     */

    @Query("SELECT Answer.id,Answer.answer ,Answer.questionId,Answer.assignmentId,Answer.credits,Answer.answeringTime, Answer.aggregateValue FROM Answer JOIN Question on Answer.questionId = Question.id and Answer.assignmentId = Question.assignmentId  join Assignment on Question.assignmentId = Assignment.id WHERE Assignment.name=:assignmentName ")
    List<AnswerEntity> getAnsweredQuestions(final String assignmentName);


    /**
     * Gets all answers from assignment.
     *
     * @return the answers list of those questions whose answered from assignment
     */
    @Query("SELECT Answer.id,Answer.answer,Answer.questionId,Answer.assignmentId,Answer.credits,Answer.answeringTime, Answer.aggregateValue FROM Answer JOIN Question on  Answer.questionId = Question.id and Answer.assignmentId = Question.assignmentId  join Assignment on Question.assignmentId = Assignment.id WHERE Assignment.name=:assignmentName ")
    List<AnswerEntity> getAnswersFromAssignment(final String assignmentName);


    /**
     * Gets answers from assignment.
     *
     * @param assignmentName the assignment name
     * @return the answered questions from assignment in specific latitude and longitude location
     */
    @Query("SELECT Question.id, Question.assignmentId,Question.longitude,Question.latitude,Question.question,Question.visibility,Question.vicinity,Question.type,Question.time,Question.sequence,Question.mandatory,Question.frequency FROM Answer JOIN Question on  Answer.questionId = Question.id and Answer.assignmentId = Question.assignmentId  join Assignment on Question.assignmentId = Assignment.id WHERE Assignment.name=:assignmentName and Question.latitude =:latitude and Question.longitude=:longitude")
    List<QuestionEntity> getCheckPointAnswers(final String assignmentName, final String latitude, final String longitude);

    @Query("SELECT Answer.id, Answer.credits, Answer.answeringTime, Answer.answer, Answer.assignmentId, Answer.questionId, Answer.aggregateValue FROM Answer JOIN Question on  Answer.questionId = Question.id and Answer.assignmentId = Question.assignmentId  join Assignment on Question.assignmentId = Assignment.id WHERE Assignment.name=:assignmentName and Question.latitude =:latitude and Question.longitude=:longitude")
    List<AnswerEntity> getCheckPointLocalAggregateAnswers(final String assignmentName, final String latitude, final String longitude);


    @Query("Select Answer.questionId, Answer.assignmentId, Answer.answer, Answer.answeringTime, Answer.credits, Answer.id, Answer.aggregateValue from Answer where Answer.assignmentId=:assignmentId and Answer.questionId=:questionId")
    AnswerEntity getAnswerFromQuestion(final String assignmentId, String questionId);

}
