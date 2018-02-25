package com.anje.kelvin.aconting;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NotificationCompat;
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
            Conta contas = realm.where(Conta.class).findFirst();
            if (contas.getStock().size() > 0) {
                for (int i = 0; i < contas.getStock().size(); i++) {

                    Conta conta = contas;
                    if(conta.getStock().get(i).getNum_item()<5){
                        Notification mBuilder = new NotificationCompat.Builder(this)
                                .setSmallIcon(R.drawable.conta_laranja)
                                .setContentTitle("Alerta")
                                .setContentText("Tem menos que")
                                .setPriority(NotificationCompat.PRIORITY_HIGH)
                                .setCategory(NotificationCompat.CATEGORY_REMINDER).build();
                        NotificationManager notificationManager =
                                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

                        notificationManager.notify(0, mBuilder);

                    }
                   Stock stock=new Stock(contas.getStock().get(i).getNome_Item()+"",contas.getStock().get(i).getNum_item()+"",
                           contas.getStock().get(i).getItens_disponiveis()+"",contas.getStock().get(i).getPreco()+"Mzn");
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
                Intent intent=new Intent(item_stock_Activity.this,Add_item_Activity.class);
                startActivity(intent);
            }
        });

    }

}
