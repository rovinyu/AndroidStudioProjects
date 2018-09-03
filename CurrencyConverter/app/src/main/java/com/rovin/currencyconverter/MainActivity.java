package com.rovin.currencyconverter;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public static final double RATE_FROM_DOLLAR_TO_POUND = 0.77;
    public static final String POUND = "\u00A3";

    public void OnConvert(View view) {
        EditText currEditText = (EditText)findViewById(R.id.currEditText);

        String currency = currEditText.getText().toString();

        Double valueInPound = Double.parseDouble(currency) * RATE_FROM_DOLLAR_TO_POUND;

        Log.i("Info", "Input currency is: " + currency);

        Toast.makeText(MainActivity.this, POUND + String.format("%.2f", valueInPound), Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
