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

        oeuvreDuJour=true;
        caller="";
        lati=45.508567;;
        longi=-73.566455;
        triType=0;
        Permission = new Permissions(this);
        if (Permission.checkPermAll()==false)
        {
            Permission.requestAll();
        }
        new getOeuvre().execute();
       // new getBadge().execute();

        //Setup of the tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tablayout);
        tabLayout.addTab(tabLayout.newTab().setText("Œuvre du jour"));
        tabLayout.addTab(tabLayout.newTab().setText("Carte"));
        tabLayout.addTab(tabLayout.newTab().setText("Œuvres"));
        tabLayout.addTab(tabLayout.newTab().setText("Collection"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);


        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) { viewPager.setCurrentItem(tab.getPosition()); }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) { }
            @Override
            public void onTabReselected(TabLayout.Tab tab) { }});


        //Setup icons on each tab
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            tabLayout.getTabAt(i).setIcon(ICONS[i]);
        }

        //Potentiellement rajouter ici des icones lorsque on a selectionner l'onglet. Voir: https://github.com/roughike/BottomBar#changing-icons-based-on-selection-state
    }

    //Array containing tab icons
    final int[] ICONS = new int[]{
            R.mipmap.ic_odj_tab,
            R.mipmap.ic_map_tab,
            R.mipmap.ic_list_tab,
            R.mipmap.ic_collection_tab

    };


    //Never used. Check later if any relation with map
    public static void MapFrag(int position)
    {
        pager.setCurrentItem(1);
    }
    public static void listFrag(int position)
    {
        numOeuvre =position;
        System.out.println("numOeuvre" +position);
        if (pager.getAdapter() != null)
            pager.setAdapter(null);
        pager.setAdapter(pagerAdapter);

        pager.setCurrentItem(0);
    }


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
/*
    public class PagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener{
        List<Drawable> icons = new ArrayList<Drawable>();
        List<Drawable> iconsHilighted = new ArrayList<>();


        public PagerAdapter(FragmentManager fm) {
            super(fm);
            pager.addOnPageChangeListener(this);

            Drawable icon1 = getApplicationContext().getResources().getDrawable(R.mipmap.ic_odj_final);
            Drawable icon1Hilighted = getApplicationContext().getResources().getDrawable(R.mipmap.ic_home_active);
            Drawable icon2 = getApplicationContext().getResources().getDrawable(R.mipmap.ic_map_final);
            Drawable icon2Hilighted = getApplicationContext().getResources().getDrawable(R.mipmap.ic_map_active);
            Drawable icon3 = getApplicationContext().getResources().getDrawable(R.mipmap.ic_listview);
            Drawable icon3Hilighted = getApplicationContext().getResources().getDrawable(R.mipmap.ic_favorite_active);
            Drawable icon4 = getApplicationContext().getResources().getDrawable(R.mipmap.ic_collection_final);
            Drawable icon4Hilighted= getApplicationContext().getResources().getDrawable(R.mipmap.ic_gallery_active);

            icons.add(icon1);
            icons.add(icon2);
            icons.add(icon3);
            icons.add(icon4);

            iconsHilighted.add(icon1Hilighted);
            iconsHilighted.add(icon2Hilighted);
            iconsHilighted.add(icon3Hilighted);
            iconsHilighted.add(icon4Hilighted);
        }


        @Override
        public Fragment getItem(int position) {
            Fragment fragment;
            Bundle args = new Bundle();
            if (position == 0) {
                System.out.println("Called for it");
                fragment = new FicheFragment();
            } else if (position == 1){
                fragment = new MapFragment();
            } else if (position == 2){
                fragment = new ListViewFragment();
            }
            else if(position == 3){
                fragment = new GalleryFragment();
            }

            else if(position == 4){
                fragment = new MapFragment();
            }

            else {
                fragment = new FicheFragment();
            }

            args.putInt("id", position);
            args.putParcelableArrayList("List",oeuvreList);
            args.putInt("numOeuvre", numOeuvre);
            fragment.setArguments(args);

            return fragment;
        }

        @Override
        public int getCount() {
            return nb;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            String titre;
            Drawable drawable;
            if (position == 0) {
                titre = "ODJ";
                drawable =getApplicationContext().getResources().getDrawable(R.mipmap.ic_lhome_passive);
            } else if (position == 1) {
                titre = "Carte";
                drawable =getApplicationContext().getResources().getDrawable(R.mipmap.ic_lhome_passive);
            } else if (position == 2) {
                titre = "Liste";
                drawable =getApplicationContext().getResources().getDrawable(R.mipmap.ic_lhome_passive);
            } else if(position == 3){
                titre = "Galerie";
                drawable =getApplicationContext().getResources().getDrawable(R.mipmap.ic_lhome_passive);
            } else {
                titre = "Badge";
                drawable =getApplicationContext().getResources().getDrawable(R.mipmap.ic_lhome_passive);
            }
            SpannableStringBuilder sb = new SpannableStringBuilder("   " + titre); // space added before text for convenience
            try {
                drawable.setBounds(5, 5, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
                ImageSpan span = new ImageSpan(drawable, DynamicDrawableSpan.ALIGN_BASELINE);
                sb.setSpan(span, 0, 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            } catch (Exception e) {
                // TODO: handle exception
            }
            return titre;
        }

        /*public CharSequence getPageTitle(int position) {
            return null;
        }

        public void setTabIcon() {
            for(int i = 0; i < icons.size(); i++) {
                if(i == 0) {
                    //noinspection ConstantConditions
                    //tabLayout.getTabAt(i).setIcon(iconsHilighted.get(i));
                }
                else {
                    //noinspection ConstantConditions
                    tabLayout.getTabAt(i).setIcon(icons.get(i));
                }
            }
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            for (int i = 0; i < getCount(); i++) {
                if(i == position) {
                    //noinspection ConstantConditions
                   // tabLayout.getTabAt(i).setIcon(iconsHilighted.get(i));
                }
                else {
                    //noinspection ConstantConditions
                  //  tabLayout.getTabAt(i).setIcon(icons.get(i));
                }
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            notifyDataSetChanged();
        }
    }
*/    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }
    /* MIT EN COMMENTAIRE LE 12 OCTOBRE 2019

        UTILE ??

    public class CustomViewPager extends android.support.v4.view.ViewPager{
        private boolean enabled;

        public CustomViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
            this.enabled = true;
        }

        @Override
        public boolean onTouchEvent(MotionEvent event) {
            return enabled ? super.onTouchEvent(event) : false;
        }

        @Override
        public boolean onInterceptTouchEvent(MotionEvent event) {
            return enabled ? super.onInterceptTouchEvent(event) : false;
        }

        public void setPagingEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public boolean isPagingEnabled() {
            return enabled;
        }

    }
    */
    private class getOeuvre extends AsyncTask<Void, Void, ArrayList<OeuvreObject>> {


        @Override
        protected ArrayList<OeuvreObject> doInBackground(Void... voids) {
            oeuvreList =new ArrayList<OeuvreObject>(FirstActivity.getDb().getOeuvreDao().getAllOeuvre());
            return new ArrayList<OeuvreObject>(FirstActivity.getDb().getOeuvreDao().getAllOeuvre());
        }
    }
    /*private class getBadge extends AsyncTask<Void, Void, ArrayList<BadgeObject>> {
        @Override
        protected ArrayList<BadgeObject> doInBackground(Void... voids) {
            badgeList =new ArrayList<BadgeObject>(FirstActivity.getDb().getBadgeDao().getAllBadges());
            return new ArrayList<BadgeObject>(FirstActivity.getDb().getBadgeDao().getAllBadges());
        }
    } */


}
