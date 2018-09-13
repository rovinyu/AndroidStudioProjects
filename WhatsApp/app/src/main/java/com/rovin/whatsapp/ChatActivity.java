package com.rovin.whatsapp;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    String activeUser = "";

    ArrayList<String> messages = new ArrayList<>();

    ArrayAdapter<String> arrayAdapter;

    Handler handler = new Handler();

    boolean chatIsActive = true;

    ListView chatListView;

    private void scrollMyListViewToBottom() {
        chatListView.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                chatListView.setSelection(chatListView.getCount() - 1);
            }
        });
    }

    @Override
    protected void onPause() {

        chatIsActive = false;
        Log.i("ChatActivity", "OnPause");

        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("ChatActivity", "OnResume");
        chatIsActive = true;
        checkForUpdate();
    }

    public void checkForUpdate() {
        ParseQuery<ParseObject> query1 = new ParseQuery<ParseObject>("Message");

        query1.whereEqualTo("sender", ParseUser.getCurrentUser().getUsername());
        query1.whereEqualTo("recipient", activeUser);

        ParseQuery<ParseObject> query2 = new ParseQuery<ParseObject>("Message");

        query2.whereEqualTo("recipient", ParseUser.getCurrentUser().getUsername());
        query2.whereEqualTo("sender", activeUser);

        List<ParseQuery<ParseObject>> queries = new ArrayList<ParseQuery<ParseObject>>();

        queries.add(query1);
        queries.add(query2);

        ParseQuery<ParseObject> query = ParseQuery.or(queries);

        query.orderByAscending("createdAt");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {

                if (e == null) {

                    if (objects.size() > 0) {

                        messages.clear();

                        for (ParseObject message : objects) {

                            String messageContent = message.getString("message");
                            if (messageContent != null) {

                                if (!message.getString("sender").equals(ParseUser.getCurrentUser().getUsername())) {

                                    messageContent = "> " + messageContent;

                                }

                                //Log.i("Info", messageContent);

                                messages.add(messageContent);
                            }

                        }

                        arrayAdapter.notifyDataSetChanged();
                        scrollMyListViewToBottom();

                    }

                }

                if(chatIsActive) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            checkForUpdate();
                        }
                    }, 2000);
                }

            }
        });
    }

    public void sendChat(View view) {

        final EditText chatEditText = (EditText) findViewById(R.id.chatEditText);

        ParseObject message = new ParseObject("Message");

        final String messageContent = chatEditText.getText().toString();

        if(!messageContent.equals("")) {

            message.put("sender", ParseUser.getCurrentUser().getUsername());
            message.put("recipient", activeUser);
            message.put("message", messageContent);

            chatEditText.setText("");

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            message.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {

                    if (e == null) {

                        messages.add(messageContent);

                        arrayAdapter.notifyDataSetChanged();

                        scrollMyListViewToBottom();

                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                checkForUpdate();
                            }
                        }, 2000);

                    } else {

                        String message = e.getMessage();

                        if (message.toLowerCase().contains("java")) {

                            message = e.getMessage().substring(e.getMessage().indexOf(" "));

                        }

                        Toast.makeText(ChatActivity.this, message, Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();

        activeUser = intent.getStringExtra("username");

        setTitle("Chat with " + activeUser);

        chatListView = (ListView) findViewById(R.id.chatListView);

        arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messages);

        chatListView.setAdapter(arrayAdapter);

        //checkForUpdate();

    }
}
