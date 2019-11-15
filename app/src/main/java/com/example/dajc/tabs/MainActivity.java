package com.example.dajc.tabs;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.*;

import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;
import android.widget.Toolbar;

import com.example.dajc.tabs.Task.Oeuvre;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.constants.OpenStreetMapTileProviderConstants;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.appcompat.widget.*;
import androidx.viewpager.widget.ViewPager;


public class MainActivity extends AppCompatActivity{
    static int numOeuvre;
    static int triType;
    //to know which layout to use for Fiche
    static boolean withImg;
    TabLayout tabLayout;
    static double lati;
    static double longi;
    static String caller;
    static NonSwipeableViewPager pager;
    static PagerAdapter pagerAdapter;
    //CustomViewPager pager;
    public static boolean oeuvreDuJour;
    public final int nb = 4;
    private String TAG = MainActivity.class.getSimpleName();
    private ListView lv;
    ArrayList<OeuvreObject> oeuvreList; //liste d'oeuvre qui sera passée à chaque activité
    ArrayList<BadgeObject> badgeList; //liste d'oeuvre qui sera passée à chaque activité
    static Permissions Permission;
    static OnSwipeTouchListener onSwipeTouchListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //https://github.com/osmdroid/osmdroid/issues/1313
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));


        //Setup of the toolbar
        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        oeuvreDuJour = true;
        caller = "";
        lati = 45.508567;
        longi = -73.566455;
        triType = 0;
        Permission = new Permissions(this);
        if (Permission.checkPermAll() == false) {
            Permission.requestAll();
        }
        try {
            oeuvreList = new Oeuvre().get();
            // new getBadge().execute();

            //Setup of the tabs
            TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
            tabLayout.addTab(tabLayout.newTab().setText(R.string.oeuvre_du_jour));
            tabLayout.addTab(tabLayout.newTab().setText(R.string.carte));
            tabLayout.addTab(tabLayout.newTab().setText(R.string.oeuvres));
            tabLayout.addTab(tabLayout.newTab().setText(R.string.collection));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


            final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
            final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(adapter);
            viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

            tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    viewPager.setCurrentItem(tab.getPosition());
                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {
                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {
                }
            });


            //Setup icons on each tab
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                tabLayout.getTabAt(i).setIcon(ICONS[i]);
            }

            //Potentiellement rajouter ici des icones lorsque on a selectionner l'onglet. Voir: https://github.com/roughike/BottomBar#changing-icons-based-on-selection-state

        }catch(InterruptedException e){
            // TODO: Manage InterruptedException
        }catch(ExecutionException e){
            // TODO: Manage ExecutionException
        }
    }
    //Array containing tab icons
    final int[] ICONS = new int[]{
            R.mipmap.ic_odj_tab,
            R.mipmap.ic_map_tab,
            R.mipmap.ic_list_tab,
            R.mipmap.ic_collection_tab

    };


    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_main, popup.getMenu());
        popup.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if (item.getItemId() == R.id.settings) {
            Intent intent = new Intent(this, PreferencesActivity.class);
            intent.putExtra("List", oeuvreList);
            startActivity(intent);
        }
        else if(item.getItemId() == R.id.badges) {
            Intent intent = new Intent(this, Badge_Activity.class);
            intent.putExtra("List", oeuvreList);
            startActivity(intent);
        }
        return true;
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }

}
