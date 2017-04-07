package com.example.samvid.myapplication;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;

import com.example.samvid.myapplication.R;

import java.util.ArrayList;

public class Rule extends Fragment {

    RecyclerView rvBookDetails;
    View rootView;

    SQLiteDatabase db;
    BookDetailsAdapter  bookadapter;

    public Rule()
    { }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_acts);
    }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.activity_acts, container, false);

//            Toolbar toolbar=(Toolbar)getActivity().findViewById(R.id.toolbar);
//            getActivity().setActionBar(toolbar);
//            getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));

            rvBookDetails=(RecyclerView)rootView.findViewById(R.id.rvBookDetails);

        com.example.samvid.myapplication.DatabaseAccess da= com.example.samvid.myapplication.DatabaseAccess.getInstance(getActivity());
        db=da.open();
        ArrayList<String> alBookDetails=new ArrayList<String>();

        Cursor c = db.rawQuery("select BookName from Books", null);
        int rows = c.getCount();

        c.moveToFirst();

        for(int i=0;i<rows;i++)
        {
            String strBookName=c.getString(c.getColumnIndex("BookName"));
            alBookDetails.add(strBookName);
            c.moveToNext();
        }

        c.close();
        bookadapter=new BookDetailsAdapter(getActivity(),alBookDetails);
        rvBookDetails.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvBookDetails.setAdapter(bookadapter);

            return rootView;
    }


    /*@Override
    public void onBackPressed() {
        super.onBackPressed();
        Acts_three.this.finish();
        this.finish();
    }*/




    private class  BookDetailsAdapter extends RecyclerView.Adapter<viewHolder>
    {
        ArrayList<String> alBooks=new ArrayList<String>();
        Context context;
        viewHolder holder;

//        public  BookDetailsAdapter()
//        {}

        public BookDetailsAdapter(Context context,ArrayList<String> albooks)
        {
            this.context=context;
            this.alBooks=albooks;
        }

        @Override
        public int getItemCount() {
            return alBooks.size();
        }

        @Override
        public viewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.act_cardview,parent,false);
            holder=new viewHolder(v);
            return holder;
        }

        @Override
        public void onBindViewHolder(viewHolder holder, int position) {
            holder.tvBookName.setText(alBooks.get(position));
        }
    }

    private class viewHolder extends  RecyclerView.ViewHolder {
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
                    Intent intent=new Intent(getActivity(), Rule_Fragment_Container.class);
                    intent.putExtra("ActName", tvBookName.getText());
                    startActivity(intent);
                }
            });
        }
    }
}