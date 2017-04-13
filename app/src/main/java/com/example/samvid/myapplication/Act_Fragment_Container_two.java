package com.example.samvid.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Act_Fragment_Container_two extends AppCompatActivity implements ResultCallBack {

    ArrayList<String> tabTitles = new ArrayList<String>();

    RecyclerView rvType;

    String strBookName;
    int iBoookid;

    SQLiteDatabase db;
    com.example.samvid.myapplication.DatabaseAccess da;
    Types bookadapter;

    String strURL="http://172.168.20.31:84/api/TreasuryBooks?bookIndexId=null&bookId=";

    AlGlobalList globalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment__container);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
//        toolbar.setBackgroundColor(getResources().getColor(R.color.acts));
        setSupportActionBar(toolbar);

        da= com.example.samvid.myapplication.DatabaseAccess.getInstance(this);
        db=da.open();
        globalList=(AlGlobalList)getApplicationContext();

        Bundle bundle = getIntent().getExtras();
        String strBookName = bundle.getString("BookName");

        Cursor c = db.rawQuery("select bookid from books where bookname='"+strBookName+"'", null);
        int rows = c.getCount();

        if(rows>0) {
            c.moveToFirst();
            iBoookid = c.getInt(c.getColumnIndex("BookId"));
        }/*else
        {
            for(int i=0;i<globalList.getBooksSize();i++)
            {
                if(globalList.getBooks(i).strBookName.equals(strBookName))
                {
                    iBoookid=globalList.getBooks(i).getStrBookId();
                    break;
                }
            }
        }*/

        strURL=strURL+iBoookid;

        getSupportActionBar().setTitle(strBookName);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        AsyncTask_WebAPI asyncTask=new AsyncTask_WebAPI(this,strURL,this);
        asyncTask.execute();

        rvType=(RecyclerView)findViewById(R.id.rvtypes);
    }

    public void setData()
    {
        db = da.open();
        tabTitles = new ArrayList<String>();

        Cursor c = db.rawQuery("select BookIndexName from BookIndex ", null);
        int rows = c.getCount();

        c.moveToFirst();

        for (int i = 0; i < rows; i++) {
            String strBoookindexName = c.getString(c.getColumnIndex("BookIndexName"));
            tabTitles.add(strBoookindexName);
            c.moveToNext();
        }

        c.close();
        db.close();

        /*for (int i = 0; i < globalList.getBookIndexSize(); i++) {
            String strBoookName = globalList.getBookIndex(i).getStrBookIndexName();
            tabTitles.add(strBoookName);
//            c.moveToNext();
        }*/

        bookadapter=new Types(this,tabTitles,strBookName);
        rvType.setLayoutManager(new LinearLayoutManager(this));
        rvType.setAdapter(bookadapter);
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            super.onBackPressed();
        } else {
            super.onBackPressed();
        }
        rvType.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate( R.menu.search, menu);

        MenuItem myActionMenuItem = menu.findItem( R.id.action_search);

        SearchView searchView = (SearchView) myActionMenuItem.getActionView();

//        searchView.setBackgroundResource(R.drawable.button_border);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                bookadapter.getFilter().filter(newText.toString());
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
        rvType.setVisibility(View.VISIBLE);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResultListener(String object) {
        try {

            JSONArray jsonArray = new JSONArray(object);
            db=da.open();

            Cursor cursor=db.rawQuery("delete from BookIndex",null);
            cursor.getCount();
            cursor.close();
            for(int i=0;i<jsonArray.length();i++){

                JSONObject jsonObjectChild=jsonArray.getJSONObject(i);

                int iBookId=jsonObjectChild.getInt("BookId");
                int iBookIndexId=jsonObjectChild.getInt("BookIndexId");
                String strBookName=jsonObjectChild.getString("BookIndexName");

//                globalList.setBookIndex(new BookIndexModel(iBookId,iBookIndexId,strBookName));
                Cursor c=db.rawQuery("insert into BookIndex values('"+iBookId+"','"+iBookIndexId+"','"+strBookName+"',1)",null);
                c.getCount();
            }
            db.close();
            setData();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private class Types extends RecyclerView.Adapter<viewHolder> implements Filterable
    {
        ArrayList<String> alBooks=new ArrayList<String>();
        Context context;
        viewHolder holder;


        public  Types()
        {}

        public Types(Context context,ArrayList<String> albooks,String bookName)
        {
            this.context=context;
            this.alBooks=albooks;
            strBookName=bookName;
        }

        @Override
        public int getItemCount() {
            return alBooks.size();
        }

        @Override
        public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.home_cardview,parent,false);
            holder=new viewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(viewHolder holder, int position) {
            holder.ivBook.setImageResource(R.drawable.cover_icon);
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
                    results.values = tabTitles;
                    results.count = tabTitles.size();

                } else {
                    //Need Filter
                    ArrayList<String> fRecords = new ArrayList<String>();

                    for (String s : tabTitles) {
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

    private class viewHolder extends  RecyclerView.ViewHolder {
        CardView cv;
        TextView tvBookName;
        ImageView ivBook;

        viewHolder(final View itemview) {
            super(itemview);

            cv = (CardView) itemview.findViewById(R.id.cardview);
            tvBookName = (TextView) itemview.findViewById(R.id.categoryName);
            ivBook=(ImageView)itemview.findViewById(R.id.bookImage);


            itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(Act_Fragment_Container_two.this, Acts_three.class);
                    intent.putExtra("BookName", tvBookName.getText());
                    startActivity(intent);
                    /*Fragment fragment = null;

                    fragment = new Acts_three(tvBookName.getText().toString());

                    if (fragment != null)
                    {
                        FragmentManager fragmentManager=getSupportFragmentManager();
                        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).addToBackStack(null).commit();
                        rvType.setVisibility(View.GONE);
                    }
                    else
                    {
                        // error in creating fragment
                        Log.e("ActSectionFragment", "Error in creating fragment");
                    }*/
                }
            });
        }
    }
}
