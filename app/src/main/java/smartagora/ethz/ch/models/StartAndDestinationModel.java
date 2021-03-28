package smartagora.ethz.ch.models;

/*
 Start and destination model store the users start and destination location
 information.
 */
public class StartAndDestinationModel {

    private String StartLatitude = null;                // user start latitude
    private String StartLongitude = null;               // user start longitude
    private String DestinationLatitude = null;          // user destination latitude
    private String DestinationLongitude = null;         // user destination longitude
    private String Mode = null;
    private String DefaultCredit = null;

    /**
     * Setter and getter for all the variables
     */

    public String getStartLatitude() {
        return StartLatitude;
    }

    public void setStartLatitude(String startLatitude) {
        StartLatitude = startLatitude;
    }

    public String getStartLongitude() {
        return StartLongitude;
    }

    public void setStartLongitude(String startLongitude) {
        StartLongitude = startLongitude;
    }

    public String getDestinationLatitude() {
        return DestinationLatitude;
    }

    public void setDestinationLatitude(String destinationLatitude) {
        DestinationLatitude = destinationLatitude;
    }

    public String getDestinationLongitude() {
        return DestinationLongitude;
    }

    public void setDestinationLongitude(String destinationLongitude) {
        DestinationLongitude = destinationLongitude;
    }

    public void setMode(String mode) {
        this.Mode = mode;
    }

    public String getMode() {
        return this.Mode;
    }

    public void setDefaultCredit(String defaultCredit) {
        this.DefaultCredit = defaultCredit;
    }

    public String getDefaultCredit() {
        return this.DefaultCredit;
    }

}
