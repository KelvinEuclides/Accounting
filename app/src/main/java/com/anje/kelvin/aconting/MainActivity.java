package com.anje.kelvin.aconting;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;

import com.anje.kelvin.aconting.Fragments.ContaFragment;
import com.anje.kelvin.aconting.Fragments.MenuFragment;

public class MainActivity extends FragmentActivity {

    final FragmentManager fm=getSupportFragmentManager();
    final FragmentTransaction ft =fm.beginTransaction();
    MenuFragment firstFragment = new MenuFragment();
    ContaFragment contaFragment=new ContaFragment();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    ft.add(R.id.container,contaFragment);

                    return true;
                case R.id.navigation_dashboard:

                    ft.replace(R.id.container,firstFragment);

                    return true;
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
        // the fragment_container FrameLayout




            ft.add(R.id.container,firstFragment);
           ft.commit();



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public static void reiniciar(Activity activity){
        activity.recreate();
    }


}
