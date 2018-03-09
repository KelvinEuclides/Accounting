package com.anje.kelvin.aconting.Operacoes;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import com.anje.kelvin.aconting.Adapters.AdapterObjects.Stock;
import com.anje.kelvin.aconting.Adapters.RecyclerVIewAdapter.AdapterStock;
import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.BaseDeDados.Despesa_db;
import com.anje.kelvin.aconting.BaseDeDados.Item;
import com.anje.kelvin.aconting.BaseDeDados.Transacao_db;
import com.anje.kelvin.aconting.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;


public class Gerir_estoque extends AppCompatActivity {
    Button adicionar;
    private RecyclerView.Adapter adapter;
    public List<Stock> lista;
    EditText descricao,quantidade,preco,precovenda;
    RadioGroup unidades;
    Button adiconar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gerir_estoque);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_item_stock);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        lista=new ArrayList<Stock>();
        Realm realm=Realm.getDefaultInstance();
        adicionar =(Button) findViewById(R.id.add_item_stock);
        adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog builder=new Dialog(Gerir_estoque.this);
                final LayoutInflater inflater=getLayoutInflater();
                builder.setTitle("Adicionar item Ao Estoque");
                builder.setCancelable(true);
                builder.setContentView(R.layout.fragment_add);

                final Realm realm=Realm.getDefaultInstance();
                Conta conta=realm.where(Conta.class).equalTo("loggado",true).findFirst();
                descricao=(EditText) builder.findViewById(R.id.et_nome_item);
                preco=(EditText) builder.findViewById(R.id.et_item_preco);
                unidades=(RadioGroup) builder.findViewById(R.id.rg_unidade_medida);
                precovenda=(EditText) builder.findViewById(R.id.item_preco_venda);
                quantidade=(EditText) builder.findViewById(R.id.et_item_quantidade);
                adiconar=(Button) builder.findViewById(R.id.bt_vender);
                adiconar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Realm realm=Realm.getDefaultInstance();
                        Conta conta = realm.where(Conta.class).equalTo("loggado",true).findFirst();
                        Item item=new Item();
                        item.setNome_Item(descricao.getText().toString());
                        item.setUnidade_de_Medida("Unidade");
                        item.setId_usuario(conta.getId_usuario());
                        item.setNum_item(Integer.parseInt(quantidade.getText().toString()));
                        item.setItens_disponiveis(Integer.parseInt(quantidade.getText().toString()));
                        item.setPreco(Double.parseDouble(preco.getText().toString()));
                        item.setPrecoUnidade(Double.parseDouble(precovenda.getText().toString()));
                        Despesa_db despesa_db=new Despesa_db();
                        despesa_db.setCategoria("Compra");
                        String medida;
                        if (item.getUnidade_de_Medida().equals("Kg")){
                            medida="kilogramas";
                        }
                        if (item.getUnidade_de_Medida().equals("Litros")){
                            medida="litros";
                        }
                        else{
                            medida="Unidades";
                        }
                        despesa_db.setDescricao("Compra de "+item.getNum_item()+" "+medida+" de "+item.getNome_Item());
                        despesa_db.setValor(item.getPreco());
                        despesa_db.setId_usuario(conta.getId_usuario());
                        despesa_db.setDia(new Date());
                        Transacao_db transacao_db=new Transacao_db();
                        transacao_db.setValor(despesa_db.getValor());
                        transacao_db.setDescricao(despesa_db.getDescricao());
                        transacao_db.setDia(despesa_db.getDia());
                        transacao_db.setCategoria("Compra");
                        realm.beginTransaction();
                        conta.adicionar_item(Double.parseDouble(preco.getText().toString()));
                        realm.copyToRealm(item);
                        realm.copyToRealm(despesa_db);
                        realm.copyToRealm(transacao_db);
                        realm.commitTransaction();
                        Intent intent=getIntent();
                        finish();
                        startActivity(intent);
                    }
                });
                builder.show();




            }
        });

        try {
            Conta conta = realm.where(Conta.class).equalTo("loggado",true).findFirst();
            List<Item> item=realm.where(Item.class).equalTo("id_usuario",conta.getId_usuario()).findAll();
            for (int i = 0; i < item.size(); i++) {
                Stock stock=new Stock(item.get(i).getNome_Item()+"",item.get(i).getNum_item()+"",
                        item.get(i).getItens_disponiveis()+"",item.get(i).getPrecoUnidade()+"Mzn");
                if(item.get(i).getNum_item()<5){
                    Notification mBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.conta_azul)
                            .setContentTitle("Alerta")
                            .setContentText("Tem menos que 5 Unidades de  "+item.get(i).getNome_Item())
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_REMINDER).build();
                    NotificationManager notificationManager =
                            (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                    notificationManager.notify(0, mBuilder);

                }
                lista.add(stock);
            }

        }finally {

        }
        adapter = new AdapterStock(lista,Gerir_estoque.this);
        recyclerView.setAdapter(adapter);



        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
