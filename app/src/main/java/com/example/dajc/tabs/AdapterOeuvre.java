package com.example.dajc.tabs;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class AdapterOeuvre extends ArrayAdapter<OeuvreObject> {

    private int layoutResourceId;

    private static final String LOG_TAG = "OeuvreAdapter";

    public AdapterOeuvre(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        layoutResourceId = textViewResourceId;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        try {
            OeuvreObject item = getItem(position);
            View v = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                v = inflater.inflate(layoutResourceId, null);

            } else {
                v = convertView;
            }

            TextView header = (TextView) v.findViewById(R.id.rangee_title);
            TextView description = (TextView) v.findViewById(R.id.rangee_sub);
            System.out.println(item.getTitre());
            header.setText(item.getTitre());
            //description.setText(item.getArtiste().getNom());
            description.setText(item.getArtiste());

            return v;
        } catch (Exception ex) {
            Log.e(LOG_TAG, "error", ex);
            return null;
        }
    }
}