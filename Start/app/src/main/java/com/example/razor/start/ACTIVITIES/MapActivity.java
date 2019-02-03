package com.example.razor.start.ACTIVITIES;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.razor.start.VOLLEYADAPTERS.AppController;
import com.example.razor.start.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback{

    public String TAG="mytag";
    private GoogleMap mMap;
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    TextView deviceNameMap;
    ImageView nextBtnMap, prevBtnMap;
    private FusedLocationProviderClient getmFusedLocationProviderClient;
    LatLng ln;
    Marker mMarker;


    public int tot = 0,count = 0;
    final Handler h =new Handler();
    ArrayList<String> ids = new ArrayList<>();
    ArrayList<String> devnamemap = new ArrayList<>();
    ArrayList<Double> latMap = new ArrayList<>();
    ArrayList<Double> lonMap = new ArrayList<>();
    ArrayList<View> viewsToFadeOut = new ArrayList<View>();
    ArrayList<View> viewsToFadeIn = new ArrayList<View>();
    RelativeLayout mapLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        int MY_PERMISSIONS_ACCESS_LOCATION = 0;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapactivity);

        ImageButton back;

        back = findViewById(R.id.back);
        deviceNameMap = findViewById(R.id.devicenamemap);
        nextBtnMap = findViewById(R.id.arrowrightmap);
        prevBtnMap = findViewById(R.id.arrowleftmap);
        mapLayout = findViewById(R.id.map);


        mapLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prevBtnMap.setVisibility(View.VISIBLE);
                nextBtnMap.setVisibility(View.VISIBLE);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MapActivity.this, Devices.class);
                startActivity(intent);
                finish();
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("alldetails",Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString("UUID","");

        String tag_json_obj = "json_obj_req";
        String url = "http://www.xclency.com/LaxGetDevices.php?UID="+uuid;

        final ProgressDialog p = new ProgressDialog(MapActivity.this);
        p.setMessage("Fetching Devices...Please Wait");
        p.show();


        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        p.cancel();
                        try{
                            tot=response.length();
                            for (int i=0;i<tot;i++){
                                JSONObject jsonObject = (JSONObject)response.get(i);
                                ids.add(jsonObject.getString("DeviceID"));
                                devnamemap.add(jsonObject.getString("DeviceName"));
                                System.out.println("SIZEEEE: "+ids.size());

                            }
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
        mGeoDataClient = Places.getGeoDataClient(this, null);
        mPlaceDetectionClient = Places.getPlaceDetectionClient(this, null);
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map_fragment);
        mapFragment.getMapAsync(this);

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
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
    }


    public void prev_map(View view){
        viewsToFadeOut.add(nextBtnMap);
        viewsToFadeOut.add(prevBtnMap);
        for (View v : viewsToFadeOut)
        {
            v.setAlpha(1);
        }

        for (View v : viewsToFadeOut)
        {
            v.animate().alpha(0.0f).setDuration(2000).start();
        }

        if(count==0)
            Toast.makeText(this,"This is the first device!",Toast.LENGTH_LONG).show();
        else
        {
            h.removeCallbacksAndMessages(null);
            count=count-1;
            onMapReady(mMap);
        }
    }
    public void next_map(View view){
        viewsToFadeOut.add(nextBtnMap);
        viewsToFadeOut.add(prevBtnMap);

        for (View v : viewsToFadeOut)
        {
            v.setAlpha(1);
        }

        for (View v : viewsToFadeOut)
        {
            v.animate().alpha(0.0f).setDuration(2000).start();
        }

        if(count==(tot-1))
            Toast.makeText(this,"This is the last device!",Toast.LENGTH_LONG).show();
        else
        {
            h.removeCallbacksAndMessages(null);
            count=count+1;
            onMapReady(mMap);
        }
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        getLocationPermission();
        final int[] countClick = {0};

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                if (countClick[0] == 0)
                {
                    prevBtnMap.setVisibility(View.VISIBLE);
                    nextBtnMap.setVisibility(View.VISIBLE);
                    viewsToFadeIn.add(nextBtnMap);
                    viewsToFadeIn.add(prevBtnMap);
                    for (View v : viewsToFadeIn) {
                        v.setAlpha(0); // make invisible to start
                    }

                    for (View v : viewsToFadeIn) {
                        // 3 second fade in time
                        v.animate().alpha(1.0f).setDuration(2000).start();
                    }
                    countClick[0]=1;
                }
                else {
                    viewsToFadeOut.add(nextBtnMap);
                    viewsToFadeOut.add(prevBtnMap);
                    for (View v : viewsToFadeIn) {
                        v.setAlpha(1); // make invisible to start
                    }
                    for (View v : viewsToFadeIn) {
                        v.animate().alpha(0.0f).setDuration(2000).start();
                    }
                    countClick[0] =0;
                }
            }
        });

        final ImageView pointToLocation;

        pointToLocation = findViewById(R.id.pointToLocation);

        SharedPreferences sharedPreferences = getSharedPreferences("alldetails",Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString("UUID","");
        String tag_json_obj = "json_obj_req";
        String url = "http://www.xclency.com/LaxGetDevices.php?UID="+uuid;
                final JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                        url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(final JSONArray response) {
                                Log.d(TAG, response.toString());
                                try{
                                        JSONObject jsonObject = (JSONObject) response.get(count);

                                        Double lat = Double.parseDouble(jsonObject.getString("Lat:"));
                                        Double longi = Double.parseDouble(jsonObject.getString("Lon:"));
                                        String curr_name = devnamemap.get(count);
                                        deviceNameMap.setText("Device: "+curr_name);
                                        Log.i(TAG, "onResponse: lat="+lat+"longi="+longi);
                                        final LatLng ln = new LatLng(lat,longi);
//                                        final LatLng ln = new LatLng(22.347836, 87.335610);

                                        pointToLocation.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ln, 18));
                                            }
                                        });
                                        String tit = "Device Name: "+jsonObject.getString("DeviceName");
                                        mMap.addMarker(new MarkerOptions().position(ln).icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pointer2)).title(tit));
                                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ln,18));


                                }catch (Exception e){
                                    e.printStackTrace();
                                }
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                });
                AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, Devices.class));
        finish();
        super.onBackPressed();
    }
}
