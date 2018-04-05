package com.anje.kelvin.aconting;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.anje.kelvin.aconting.Adapters.ViewPAgerAdapter.AdapterFragment;
import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.BaseDeDados.Item;
import com.anje.kelvin.aconting.Fragments.ContaFragment;
import com.anje.kelvin.aconting.Fragments.MenuFragment;

import java.util.List;

import io.realm.Realm;

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
                    return true;
                case R.id.navigation_estatisticas:
                    viewPager.setCurrentItem(3);


                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewp);
        adapterFragment = new AdapterFragment(getSupportFragmentManager());
        viewPager.setAdapter(adapterFragment);
        Realm realm= Realm.getDefaultInstance();
        Conta conta=realm.where(Conta.class).equalTo("loggado",true).findFirst();
        List<Item> item=realm.where(Item.class).equalTo("id_usuario",conta.getId_usuario()).findAll();
        for(int i=0;i<item.size();i++){
            if(item.get(i).getItens_disponiveis()<5) {
                final Dialog dialog=new Dialog(MainActivity.this);
                dialog.setTitle("Aviso");
                dialog.setContentView(R.layout.dialogalerta);
                TextView textView=(TextView) dialog.findViewById(R.id.textView52);
                textView.setText("Tem Apenas"+item.get(i).getItens_disponiveis()+"Itens de "+item.get(i).getNome_Item()+" Em Estoque deseja repor?");
                Button ignorar=(Button) dialog.findViewById(R.id.bt_not_eliminar);
                ignorar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });

                Button repor=(Button) dialog.findViewById(R.id.bt_repor);
                repor.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                    }
                });
                dialog.show();
            }
        }


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    public static void reiniciar(Activity activity){
        activity.recreate();
    }


}
