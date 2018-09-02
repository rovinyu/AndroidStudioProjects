package com.rovin.helloworld;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public void onSubmit(View view) {
        EditText nameEditText = (EditText) findViewById(R.id.nameEditText);
        EditText passwdEditText = (EditText) findViewById(R.id.passwdEditText);

        Log.i("Name", nameEditText.getText().toString());
        Log.i("Password", passwdEditText.getText().toString());

        Toast.makeText(MainActivity.this, "Hi " + nameEditText.getText().toString(),Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
