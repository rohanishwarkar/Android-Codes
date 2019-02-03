package com.example.rohan.databases;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    EditText e ;
    TextView t;
    databasehandler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        e = (EditText) findViewById(R.id.text);
        t = (TextView)  findViewById(R.id.view);
        handler = new databasehandler(this,null,null,1);
        printDatabase();
    }
    public void printDatabase(){
        String f = handler.display();
        t.setText(f);
        e.setText("");
    }
    public void add(View view){
        product p = new product(e.getText().toString());
        handler.add(p);
        printDatabase();
    }
    public void delete(View view){
        String o = e.getText().toString();
        handler.delete(o);
        printDatabase();
    }
}
