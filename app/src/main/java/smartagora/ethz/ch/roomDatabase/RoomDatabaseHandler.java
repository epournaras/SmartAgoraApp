package smartagora.ethz.ch.roomDatabase;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.concurrent.Executors;

import smartagora.ethz.ch.roomDatabase.dataAccessObjects.AnswerDao;
import smartagora.ethz.ch.roomDatabase.dataAccessObjects.AssignmentDao;
import smartagora.ethz.ch.roomDatabase.dataAccessObjects.CheckBoxDecisionModeDao;
import smartagora.ethz.ch.roomDatabase.dataAccessObjects.CheckBoxDao;
import smartagora.ethz.ch.roomDatabase.dataAccessObjects.CombinationDao;
import smartagora.ethz.ch.roomDatabase.dataAccessObjects.LikertScaleDao;
import smartagora.ethz.ch.roomDatabase.dataAccessObjects.LikertScaleDecisionModeDao;
import smartagora.ethz.ch.roomDatabase.dataAccessObjects.QuestionDao;
import smartagora.ethz.ch.roomDatabase.dataAccessObjects.QuestionSensorsDao;
import smartagora.ethz.ch.roomDatabase.dataAccessObjects.RadioDecisionModeDao;
import smartagora.ethz.ch.roomDatabase.dataAccessObjects.RadioButtonDao;
import smartagora.ethz.ch.roomDatabase.dataAccessObjects.SensorDao;
import smartagora.ethz.ch.roomDatabase.dataAccessObjects.SensorReadingDao;
import smartagora.ethz.ch.roomDatabase.dataAccessObjects.StartAndDestinationDao;
import smartagora.ethz.ch.roomDatabase.dataAccessObjects.TextBoxDao;
import smartagora.ethz.ch.roomDatabase.dataAccessObjects.TextBoxDecisionModeDao;
import smartagora.ethz.ch.roomDatabase.entityModels.AnswerEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.AssignmentEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.CheckBoxDecisionModeEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.CheckBoxEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.CombinationEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.LikertScaleDecisionModeEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.LikertScaleEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.QuestionEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.QuestionSensorsEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.RadioDecisionModeEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.RadioButtonEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.SensorEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.SensorReadingEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.StartAndDestinationEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.TextBoxDecisionModeEntity;
import smartagora.ethz.ch.roomDatabase.entityModels.TextBoxEntity;


/**
 * @Database includes list of entities, version and exportSchema.It is used to create all tables in database
 * entities includes the list of all database tables classes
 * version specify the database current version
 */
@Database(entities = {StartAndDestinationEntity.class, AssignmentEntity.class, AnswerEntity.class, CheckBoxDecisionModeEntity.class,
        CheckBoxEntity.class, CombinationEntity.class, LikertScaleEntity.class, LikertScaleDecisionModeEntity.class, QuestionEntity.class,
        QuestionSensorsEntity.class, RadioDecisionModeEntity.class, RadioButtonEntity.class, SensorEntity.class,
        SensorReadingEntity.class, TextBoxEntity.class, TextBoxDecisionModeEntity.class}, version = 4, exportSchema = false)

public abstract class RoomDatabaseHandler extends RoomDatabase {

    private static RoomDatabaseHandler roomDatabase;

    /**
     * Gets answerDao Interface object.
     */
    public abstract AnswerDao getAnswerDao();

    /**
     * Gets assignmentDao Interface object.
     */
    public abstract AssignmentDao getAssignmentDao();

    /**
     * Gets CheckBoxDecisionModeDao Interface Object.
     */
    public abstract CheckBoxDecisionModeDao getCheckBoxDecisionModeDao();

    /**
     * Gets checkBoxDao Interface Object.
     */
    public abstract CheckBoxDao getCheckBoxDao();

    /**
     * Gets combinationDao Interface Object.
     */
    public abstract CombinationDao getCombinationDao();

    /**
     * Gets likertScaleDao Interface Object.
     */
    public abstract LikertScaleDao getLikertScaleDao();

    /**
     * Gets likertScaleDecisionModeDao Interface Object.
     */
    public abstract LikertScaleDecisionModeDao getLikertScaleDecisionModeDao();

    /**
     * Gets textBoxDecisionModeDao Interface Object
     */
    public abstract TextBoxDecisionModeDao getTextBoxDecisionModeDao();

    /**
     * Gets questionDao Interface Object
     */
    public abstract QuestionDao getQuestionDao();

    /**
     * Gets questionSensorsDao Interface Object
     */
    public abstract QuestionSensorsDao getQuestionSensorsDao();

    /**
     * Gets radioDecisionModeDao Interface Object
     */
    public abstract RadioDecisionModeDao getRadioDecisionModeDao();

    /**
     * Gets radioButtonDao Interface Object
     */
    public abstract RadioButtonDao getRadioButtonDao();

    /**
     * Gets sensorDao Interface Object
     */
    public abstract SensorDao getSensorDao();

    /**
     * Gets sensorReadingDao Interface Object
     */
    public abstract SensorReadingDao getSensorReadingDao();

    /**
     * Gets startAndDestinationDao Interface Object
     */
    public abstract StartAndDestinationDao getStartAndDestinationDao();

    /**
     * Gets textBoxDao Interface Object
     */
    public abstract TextBoxDao getTextBoxDao();


    /**
     * Gets Application database object.
     *
     * @return the app database
     */
    public static RoomDatabaseHandler getAppDatabase(final Context context) {


        if (roomDatabase == null) {
            roomDatabase =
                    Room.databaseBuilder(context.getApplicationContext(), RoomDatabaseHandler.class, "smartagora")

                            .fallbackToDestructiveMigration()
                            //      Add Static data one time in Table in separate Thread
                            .addCallback(new Callback() {
                                @Override
                                public void onCreate(@NonNull SupportSQLiteDatabase db) {
                                    super.onCreate(db);
                                    Executors.newSingleThreadScheduledExecutor().execute(new Runnable() {
                                        @Override
                                        public void run() {
                                            getAppDatabase(context).getSensorDao().insert(SensorEntity.insertAllSensors());
                                        }
                                    });
                                }
                            })
                            .addMigrations(MIGRATION_1_2, MIGRATION_2_3)

                            // allow queries on the main thread.
                            // Don't do this on a real app! See PersistenceBasicSample for an example.

                            .allowMainThreadQueries()
                            .build();
        }
        return roomDatabase;
    }

    private static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {

            database.execSQL("Drop table if  exists Sensor");
            String groupsQuery = "create table Sensor (id integer primary key autoincrement, name text not null);";
            database.execSQL(groupsQuery);
            new PopulateDBAsyncTask(roomDatabase).execute();
        }
    };

    private static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Override
        public void migrate(SupportSQLiteDatabase database) {
            database.execSQL("ALTER TABLE Answer "
                    + " ADD COLUMN aggregateValue TEXT default 0 NOT NULL");

        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {

        private final SensorDao sensorDao;

        private PopulateDBAsyncTask(RoomDatabaseHandler roomDatabaseHandler) {
            sensorDao = roomDatabaseHandler.getSensorDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            sensorDao.insert(new SensorEntity("Light"));
            sensorDao.insert(new SensorEntity("Gyroscope"));
            sensorDao.insert(new SensorEntity("Proximity"));
            sensorDao.insert(new SensorEntity("Accelerometer"));
            sensorDao.insert(new SensorEntity("Location"));
            sensorDao.insert(new SensorEntity("Noise"));
            return null;
        }
    }


}
