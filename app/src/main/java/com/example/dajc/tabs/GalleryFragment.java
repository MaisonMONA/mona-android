package com.example.dajc.tabs;

import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;

/**
 * Created by DAJC on 2016-04-19.
 */
public class GalleryFragment extends Fragment implements AdapterView.OnItemClickListener {

    GridView lv;
    public static GalleryAdaptor sca;
    public static ArrayList<OeuvreObject> oeuvreList;
    FichePopUpFragment dialogFragment;
    public GalleryFragment() {
        // this.dbh =  FirstActivity.getDBH();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.gallery_frag, container, false);

        lv = (GridView) v.findViewById(R.id.gridview);

        sca = new GalleryAdaptor(getContext(),R.layout.gallery_item);

        lv.setAdapter(sca);

        lv.setOnItemClickListener(this);
        new getGalleryOeuvre().execute();
        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        /*MainActivity.oeuvreDuJour=false;
        MainActivity.listFrag((Integer.parseInt(oeuvreList.get(position).getId())));*/
        MainActivity.numOeuvre= Integer.parseInt(oeuvreList.get(position).getId());
        if(oeuvreList.get(position).getEtat()==2){
            MainActivity.withImg=true;
        }
        else{
            MainActivity.withImg=false;
        }
        MainActivity.caller="Gallery";
        dialogFragment = new FichePopUpFragment ();
        dialogFragment.setTargetFragment(GalleryFragment.this, 1337);
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
        dialogFragment = new FichePopUpFragment ();
        dialogFragment.setTargetFragment(GalleryFragment.this, 1337);
        dialogFragment.show(getActivity().getSupportFragmentManager(),"simple dialog");
    }

    private class getGalleryOeuvre extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            oeuvreList = new ArrayList<OeuvreObject>(FirstActivity.getDb().getOeuvreDao().getGalleryList());
            System.out.println(oeuvreList.size());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            sca.clear();
            sca.addAll(oeuvreList);
        }
    }
}