package com.example.samvid.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ActSectionFragment extends Fragment {

    SQLiteDatabase db;
    ArrayList<String> alChapter=new ArrayList<>();
    ListView lvChapter;
    View rootView;
    String strBookName;

    public ActSectionFragment(){}

    public ActSectionFragment(String bookname){
        strBookName=bookname;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_section, container, false);

        com.example.samvid.myapplication.DatabaseAccess da= com.example.samvid.myapplication.DatabaseAccess.getInstance(getActivity());
        db=da.open();

        lvChapter=(ListView)rootView.findViewById(R.id.lvChapter);

//        Cursor c = db.rawQuery("select distinct(Chapter) from sectionDetails where book_id=(select book_id from tbl_book_detail where book_name='"+strActName+"')", null);
//        int rows = c.getCount();

        Cursor c = db.rawQuery("select BookIndexName from BookIndex join Books where BookIndex.BookId=Books.BookId and BookName='"+strBookName+"'", null);
        int rows = c.getCount();

        c.moveToFirst();

        for(int i=0;i<rows;i++)
        {
            String strChapter=c.getString(c.getColumnIndex("BookIndexName"));
            alChapter.add(strChapter);
            c.moveToNext();
        }

        c.close();
        ArrayAdapter<String> adapter=new ArrayAdapter(getActivity(),android.R.layout.simple_list_item_1,alChapter);
        lvChapter.setAdapter(adapter);

        lvChapter.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                android.support.v4.app.Fragment fragment = null;

                lvChapter.setVisibility(View.GONE);
                fragment = new com.example.samvid.myapplication.ActSubSectionFragment(alChapter.get(position));

                if (fragment != null)
                {
                    android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.frame_container, fragment).addToBackStack(null).commit();
                }
                else
                {
                    // error in creating fragment
                    Log.e("ActSectionFragment", "Error in creating fragment");
                }
            }
        });
        return rootView;
    }
}
