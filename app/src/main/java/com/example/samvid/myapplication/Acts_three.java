package com.example.samvid.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Acts_three extends AppCompatActivity implements ResultCallBack {

    RecyclerView rvBookDetails;
    View rootView;

    SQLiteDatabase db;
    com.example.samvid.myapplication.DatabaseAccess da;
    BookDetailsAdapter bookadapter;
    Toolbar toolbar;
    String strBookName;
    ArrayList<String> alBookDetails;
    EditText etSearch;
    int iBookId;

    String strURL = "http://172.168.20.31:84/api/TreasuryBooks?bookInfoId=null&bookIndexId=";

    AlGlobalList globalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment__container);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
//        toolbar.setBackgroundColor(getResources().getColor(R.color.acts));
        setSupportActionBar(toolbar);

        da = com.example.samvid.myapplication.DatabaseAccess.getInstance(this);
        db = da.open();
        globalList = (AlGlobalList) getApplicationContext();

        Bundle bundle = getIntent().getExtras();
        strBookName = bundle.getString("BookName");

        getSupportActionBar().setTitle(strBookName);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        Cursor c = db.rawQuery("select BookIndexId from bookIndex where bookindexname='" + strBookName + "'", null);
        int rowscount = c.getCount();

        if(rowscount>0) {
            c.moveToFirst();
            iBookId = c.getInt(c.getColumnIndex("BookIndexId"));
            c.close();
        }

        strURL = strURL + iBookId + "&paarentInfoId=null&typeId=null";

//        AsyncTask_WebAPI asyncTask = new AsyncTask_WebAPI(this, strURL, this);
//        asyncTask.execute();

        rvBookDetails = (RecyclerView) findViewById(R.id.rvtypes);

        setData();
    }


    public void setData()
    {
        alBookDetails = new ArrayList<String>();

        db = da.open();
        Cursor c = db.rawQuery("select BookInfoName from BookInformation where BookIndexId='" + iBookId + "' and TypeId=1", null);
        int rows = c.getCount();

        c.moveToFirst();

        for (int i = 0; i < rows; i++) {
            String strBookIndexName = c.getString(c.getColumnIndex("BookInfoName"));
            alBookDetails.add(strBookIndexName);
            c.moveToNext();
        }

        c.close();
        db.close();

        bookadapter = new BookDetailsAdapter(this, alBookDetails);
        rvBookDetails.setLayoutManager(new LinearLayoutManager(this));
        rvBookDetails.setAdapter(bookadapter);

    }

    @Override
    public void onResultListener(String object) {
        try {
            JSONArray jsonArray = new JSONArray(object);
            db = da.open();

            Cursor cursor=db.rawQuery("delete from BookInformation",null);
            cursor.getCount();
            cursor.close();
            for (int i = 0; i < jsonArray.length(); i++) {

                JSONObject jsonObjectChild = jsonArray.getJSONObject(i);

                int iBookInfoId = jsonObjectChild.getInt("BookInfoId");
                int iBookIndexId = jsonObjectChild.getInt("BookIndexId");
                String strBookInfoName = jsonObjectChild.getString("BookInfoName");
                int iParentBookInfoId = jsonObjectChild.getInt("ParentBookInfoId");
                int iTypeId=jsonObjectChild.getInt("TypeId");

                Cursor c = db.rawQuery("insert into BookInformation values('" + iBookInfoId + "','" + iBookIndexId + "','" + strBookInfoName + "','" + iParentBookInfoId + "','" + iTypeId + "',1)", null);
                c.getCount();
            }
            db.close();
            setData();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.search, menu);

        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);

        SearchView searchView = (SearchView) myActionMenuItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                bookadapter.getFilter().filter(newText);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home)
        {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else {
                super.onBackPressed();
            }
        }
        rvBookDetails.setVisibility(View.VISIBLE);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            super.onBackPressed();
        } else {
            super.onBackPressed();
        }
        rvBookDetails.setVisibility(View.VISIBLE);
    }

    private class BookDetailsAdapter extends RecyclerView.Adapter<viewHolder> implements Filterable {
        ArrayList<String> alBooks = new ArrayList<String>();
        Context context;
        viewHolder holder;

        public BookDetailsAdapter(Context context, ArrayList<String> albooks) {
            this.context = context;
            this.alBooks = albooks;
        }

        @Override
        public int getItemCount() {
            return alBooks.size();
        }

        @Override
        public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.act_cardview, parent, false);
            holder = new viewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(viewHolder holder, int position) {
            holder.tvBookName.setText(alBooks.get(position));
        }

        private Filter fRecords;

        @Override
        public Filter getFilter() {
            if (fRecords == null) {
                fRecords = new RecordFilter();
            }
            return fRecords;
        }

        private class RecordFilter extends Filter {

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                FilterResults results = new FilterResults();

                //Implement filter logic
                if (constraint == null || constraint.length() == 0) {
                    //No need for filter
                    results.values = alBookDetails;
                    results.count = alBookDetails.size();

                } else {
                    //Need Filter
                    ArrayList<String> fRecords = new ArrayList<String>();

                    for (String s : alBookDetails) {
                        if (s.toUpperCase().trim().contains(constraint.toString().toUpperCase().trim())) {
                            fRecords.add(s);
                        }
                    }
                    results.values = fRecords;
                    results.count = fRecords.size();
                }
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {

                alBooks = (ArrayList<String>) results.values;
                notifyDataSetChanged();
            }
        }
    }

    private class viewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        ImageView ibBookImage;
        TextView tvBookName;

        viewHolder(final View itemview) {
            super(itemview);

            cv = (CardView) itemview.findViewById(R.id.cardview);
            ibBookImage = (ImageView) itemview.findViewById(R.id.bookImage);
            tvBookName = (TextView) itemview.findViewById(R.id.bookName);

            itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Fragment fragment = null;
                    Bundle bundle =new Bundle();
                    bundle.putString("section",tvBookName.getText().toString());

                    fragment = new ActSectionDetailFragment_four(/*tvBookName.getText().toString()*/);
                    fragment.setArguments(bundle);

                    if (fragment != null) {
                        //FragmentManager fragmentManager=getSupportFragmentManager();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        fragmentManager.beginTransaction()
                                .replace(R.id.frame_container, fragment).addToBackStack(null).commit();
                        rvBookDetails.setVisibility(View.GONE);
                    } else {
                        // error in creating fragment
                        Log.e("ActSectionFragment", "Error in creating fragment");
                    }
                }
            });
        }
    }
}