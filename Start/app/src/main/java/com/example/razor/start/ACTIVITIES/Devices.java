package com.example.razor.start.ACTIVITIES;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.transition.TransitionManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.razor.start.VOLLEYADAPTERS.AppController;
import com.example.razor.start.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Devices extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    final Handler h = new Handler();
    public int count=0,tot=0;
    String TAG="mytag";
    ArrayList<String> ids = new ArrayList<>();
    ArrayList<String> devname = new ArrayList<>();
    RelativeLayout device_values, aqi_value_layout;
    TextView aqi_value, aqi_decision;
    TextView temperature,humidity,voc,dust10,dust25,dust1,o3,co,co2,nh4,h2s,ch4;
    ImageView prev_device,next_device;
    TextView deviceid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_devices);
        registertoken();
        temperature = (TextView) findViewById(R.id.temperature);
        humidity = (TextView) findViewById(R.id.humidity);
        voc = (TextView) findViewById(R.id.voc);
        dust10 = (TextView) findViewById(R.id.dust10);
        dust25 = (TextView) findViewById(R.id.dust25);
        dust1 = (TextView) findViewById(R.id.dust1);
        o3 = (TextView) findViewById(R.id.o3);
        co = (TextView) findViewById(R.id.co);
        co2 = (TextView) findViewById(R.id.co2);
        nh4 = (TextView) findViewById(R.id.nh4);
        h2s = (TextView) findViewById(R.id.h2s);
        ch4 = (TextView) findViewById(R.id.ch4);
        prev_device = findViewById(R.id.previous_device);
        next_device = findViewById(R.id.next_device);
        deviceid = (TextView) findViewById(R.id.deviceid);
        device_values = findViewById(R.id.device_values);
        aqi_value_layout = findViewById(R.id.aqi_Value_layout);
        aqi_value = findViewById(R.id.aqi_value);
        aqi_decision = findViewById(R.id.aqi_decision);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {



                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();
                        Log.d(TAG, "reg token: "+token);
//                        Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


        deviceid.setOnClickListener(new View.OnClickListener() {

            int c =0;
            @Override
            public void onClick(View v) {

                if (c==0) {
                    prev_device.setVisibility(View.VISIBLE);
                    next_device.setVisibility(View.VISIBLE);
                    c=1;
                }
                else
                {
                    prev_device.setVisibility(View.INVISIBLE);
                    next_device.setVisibility(View.INVISIBLE);
                    c=0;
                }
            }
        });

        prev_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==0)
                    Toast.makeText(Devices.this,"This is the first device!",Toast.LENGTH_LONG).show();
                else
                {
                    h.removeCallbacksAndMessages(null);
                    count=count-1;
                    final ViewGroup vg = (ViewGroup) findViewById(R.id.main_layout);
                    Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                TransitionManager.beginDelayedTransition(vg);
                            }
                            aqi_value_layout.setVisibility(View.GONE);

                        }
                    };
                    handler.postDelayed(runnable,100);

                    Runnable runnable1 = new Runnable() {
                        @Override
                        public void run() {
                            handlerfunction();
                        }
                    };
                    handler.postDelayed(runnable1,1000);

                }
            }
        });

        next_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(count==(tot-1))
                    Toast.makeText(Devices.this,"This is the last device!",Toast.LENGTH_LONG).show();
                else
                {
                    h.removeCallbacksAndMessages(null);
                    count=count+1;
                    final ViewGroup vg = (ViewGroup) findViewById(R.id.main_layout);
                    Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                TransitionManager.beginDelayedTransition(vg);
                            }
                            aqi_value_layout.setVisibility(View.GONE);

                        }
                    };

                    handler.postDelayed(runnable,100);
                    Runnable runnable1 = new Runnable() {
                        @Override
                        public void run() {
                            handlerfunction();
                        }
                    };
                    handler.postDelayed(runnable1,1000);
                }
            }
        });

        SharedPreferences sharedPreferences = getSharedPreferences("alldetails",Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString("UUID","");
        String name = sharedPreferences.getString("Name","");
        String ph = sharedPreferences.getString("ph_no","");
        final ProgressDialog p = new ProgressDialog(Devices.this);
        p.setMessage("Fetching Devices...Please Wait");
        p.show();

        String tag_json_obj = "json_obj_req";
        String url = "http://www.xclency.com/LaxGetDevices.php?UID="+uuid;
        System.out.println(uuid);

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        p.cancel();
                        try{
                            tot=response.length();
                            handlerfunction();
                            System.out.print("device number: "+tot);
                            for (int i=0;i<tot;i++){
                                JSONObject jsonObject = (JSONObject)response.get(i);
                                ids.add(jsonObject.getString("DeviceID"));
                                devname.add(jsonObject.getString("DeviceName"));
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

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View hView =  navigationView.getHeaderView(0);

        TextView dashboard_phno = hView.findViewById(R.id.phno);
        TextView dashboardName = hView.findViewById(R.id.name);
        dashboardName.setText(name);
        dashboard_phno.setText(ph);
        dashboard_phno.setTextColor(getResources().getColor(R.color.cyan));
        dashboardName.setTextColor(getResources().getColor(R.color.blue));
    }
    public void handlerfunction(){
        h.post(new Runnable() {
            @Override
            public void run() {
                updatefunc();
                h.postDelayed(this,5000);
            }
        });
    }
    public void updatefunc(){
                System.out.println(count);
                System.out.println("IDS size : "+ids.size());
                final String curr_device_id=ids.get(count);
                String curr_name = devname.get(count);
                deviceid.setText("Device: "+curr_name);
                String tag_json_obj = "json_obj_req";
                SharedPreferences sharedPreferences = getSharedPreferences("alldetails", Context.MODE_PRIVATE);
                String uuid = sharedPreferences.getString("UUID","");
                String url = "http://www.xclency.com/GetValuesJSon.php?Device=%27"+curr_device_id+"%27&UID="+uuid;

                Log.i(TAG, "url: "+url);
                    String tag_json_obj2 = "json_obj_req";
                    String url_aqi = " http://www.xclency.com/CalculateAQI.php?Device=%27"+curr_device_id+"%27&UID="+uuid;


                    final ViewGroup vg = (ViewGroup) findViewById(R.id.main_layout);
                    Handler handler = new Handler();
                    Runnable runnable = new Runnable() {
                        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                        @Override
                        public void run() {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                TransitionManager.beginDelayedTransition(vg);
                            }
                            aqi_value_layout.setVisibility(View.VISIBLE);

                        }
                    };

                    handler.postDelayed(runnable, 200);



                    JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.POST, url_aqi, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {

                            System.out.println(response.toString());
                            JSONObject jsonObject = null;
                            try {
                                jsonObject = (JSONObject) response.get(0);
                                double aqi = Double.parseDouble(jsonObject.getString("AQI"));

                                if (aqi>0 && aqi<=50){
                                    aqi_value.setText(""+aqi);
                                    aqi_value.setTextColor(getResources().getColor(R.color.green));
                                    aqi_decision.setTextColor(getResources().getColor(R.color.green));
                                    aqi_decision.setText("GOOD");
                                }
                                else if (aqi>50 && aqi<=100){
                                    aqi_value.setTextColor(getResources().getColor(R.color.yellow));
                                    aqi_value.setText(""+aqi);
                                    aqi_decision.setTextColor(getResources().getColor(R.color.yellow));
                                    aqi_decision.setText("MODERATE");
                                }
                                else if (aqi>100 && aqi<=150){
                                    aqi_value.setTextColor(getResources().getColor(R.color.orange));
                                    aqi_value.setText(""+aqi);
                                    aqi_decision.setTextColor(getResources().getColor(R.color.orange));
                                    aqi_decision.setText("UNHEALTHY FOR SENSITIVE GROUP");
                                }
                                else if (aqi>150 && aqi<=200){
                                    aqi_value.setTextColor(getResources().getColor(R.color.red));
                                    aqi_value.setText(""+aqi);
                                    aqi_decision.setTextColor(getResources().getColor(R.color.red));
                                    aqi_decision.setText("UNHEALTHY");
                                }
                                else
                                {
                                    aqi_value.setTextColor(getResources().getColor(R.color.purple));
                                    aqi_value.setText(""+aqi);
                                    aqi_decision.setTextColor(getResources().getColor(R.color.purple));
                                    aqi_decision.setText("VERY UNHEALTHY");
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    AppController.getInstance().addToRequestQueue(jsonArrayRequest, tag_json_obj2);


                JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                        url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                try{
                                    JSONObject jsonObject = (JSONObject) response.get(0);
                                    double temp = Double.parseDouble(jsonObject.getString("Temperature"));

                                    if (temp>20 && temp<=28){
                                        temperature.setText(""+temp+"°");
                                        temperature.setTextColor(getResources().getColor(R.color.red_different));
                                        Log.i(TAG, "Warm");
                                    }
                                    else if (temp>28){
                                        temperature.setText(""+temp+"°");
                                        temperature.setTextColor(getResources().getColor(R.color.red));
                                        Log.i(TAG, "hot");
                                    }
                                    else
                                    {
                                        temperature.setText(""+temp+"°");
                                        temperature.setTextColor(getResources().getColor(R.color.blue));
                                    }

                                    humidity.setText(jsonObject.getString("Humidity"));
                                    voc.setText(jsonObject.getString("VOC"));
                                    dust10.setText(jsonObject.getString("DUST PM10"));
                                    dust25.setText(jsonObject.getString("DUST PM2.5"));
                                    dust1.setText(jsonObject.getString("DUST PM1.0"));
                                    o3.setText(jsonObject.getString("Ozone"));
                                    co.setText(jsonObject.getString("CO"));
                                    co2.setText(jsonObject.getString("CO2"));
                                    nh4.setText(jsonObject.getString("NH4"));
                                    h2s.setText(jsonObject.getString("H2S"));
                                    ch4.setText(jsonObject.getString("Methane"));

                                }catch (Exception e){
                                    e.printStackTrace();
                                }

                                Log.d(TAG, response.toString());
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                    }
                });
                AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }

    public void registertoken(){
                String tag_json_obj = "json_obj_req";
                SharedPreferences sharedPreferences = getSharedPreferences("alldetails",Context.MODE_PRIVATE);
                String uuid = sharedPreferences.getString("UUID","");
                Log.d("UUID:  ",uuid);
                String fcmtoken = FirebaseInstanceId.getInstance().getToken();
                Log.d("Token here: ",fcmtoken);
                String url = "http://www.xclency.com/RegisterKeyLax.php?UID="+uuid+"&KEY="+fcmtoken;

                JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                        url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                Log.d(TAG, response.toString());
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
        System.out.println("Terminated");
        h.removeCallbacksAndMessages(null);
        super.onBackPressed();

    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_map) {
            startActivity(new Intent(this,MapActivity.class));
            finish();
        }
        else if(id==R.id.nav_devices){
            startActivity(new Intent(this,Devices.class));
            finish();
        }
        else if(id==R.id.nav_logout){
            SharedPreferences sharedPreferences = getSharedPreferences("alldetails",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
            Intent intent = new Intent(getApplicationContext(), Login.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        else if(id==R.id.nav_alerts){
            startActivity(new Intent(this,alerts.class));
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
