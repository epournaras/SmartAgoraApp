package smartagora.ethz.ch.utils;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;

import java.util.ArrayList;
import java.util.List;


public class Ellipse {
    private List<LatLng> ellipsePointsList;
    private int size;
    private LatLng centerPoint;
    private double angle;
    private double radiusX, radiusY;

    /**
     * returns angle of rotation of Ellipse
     */
    public double getAngle() {
        return this.angle;
    }

    /**
     * Create an Ellipse of given: Center, Radius X, Radius Y, Angle (in degrees) of rotation.
     * An ellipse has typically two radius parameters: the min and max radius.
     * Those radii are here radX and radY. Which is which is not important, as the ellipse
     * can be rotated with the given angle.
     *
     * @param center the center point of the ellipse
     * @param radX the horizontal radius of the ellipse
     * @param radY the vertical radius of the ellipse
     * @param angle angle of ellipse
     */
    private void createEllipse(LatLng center, double radX, double radY, double angle) {
        radX = radX / 44459.6;                      //divide given radius X to get appropriate value
        radY = radY / 44459.6;                      //divide given radius Y to get appropriate value
        double theta;                               //angle of each point from the center
        double x1;                                  //x coordinate of point
        double y1;                                  //Y coordinate of point
        double step = 2.0 * Math.PI / 50;           //amount to add to theta each Time (degrees)
        angle = angle * Math.PI / 180.0;            //Convert to radians

        this.ellipsePointsList = new ArrayList<>();

        //Iterate loop 20 times to make 20 points of ellipse at angle "theta"
        for (theta = 0.0; theta < 2.00 * Math.PI; theta += step) {
            x1 = center.longitude + radX * Math.cos(theta) * Math.cos(angle) - radY * Math.sin(theta) * Math.sin(angle);
            y1 = center.latitude - radX * Math.cos(theta) * Math.sin(angle) - radY * Math.sin(theta) * Math.cos(angle);

            this.ellipsePointsList.add(new LatLng(y1, x1));
        }


        size = this.ellipsePointsList.size();
        centerPoint = center;
        this.angle = angle;

        this.radiusX = radX;
        this.radiusY = radY;

    }


    /**
     * Checks whether a point lies in Ellipse or not, by entering values in Ellipse equation. If equation satisfies then point exist in equation.
     *
     * @param questionPoint the point in question
     * @return returns True/False
     */
    public boolean IsInEllipse(LatLng questionPoint) {

        // (x,y): coordinate of point
        // (h,k): coordinate of center of Ellipse
        // theta: angle of rotation of Ellipse
        // a: X Radius of Ellipse
        // b: Y Radius of Ellipse

        //Equation of ellipse : ((((x-h) * Cos (theta) + (y-k) * Sin(theta))/a) ^ 2) + ((((x-h) * Sin (theta) - (y-k) * Cos(theta))/b) ^ 2) <= 1.00
        double xh = questionPoint.longitude - centerPoint.longitude;
        double yk = questionPoint.latitude - centerPoint.latitude;
        double distX = Math.pow(((xh * Math.cos(angle)) + yk * Math.sin(angle)) / radiusX, 2);
        double distY = Math.pow(((xh * Math.sin(angle)) - yk * Math.cos(angle)) / radiusY, 2);
        double result = distX + distY;

        return result <= 1.00;

    }

    /**
     * draw ellipse on current position of user
     *
     * @param position position of the user
     */
    public Polygon drawMarkerWithEllipse(LatLng position, double angle, GoogleMap map, int vicinity) {
        int strokeColor = 0xffff0000; //red outline
        int shadeColor = 0x44ff0000; //opaque red fill

        int yAxis = vicinity/2;

        this.createEllipse(position, vicinity, yAxis, angle); // x = vicinity, y = vicinity/2

        PolygonOptions polygonOptions = new PolygonOptions().fillColor(shadeColor).strokeColor(strokeColor).strokeWidth(8);
        for (int i = 0; i < size; i++) {
            polygonOptions.add(ellipsePointsList.get(i));
        }

        if (map != null)
            return map.addPolygon(polygonOptions);

        return null;
    }
}
