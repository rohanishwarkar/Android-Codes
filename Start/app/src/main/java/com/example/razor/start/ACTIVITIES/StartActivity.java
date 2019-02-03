/*
    Created By: Rohan Ishwarkar
*/

package com.example.razor.start.ACTIVITIES;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.razor.start.VOLLEYADAPTERS.AppController;
import com.example.razor.start.R;
import com.google.firebase.iid.FirebaseInstanceId;
import org.json.JSONArray;
import org.json.JSONObject;

public class StartActivity extends AppCompatActivity {
    ProgressBar p;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("Token:  "+FirebaseInstanceId.getInstance().getToken());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_startactivity);
        p = (ProgressBar)  findViewById(R.id.progressBar);
        p.getProgressDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);

        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            connected = true;
        }
        else {
            connected = false;
        }

        if (!connected){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(StartActivity.this);
            alertDialogBuilder.setMessage("Internet Not Connected").setCancelable(false).setNegativeButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setTitle("Error!!");
            alertDialog.show();
        }

        else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    SharedPreferences sharedPreferences = getSharedPreferences("alldetails", Context.MODE_PRIVATE);
                    final String uid = sharedPreferences.getString("uid", "");
                    final String pass = sharedPreferences.getString("pass", "");
                    String url = "http://www.xclency.com/LaxLogin.php?user=" + uid + "&pass=" + pass;
                    String tag_json_obj = "json_obj_req";
                    if(!uid.equals("")){
                        JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                                url, null,
                                new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        try {
                                            JSONObject jsonObject = (JSONObject) response.get(0);
                                            if (jsonObject.getString("UUID").equals("0")) {
                                                Log.i("splash", "failed");
                                                Intent mainIntent = new Intent(StartActivity.this, Login.class);
                                                StartActivity.this.startActivity(mainIntent);
                                                StartActivity.this.finish();
                                                Toast.makeText(StartActivity.this, "Invalid credentials", Toast.LENGTH_LONG).show();
                                            } else {
                                                Log.i("splash", "uid: " + uid + "pass" + pass);
                                                startActivity(new Intent(StartActivity.this, Devices.class));
                                                StartActivity.this.finish();
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                                    p.incrementProgressBy(50);
                                                    p.setProgress(100, true);
                                                }
                                            }

                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                        Log.d("splash", response.toString());
                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                VolleyLog.d("splash", "Error: " + error.getMessage());
                            }
                        });
                        AppController.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
                    }
                    else{
                        startActivity(new Intent(StartActivity.this,Login.class));
                    }
                }
            }, 3000);
        }
    }
}
