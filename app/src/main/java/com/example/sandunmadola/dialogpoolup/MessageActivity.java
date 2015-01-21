package com.example.sandunmadola.dialogpoolup;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Sandun_Madola on 1/18/2015.
 */
public class MessageActivity extends Activity {
    String one = "Hai,welcome to pool up.";
    String two = "You can enjoy your journey anywhere in the country";
    String three = "Please on your GPS and Mobile data through the entire journey.";
    String four = "You can inform us any issue reagarding to the system.";
    String five = "You can log into the web site and get to no about this.";


        private ListView mainListView;
        private ArrayAdapter<String> listAdapter;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_messagelistview);
            //ff.setText(f);
            mainListView = (ListView) findViewById(R.id.mainListView);

            // Create and populate a List of planet names.
           String[] msg = new String[]{one,two,three,four,five};
            ArrayList<String> msglist = new ArrayList<String>();
            msglist.addAll(Arrays.asList(msg));

            // Create ArrayAdapter using the planet list.
            listAdapter = new ArrayAdapter<String>(this, R.layout.activity_messagerowlistview, msglist);

            // Set the ArrayAdapter as the ListView's adapter.
            mainListView.setAdapter(listAdapter);
        }
    }
