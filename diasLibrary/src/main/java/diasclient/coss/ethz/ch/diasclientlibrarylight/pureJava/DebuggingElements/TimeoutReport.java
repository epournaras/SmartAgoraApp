package diasclient.coss.ethz.ch.diasclientlibrarylight.pureJava.DebuggingElements;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeoutReport {
    public final String time = new SimpleDateFormat("dd. MMMM, HH:mm (ss 'sekunden')").format(new Date());
    public final String dataSourceID;

    public TimeoutReport(String dataSourceID) {
        this.dataSourceID = dataSourceID;
    }
}
