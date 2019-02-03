package com.example.rohan.sharedpref;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText username ;
    EditText password;
    TextView text;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        text = (TextView) findViewById(R.id.view);
    }
    public void save(View view){

        SharedPreferences sh = getSharedPreferences("userinfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sh.edit();
        editor.putString("username",username.getText().toString());
        editor.putString("password",password.getText().toString());
        editor.apply();
        Toast.makeText(this,"Saved Successfully.",Toast.LENGTH_LONG).show();
    }
    public void display(View view){
        SharedPreferences sh = getSharedPreferences("userinfo",Context.MODE_PRIVATE);
        String name = sh.getString("username","");
        String password = sh.getString("password","");

        text.setText(name + " "+password);
    }


}
