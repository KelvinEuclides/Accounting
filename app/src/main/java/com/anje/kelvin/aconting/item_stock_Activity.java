package com.anje.kelvin.aconting;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.anje.kelvin.aconting.Adapters.AdapterStock;
import com.anje.kelvin.aconting.Adapters.Stock;
import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.BaseDeDados.Item;
import com.anje.kelvin.aconting.BaseDeDados.Transacao_db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class item_stock_Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    public List<Stock> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_stock_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        adapter = new AdapterStock(lista,item_stock_Activity.this);
        recyclerView.setAdapter(adapter);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(item_stock_Activity.this);
                LayoutInflater inflater1=LayoutInflater.from(item_stock_Activity.this);
                final View dalogView1=inflater1.inflate(R.layout.dialogoadicionaritem,null);
                builder.setView(dalogView1);
                builder.setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                    final String unidadee=null;
                    final EditText nome_item=(EditText) findViewById(R.id.et_add_nomeitem2);
                    final EditText preco_item=(EditText) findViewById(R.id.et_add_valor_compra2);
                    final EditText preco_venda=(EditText) findViewById(R.id.et_add_preco_unidade2);
                    final EditText numero_itens=(EditText) findViewById(R.id.et_add_numitm2);
                    final RadioButton kg=(RadioButton) findViewById(R.id.rb_kilograma2);
                    final RadioButton litros=(RadioButton) findViewById(R.id.rb_litros2);
                    final RadioButton unidade=(RadioButton) findViewById(R.id.rb_unidade);
                    final TextView tv_unidade=(TextView) findViewById(R.id.tv_dv_unimed);

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Realm realm = Realm.getDefaultInstance();
                        RealmResults<Conta> contas=realm.where(Conta.class).findAll();
                        Item item=new Item();
                        item.setNome_Item(nome_item.getText().toString());
                        item.setPreco(Double.parseDouble(preco_item.getText().toString()));
                        item.setPrecoUnidade(Double.parseDouble(preco_venda.getText().toString()));
                        item.setNum_item(Integer.parseInt(numero_itens.getText().toString()));;
                        item.setUnidade_de_Medida(unidadee);
                        Transacao_db transacao_db=new Transacao_db();
                        transacao_db.setDescricao("Compra de "+item.getNum_item() +"Unidades de "+item.getNome_Item());
                        transacao_db.setValor(item.getPreco());
                        transacao_db.setCategoria("Despesa");
                        transacao_db.setRecorrencia("Nenhuma");
                        transacao_db.setDia(new Date());
                        realm.beginTransaction();
                        contas.get(0).setStock(item);
                        contas.get(0).setTransacaoDbs(transacao_db);
                        realm.commitTransaction();
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();


            }
        });

    }

}
