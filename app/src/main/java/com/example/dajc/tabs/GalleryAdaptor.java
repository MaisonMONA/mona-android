package com.example.dajc.tabs;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by DAJC on 2016-04-19.
 */
public class GalleryAdaptor extends ArrayAdapter<OeuvreObject> {

    private ArrayList<Boolean> itemChecked = new ArrayList<Boolean>();
    private Context context;

    public GalleryAdaptor(Context context, int layout) {
        super(context, layout);
        this.context = context;


    }


    public View getView(final int pos, View inView, ViewGroup parent) {
        if (inView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inView = inflater.inflate(R.layout.gallery_item, null);
        }

        OeuvreObject oeuvre = getItem(pos);
        String uri = oeuvre.getURI();


        ImageView iv = (ImageView)inView.findViewById(R.id.image);
        Bitmap bmImg = BitmapFactory.decodeFile(uri);
        iv.setImageBitmap(bmImg);

        return inView;


    }


}
