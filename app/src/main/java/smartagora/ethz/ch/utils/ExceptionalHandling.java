package smartagora.ethz.ch.utils;



import com.crashlytics.android.Crashlytics;

public class ExceptionalHandling {


    public static void logException(Exception ex) {
        ex.printStackTrace();
        Crashlytics.getInstance().core.logException(new Exception(ex));
    }
}
