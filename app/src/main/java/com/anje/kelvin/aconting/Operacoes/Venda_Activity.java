package com.anje.kelvin.aconting.Operacoes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.anje.kelvin.aconting.Adapters.AdapterStock;
import com.anje.kelvin.aconting.Adapters.AdapterVenda;
import com.anje.kelvin.aconting.Adapters.Stock;
import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.BaseDeDados.Deposito_db;
import com.anje.kelvin.aconting.BaseDeDados.Venda;
import com.anje.kelvin.aconting.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class Venda_Activity extends AppCompatActivity {
    Venda venda;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    public List<Stock> lista;
    TextView saldo, itens;
    Realm realm= Realm.getDefaultInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venda_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        venda = new Venda();
        Date hoje = new Date();
        venda.setVenda("Venda" + hoje.getDate() + "" + hoje.getYear() + "" + hoje.getMonth());
        saldo = (TextView) findViewById(R.id.tv_venda_itens_vendidos);
        itens = (TextView) findViewById(R.id.tv_venda_itens_vendidos);
        saldo.setText(venda.getValor() + " MZN");
        itens.setText(venda.getItens_vendidos() + "");
        recyclerView = (RecyclerView) findViewById(R.id.rv_vendas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        lista = new ArrayList<Stock>();
        try {
            Realm realm = Realm.getDefaultInstance();
            try {
                RealmResults<Conta> contas = realm.where(Conta.class).findAll();
                if (contas.get(0).getStock().size() > 0) {
                    for (int i = 0; i < contas.get(0).getStock().size(); i++) {
                        Conta conta = contas.get(0);
                        Stock stock = new Stock(contas.get(0).getStock().get(i).getNome_Item() + "", contas.get(0).getStock().get(i).getNum_item() + "",
                                contas.get(0).getStock().get(i).getItens_disponiveis() + "", contas.get(0).getStock().get(i).getPreco() + "Mzn");
                        lista.add(stock);
                    }
                }


            } finally {

            }


            adapter = new AdapterVenda(lista,Venda_Activity.this);
            recyclerView.setAdapter(adapter);


            Button adicionar_item = (Button) findViewById(R.id.bt_adicionar_producto);
            adicionar_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    venda.setData(new Date());
                    AlertDialog.Builder vender = new AlertDialog.Builder(Venda_Activity.this);
                    vender.setTitle("Aviso").setTitle("Tem a Certeza que Deseja vender os itens Selecionados").setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {


                        }
                    }).setNegativeButton("Nao", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).create().show();

                }
            });
        } finally {

        }


    }
}

