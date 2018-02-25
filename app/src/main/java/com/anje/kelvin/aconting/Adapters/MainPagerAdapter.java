package com.anje.kelvin.aconting.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.Fragments.ContaFragment;
import com.anje.kelvin.aconting.Fragments.MenuFragment;

import java.util.List;

/**
 * Created by sala on 25-02-2018.
 */

public class MainPagerAdapter extends FragmentPagerAdapter {
    private static int NUM_ITEMS = 1;

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);


    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: // Fragment # 0 - This will show FirstFragment
                return MenuFragment.newInstance(0,"Menu");
            case 1: // Fragment # 0 - This will show FirstFragment different title
                return ContaFragment.newInstance(1,"Conta");
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }


}
