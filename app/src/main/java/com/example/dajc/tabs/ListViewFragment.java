package com.example.dajc.tabs;

import android.content.Context;
import android.content.Intent;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class ListViewFragment extends Fragment implements AdapterView.OnItemClickListener{
    ArrayList<OeuvreObject> oeuvreList= new ArrayList<>();
    static double user_longi;
    static double user_lati;
    RadioButton rb_dist;
    RadioButton rb_quart;
    RadioButton rb_title;
    RadioButton rb_art;
    View v;
    ListView lv;
    RadioGroup rg;


    public ListViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle bundle = this.getArguments();
        oeuvreList = bundle.getParcelableArrayList("List");
        v = inflater.inflate(R.layout.listview_tri, container, false);

        lv = (ListView)v.findViewById(R.id.listView);

        lv.setOnItemClickListener(this);
        rb_dist = (RadioButton) v.findViewById(R.id.rb_distance);
        rb_quart = (RadioButton) v.findViewById(R.id.rb_quartier);
        rb_title = (RadioButton) v.findViewById(R.id.rb_title);
        rb_art = (RadioButton) v.findViewById(R.id.rb_art);
        rg=(RadioGroup) v.findViewById(R.id.rb_sort);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener(){
            public void onCheckedChanged(RadioGroup group, int checkedId) {
               // Check which radio button was clicked
                View radioButton = rg.findViewById(checkedId);
                int index = rg.indexOfChild(radioButton);

                switch(index) {
                    case 1:
                            sortDist();
                        break;
                    case 2:
                            sortQuartier();
                        break;
                    case 3:
                            sortArtiste();
                        break;
                    case 4:
                            sortTitre();
                        break;
                }
            }
        });
        //rb_title.setChecked(true);
        showList();
        return v;
    }




    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("onItemClick", "item was clicked "+position);
        MainActivity.oeuvreDuJour=false;
        MainActivity.listFrag((Integer.parseInt(oeuvreList.get(position).getId())));
    }
    protected void showList()
    {
        AdapterOeuvre adapter = new AdapterOeuvre(getContext(),R.layout.rangee);
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
                //return s1.getArtiste().getNom().compareTo(s2.getArtiste().getNom());
                return s1.getArtiste().compareTo(s2.getArtiste());
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
/*
    public void onItemClick(View view) {
    }
    */

}
