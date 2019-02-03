package com.example.razor.start.ACTIVITIES;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.razor.start.VOLLEYADAPTERS.AppController;
import com.example.razor.start.MODELS.singlealert;
import com.example.razor.start.R;
import com.example.razor.start.ADAPTERS.AlertRecyclerAdapter;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class alerts extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    RecyclerView recyclerView;
    SwipeRefreshLayout swipeRefreshLayout;
    ProgressBar progressBar;
    GridLayoutManager gridLayoutManager;
    AlertRecyclerAdapter adapter;
    int page_number=0,item_count=10;
    ArrayList<String>devid = new ArrayList<>();
    ArrayList<String>devname = new ArrayList<>();
    boolean isloading=true;
    int pastvisible=0,visibleitemcount=0,totalitemcount=0,prev_total=0;
    int view_thresh=10,tot=0,curr=0;
    ImageView prev_device,next_device;
    TextView devicename;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alerts);
        gridLayoutManager = new GridLayoutManager(this,1);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        progressBar = findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.VISIBLE);
        prev_device = findViewById(R.id.previous_device);
        next_device = findViewById(R.id.next_device);
        devicename = (TextView) findViewById(R.id.deviceid);
        swipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipe);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                page_number=0;
                System.out.println("Reachhehhehd here");
                loadalert();
                swipeRefreshLayout.setRefreshing(false);
            }
        });

        getdevices();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                visibleitemcount = gridLayoutManager.getChildCount();
                totalitemcount = gridLayoutManager.getItemCount();
                pastvisible = gridLayoutManager.findFirstVisibleItemPosition();
                if(dy>0){
                    System.out.println("assssss");
                    if(isloading){
                        if(totalitemcount>prev_total){
                            isloading=false;
                            prev_total=totalitemcount;
                        }
                    }
                    if(!isloading&&(totalitemcount-visibleitemcount)<=(pastvisible+view_thresh)){
                        page_number++;
                        performPagination();
                        isloading=true;
                    }
                }
            }
        });

        prev_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(curr==0)
                    Toast.makeText(alerts.this,"This is the first device!",Toast.LENGTH_LONG).show();
                else{
                    page_number=0;
                    curr--;
                    loadalert();
                }
            }
        });

        next_device.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(curr==tot-1)
                    Toast.makeText(alerts.this,"This is the last device!",Toast.LENGTH_LONG).show();
                else{
                    page_number=0;
                    curr++;
                    loadalert();
                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    public void getdevices(){

        SharedPreferences sharedPreferences = getSharedPreferences("alldetails",Context.MODE_PRIVATE);
        String uuid = sharedPreferences.getString("UUID","");
        final ProgressDialog p = new ProgressDialog(alerts.this);
        p.setMessage("Fetching Alerts...Please Wait");
        p.show();

        String tag_json_obj = "json_obj_req";
        String url = "http://www.xclency.com/LaxGetDevices.php?UID="+uuid;
        System.out.println(uuid);

        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        p.cancel();
                        try{
                            tot=response.length();
                            System.out.print("device number: "+tot);
                            for (int i=0;i<tot;i++){
                                JSONObject jsonObject = (JSONObject)response.get(i);
                                devid.add(jsonObject.getString("DeviceID"));
                                devname.add(jsonObject.getString("DeviceName"));
                            }
                            loadalert();

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
    public void loadalert(){
        final ProgressDialog p = new ProgressDialog(alerts.this);
        p.setMessage("Fetching Alerts...Please Wait");
        p.show();
        String tag_json_obj = "json_obj_req";
        String url = "http://www.xclency.com/GetDeviceNotifications.php?DeviceID="+devid.get(curr)+"&Start="+page_number*item_count+"&Count="+item_count;
        System.out.println("URLLLL: "+url);
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<singlealert>list = new ArrayList<>();
                        try{
                            for(int i=0;i<response.length();i++){
                                JSONObject ob = response.getJSONObject(i);
                                String message = ob.getString("Message");
                                String title = ob.getString("Title");
                                String time = ob.getString("Time");
                                list.add(new singlealert(title,message,time));
//                                list.add(new singlealert("Rohan","Rohan","Rohan"));
                            }
                            p.cancel();
                            devicename.setText("Device: "+devname.get(curr));
                            System.out.println(response.toString());
                            adapter = new AlertRecyclerAdapter(list,alerts.this);
                            recyclerView.setAdapter(adapter);
                            progressBar.setVisibility(View.INVISIBLE);

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }
    private void performPagination(){
        progressBar.setVisibility(View.VISIBLE);
        String tag_json_obj = "json_obj_req";
        String url = "http://www.xclency.com/GetDeviceNotifications.php?DeviceID=1&Start="+page_number*item_count+"&Count="+item_count;
        System.out.println("URLLLL: "+url);
        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<singlealert>list = new ArrayList<>();
                        try{
                            if(response.length()==0)
                                Toast.makeText(alerts.this,"No more alerts available!",Toast.LENGTH_LONG).show();
                            else{
                                for(int i=0;i<response.length();i++){
                                    JSONObject ob = response.getJSONObject(i);
                                    String message = ob.getString("Message");
                                    String title = ob.getString("Title");
                                    String time = ob.getString("Time");
                                    list.add(new singlealert(title,message,time));
//                                    list.add(new singlealert("Rohan","Rohan","Rohan"));
                                }
                                System.out.println(response.toString());
                                adapter.add(list);
                            }
                            progressBar.setVisibility(View.GONE);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
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