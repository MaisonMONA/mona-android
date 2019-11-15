package com.example.dajc.tabs.Task;

import android.os.AsyncTask;

import com.example.dajc.tabs.FirstActivity;
import com.example.dajc.tabs.OeuvreObject;

import java.util.ArrayList;

public class Oeuvre extends AsyncTask<Void, Void, ArrayList<OeuvreObject>> {
    @Override
    protected ArrayList<OeuvreObject> doInBackground(Void... voids) {
        return new ArrayList<OeuvreObject>(FirstActivity.getDb().getOeuvreDao().getAllOeuvre());
    }
}
