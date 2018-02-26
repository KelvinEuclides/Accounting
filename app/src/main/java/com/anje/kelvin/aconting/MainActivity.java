package com.anje.kelvin.aconting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.anje.kelvin.aconting.Adapters.AdapterFragment;
import com.anje.kelvin.aconting.Fragments.ContaFragment;
import com.anje.kelvin.aconting.Fragments.MenuFragment;

public class MainActivity extends FragmentActivity {

    ViewPager viewPager;
    AdapterFragment adapterFragment;

    final FragmentManager fm=getSupportFragmentManager();
    MenuFragment firstFragment = new MenuFragment();
    ContaFragment contaFragment=new ContaFragment();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                   viewPager.setCurrentItem(0);


                    return true;
                case R.id.navigation_dashboard:
                    viewPager.setCurrentItem(1);
                case R.id.navigation_estatisticas:



                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewPager vpPager = (ViewPager) findViewById(R.id.viewp);
        adapterFragment = new AdapterFragment(getSupportFragmentManager());
        vpPager.setAdapter(adapterFragment);



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public static void reiniciar(Activity activity){
        activity.recreate();
    }


}
