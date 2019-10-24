package com.example.dajc.tabs;

import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Created by DAJC on 2016-04-17.
 */
public class SimpleFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.simple_fragment, container, false);
        TextView text = (TextView)v.findViewById(R.id.testfragment);
        Bundle args = getArguments();
        int fragmentid = args.getInt("id");
        text.setText("Fragment "+fragmentid);
        return v;
    }
}
