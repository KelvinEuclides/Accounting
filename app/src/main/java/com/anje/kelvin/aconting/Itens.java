package com.anje.kelvin.aconting;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.anje.kelvin.aconting.Adapters.AdapterVenda;
import com.anje.kelvin.aconting.BaseDeDados.Venda;

public class Itens extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    Venda venda;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_itens);
        recyclerView = (RecyclerView) findViewById(R.id.rv_ac);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        adapter = new AdapterVenda(Itens.this,venda);
        recyclerView.setAdapter(adapter);
    }
}
