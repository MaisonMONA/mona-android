package com.example.dajc.tabs;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;

public class FichePopUpFragment2 extends DialogFragment {
    private OeuvreObject oeuvre_selected;

    public View OnCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v;
        v = inflater.inflate(R.layout.fiche_popup_general, container, true);

        //TODO: en lien avec fiche_popuup_general.


        return v;
    }

    public static FichePopUpFragment2 newInstance(int arg, OeuvreObject oeuvre_selected) {
        FichePopUpFragment2 pop_frag = new FichePopUpFragment2();
        Bundle args = new Bundle();
        args.putInt("count", arg);
        pop_frag.setArguments(args);
        pop_frag.setOeuvre(oeuvre_selected);
        return pop_frag;
    }

    public void setOeuvre(OeuvreObject oeuvre_selected) {
        this.oeuvre_selected = oeuvre_selected;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }
}
