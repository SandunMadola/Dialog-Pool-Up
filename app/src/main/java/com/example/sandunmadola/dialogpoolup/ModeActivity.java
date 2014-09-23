package com.example.sandunmadola.dialogpoolup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Sandun_Madola on 9/23/2014.
 */
public class ModeActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mode);
    }

    public void gotoPassengerMap(View view){
        Intent intent =new Intent(this,PassengerMapActivity.class);
        startActivity(intent);
    }
}
