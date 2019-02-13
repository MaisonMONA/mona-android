package com.example.dajc.tabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LenaMK on 01/07/2016.
 */
public class PhotoActivity extends Activity {
    String uri_photo;
    int numOeuvre;
    ImageView photo;
    SharedPreferences changes;
    OeuvreObject oeuvre;
    List<OeuvreObject> list;

    public PhotoActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle= getIntent().getExtras();
        numOeuvre=bundle.getInt("numOeuvre",0);
        System.out.println("PhotoActivity Called");
        setContentView(R.layout.photo);
        photo = (ImageView) findViewById(R.id.photo_view);

        new getOeuvre().execute();
        //récupère les données dans c;
        changes = getApplicationContext().getSharedPreferences("change_rating", Context.MODE_PRIVATE);
    }


    public void treatPhoto() {
        if (uri_photo.equals("")) {
            Picasso.with(getApplicationContext()).load(R.mipmap.ic_favorite_passive).resize(500, 888).into(photo);
            photo.setVisibility(View.VISIBLE);
        } else {
            Bitmap bmImg = BitmapFactory.decodeFile(uri_photo);
            photo.setImageBitmap(bmImg);
        }
    }


    private class getOeuvre extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            list = FirstActivity.getDb().getOeuvreDao().verifyID(String.valueOf(numOeuvre));
            oeuvre = list.get(0);
            uri_photo = oeuvre.getURI();
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            treatPhoto();
        }
    }
}