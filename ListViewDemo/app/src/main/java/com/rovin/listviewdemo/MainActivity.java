package com.rovin.listviewdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import static java.util.Arrays.asList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView nameListView = (ListView) findViewById(R.id.nameListView);

        final ArrayList<String> friendArray = new ArrayList<String>(asList("Rovin","Rob","David","Frank","Jack"));

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, friendArray);

        nameListView.setAdapter(arrayAdapter);

        nameListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("ItemName", friendArray.get(position));

                Toast.makeText(MainActivity.this,"Hello " + friendArray.get(position),Toast.LENGTH_LONG).show();
            }
        });
    }
}
