package com.example.dajc.tabs;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by DAJC on 2016-04-21.
 */
public class ListViewActivity extends Activity implements AdapterView.OnItemClickListener {
    ArrayList<OeuvreObject> oeuvreList= new ArrayList<>();
    static double user_longi;
    static double user_lati;
    RadioButton rb_dist;
    RadioButton rb_quart;
    RadioButton rb_title;
    RadioButton rb_art;
    ListView lv;

    public ListViewActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //get data for user location
        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        oeuvreList = bundle.getParcelableArrayList("List");
        System.out.println(oeuvreList.get(2).getTitre()+"ok");
        try {
            user_longi = Double.parseDouble(intent.getStringExtra("Geo_longi"));
            user_lati = Double.parseDouble(intent.getStringExtra("Geo_lati"));
        }
        catch(NumberFormatException e)
        {
            Toast.makeText(getApplicationContext(), "Activer le GPS pour le tri par distance", Toast.LENGTH_LONG).show();
        }
        //for debug
        Log.d("ListView gps", "Longitude utilisateur = "+user_longi);
        Log.d("ListView gps", "Latitude utilisateur = " + user_lati);

        //set View
        setContentView(R.layout.listview_tri);

        lv = (ListView)findViewById(R.id.listView);

        lv.setOnItemClickListener(this);

        rb_dist = (RadioButton) findViewById(R.id.rb_distance);
        rb_quart = (RadioButton) findViewById(R.id.rb_quartier);
        rb_title = (RadioButton) findViewById(R.id.rb_title);
        rb_art = (RadioButton) findViewById(R.id.rb_art);

        //rb_title.setChecked(true);
        showList();
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.rb_distance:
                if (checked)
                    sortDist();
                break;
            case R.id.rb_quartier:
                if (checked)
                    sortQuartier();
                break;
            case R.id.rb_art:
                if (checked)
                    sortArtiste();
                break;
            case R.id.rb_title:
                if (checked)
                    sortTitre();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("onItemClick", "item was clicked "+position);

        Intent intent= new Intent(this, FicheActivity.class);
        intent.putExtra("numOeuvre", position);
        intent.putExtra("List",oeuvreList);

        startActivity(intent);
    }
    protected void showList()
    {
        AdapterOeuvre adapter = new AdapterOeuvre(getApplicationContext(),R.layout.rangee);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(this);
        adapter.addAll(oeuvreList);
    }

    public static Comparator<OeuvreObject> getCompByTitre()
    {
        Comparator comp = new Comparator<OeuvreObject>(){
            @Override
            public int compare(OeuvreObject s1, OeuvreObject s2)
            {
                return s1.getTitre().compareTo(s2.getTitre());
            }
        };
        return comp;
    }
    public static Comparator<OeuvreObject> getCompByArtiste()
    {
        Comparator comp = new Comparator<OeuvreObject>(){
            @Override
            public int compare(OeuvreObject s1, OeuvreObject s2)
            {
                return s1.getArtiste().getNom().compareTo(s2.getArtiste().getNom());
            }
        };
        return comp;
    }
    public static Comparator<OeuvreObject> getCompByQuartier()
    {
        Comparator comp = new Comparator<OeuvreObject>(){
            @Override
            public int compare(OeuvreObject s1, OeuvreObject s2)
            {
                return s1.getQuartier().compareTo(s2.getQuartier());
            }
        };
        return comp;
    }
    public static Comparator<OeuvreObject> getCompByDist()
    {
        Comparator comp = new Comparator<OeuvreObject>(){
            @Override
            public int compare(OeuvreObject s1, OeuvreObject s2)
            {
                double x1;
                double x2;
                double y1;
                double y2;
                if(s1.getLocationX()>user_lati) {
                    x1 = s1.getLocationX()-user_lati;
                }
                else{
                    x1 = user_lati-s1.getLocationX();
                }
                if(s2.getLocationX()>user_lati) {
                    x2 = s2.getLocationX()-user_lati;
                }
                else{
                    x2 = user_lati-s2.getLocationX();
                }
                if(s1.getLocationY()>user_lati) {
                    y1 = s1.getLocationY()-user_lati;
                }
                else{
                    y1 = user_lati-s1.getLocationY();
                }
                if(s2.getLocationY()>user_lati) {
                    y2 = s2.getLocationY()-user_lati;
                }
                else{
                    y2 = user_lati-s2.getLocationY();
                }
                if (x1+y1>x2+y2) {
                    return 1;
                }
                else{
                    return -1;
                }
            }
        };
        return comp;
    }
    protected void sortTitre()
    {
        Collections.sort(oeuvreList, getCompByTitre());
        showList();
    }
    protected void sortArtiste()
    {
        Collections.sort(oeuvreList, getCompByArtiste());
        showList();
    }
    protected void sortDist()
    {
        Collections.sort(oeuvreList, getCompByDist());
        showList();
    }
    protected void sortQuartier()
    {
        Collections.sort(oeuvreList, getCompByQuartier());
        showList();
    }
}
