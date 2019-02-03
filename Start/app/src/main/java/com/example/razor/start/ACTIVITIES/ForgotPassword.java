/*
    Created By: Rohan Ishwarkar
*/


package com.example.razor.start.ACTIVITIES;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.razor.start.R;

public class ForgotPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpass);
        TextView text = (TextView) findViewById(R.id.text);
    }
}
