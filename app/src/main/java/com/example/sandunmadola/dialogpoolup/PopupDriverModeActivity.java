package com.example.sandunmadola.dialogpoolup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * Created by Sandun_Madola on 10/13/2014.
 */
public class PopupDriverModeActivity extends Activity {


    public void gotoDriverMap(View view){
        Intent intent = new Intent(this, DriverMapActivity.class);
        startActivity(intent);
    }

}
