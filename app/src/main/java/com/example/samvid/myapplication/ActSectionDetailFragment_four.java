package com.example.samvid.myapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class ActSectionDetailFragment_four extends Fragment implements ResultCallBack{

    View rootView;

    TextView tvChapter,tvSectionNo,tvSectionHeader,tvSectionContent;
    String strSectionNo,strSectionHeader;

    SQLiteDatabase db;

    String strURL="http://172.168.20.31:84/api/TreasuryBooks?bookIndexId=1&bookId=1";

    AlGlobalList globalList;

    int iBookIndexId;
    int iBookInfoId;

    public ActSectionDetailFragment_four()
    { }

    public ActSectionDetailFragment_four(String sectionNo)
    {
        strSectionNo=sectionNo;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.section_details, container, false);

        com.example.samvid.myapplication.DatabaseAccess da = com.example.samvid.myapplication.DatabaseAccess.getInstance(getActivity());
        db = da.open();

        globalList=(AlGlobalList)getActivity().getApplicationContext();

        tvChapter=(TextView)rootView.findViewById(R.id.tvChapter);
        tvSectionNo=(TextView)rootView.findViewById(R.id.tvSectionNo);
        tvSectionHeader=(TextView)rootView.findViewById(R.id.tvSectionHeader);
        tvSectionContent=(TextView)rootView.findViewById(R.id.tvSectionContent);

        tvChapter.setVisibility(View.GONE);
        tvSectionNo.setVisibility(View.GONE);
        tvSectionHeader.setVisibility(View.GONE);
        tvSectionContent.setVisibility(View.GONE);


        Cursor c = db.rawQuery("select bookinfoid,bookindexid from bookinformation where bookinfoname='"+strSectionNo+"'", null);
        int rows = c.getCount();

        if(rows>0) {
            c.moveToFirst();
            iBookIndexId = c.getInt(c.getColumnIndex("BookIndexId"));
            iBookInfoId = c.getInt(c.getColumnIndex("BookInfoId"));

            c.close();
        }/*else
        {
            for(int i=0;i<globalList.getBookInformationSize();i++)
            {
                if(globalList.getBookInformation(i).getStrBookInfoName().equals(strSectionNo))
                {
                    iBookIndexId=globalList.getBookInformation(i).getiBookIndexId();
                    iBookInfoId=globalList.getBookInformation(i).getiBookInfoId();
                    break;
                }
            }
        }*/

         c = db.rawQuery("select BookInfoName from BookInformation  where BookIndexId='"+iBookIndexId+"' and ParentBookId='"+iBookInfoId+"' and BookInformation.TypeId=1", null);
         rows = c.getCount();

        if(rows>0) {
            c.moveToFirst();
            tvSectionNo.setVisibility(View.VISIBLE);
            tvSectionNo.setText(c.getString(c.getColumnIndex("BookInfoName")));
            c.moveToNext();
        }
        /*else {
            for(int i=0;i<globalList.getBookInformationSize();i++)
            {
                if(globalList.getBookInformation(i).getiBookIndexId() == iBookIndexId && globalList.getBookInformation(i).getiBookInfoId() == iBookInfoId && globalList.getBookInformation(i).getiTypeId() ==1)
                {
                    tvSectionNo.setVisibility(View.VISIBLE);
                    tvSectionNo.setText(globalList.getBookInformation(i).getStrBookInfoName());
                }
            }
        }*/
        c.close();

        c = db.rawQuery("select BookInfoName from BookInformation  where BookIndexId='"+iBookIndexId+"' and ParentBookId='"+iBookInfoId+"' and BookInformation.TypeId=2", null);
        rows = c.getCount();

        if(rows>0) {
            c.moveToFirst();
            tvSectionHeader.setVisibility(View.VISIBLE);
            tvSectionHeader.setText(c.getString(c.getColumnIndex("BookInfoName")));
            c.moveToNext();
        }
        /*else {
            for(int i=0;i<globalList.getBookInformationSize();i++)
            {
                if(globalList.getBookInformation(i).getiBookIndexId() == iBookIndexId && globalList.getBookInformation(i).getiParentBookInfoId() == iBookInfoId && globalList.getBookInformation(i).getiTypeId() ==2)
                {
                    tvSectionHeader.setVisibility(View.VISIBLE);
                    tvSectionHeader.setText(globalList.getBookInformation(i).getStrBookInfoName());
                }
            }
        }*/
        c.close();

        c = db.rawQuery("select BookInfoName from BookInformation  where BookIndexId='"+iBookIndexId+"' and ParentBookId='"+iBookInfoId+"' and BookInformation.TypeId=3", null);
        rows = c.getCount();

        if(rows>0) {
            c.moveToFirst();
            tvSectionContent.setVisibility(View.VISIBLE);
            String strFormat=c.getString(c.getColumnIndex("BookInfoName"));
            String strFormated=strFormat.replace(".", "." + System.getProperty("line.separator")).replace("(a)",  System.getProperty("line.separator") + "(a)").replace("(b)", System.getProperty("line.separator") + "(b)").replace("(a&b)", System.getProperty("line.separator") + "(a&b)").replace("(c)", System.getProperty("line.separator") + "(c)").replace("(h)", System.getProperty("line.separator") + "(h)").replace("(g)", System.getProperty("line.separator") + "(g)").replace("(f)", System.getProperty("line.separator") + "(f)").replace("(e)", System.getProperty("line.separator") + "(e)").replace("(d)", System.getProperty("line.separator") + "(d)").replace("(i)", System.getProperty("line.separator") + "(i)").replace("(ii)", System.getProperty("line.separator") + "(ii)").replace("(iii)", System.getProperty("line.separator") + "(iii)").replace("(iv)", System.getProperty("line.separator") + "(iv)").replace("(v)", System.getProperty("line.separator") + "(v)");
            tvSectionContent.setText(strFormated);
            c.moveToNext();
        }
        /*else {
            for(int i=0;i<globalList.getBookInformationSize();i++)
            {
                if(globalList.getBookInformation(i).getiBookIndexId() == iBookIndexId && globalList.getBookInformation(i).getiParentBookInfoId() == iBookInfoId && globalList.getBookInformation(i).getiTypeId() ==3)
                {
                    tvSectionContent.setVisibility(View.VISIBLE);
                    String strFormat=globalList.getBookInformation(i).getStrBookInfoName();
                    String strFormated=strFormat.replace(".", "." + System.getProperty("line.separator")).replace("(a)",  System.getProperty("line.separator") + "(a)").replace("(b)", System.getProperty("line.separator") + "(b)").replace("(a&b)", System.getProperty("line.separator") + "(a&b)").replace("(c)", System.getProperty("line.separator") + "(c)").replace("(h)", System.getProperty("line.separator") + "(h)").replace("(g)", System.getProperty("line.separator") + "(g)").replace("(f)", System.getProperty("line.separator") + "(f)").replace("(e)", System.getProperty("line.separator") + "(e)").replace("(d)", System.getProperty("line.separator") + "(d)").replace("(i)", System.getProperty("line.separator") + "(i)").replace("(ii)", System.getProperty("line.separator") + "(ii)").replace("(iii)", System.getProperty("line.separator") + "(iii)").replace("(iv)", System.getProperty("line.separator") + "(iv)").replace("(v)", System.getProperty("line.separator") + "(v)");
                    tvSectionContent.setText(strFormated);
                }
            }
        }*/
        c.close();
        return  rootView;
    }

    @Override
    public void onResultListener(String object) {

    }
}
