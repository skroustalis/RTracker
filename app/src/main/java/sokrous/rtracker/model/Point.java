package sokrous.rtracker.model;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;


public class Point {

    public int recordId;

    public GeoPoint geoPoint;

    public Point(){}

    public Point(int recordId, GeoPoint geoPoint){
        this.recordId = recordId;
        this.geoPoint = geoPoint;
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }
}
