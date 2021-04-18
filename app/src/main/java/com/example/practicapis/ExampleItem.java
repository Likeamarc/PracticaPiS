package com.example.practicapis;

public class ExampleItem {
    private int mImageResource;
    private String mText1;
    private String mText2;
    private boolean expanded;

    public ExampleItem(int imagegeResource, String text1, String text2){
        mImageResource = imagegeResource;
        mText1 = text1;
        mText2 = text2;
        this.expanded = false;
    }

    public int getmImageResource(){
        return mImageResource;
    }

    public String getmText1(){
        return mText1;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getmText2(){
        return mText2;
    }
}
