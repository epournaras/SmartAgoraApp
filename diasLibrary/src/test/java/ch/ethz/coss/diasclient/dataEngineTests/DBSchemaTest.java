package ch.ethz.coss.diasclient.dataEngineTests;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import ch.ethz.coss.diasclient.BuildConfig;
import ch.ethz.coss.diasclient.androiddependent.dataEngine.database.DBManager;

import static junit.framework.Assert.assertTrue;

/**
 *
 * Created by jubatyrn on 31.10.17.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, manifest = "/src/main/AndroidManifest.xml")
public class DBSchemaTest {

    private Context context;
    private DBManager helper;
    private SQLiteDatabase db;

    @Before
    public void setup(){
        context = RuntimeEnvironment.application;
        helper = DBManager.getInstance(context);
        db = helper.getReadableDatabase();
    }

    @After
    public void cleanup() {
        db.close();
    }

    @Test
    public void testDBCreated(){
        helper = DBManager.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();
        // Verify is the DB is opening correctly
        assertTrue("DB didn't open", db.isOpen());
        db.close();
    }
}
