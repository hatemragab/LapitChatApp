package com.example.temo.lapitchatapp;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class MFragmetAdapter extends FragmentPagerAdapter {
    public MFragmetAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0: {
                return new Request();
            }

            case 1: {
                return new Chat();
            }

            case 2: {
                return new Request();
            }


            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return 3;
    }


    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {

        switch (position) {
            case 0:
                return "requests";
            case 1:
                return "chat";
            case 2:
                return "Friends";
            default:
                return null;


        }
    }
}
