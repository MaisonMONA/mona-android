package com.example.dajc.tabs;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;

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
    TextView bravo;
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
        bravo = (TextView) findViewById(R.id.bravo);
        retourbutton = (Button) findViewById(R.id.retourbutton);
        badge = Badge_Activity.selectedbadge;
        final BadgePopFragment that = this;
        retourbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                that.finish();
            }
        });



        titleview.setText(badge.getTitre());
        imageview.setImageResource(badge.getLogoNoColor());

        if ((badge.Progress) >= (badge.getObjective())) {
            progressview.setText(badge.getObjective() + "/" + badge.getObjective());
            progressview.setTextColor(Color.parseColor("#fad901"));
            progressview.setTypeface(progressview.getTypeface(), Typeface.BOLD);
            imageview.setImageResource(badge.getLogoWithColor());

           switch(badge.getTitre()){
                case "Université de Montréal":
                    bravo.setText("Déjà "+badge.getProgress()+" œuvres collectionnées à l'UdeM. Impresionnant!");
                    break;
                case "Hochelaga":
                    bravo.setText(badge.getProgress()+" œuvres collectionnées dans Mercier-Hochelaga-Maisonneuve, bravo! Apercevez-vous le Stade Olympique d'où vous êtes présentement?");
                    break;
                case "Mont-Royal":
                    bravo.setText("Le Plateau et le Mont-Royal n'ont plus de secret pour vous! Vous venez d'y collectionner votre"+badge.getProgress()+"e œuvre.");
                    break;
                case "Villeray":
                    bravo.setText(badge.getProgress()+" œuvres collectionnées dans Villeray-Saint-Michel-Parc-Extension, bravo! Combien de ruelles avez-vous visitées pour y arriver?");
                    break;
                case "Ahuntsic-Cartierville":
                    bravo.setText("Bravo! Votre collection compte "+badge.getProgress()+" œuvres d'Ahuntsic-Cartierville. Êtes-vous passé devant le Cégep?");
                    break;
                case "Côte-des-Neiges-Notre-Dame-de-Grâce":
                    bravo.setText("Bravo, vous avez collectionné "+badge.getProgress()+" œuvres dans Côte-des-Neiges-Notre-Dame-de-Grâce. En avez-vous profité pour visiter l'Oratoire St-Joseph? ");
                    break;
                case "Lachine":
                    bravo.setText("Vos balades au long du Canal ont porté fruit : vous avez collectionné "+badge.getProgress()+" œuvres à Lachine! ");
                    break;
                case "Sud-Ouest":
                    bravo.setText("Vous avez collectionné "+badge.getProgress()+" œuvres dans le Sud-Ouest, félicitations! Vous pouvez en profiter pour (re-)découvrir le Marché Atwater.");
                    break;
                default:
                    break;
            }


            bravo.setVisibility(View.VISIBLE);
            System.out.println("ouiyello");
        }
        else {
            progressview.setText(badge.getProgress() + "/" + badge.getObjective());
            //bravo.setVisibility(View.GONE);

        }

    }
}