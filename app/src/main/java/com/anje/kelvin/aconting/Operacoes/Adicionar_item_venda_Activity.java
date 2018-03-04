package com.anje.kelvin.aconting.Operacoes;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.anje.kelvin.aconting.Adapters.RecyclerVIewAdapter.AdapterStock;
import com.anje.kelvin.aconting.Adapters.AdapterObjects.Stock;
import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.BaseDeDados.Item;
import com.anje.kelvin.aconting.R;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class Adicionar_item_venda_Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private AdapterStock adapter;
    public List<Stock> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_item_venda_);
        recyclerView = (RecyclerView) findViewById(R.id.rv_add_itens);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        lista=new ArrayList<Stock>();
        Realm realm = Realm.getDefaultInstance();
        try {
            Conta conta = realm.where(Conta.class).equalTo("loggado",true).findFirst();
            List<Item> item=realm.where(Item.class).equalTo("id_usuario",conta.getId_usuario()).findAll();
            if (item.size() > 0) {
                for (int i = 0; i < item.size(); i++) {
                    Stock stock=new Stock(item.get(i).getNome_Item()+"",item.get(i).getNum_item()+"",
                            item.get(i).getItens_disponiveis()+"",item.get(i).getPreco()+"Mzn");
                    lista.add(stock);
                }
            }
        }finally {

        }
        adapter = new AdapterStock(lista,Adicionar_item_venda_Activity.this);
        recyclerView.setAdapter(adapter);

    }
}
