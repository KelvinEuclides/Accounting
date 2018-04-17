package com.anje.kelvin.aconting;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import com.anje.kelvin.aconting.BaseDeDados.Debito_automatico;
import com.anje.kelvin.aconting.BaseDeDados.Despesa_db;
import com.anje.kelvin.aconting.BaseDeDados.Item;
import com.anje.kelvin.aconting.BaseDeDados.Transacao_db;
import com.anje.kelvin.aconting.Fragments.ContaFragment;
import com.anje.kelvin.aconting.Fragments.MenuFragment;

import java.util.Date;
import java.util.List;

import io.realm.Realm;

public class MainActivity extends FragmentActivity {
    ViewPager viewPager;
    AdapterFragment adapterFragment;

    final FragmentManager fm=getSupportFragmentManager();
    MenuFragment firstFragment = new MenuFragment();
    ContaFragment contaFragment=new ContaFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewPager = (ViewPager) findViewById(R.id.viewp);
        adapterFragment = new AdapterFragment(getSupportFragmentManager());
        viewPager.setAdapter(adapterFragment);
        final Realm realm= Realm.getDefaultInstance();
        final Conta conta=realm.where(Conta.class).equalTo("loggado",true).findFirst();
        List<Item> item=realm.where(Item.class).equalTo("id_usuario",conta.getId_usuario()).findAll();
        for(int i=0;i<item.size();i++){
            if(item.get(i).getItens_disponiveis()<5 && conta.getAlertas()==true) {
                AlertDialog.Builder builder =new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Aviso").setMessage("Tem Apenas"+item.get(i).getItens_disponiveis()+"Itens de "+item.get(i).getNome_Item()+" Em Estoque deseja repor?");
                builder.setPositiveButton("ignorar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).setNegativeButton("Repor", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();

            }
        }
        if(conta.getSaldo_conta()<0){
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Aviso").setMessage("Possui saldo negativo na conta Efecctue algum Deposito").setPositiveButton("OK",new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            }).create().show();
        }
        try {
            List<Debito_automatico> debito_automaticoList = realm.where(Debito_automatico.class).equalTo("id_usuario;", conta.getId_usuario()).greaterThanOrEqualTo("DataFim", new Date()).findAll();
            if (debito_automaticoList.isEmpty() == false && conta.getAlertas()==true) {
                for (int i = 0; i < debito_automaticoList.size(); i++) {
                    if (debito_automaticoList.get(i).getMensal() == false) {
                        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
                        builder.setMessage("Deseja Adicionar a despesa " + debito_automaticoList.get(i).getDescricao() + " ? ");
                        builder.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                List<Debito_automatico> debito_automaticoList = realm.where(Debito_automatico.class).equalTo("id_usuario;", conta.getId_usuario()).greaterThanOrEqualTo("DataFim", new Date()).findAll();
                                final int k = i;
                                Despesa_db despesa_db = new Despesa_db();
                                despesa_db.setId_usuario(conta.getId_usuario());
                                despesa_db.setValor(debito_automaticoList.get(k).getValor());
                                despesa_db.setDescricao(debito_automaticoList.get(k).getDescricao());
                                despesa_db.setDia(new Date());
                                Transacao_db transacao_db = new Transacao_db();
                                transacao_db.setDescricao(debito_automaticoList.get(k).getDescricao());
                                transacao_db.setId_usuario(conta.getId_usuario());
                                transacao_db.setDia(new Date());
                                transacao_db.setValor(debito_automaticoList.get(k).getValor());
                                Realm realm1 = Realm.getDefaultInstance();
                                realm1.beginTransaction();
                                realm1.copyToRealm(despesa_db);
                                realm1.copyToRealm(transacao_db);
                                realm1.commitTransaction();
                            }
                        }).setNegativeButton("Nao", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).create().show();

                    }
                }

            }
        }catch (IllegalArgumentException e){
            
        }


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
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


    public static void reiniciar(Activity activity){
        activity.recreate();
    }


}
