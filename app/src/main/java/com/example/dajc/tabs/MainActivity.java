package com.example.dajc.tabs;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

public class MainActivity extends AppCompatActivity {
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
    static Permissions Permission;
    static OnSwipeTouchListener onSwipeTouchListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        tabLayout = (TabLayout) findViewById(R.id.tablayout);
        //pager = (ViewPager) findViewById(R.id.pager);
        //custom pager without swiping
        pager = findViewById(R.id.pager);
        //pager.setPagingEnabled(false);


        pagerAdapter = new PagerAdapter(getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(pager);
        pagerAdapter.setTabIcon();
    }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
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

        return true;
    }

    public class PagerAdapter extends FragmentPagerAdapter implements ViewPager.OnPageChangeListener{
        List<Drawable> icons = new ArrayList<Drawable>();
        List<Drawable> iconsHilighted = new ArrayList<>();


        public PagerAdapter(FragmentManager fm) {
            super(fm);
            pager.addOnPageChangeListener(this);
/*
            Drawable icon1 =getApplicationContext().getResources().getDrawable(R.mipmap.ic_lhome_passive);
            Drawable icon1Hilighted = getApplicationContext().getResources().getDrawable(R.mipmap.ic_home_active);
            Drawable icon2 = getApplicationContext().getResources().getDrawable(R.mipmap.ic_map_passive);
            Drawable icon2Hilighted = getApplicationContext().getResources().getDrawable(R.mipmap.ic_map_active);
            Drawable icon3 = getApplicationContext().getResources().getDrawable(R.mipmap.ic_favorite_passive);
            Drawable icon3Hilighted = getApplicationContext().getResources().getDrawable(R.mipmap.ic_favorite_active);
            Drawable icon4 = getApplicationContext().getResources().getDrawable(R.mipmap.ic_gallery_passive);
            Drawable icon4Hilighted= getApplicationContext().getResources().getDrawable(R.mipmap.ic_gallery_active);

            icons.add(icon1);
            icons.add(icon2);
            icons.add(icon3);
            icons.add(icon4);

            iconsHilighted.add(icon1Hilighted);
            iconsHilighted.add(icon2Hilighted);
            iconsHilighted.add(icon3Hilighted);
            iconsHilighted.add(icon4Hilighted);*/
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
            if (position == 0) {
                titre = "Oeuvre du jour";

            } else if (position == 1) {
                titre = "Carte";
            } else if (position == 2) {
                titre = "Liste";
            } else {
                titre = "Galerie";
            }

            return titre;
        }

        /*public CharSequence getPageTitle(int position) {
            return null;
        }
*/
        public void setTabIcon() {
            /*for(int i = 0; i < icons.size(); i++) {
                if(i == 0) {
                    //noinspection ConstantConditions
                    tabLayout.getTabAt(i).setIcon(iconsHilighted.get(i));
                }
                else {
                    //noinspection ConstantConditions
                    tabLayout.getTabAt(i).setIcon(icons.get(i));
                }
            }*/
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
    public void reload() {
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();
        overridePendingTransition(0, 0);
        startActivity(intent);
    }
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
    private class getOeuvre extends AsyncTask<Void, Void, ArrayList<OeuvreObject>> {


        @Override
        protected ArrayList<OeuvreObject> doInBackground(Void... voids) {
            oeuvreList =new ArrayList<OeuvreObject>(FirstActivity.getDb().getOeuvreDao().getAllOeuvre());
            return new ArrayList<OeuvreObject>(FirstActivity.getDb().getOeuvreDao().getAllOeuvre());
        }
    }


}
