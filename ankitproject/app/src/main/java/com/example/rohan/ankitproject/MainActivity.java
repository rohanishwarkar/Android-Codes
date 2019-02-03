package  com.example.rohan.ankitproject;

import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rohan.ankitproject.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {


    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";
    private static final int M_MAX_ENTRIES = 5;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    public String latitude, longitude;
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    Button searchaddedbutton;
    EditText search_text;
    Double curr_latitude;
    Double curr_longitude;
    Double edit_latitude;
    Double edit_longitude;
    String curr_title;
    Marker curr_marker;
    Double search_latitude;
    Double search_longitude;
    Button b;
    Button nearbyps;
    Spinner spinner;
    private GoogleMap mMap;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private String[] mLikelyPlaceNames;
    private String[] mLikelyPlaceAddresses;
    private String[] mLikelyPlaceAttributions;
    private LatLng[] mLikelyPlaceLatLngs;
    private int currentfragment = 0;

    PolylineOptions polyOptions;
    public int count=0;
    public int[] allcolors={Color.RED,Color.GREEN,Color.YELLOW,Color.CYAN};
    Button bb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int MY_PERMISSIONS_ACCESS_LOCATION = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mGeoDataClient = Places.getGeoDataClient(this, null);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);



        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        bb=findViewById(R.id.getdetails);
        bb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialogDetails = null;
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                View dialogview = inflater.inflate(R.layout.getalldetails, null);
                AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(MainActivity.this);
                dialogbuilder.setView(dialogview);
                dialogDetails = dialogbuilder.create();
                dialogDetails.show();

            }
        });


    }
    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }





    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[],
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }




    private void getDeviceLocation() {
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            System.out.println("Rohan Ishwarkar");
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(mLastKnownLocation.getLatitude(),
                                            mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            LatLng current_loc = new LatLng(22.3196,87.3099);
                            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(current_loc, 17.5f));
                        } else {
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));

                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void print(LatLng ln){
        System.out.println(ln.latitude+" "+ln.longitude);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
//        mMap.addPolyline(polyOptions);
//        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(this, R.raw.map_style));
        getLocationPermission();
        getDeviceLocation();

        LatLng p1 = new LatLng(22.32024976598328, 87.30958241969347);
        LatLng p2 = new LatLng(22.320440511637887, 87.3109832033515);
        LatLng p3 = new LatLng(22.320528905877225, 87.31161151081324);
        LatLng p4 = new LatLng(22.32077733991326, 87.31348469853403);
        LatLng p5 = new LatLng(22.319206398147333, 87.31396581977604);
        LatLng p6 = new LatLng(22.31846759967736, 87.31244500726461);
        LatLng p7 = new LatLng(22.31812115085315, 87.3098301887512);

        LatLng p8 = new LatLng(22.320542862857266, 87.31158837676048);
        LatLng p9 = new LatLng(22.318376102533414, 87.31188878417014);
        LatLng p10 = new LatLng(22.319671634471785, 87.3117147758603);
        LatLng p11 = new LatLng(22.31968342043852, 87.3102891817689);
        LatLng p12 = new LatLng(22.31903426031421, 87.31174327433109);
        LatLng p13 = new LatLng(22.31912172496947, 87.31033980846406);

        // Getting URL to the Google Directions API
        String url = getUrl(p1 , p2);
        FetchUrl FetchUrl = new FetchUrl();
        FetchUrl.execute(url);


        url = getUrl(p2, p3);
        FetchUrl = new FetchUrl();
        FetchUrl.execute(url);

        url = getUrl(p3 , p4);
        FetchUrl = new FetchUrl();
        FetchUrl.execute(url);


        url = getUrl(p4, p5);
        FetchUrl = new FetchUrl();
        FetchUrl.execute(url);

        url = getUrl(p5, p6);
        FetchUrl = new FetchUrl();
        FetchUrl.execute(url);

        url = getUrl(p6, p7);
        FetchUrl = new FetchUrl();
        FetchUrl.execute(url);

        url = getUrl(p7, p1);
        FetchUrl = new FetchUrl();
        FetchUrl.execute(url);

        url = getUrl(p8, p9);
        FetchUrl = new FetchUrl();
        FetchUrl.execute(url);

        url = getUrl(p10, p11);
        FetchUrl = new FetchUrl();
        FetchUrl.execute(url);

        url = getUrl(p12, p13);
        FetchUrl = new FetchUrl();
        FetchUrl.execute(url);

        mMap.setInfoWindowAdapter(new IncidentInfoWindowAdapter(this));
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
//                print(latLng);
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                curr_latitude = latLng.latitude;
                curr_longitude = latLng.longitude;
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                int max2 = 0, max3 = 0;
                double[] maxvec2 = new double[4];
                double[] maxvec3 = new double[4];
//                {0.9, 0.1, 0, 0},
//                {0.8, 0.2, 0, 0},
//                {0.7, 0.3, 0, 0}

                double[][] init_vec_array = {
                        {0.22, 0.32,0.26,0.22},
                        {0.15,0.26,0.26,0.35},
                        {0.7, 0.3, 0, 0}
                };
                double[][] prob_tran_matrix = {
                        {0.7, 0.3, 0, 0},
                        {0, 0.6, 0.4, 0},
                        {0, 0, 0.5, 0.5},
                        {0, 0, 0, 1}
                };
                Random rand = new Random();
                double[] initial_vector = init_vec_array[1];
                double[] ii=initial_vector;
                int start_year = 2018;
                Map<Integer, Double[]> max_val_map = new HashMap<Integer, Double[]>();
                int cc = 0;
                while (true) {
                    double[] temp_vector = new double[4];
                    cc = cc + 1;
                    for (int i = 0; i < 4; i++) {
                        double ans = 0;
                        for (int j = 0; j < 4; j++) {
                            ans = ans + prob_tran_matrix[j][i] * initial_vector[j];
                        }
                        temp_vector[i] = Math.round(ans * 100.0) / 100.0;
                    }
                    double max_value = 0;
                    for (int k = 0; k < 4; k++) {
                        if (temp_vector[k] > max_value)
                            max_value = temp_vector[k];
                    }
                    if (max_value == temp_vector[1]) {
                        max2 = start_year + cc;
                        maxvec2 = temp_vector;
                    } else if (max_value == temp_vector[2]) {
                        max3 = start_year + cc;
                        maxvec3 = temp_vector;
                    }
                    initial_vector = temp_vector;
                    if (max_value == temp_vector[3])
                        break;
                }
                windowdetails wd=new windowdetails(ii,maxvec2,maxvec3,initial_vector,max2,max3,start_year+cc,start_year);
                Gson gson = new Gson();
                String info = gson.toJson(wd);
                markerOptions.title(info);
                mMap.addMarker(markerOptions);
            }
        });

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                marker.showInfoWindow();
                return true;
            }
        });

    }

    private String getUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;


        // Sensor enabled
        String sensor = "key=AIzaSyAK5e5WDwHt5qzvsmcnrk2ly-WHzsxy2M8";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;


        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);

        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                DataParser parser = new DataParser();
                Log.d("ParserTask", parser.toString());

                // Starts parsing data
                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(30);
                lineOptions.color(allcolors[count]);
                count=count+1;
                if(count>3)
                    count=0;
                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }

            // Drawing polyline in the Google Map for the i-th route
            if(lineOptions != null) {
                mMap.addPolyline(lineOptions);
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }

}
