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
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.anje.kelvin.aconting.Adapters.AdapterStock;
import com.anje.kelvin.aconting.Adapters.Stock;
import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.BaseDeDados.Deposito_db;
import com.anje.kelvin.aconting.BaseDeDados.Item;
import com.anje.kelvin.aconting.BaseDeDados.Venda;
import com.anje.kelvin.aconting.R;
import com.anje.kelvin.aconting.item_stock_Activity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venda_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        venda = new Venda();
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
            try {



            }finally {

            }



                adapter = new AdapterStock(lista, Venda_Activity.this);
                recyclerView.setAdapter(adapter);

                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder vender = new AlertDialog.Builder(Venda_Activity.this);
                        vender.setTitle("Aviso").setMessage("Deseja Adicionar os itens selecionados a venda").setPositiveButton("sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Realm realm = Realm.getDefaultInstance();
                                RealmResults<Conta> contas = realm.where(Conta.class).findAll();
                                contas.get(0).setVendas(venda);
                                Deposito_db deposito_db = new Deposito_db();
                                deposito_db.setDescricao("Venda de " + venda.getItens_vendidos() + "Itens");
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
                Button adicionar_item = (Button) findViewById(R.id.bt_adicionar_producto);
                adicionar_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       AlertDialog.Builder builder =new AlertDialog.Builder(Venda_Activity.this);
                       builder.setMessage("Selecione o item a venda");
                       final ListView listView =(ListView) view.findViewById(R.id.lv_adicionar);
                        builder.create().show();

                    }
                });
            }finally {

        }

        }
    public class Adapter_Venda extends BaseAdapter implements ListAdapter {
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Conta> contas = realm.where(Conta.class).findAll();

        @Override
        public int getCount() {
            return contas.get(0).getStock().size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = getLayoutInflater().inflate(R.layout.vender_item, null);

            TextView nome_item = (TextView) view.findViewById(R.id.tv_vender_nome);
            TextView itensdispo = (TextView) view.findViewById(R.id.tv_item_dispo);
            TextView itens = (TextView) view.findViewById(R.id.tv_item_venda_item);
            TextView preco = (TextView) view.findViewById(R.id.tv_venda_item_precoun);
            final Button vender = (Button) view.findViewById(R.id.bt_item_vendr);
            nome_item.setText(contas.get(0).getStock().get(i).getNome_Item());
            itens.setText(contas.get(0).getStock().get(i).getNum_item());
            itensdispo.setText(contas.get(0).getStock().get(i).getItens_disponiveis());
            preco.setText(contas.get(0).getStock().get(i).getPrecoUnidade() + "MZN");
            vender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int quantidadew;
                    AlertDialog.Builder builder = new AlertDialog.Builder(Venda_Activity.this).setTitle("QUANDIDADE").setMessage("Escreva A Quantidade" +
                            " que deseja vender");
                    final EditText quantidade = new EditText(Venda_Activity.this);
                    quantidade.setHint("0");
                    builder.setView(quantidade);
                    quantidadew = Integer.parseInt(quantidade.getText().toString());
                    builder.setPositiveButton("Vender", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (quantidadew <= 0) {
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(Venda_Activity.this);
                                builder1.setMessage("Nao pode Adicionar Productos com valores nulos!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).create().show();
                            } else {
                                venderItem(i, contas.get(0).getStock().get(i).getUnidade_de_Medida(), quantidadew);

                            }
                        }
                    }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).create().show();

                }
            });
            return view;

        }

        public void venderItem(int i, String medida, int numunidade) {
            double valor = contas.get(0).getStock().get(i).getPrecoUnidade();
            if (medida.equals("kg") || medida.equals("KG")) {
                valor = valor * numunidade;
            }
            if (medida.equals("UNIDADE") || medida.equals("unidade")) {
                valor = valor * numunidade;
            } else {
                valor = valor * numunidade;
            }
            venda.setItems(contas.get(0).getStock().get(i), numunidade, valor);
        }
    }


}

