package com.anje.kelvin.aconting.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.anje.kelvin.aconting.Fragments.ContaFragment;
import com.anje.kelvin.aconting.Fragments.DefinicoesFragment;
import com.anje.kelvin.aconting.Fragments.MenuFragment;

/**
 * Created by sala on 25-02-2018.
 */

public class AdapterFragment extends FragmentPagerAdapter {

    private int NUM_ITEMS = 3;

    public AdapterFragment(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new MenuFragment();
            case 1:
                return new ContaFragment();
            case 2:
                return new DefinicoesFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return  NUM_ITEMS;
    }
}
