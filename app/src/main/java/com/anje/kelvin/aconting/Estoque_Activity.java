package com.anje.kelvin.aconting;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.anje.kelvin.aconting.Adapters.AdapterStock;
import com.anje.kelvin.aconting.Adapters.Stock;
import com.anje.kelvin.aconting.BaseDeDados.Conta;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class Estoque_Activity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    public List<Stock> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estoque);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_item_stock);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        lista=new ArrayList<Stock>();
        Realm realm = Realm.getDefaultInstance();
        try {
            Conta contas = realm.where(Conta.class).equalTo("loggado",true).findFirst();
            if (contas.getStock().size() > 0) {
                for (int i = 0; i < contas.getStock().size(); i++) {
                    Stock stock=new Stock(contas.getStock().get(i).getNome_Item()+"",contas.getStock().get(i).getNum_item()+"",
                            contas.getStock().get(i).getItens_disponiveis()+"",contas.getStock().get(i).getPreco()+"Mzn");
                    lista.add(stock);
                }
            }
        }finally {

        }
        adapter = new AdapterStock(lista,Estoque_Activity.this);
        recyclerView.setAdapter(adapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Estoque_Activity.this,Add_item_Activity.class);
                startActivity(intent);
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
