/*
    Created By: Rohan Ishwarkar
*/

package com.example.razor.start.ACTIVITIES;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.razor.start.VOLLEYADAPTERS.AppController;
import com.example.razor.start.R;
import org.json.JSONArray;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    TextView forgot;
    Button login;
    String TAG="TAGGGGG";
    EditText username,password;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        forgot = (TextView) findViewById(R.id.forgotpass);
        login = (Button) findViewById(R.id.login);

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,ForgotPassword.class));
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog p = new ProgressDialog(Login.this);
                p.setMessage("Checking your credentials...Please Wait");
                p.show();

                final String uname = username.getText().toString();
                final String pass = password.getText().toString();

                String tag_json_obj = "json_obj_req";
                String url = "http://www.xclency.com/LaxLogin.php?user="+uname+"&pass="+pass;

                JsonArrayRequest jsonObjReq = new JsonArrayRequest(Request.Method.POST,
                        url, null,
                        new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                p.cancel();
                                try{
                                    JSONObject jsonObject = (JSONObject) response.get(0);
                                    if(jsonObject.getString("UUID").equals("0")){
                                        Toast.makeText(Login.this,"Invalid credentials",Toast.LENGTH_LONG).show();
                                    }else{
                                        SharedPreferences sharedPreferences = getSharedPreferences("alldetails",Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString("uid",uname);
                                        editor.putString("pass",pass);
                                        editor.putString("UUID",jsonObject.getString("UUID"));
                                        editor.putString("Name",jsonObject.getString("Fullname"));
                                        editor.putString("ph_no",jsonObject.getString("PhoneNumber"));
                                        editor.commit();
                                        startActivity(new Intent(Login.this,Devices.class));
                                        finish();
                                    }
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
        });
    }
}
