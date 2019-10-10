package com.example.splitshare;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PageAdapterMainActivity extends FragmentStatePagerAdapter {

    private int numOfTabs;
    private String ref;

    public PageAdapterMainActivity(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
        this.ref = "";
    }

    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new FragmentOne();
            case 1:
                return FragmentTwo.newInstance(ref);
            default:
                return null;
        }
    }


    public String getRef() {
        return ref;
    }


    public void setRef(String ref) {
        this.ref = ref;
    }


    @Override
    public int getCount() {
        return numOfTabs;
    }

}
