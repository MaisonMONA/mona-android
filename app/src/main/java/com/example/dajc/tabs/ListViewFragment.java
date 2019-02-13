package com.example.dajc.tabs;

import android.annotation.TargetApi;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static com.example.dajc.tabs.ListViewActivity.user_lati;


public class ListViewFragment extends Fragment implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener, CheckBox.OnClickListener {
    ArrayList<OeuvreObject> oeuvreList= new ArrayList<>();
    /*RadioButton rb_dist;
    RadioButton rb_quart;
    RadioButton rb_title;
    RadioButton rb_art;*/
    View v;
    ListView lv;
    PopupWindow popUpWindow;
    FichePopUpFragment dialogFragment;
    //RadioGroup rg;
    Spinner dropdown;
    CheckBox favFilter;

    public ListViewFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.listview_tri_dropdown, container, false);
        popUpWindow = new PopupWindow(getActivity());
        lv = (ListView)v.findViewById(R.id.listView);
        lv.setOnItemClickListener(this);
        dropdown = v.findViewById(R.id.spinner1);
//create a list of items for the spinner.
        String[] items = new String[]{"Distance", "Quartier", "Titre", "Artiste"};
//create an adapter to describe how the items are displayed, adapters are used in several places in android.
//There are multiple variations of this, but this is the basic variant.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, items);
//set the spinners adapter to the previously created one.
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(this);
        favFilter = v.findViewById(R.id.checkbox_fav);
        favFilter.setOnClickListener(this);
        if (favFilter.isChecked()){
            new getFavOeuvre().execute();
        }
        else{
            new getAllOeuvre().execute();
        }
        return v;
    }


    public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {

        switch (position) {
            case 0:
                MainActivity.triType=0;
                sortDist();
                break;
            case 1:
                MainActivity.triType=1;
                sortQuartier();
                break;
            case 2:
                MainActivity.triType=2;
                sortTitre();
                break;
            case 3:
                MainActivity.triType=3;
                sortArtiste();
                break;


        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        sortDist();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.d("onItemClick", "item was clicked "+position);
        MainActivity.numOeuvre= Integer.parseInt(oeuvreList.get(position).getId());
        if(oeuvreList.get(position).getEtat()==2){
            MainActivity.withImg=true;
        }
        else{
            MainActivity.withImg=false;
        }
        MainActivity.caller="List";
        dialogFragment = new FichePopUpFragment ();
        dialogFragment.setTargetFragment(ListViewFragment.this, 1337);
        dialogFragment.show(getActivity().getSupportFragmentManager(),"simple dialog");

    }

    public void refreshDialog(){
        //en prevision du swipe
        /*if(oeuvreList.get(MainActivity.numOeuvre).getEtat()==2){
            MainActivity.withImg=true;
        }
        else{
            MainActivity.withImg=false;
        }*/
        new getAllOeuvre().execute();
        dialogFragment = new FichePopUpFragment ();
        dialogFragment.setTargetFragment(ListViewFragment.this, 1337);
        dialogFragment.show(getActivity().getSupportFragmentManager(),"simple dialog");
    }
    @TargetApi(Build.VERSION_CODES.M)
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
                Location locS1 = new Location("");
                locS1.setLatitude(s1.getLocationX());
                locS1.setLongitude(s1.getLocationY());
                Location locUser = new Location("");
                locUser.setLatitude(MainActivity.lati);
                locUser.setLongitude(MainActivity.longi);
                Location locS2 = new Location("");
                locS2.setLatitude(s2.getLocationX());
                locS2.setLongitude(s2.getLocationY());
                float distanceInMeterss1 = locUser.distanceTo(locS1);
                float distanceInMeterss2 = locUser.distanceTo(locS2);
                /*if(s1.getLocationY()>MainActivity.lati) {
                    x1 = s1.getLocationY()-MainActivity.lati;
                }
                else{
                    x1 = MainActivity.lati-s1.getLocationY();
                }
                if(s2.getLocationY()>MainActivity.lati) {
                    x2 = s2.getLocationY()-MainActivity.lati;
                }
                else{
                    x2 = MainActivity.lati-s2.getLocationY();
                }
                if(s1.getLocationX()>MainActivity.longi) {
                    y1 = s1.getLocationX()-MainActivity.longi;
                }
                else{
                    y1 = MainActivity.longi-s1.getLocationX();
                }
                if(s2.getLocationX()>MainActivity.longi) {
                    y2 = s2.getLocationX()-MainActivity.longi;
                }
                else{
                    y2 = MainActivity.longi-s2.getLocationX();
                }*/
                if (distanceInMeterss1>distanceInMeterss2) {
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

    @Override
    public void onClick(View view) {
        boolean checked = ((CheckBox) view).isChecked();
        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_fav:
                if (checked){
                    new getFavOeuvre().execute();
                }
                // Put some meat on the sandwich
                else{
                    new getAllOeuvre().execute();
                }
                // Remove the meat
                break;
        }
    }

private class getFavOeuvre extends AsyncTask<Void, Void, Void> {


    @Override
    protected Void doInBackground(Void... voids) {
        oeuvreList = new ArrayList<OeuvreObject>(FirstActivity.getDb().getOeuvreDao().getFavOeuvre());
        System.out.println(oeuvreList.size());
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        showList();
    }
}

private class getAllOeuvre extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            oeuvreList = new ArrayList<OeuvreObject>(FirstActivity.getDb().getOeuvreDao().getAllOeuvre());
            System.out.println(oeuvreList.size());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            showList();
        }
    }
}
