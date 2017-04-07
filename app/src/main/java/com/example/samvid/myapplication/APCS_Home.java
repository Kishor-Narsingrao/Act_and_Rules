package com.example.samvid.myapplication;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toolbar;
import android.view.MenuItem;

import org.json.JSONObject;

public class APCS_Home extends AppCompatActivity implements TabLayout.OnTabSelectedListener,ResultCallBack {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apcs__home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setActionBar(toolbar);

        Bundle bundle = getIntent().getExtras();
        String strBookName = bundle.getString("BookName");

        getActionBar().setTitle(strBookName);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(true);

//        AsyncTask_WebAPI asyncTask_webAPI=new AsyncTask_WebAPI(this,"http://api.androidhive.info/contacts/",this);
//        asyncTask_webAPI.execute();

        tabLayout = (TabLayout) findViewById(R.id.tabbar);
        viewPager = (ViewPager) findViewById(R.id.pager);

        tabLayout.addTab(tabLayout.newTab().setText("Acts_three"));
        tabLayout.addTab(tabLayout.newTab().setText("Rule"));
       /* tabLayout.getTabAt(0).setIcon(R.drawable.ic_nfc);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_mobile_payment);*/
//        tabLayout.getTabAt(0).getCustomView().setBackgroundColor(Color.parseColor("#198C19"));
//        tabLayout.getChildAt(0).setBackgroundColor(Color.parseColor("#198C19"));
//        tabLayout.getChildAt(0).setBackground(getResources().getDrawable(R.drawable.tab_selector_bg));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        Pager_APCS adapter = new Pager_APCS(getSupportFragmentManager(), strBookName,APCS_Home.this);

        //Adding adapter to pager
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        //Adding onTabSelectedListener to swipe views
        tabLayout.setOnTabSelectedListener(this);
    }

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            Intent intent=new Intent(APCS_Home.this, Home_one.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResultListener(String object) {

//        JSONObject response=(JSONObject) object;
    }
}
