package com.example.samvid.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class Home_one extends AppCompatActivity implements ResultCallBack {

    SQLiteDatabase db;
    ArrayList<String> alCategorName = new ArrayList<>();
    RecyclerView rvCategory;
    EditText etSearch;
    Category bookadapter;
    ArrayList<String> alBookInfo;
    com.example.samvid.myapplication.DatabaseAccess da;
    String strURL = "http://Localhost:84/api/TreasuryBooks?BookId=Null";
    AlGlobalList globalList;
    ArrayList<BooksModel> alBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        AsyncTask_WebAPI asyncTask = new AsyncTask_WebAPI(this, strURL, this);
        asyncTask.execute();

        globalList=(AlGlobalList)getApplicationContext();
        alBooks=new ArrayList<BooksModel>();

        getSupportActionBar().setTitle("Acts and Rule");

//        etSearch = (EditText) findViewById(R.id.etSearch);
//        etSearch.setText("");

        da = com.example.samvid.myapplication.DatabaseAccess.getInstance(this);

       /* etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                bookadapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/
    }

    public void readData() {
        db = da.open();
        alBookInfo = new ArrayList<String>();

        Cursor c = db.rawQuery("select BookName from Books", null);
        int rows = c.getCount();

        c.moveToFirst();

        for (int i = 0; i < rows; i++) {
            String strBookName = c.getString(c.getColumnIndex("BookName"));
            alBookInfo.add(strBookName);
            c.moveToNext();
        }

        c.close();
        db.close();

       /* for (int i = 0; i < globalList.getBooksSize(); i++) {
            String strBoookName = globalList.getBooks(i).getStrBookName();
            alBookInfo.add(strBoookName);
//            c.moveToNext();
        }*/

        rvCategory = (RecyclerView) findViewById(R.id.rvCategory);

        bookadapter = new Category(this, alBookInfo);
        rvCategory.setLayoutManager(new LinearLayoutManager(this));
        rvCategory.setAdapter(bookadapter);
    }

    @Override
    public void onBackPressed() {
        Home_one.this.finish();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        etSearch.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.search, menu);

        MenuItem myActionMenuItem = menu.findItem(R.id.action_search);

        SearchView searchView = (SearchView) myActionMenuItem.getActionView();
//        searchView.setBackgroundColor(getResources().getColor(R.color.white));

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
    public void onResultListener(String object) {

        String jsonObjects = object;

        try {
//            JSONObject jsonObject = new JSONObject(jsonObjects);
            JSONArray jsonArray = new JSONArray(jsonObjects);
            db = da.open();
            Cursor cursor = db.rawQuery("delete from Books", null);
            cursor.getCount();
            cursor.close();

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObjectChild = jsonArray.getJSONObject(i);
//                int strId = jsonObjectChild.getInt("$id");
                int strBookId = jsonObjectChild.getInt("BookId");
                String strBookName = jsonObjectChild.getString("BookName");

//                alBooks.add(new BooksModel(strBookId,strBookName));
//                globalList.setBooks(new BooksModel(strBookId,strBookName));

                Cursor c = db.rawQuery("insert into Books values('" + strBookId + "','" + strBookName + "',1)", null);
                c.getCount();
            }
            db.close();
            readData();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class Category extends RecyclerView.Adapter<viewHolder> implements Filterable {
        ArrayList<String> alBooks = new ArrayList<String>();
        Context context;
        viewHolder holder;

        public Category() {
        }

        public Category(Context context, ArrayList<String> albooks) {
            this.context = context;
            this.alBooks = albooks;
        }

        @Override
        public int getItemCount() {
            return alBooks.size();
        }

        @Override
        public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_cardview, parent, false);
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
                    results.values = alBookInfo;
                    results.count = alBookInfo.size();

                } else {

                    //Need Filter
                    ArrayList<String> fRecords = new ArrayList<String>();

                    for (String s : alBookInfo) {
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
        TextView tvBookName;

        viewHolder(final View itemview) {
            super(itemview);

            cv = (CardView) itemview.findViewById(R.id.cardview);
            tvBookName = (TextView) itemview.findViewById(R.id.categoryName);

            itemview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Home_one.this, Act_Fragment_Container_two.class);
                    intent.putExtra("BookName", tvBookName.getText());
                    startActivity(intent);
                }
            });
        }
    }
}
