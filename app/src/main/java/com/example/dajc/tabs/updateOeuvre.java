package com.example.dajc.tabs;

import android.os.AsyncTask;

import java.util.ArrayList;

/**
 * Created by emile on 2018-03-11.
 */
public class updateOeuvre extends AsyncTask<OeuvreObject, Void, Void> {


    @Override
    protected Void doInBackground(OeuvreObject... oeuvre) {
        System.out.println("updated");
        FirstActivity.getDb().getOeuvreDao().updateUsers(oeuvre);
        return null;
    }
}