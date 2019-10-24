package com.example.dajc.tabs;

import android.content.Intent;
import android.os.AsyncTask;

import androidx.appcompat.app.*;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


public class Badge_Activity extends AppCompatActivity {

    public static ArrayList<BadgeObject> badgeList;
    public static BadgeObject[] badgeArray;
    public static int oeuvreCount;
    static public BadgeObject selectedbadge;
    static public BadgeObject getbadge(){return selectedbadge;}
    String[] names ;
    int[] tmpProgress;
    int[] objectives ;
    int[] progress;
    int sculpturecount;
    FichePopUpFragment dialogFragment;
    int installationcount;
    int muralecount;
    int[] imageId = {
            R.drawable.center,
            R.drawable.center,
            R.drawable.center,
            R.drawable.center,
            R.drawable.center,
            R.drawable.center,
            R.drawable.center,
            R.drawable.center,
            R.drawable.center

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            new getBadge().execute().get();
            tmpProgress = new int[badgeList.size()];
            new getOeuvre().execute().get();
            new getprogress().execute().get();
            new updateBadges().execute().get();
            new getBadge().execute().get();
            System.out.println("yo0:"+tmpProgress[0]);
            System.out.println("yo1:"+tmpProgress[1]);
            System.out.println("yo2:"+tmpProgress[2]);
            System.out.println("yo3:"+tmpProgress[3]);
            System.out.println("yo4:"+tmpProgress[4]);
            System.out.println("yo5:"+tmpProgress[5]);

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        int numBadge = badgeList.size();
        names = new String[numBadge];
        objectives = new int[numBadge];
        progress = new int[numBadge];
        imageId = new int[numBadge];
        badgeArray = new BadgeObject[numBadge];
        for(BadgeObject badge:badgeList){
            int pos = badgeList.indexOf(badge);

            System.out.println(pos);
            badgeArray[pos]=badge;
            names[pos]=badge.getTitre();
            objectives[pos]=badge.getObjective();
            progress[pos]=badge.getProgress();
            imageId[pos]=R.drawable.center;
        }


        setContentView(R.layout.activity_badge_);
        final TextView totalTextView = (TextView) findViewById(R.id.oeuvreCount);
        final TextView muralTextView = (TextView) findViewById(R.id.muralnum);
        final TextView sculptureTextView = (TextView) findViewById(R.id.sculpturenum);
        final TextView installationTextView = (TextView) findViewById(R.id.installationnum);
        totalTextView.setText(""+oeuvreCount);
        muralTextView.setText(""+muralecount);
        sculptureTextView.setText(""+sculpturecount);
        installationTextView.setText(""+installationcount);





        CustomGrid adapter = new CustomGrid(getApplicationContext(), names, imageId, progress, objectives);
        ExpandableHeightGridView grid;
        grid=(ExpandableHeightGridView) findViewById(R.id.grid);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                selectedbadge = badgeArray[position];
                Intent intent = new Intent(getApplicationContext(), BadgePopFragment.class);
                startActivity(intent);

            }
        });
        grid.setExpanded(true);

        //Section pour contourner le bug cosee par le ExpandableHeightGridView qui scroll vers le bas
        final ScrollView scrollView = ((ScrollView) findViewById(R.id.scrollviewbadge));
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });
        //ExpandableHeightGridView mAppsGrid = (ExpandableHeightGridView) findViewById(R.id.grid2);
        //mAppsGrid.setExpanded(true);

    }

    private class getBadge extends AsyncTask<Void, Void, ArrayList<BadgeObject>> {
        @Override
        protected ArrayList<BadgeObject> doInBackground(Void... voids) {
            badgeList = (ArrayList<BadgeObject>) FirstActivity.getDb().getBadgeDao().getAllBadges();
            return (ArrayList<BadgeObject>) FirstActivity.getDb().getBadgeDao().getAllBadges();
        }
    }
    private class getOeuvre extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
            oeuvreCount =  FirstActivity.getDb().getOeuvreDao().getCountCollection();
            return FirstActivity.getDb().getOeuvreDao().getCountCollection();
        }
    }
    private class updateBadges extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
            int[] progress= new int[badgeList.size()];
            for (BadgeObject badge: badgeList) {
                badge.setProgress(tmpProgress[badgeList.indexOf(badge)]);
                FirstActivity.getDb().getBadgeDao().updatebadge(badge);
            }

            return 1;
        }
    }
    protected class getprogress extends AsyncTask<Void, Void, Integer> {
        @Override
        protected Integer doInBackground(Void... voids) {
            String tmpCond;
            for (BadgeObject badge: badgeList) {
                switch(badge.getCondition()){
                    case "Id" : tmpProgress[badgeList.indexOf(badge)] =
                            FirstActivity.
                                    getDb().
                                    getOeuvreDao().
                                    getconditionCountId(badge.getConditionValue());break;
                    case "Quartier": tmpProgress[badgeList.indexOf(badge)] =
                            FirstActivity.
                                    getDb().
                                    getOeuvreDao().
                                    getconditionCountQuartier(badge.getConditionValue());break;
                    case "Categorie":tmpProgress[badgeList.indexOf(badge)] =
                            FirstActivity.
                                    getDb().
                                    getOeuvreDao().
                                    getconditionCountCategorie(badge.getConditionValue());break;
                    case "SousCategorie":tmpProgress[badgeList.indexOf(badge)] =
                            FirstActivity.
                                    getDb().
                                    getOeuvreDao().
                                    getconditionCountSousCat(badge.getConditionValue());break;
                }
                installationcount = FirstActivity.
                        getDb().
                        getOeuvreDao().
                        getconditionCountSousCat("Installation");
                sculpturecount = FirstActivity.
                        getDb().
                        getOeuvreDao().
                        getconditionCountSousCat("Sculpture");
                muralecount = FirstActivity.
                        getDb().
                        getOeuvreDao().
                        getconditionCountCategorie("Murale");

            }
        return 1;}

    }
}
