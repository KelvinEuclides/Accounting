package com.anje.kelvin.aconting.Operacoes;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.anje.kelvin.aconting.Adapters.AdapterStock;
import com.anje.kelvin.aconting.Adapters.Stock;
import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.BaseDeDados.Deposito_db;
import com.anje.kelvin.aconting.BaseDeDados.Venda;
import com.anje.kelvin.aconting.R;
import com.anje.kelvin.aconting.item_stock_Activity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class Venda_Activity extends AppCompatActivity {
    Venda venda=new Venda();
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    public List<Stock> lista;
    TextView saldo,itens;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venda_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        saldo = (TextView) findViewById(R.id.tv_venda_itens_vendidos);
        saldo.setText(venda.getValor().toString());
        itens.setText(venda.getItens_vendidos());
        recyclerView = (RecyclerView) findViewById(R.id.rv_vendas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        lista=new ArrayList<Stock>();
        Realm realm = Realm.getDefaultInstance();
        try {
            RealmResults<Conta> contas = realm.where(Conta.class).findAll();

                for (int i = 0; i < venda.getItems().size() ;i++) {
                    Stock stock=new Stock(venda.getItems().get(i).getNome_Item()+"",venda.getItems().get(i).getNum_item()+"",
                            "",(venda.getItems().get(i).getNum_item()*venda.getItems().get(i).getPrecoUnidade())+"Mzn");
                    lista.add(stock);

            }
        }finally {

        }
        adapter = new AdapterStock(lista,Venda_Activity.this);
        recyclerView.setAdapter(adapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder vender=new AlertDialog.Builder(Venda_Activity.this);
                vender.setTitle("Aviso").setMessage("Deseja Adicionar os itens selecionados a venda").setPositiveButton("sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Realm realm = Realm.getDefaultInstance();
                        RealmResults<Conta> contas = realm.where(Conta.class).findAll();
                        contas.get(0).setVendas(venda);
                        Deposito_db deposito_db=new Deposito_db();
                        deposito_db.setDescricao("Venda de "+venda.getItens_vendidos());
                        deposito_db.setValor(venda.getValor());
                        deposito_db.setDia(new Date());
                        contas.get(0).setDeposito_dbs(deposito_db);
                        finish();
                    }
                }).setNegativeButton("nao", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
             vender.show();
            }
        });
    }
}
