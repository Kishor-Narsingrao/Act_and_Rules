package com.example.samvid.myapplication;


public class SectionsDetailsPOJO {

    String section_header;
    String section_no;

    public SectionsDetailsPOJO(String section_no,String section_header)
    {
        this.section_header=section_header;
        this.section_no=section_no;
    }

    public String getSection_header() {
        return section_header;
    }

    public void setSection_header(String section_header) {
        this.section_header = section_header;
    }

    public String getSection_no() {
        return section_no;
    }

    public void setSection_no(String section_no) {
        this.section_no = section_no;
    }
}
