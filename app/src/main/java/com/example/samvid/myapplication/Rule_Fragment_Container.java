package com.example.samvid.myapplication;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.example.samvid.myapplication.ActSectionFragment;
import com.example.samvid.myapplication.R;

public class Rule_Fragment_Container extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment__container);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setBackgroundColor(getResources().getColor(R.color.rules));
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Rule");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Bundle bundle = getIntent().getExtras();
        String strActName = bundle.getString("ActName");

        Fragment fragment = null;

        fragment = new ActSectionFragment(strActName);
        if (fragment != null)
        {
            FragmentManager fragmentManager=getSupportFragmentManager();
                    /*FragmentManager fragmentManager=getSupportFragmentManager();*/
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).addToBackStack(null).commit();
        }
        else
        {
            // error in creating fragment
            Log.e("ActSectionFragment", "Error in creating fragment");
        }
    }

    @Override
    public void onBackPressed() {
         if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
             super.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
                super.onBackPressed();
            } else {
                super.onBackPressed();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
