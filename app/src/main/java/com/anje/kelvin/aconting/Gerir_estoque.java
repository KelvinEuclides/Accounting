package com.anje.kelvin.aconting;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
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
import android.widget.FrameLayout;
import android.widget.RadioGroup;

import com.anje.kelvin.aconting.Adapters.AdapterObjects.Stock;
import com.anje.kelvin.aconting.Adapters.RecyclerVIewAdapter.AdapterStock;
import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.BaseDeDados.Despesa_db;
import com.anje.kelvin.aconting.BaseDeDados.Item;
import com.anje.kelvin.aconting.BaseDeDados.Transacao_db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;


public class Gerir_estoque extends AppCompatActivity {
    Button adicionar;
    private RecyclerView.Adapter adapter;
    public List<Stock> lista;
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
                FrameLayout f1=(FrameLayout)findViewById(R.id.frame_add);
                AlertDialog.Builder builder=new AlertDialog.Builder(Gerir_estoque.this);
                LayoutInflater inflater=getLayoutInflater();
                builder.setTitle("Adicionar item Ao Estoque").setPositiveButton("Adicionar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Realm realm=Realm.getDefaultInstance();
                        Conta conta=realm.where(Conta.class).equalTo("loggado",true).findFirst();
                        EditText descricao=(EditText) findViewById(R.id.et_nome_item);
                        EditText preco=(EditText) findViewById(R.id.et_item_preco);
                        RadioGroup unidades=(RadioGroup) findViewById(R.id.rg_unidade_medida);
                        EditText precovenda=(EditText) findViewById(R.id.item_preco_venda);
                        EditText quantidade=(EditText) findViewById(R.id.et_item_quantidade);
                        Item item=new Item();
                        item.setNome_Item("Agua");
                        item.setUnidade_de_Medida("Unidade");
                        item.setId_usuario(conta.getId_usuario());
                        item.setNum_item(12);
                        item.setItens_disponiveis(12);
                        item.setPreco(50.0);
                        item.setPrecoUnidade(100.0);
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
                        despesa_db.setDia(new Date());
                        Transacao_db transacao_db=new Transacao_db();
                        transacao_db.setValor(despesa_db.getValor());
                        transacao_db.setDescricao(despesa_db.getDescricao());
                        transacao_db.setDia(despesa_db.getDia());
                        transacao_db.setCategoria("Compra");
                        realm.beginTransaction();
                        conta.adicionar_item(550);
                        realm.copyToRealm(item);
                        realm.copyToRealm(despesa_db);
                        realm.copyToRealm(transacao_db);
                        realm.commitTransaction();
                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setView(inflater.inflate(R.layout.fragment_add,f1,false));
                builder.create().show();
            }
        });

        try {
            Conta conta = realm.where(Conta.class).equalTo("loggado",true).findFirst();
            List<Item> item=realm.where(Item.class).equalTo("id_usuario",conta.getId_usuario()).findAll();
            for (int i = 0; i < item.size(); i++) {
                Stock stock=new Stock(item.get(i).getNome_Item()+"",item.get(i).getNum_item()+"",
                        item.get(i).getItens_disponiveis()+"",item.get(i).getPreco()+"Mzn");
                if(item.get(i).getNum_item()<5){
                    Notification mBuilder = new NotificationCompat.Builder(this)
                            .setSmallIcon(R.drawable.conta_laranja)
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
