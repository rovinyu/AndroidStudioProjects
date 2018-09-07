package com.rovin.databasedemo;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            SQLiteDatabase myDatabase = this.openOrCreateDatabase("Users", MODE_PRIVATE, null);
            myDatabase.execSQL("CREATE TABLE IF NOT EXISTS users (name VARCHAR, age INT(3))");

            myDatabase.execSQL("INSERT INTO users (name, age) VALUES ('Rob', 34)");

            myDatabase.execSQL("INSERT INTO users (name, age) VALUES ('David', 12)");

            Cursor c = myDatabase.rawQuery("SELECT * FROM users", null);

            if (c != null) {
                int nameIndex = c.getColumnIndex("name");
                int ageIndex = c.getColumnIndex("age");

                c.moveToFirst();
                do {

                    Log.i("name", c.getString(nameIndex));
                    Log.i("age", Integer.toString(c.getInt(ageIndex)));

                } while (c.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
