
package smartagora.ethz.ch;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;


public class Application extends android.app.Application {

    private static final String LOG_TAG = Application.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();

        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable e) {
                handleUncaughtException(e);
            }
        });
        Fabric.with(this, new Crashlytics());
        init();

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(getBaseContext());
    }
    /**
     *
     */
    private void init() {
        Log.d(LOG_TAG, "Inside Application init()");
    }

    private void handleUncaughtException(Throwable e) {
        Log.e(LOG_TAG, "Inside handleUncaughtException: Exception thrown here.");

        Crashlytics.getInstance().core.logException(new RuntimeException(e));

        e.printStackTrace();
        System.exit(0);
    }
}
