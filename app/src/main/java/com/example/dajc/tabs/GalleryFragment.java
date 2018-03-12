package com.example.dajc.tabs;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by DAJC on 2016-04-19.
 */
public class GalleryFragment extends Fragment implements AdapterView.OnItemClickListener{

    ListView lv;
    public static GalleryAdaptor sca;

    public GalleryFragment(){
       // this.dbh =  FirstActivity.getDBH();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.listview, container, false);

        lv = (ListView)v.findViewById(R.id.listView);

        sca = new GalleryAdaptor(getContext(), android.R.layout.simple_list_item_2);

        lv.setAdapter(sca);

        lv.setOnItemClickListener(this);

        return v;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent= new Intent(getActivity(), FicheActivity.class);
        intent.putExtra("numOeuvre", position);
        startActivity(intent);
    }
}
