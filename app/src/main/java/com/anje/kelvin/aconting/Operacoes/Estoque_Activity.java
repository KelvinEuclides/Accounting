package com.anje.kelvin.aconting.Operacoes;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.anje.kelvin.aconting.Adapters.RecyclerVIewAdapter.AdapterStock;
import com.anje.kelvin.aconting.Adapters.AdapterObjects.Stock;
import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.BaseDeDados.Item;
import com.anje.kelvin.aconting.R;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class Estoque_Activity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    public List<Stock> lista;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estoque);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rv_item_stock);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        lista=new ArrayList<Stock>();
        Realm realm = Realm.getDefaultInstance();
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
        adapter = new AdapterStock(lista,Estoque_Activity.this);
        recyclerView.setAdapter(adapter);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Estoque_Activity.this,Add_item_Activity.class);
                startActivity(intent);
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
