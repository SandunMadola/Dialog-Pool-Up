package com.example.sandunmadola.dialogpoolup;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Sandun_Madola on 1/20/2015.
 */
public class AvailableDriverListActivity extends ListActivity {
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maindriverlist);

        setListAdapter(new MyAdapter(this,
                android.R.layout.simple_list_item_1, R.id.listdrivername,
                getResources().getStringArray(R.array.Drivers)));
    }

    private class MyAdapter extends ArrayAdapter<String> {
        public MyAdapter(Context context, int resource, int textViewResourceId, String[] objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View row = inflater.inflate(R.layout.activity_driverrowlistview, parent, false);
            String[] items = getResources().getStringArray(R.array.Drivers);

            ImageView iv = (ImageView) row.findViewById(R.id.listdriverimage);
            TextView tv = (TextView) row.findViewById(R.id.listdrivername);
            RatingBar rt = (RatingBar) row.findViewById(R.id.ratingBar);

            tv.setText(items[position]);
            //rt.setNumStars(3);
            //rt.setRating(3);
            if (items[position].equals("Chathura")) {
                iv.setImageResource(R.drawable.chathura);
                rt.setRating((float) 4.5);
            } else if (items[position].equals("Kasun")) {
                iv.setImageResource(R.drawable.kasun);
                rt.setRating((float) 3.5);
            } else if (items[position].equals("Sanath")) {
                iv.setImageResource(R.drawable.sanath_peris2);
                rt.setRating((float) 3);
            } else if (items[position].equals("Suminda")) {
                iv.setImageResource(R.drawable.suminda);
                rt.setRating((float) 2.5);
            } else if (items[position].equals("Kapila")) {
                iv.setImageResource(R.drawable.kapila);
                rt.setRating((float) 2);
            }

            return row;

            //tv.setText(items[position]);

        }
    }
}
     /*






    }*/


