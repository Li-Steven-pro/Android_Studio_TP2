package steven.li.geolocalized;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public double latitude;
    public double longitude;
    public LocationManager locationManager;
    public Criteria criteria;
    public String bestProvider;

    private static  final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Get the button that will request
        Button button = findViewById(R.id.button);
        ListView listView = findViewById(R.id.list);
        button.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                // Ask the permission for using localisation
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                // Get location, set url request with localisation and get images
                MainActivity.this.getCurrentLocation();
            }
        });
    }

    // We are sure we request permission
    @SuppressLint("MissingPermission")
    protected void getCurrentLocation(){
        // Request the service for the location
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        bestProvider = null;
        // Get all providers that can give the current location
        List<String> providers = locationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            // Get the last location
            Location l = locationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        if(bestLocation == null){
            return;
        }else{
            Log.i("bestLocation ","Latitude" +
                    bestLocation.getLatitude());
            Log.i("bestLocation ","Latitude" +
                    bestLocation.getLongitude());
            // Set the url for the request
            URL url = null;
            String s = new String("https://api.flickr.com/services/rest/?" +
                    "method=flickr.photos.search" +
                    "&license=4" +
                    "&api_key=4167810fbc47cc7f1da6d151edf224d5" +
                    "&has_geo=1&lat=" + bestLocation.getLatitude() +
                    "&lon=" + bestLocation.getLongitude()+ "&per_page=10&format=json"); // well formed JSON
            try {
                url = new URL(s);
                new AsyncFlickrJSONDataForList(findViewById(R.id.list), new MyAdapter() ,url).execute();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
    }


    //Function execute when a permission is request
    public void onRequestPermissionsResult(int requestCode,@NonNull String permissions[],@NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode,permissions,grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION &&  grantResults.length > 0){
            //getCurrentLocation();
        }else {
            Toast.makeText(this, "Permission denied!" , Toast.LENGTH_SHORT).show();
        }
    }
}