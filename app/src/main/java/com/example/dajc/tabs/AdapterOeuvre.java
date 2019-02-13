package com.example.dajc.tabs;

import android.content.Context;
import android.location.Location;
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
            //System.out.println(item.getTitre());
            header.setText(item.getTitre());
            //description.setText(item.getArtiste().getNom());
            if (MainActivity.triType==0) {
                Location locObjet = new Location("");
                locObjet.setLatitude(item.getLocationX());
                locObjet.setLongitude(item.getLocationY());
                Location locUser = new Location("");
                locUser.setLatitude(MainActivity.lati);
                locUser.setLongitude(MainActivity.longi);
                float distanceInMeters = locUser.distanceTo(locObjet);
                System.out.println(locObjet.getLongitude());
                System.out.println(locObjet.getLatitude());
                System.out.println(locUser.getLongitude());
                System.out.println(locUser.getLatitude());
                System.out.println((int) distanceInMeters);
                int dMeter=(int) distanceInMeters;
                double dKm=(double)dMeter/1000;
                dKm = Math.round(dKm * 10);
                dKm = dKm/10;
                if((int) distanceInMeters>999){
                    description.setText(String.valueOf(dKm)+"km");
                }
                else{
                    description.setText(String.valueOf(dMeter)+"m");
                }
            }
            else if (MainActivity.triType==1){
                description.setText(item.getQuartier());}
            else if (MainActivity.triType==2){
                description.setText(item.getArtiste());}
            else if (MainActivity.triType==3){
                description.setText(item.getArtiste());}



            return v;
        } catch (Exception ex) {
            Log.e(LOG_TAG, "error", ex);
            return null;
        }
    }
}