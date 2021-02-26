package steven.li.geolocalized;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import java.text.BreakIterator;

public class MyLocationListener implements LocationListener {
    public String longitude, latitude;

    @Override
    public void onLocationChanged(Location loc) {
        longitude = "" + loc.getLongitude();
        Log.i("TAG", longitude);
        latitude = "" + loc.getLatitude();
        Log.i("TAG", latitude);
        String s = longitude + "\n" + latitude + "\n";
        Log.i("TAG", s);
    }

    @Override
    public void onProviderDisabled(String provider) {}

    @Override
    public void onProviderEnabled(String provider) {}

}