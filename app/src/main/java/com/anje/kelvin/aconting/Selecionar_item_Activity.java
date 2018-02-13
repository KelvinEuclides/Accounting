package com.anje.kelvin.aconting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.anje.kelvin.aconting.Adapters.AdapterStock;
import com.anje.kelvin.aconting.Adapters.Stock;
import com.anje.kelvin.aconting.Adapters.Transacao_itens;
import com.anje.kelvin.aconting.BaseDeDados.Conta;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class Selecionar_item_Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    public List<Stock> lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecionar_item_);
        recyclerView = (RecyclerView) findViewById(R.id.rv_item_stock);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        lista=new ArrayList<Stock>();
        Realm realm = Realm.getDefaultInstance();
        try {
            RealmResults<Conta> contas = realm.where(Conta.class).findAll();
            if (contas.get(0).getStock().size() > 0) {
                for (int i = 0; i < contas.get(0).getStock().size(); i++) {
                    Conta conta = contas.get(0);
                    Stock stock=new Stock(contas.get(0).getStock().get(i).getNome_Item()+"",contas.get(0).getStock().get(i).getNum_item()+"",
                            contas.get(0).getStock().get(i).getItens_disponiveis()+"",contas.get(0).getStock().get(i).getPreco()+"Mzn");
                    lista.add(stock);
                }
            }
        }finally {

        }
        adapter = new AdapterStock(lista,Selecionar_item_Activity.this);
        recyclerView.setAdapter(adapter);

    }
}
