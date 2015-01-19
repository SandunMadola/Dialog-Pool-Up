package com.example.sandunmadola.dialogpoolup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Sandun_Madola on 9/22/2014.
 */
public class HomeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

    }

    public void gotoMode(View view){
        Intent intent = new Intent(this, ModeActivity.class);
        startActivity(intent);
    }
    public void gotoProfile(View view){
        Intent intent = new Intent(this, ProfileActivity.class);
        startActivity(intent);
    }
    public void gotoMessage(View view){
        Intent intent = new Intent(this, MessageActivity.class);
        startActivity(intent);
    }

}
