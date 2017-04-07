package com.example.samvid.myapplication;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONObject;

import java.util.ArrayList;

public class ActSubSectionFragment extends Fragment implements ResultCallBack{

    View rootView;
    TextView tvChapterHeading;
    SQLiteDatabase db;
    ArrayList<SectionsDetailsPOJO> alSection = new ArrayList<>();
    ListView lvSection;
    LinearLayout llView;
    String strChapter;

    public ActSubSectionFragment(){}

    public ActSubSectionFragment(String chapterName){
        strChapter=chapterName;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_sub_section_fragment);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_sub_section_fragment, container, false);

        com.example.samvid.myapplication.DatabaseAccess da = com.example.samvid.myapplication.DatabaseAccess.getInstance(getActivity());
        db = da.open();

//        Bundle bundle = getIntent().getExtras();
//        strChapter = bundle.getString("chapterName");

//        AsyncTask_WebAPI asyncTask_webAPI=new AsyncTask_WebAPI(getActivity(),"http://api.androidhive.info/contacts/",this);
//        asyncTask_webAPI.execute();

        lvSection = (ListView) rootView.findViewById(R.id.lvSection);
        tvChapterHeading = (TextView) rootView.findViewById(R.id.tvChapterHeading);
        llView = (LinearLayout) rootView.findViewById(R.id.llView);

        Cursor c = db.rawQuery("select  sectionno,sectionheader from sectiondetails where Chapter='" + strChapter + "'", null);
        int rows = c.getCount();

        c.moveToFirst();

        for (int i = 0; i < rows; i++) {
            String strsectionNo = c.getString(c.getColumnIndex("SectionNo"));
            String strSection_Header = c.getString(c.getColumnIndex("SectionHeader"));
            alSection.add(new SectionsDetailsPOJO(strsectionNo, strSection_Header));
            c.moveToNext();
        }
        c.close();

        tvChapterHeading.setText(strChapter);
        CustomAdapter adapter = new CustomAdapter(getActivity(), alSection);
        lvSection.setAdapter(adapter);

        lvSection.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Fragment fragment = null;

                llView.setVisibility(View.GONE);
//                fragment = new com.example.samvid.myapplication.ActSectionDetailFragment_four(alSection.get(position).getSection_no(),alSection.get(position).getSection_header());

                if (fragment != null)
                {
                    FragmentManager fragmentManager=getActivity().getSupportFragmentManager();
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
        });
        return rootView;
    }

    @Override
    public void onResultListener(String object) {
//        JSONObject response=(JSONObject) object;
    }


    public class CustomAdapter extends BaseAdapter  {

        Activity activity;
        ArrayList<SectionsDetailsPOJO> alSectionDetails;

        public CustomAdapter(Activity a, ArrayList<SectionsDetailsPOJO> d) {
            activity = a;
            alSectionDetails = d;
        }

        @Override
        public int getViewTypeCount() {
            if(alSectionDetails.size()>0) {
                return alSectionDetails.size();
            }
            return 1;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public int getCount() {
            if (alSectionDetails.size() <= 0)
                return 1;
            return alSectionDetails.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder;

            final LayoutInflater inflater = ( LayoutInflater )activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.custom_view, null);

                holder = new ViewHolder();
                holder.SectionNo = (TextView) convertView.findViewById(R.id.textView1);
                holder.SectionHeader=(TextView)convertView.findViewById(R.id.textView2);

                holder.SectionNo.setText(getResources().getString(R.string.section)+alSectionDetails.get(position).getSection_no());
                holder.SectionHeader.setText(alSectionDetails.get(position).getSection_header());

                convertView.setTag(holder);
            }
            else {
                holder = (ViewHolder) convertView.getTag();
                convertView.setTag(holder);
            }
            return convertView;
        }

        public  class ViewHolder {
            public TextView SectionNo;
            public TextView SectionHeader;
        }
    }

}