package com.anje.kelvin.aconting.Operacoes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;

import com.anje.kelvin.aconting.Adapters.RecyclerVIewAdapter.AdapterVenda;
import com.anje.kelvin.aconting.Adapters.AdapterObjects.Stock;
import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.BaseDeDados.Item;
import com.anje.kelvin.aconting.R;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class Itens_venda_Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    String vid;
    public List<Stock> lista;
    Button salvaar_econtinuar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itens_venda);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent bundle = getIntent();
        vid = bundle.getStringExtra("id");
        recyclerView = (RecyclerView) findViewById(R.id.rv_itm);
        salvaar_econtinuar=(Button) findViewById(R.id.bt_id_concluir);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        lista = new ArrayList<Stock>();
        try {
            Realm realm = Realm.getDefaultInstance();
            try {
                Conta conta = realm.where(Conta.class).equalTo("loggado",true).findFirst();
                List<Item> item=realm.where(Item.class).equalTo("id_usuario",conta.getId_usuario()).findAll();
                if (item.size() > 0) {
                    for (int i = 0; i < item.size(); i++) {
                        Stock stock = new Stock(item.get(i).getNome_Item() + "", item.get(i).getNum_item() + "",
                                item.get(i).getItens_disponiveis() + "", item.get(i).getPreco() + "Mzn");
                        lista.add(stock);
                    }
                }


            } finally {

            }


            adapter = new AdapterVenda(lista, Itens_venda_Activity.this, vid);
            recyclerView.setAdapter(adapter);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        }finally {

        }

    }

}
