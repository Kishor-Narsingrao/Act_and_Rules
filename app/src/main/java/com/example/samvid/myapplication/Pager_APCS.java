package com.example.samvid.myapplication;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class Pager_APCS extends FragmentStatePagerAdapter {

    int tabCount=0;
    Context context;
    String tabTitles[] = new String[] { "Acts_three","Rule" };
    String strBookName;

    public Pager_APCS(FragmentManager fm, String BookName, Context context)
    {
        super(fm);
        //Initializing tab count
        this.tabCount = tabTitles.length;
        this.context =context;
        this.strBookName=BookName;
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment = null;

        switch (position) {
            case 0:
//                fragment = new Acts_three(strBookName);
                return fragment;
            case 1:
                fragment = new Rule();
                return fragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}