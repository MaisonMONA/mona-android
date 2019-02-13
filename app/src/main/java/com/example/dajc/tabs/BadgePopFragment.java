package com.example.dajc.tabs;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class BadgePopFragment extends Activity{
    static final int REQUEST_IMAGE_PICTURE = 1;
    BadgeObject badge;
    TextView titleview;
    ImageView imageview;
    TextView progressview;
    Button retourbutton;
    String title;
    String progress;


    SharedPreferences changes;
    @SuppressLint("ResourceAsColor")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_badge_pop_fragment);
        imageview = (ImageView) findViewById(R.id.badgeimageview);
        titleview = (TextView) findViewById(R.id.titletextview);
        progressview = (TextView) findViewById(R.id.progresstextview);
        retourbutton = (Button) findViewById(R.id.retourbutton);
        badge = Badge_Activity.selectedbadge;
        final BadgePopFragment that = this;
        retourbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                that.finish();
            }
        });
        if(!badge.getTitre().contains("Neige"))
        {
            imageview.setImageURI(null);
        }
        titleview.setText(badge.getTitre());
        if ((badge.Progress) >= (badge.getObjective())) {
            progressview.setText(badge.getObjective() + "/" + badge.getObjective());
            progressview.setTextColor(Color.parseColor("#fad901"));
            progressview.setTypeface(progressview.getTypeface(), Typeface.BOLD);
            System.out.println("ouiyello");
        }
        else
            progressview.setText(badge.getProgress() + "/" + badge.getObjective());
    }
}