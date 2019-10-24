package com.example.dajc.tabs;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.DynamicDrawableSpan;
import android.text.style.ImageSpan;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.example.dajc.tabs.MainActivity.pager;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNoOfTabs;

    public PagerAdapter(FragmentManager fm, int NumberOfTabs)
    {
        super(fm);
        this.mNoOfTabs = NumberOfTabs;
    }
    @Override
    public Fragment getItem(int position) {
        switch(position){
            case 0:
                FicheFragment ff = new FicheFragment();
                return ff;
            case 1:
                MapFragment mf = new MapFragment();
                return mf;
            case 2:
                ListViewFragment lf = new ListViewFragment();
                return lf;
            case 3:
                GalleryFragment gf = new GalleryFragment();
                return gf;
            default:
                return null;
        }

    }
    @Override
    public int getCount() {
        return mNoOfTabs;
    }
}