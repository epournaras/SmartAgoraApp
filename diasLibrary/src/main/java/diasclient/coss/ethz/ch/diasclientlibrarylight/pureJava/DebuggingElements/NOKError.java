package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.DebuggingElements;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NOKError {
    public final String time = new SimpleDateFormat("dd. MMMM, HH:mm (ss 'sekunden')").format(new Date());
    public final String dataSourceID;
    public final String errorMessage;
    public final int errorCode;
    public final boolean handled;

    public NOKError(String dataSourceID, String errorMessage, int errorCode, boolean handled) {
        this.dataSourceID = dataSourceID;
        this.errorMessage = errorMessage;
        this.errorCode = errorCode;
        this.handled = handled;
    }
}
