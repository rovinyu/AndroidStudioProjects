package com.rovin.whatsapp;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseLiveQueryClient;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SubscriptionHandling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {

    String activeUser = "";

    List<Map<String, String>> messages = new ArrayList<Map<String, String>>();

    //ArrayList<String> messages = new ArrayList<>();

    Handler handler;

    SimpleAdapter arrayAdapter;

    Handler uiHandler = new Handler();

    boolean chatIsActive = true;

    ListView chatListView;

    private void scrollMyListViewToBottom() {

        uiHandler.sendEmptyMessage(0);

    }

    @Override
    protected void onStop() {

        chatIsActive = false;
        Log.i("ChatActivity", "OnPause");

        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("ChatActivity", "OnResume");
        chatIsActive = true;
        checkForUpdate();
    }

    public boolean addMessage(ParseObject object) {

        String messageContent = object.getString("message");
        if (messageContent != null) {

            if (!object.getString("sender").equals(ParseUser.getCurrentUser().getUsername())) {

                messageContent = "> " + messageContent;

            }
            Map<String, String> mapInfo = new HashMap<String, String>();
            mapInfo.put("content", messageContent);
            mapInfo.put("id", object.getObjectId());

            messages.add(mapInfo);
            return true;
        }

        return false;
    }

    public boolean delMessage(ParseObject object) {


        Iterator<Map<String, String>> itr = messages.iterator();

        while (itr.hasNext()) {
            Map<String, String> mapInfo = itr.next();
            if (mapInfo.get("id").equals(object.getObjectId())) {
                itr.remove();
            }
        }
        return true;

    }

    public boolean updateMessage(ParseObject object) {

        String messageContent = object.getString("message");
        if (messageContent != null) {

            if (!object.getString("sender").equals(ParseUser.getCurrentUser().getUsername())) {

                messageContent = "> " + messageContent;

            }
            ListIterator<Map<String, String>> iterator = messages.listIterator();

            while (iterator.hasNext()) {
                Map<String, String> mapItem = iterator.next();
                if (mapItem.get("id").equals(object.getObjectId())) {
                    mapItem.put("content", messageContent);
                }
                iterator.set(mapItem);
            }

            return true;
        }
        return false;
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

                            addMessage(message);

                        }
                        scrollMyListViewToBottom();

                    }

                }
/*
                if(chatIsActive) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            checkForUpdate();
                        }
                    }, 2000);
                }
*/
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

                        //messages.add(messageContent);

                        //scrollMyListViewToBottom();
/*
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                checkForUpdate();
                            }
                        }, 2000);
*/
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
        arrayAdapter = new SimpleAdapter(ChatActivity.this, messages, android.R.layout.simple_list_item_1, new String[] {"content"}, new int[] {android.R.id.text1});

        //arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, messages);

        chatListView.setAdapter(arrayAdapter);

        uiHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                arrayAdapter.notifyDataSetChanged();
                chatListView.post(new Runnable() {
                    @Override
                    public void run() {
                        // Select the last row so it will scroll into view...
                        chatListView.setSelection(chatListView.getCount() - 1);
                    }
                });
            }
        };


        ParseLiveQueryClient parseLiveQueryClient = ParseLiveQueryClient.Factory.getClient();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Message");

        SubscriptionHandling<ParseObject> subscriptionHandling = parseLiveQueryClient.subscribe(query);

        subscriptionHandling.handleEvents(new SubscriptionHandling.HandleEventsCallback<ParseObject>() {
            @Override
            public void onEvents (ParseQuery<ParseObject> query, SubscriptionHandling.Event event, ParseObject object) {
                Log.i("ChatActivity", "Received subscription event : " + event + "\n current object state : " + object.toString());

                if(chatIsActive) {
                    switch (event) {
                        case CREATE:
                            if (addMessage(object)) {
                                scrollMyListViewToBottom();
                            }
                            break;
                        case DELETE:
                            if (delMessage(object)) {
                                scrollMyListViewToBottom();
                            }
                            break;
                        case UPDATE:
                            if (updateMessage(object)) {
                                scrollMyListViewToBottom();
                            }
                        default:
                            break;
                    }
                }
            }
        });

        //checkForUpdate();

    }
}
