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
    MenuFragment firstFragment = new MenuFragment();
    ContaFragment contaFragment=new ContaFragment();
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    final FragmentTransaction ft =fm.beginTransaction();
                    ft.add(R.id.container,contaFragment);
                    ft.commit();


                    return true;
                case R.id.navigation_dashboard:

                    FragmentTransaction f =fm.beginTransaction();
                    f.add(R.id.container,firstFragment);
                    f.commit();
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
        FragmentTransaction ft =fm.beginTransaction();




            ft.add(R.id.container,firstFragment);
           ft.commit();



        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public static void reiniciar(Activity activity){
        activity.recreate();
    }


}
