package com.anje.kelvin.aconting.Relatorios;

import android.annotation.SuppressLint;
import android.icu.text.DateFormat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.anje.kelvin.aconting.Adapters.AdapterObjects.Relatorio;
import com.anje.kelvin.aconting.Adapters.RecyclerVIewAdapter.AdapterRelatoriosDespesas;
import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.BaseDeDados.Despesa_db;
import com.anje.kelvin.aconting.BaseDeDados.Item;
import com.anje.kelvin.aconting.R;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;

public class Relatorio_de_Despesas_Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    public List<Relatorio> lista;
    double total;
    double v;
    TextView datainicio,datafim;
    TextView saldo;
    Date hoje =new Date();
    Date diainicial=new Date();

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_de_despesas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Date date=new Date();
        Date d=new Date((date.getTime()-24*24*600*1000)-6*24*3600*1000);
        setSupportActionBar(toolbar);
        datainicio= (TextView) findViewById(R.id.tv_id_re_despesas_datainicio);
        datafim=(TextView) findViewById(R.id.tv_id_re_despesas_data_fim);

        try {
            datainicio.setText(DateFormat.getDateInstance().format(d));
            datafim.setText(DateFormat.getDateInstance().format(hoje));
        }catch (RuntimeException e){

        }


        saldo=findViewById(R.id.tv_id_total_despesas_ac);
        saldo.setText(total+" mzn");
        recyclerView = (RecyclerView) findViewById(R.id.rv_rrelatorio);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        lista =new ArrayList<Relatorio>();

      try {
            Realm realm = Realm.getDefaultInstance();
            Conta conta = realm.where(Conta.class).equalTo("loggado",true).findFirst();
            List<Despesa_db> despesa_dbs=realm.where(Despesa_db.class).equalTo("id_usuario",conta.getId_usuario()).between("dia",d,date).findAll();

            if (despesa_dbs.size()>0){
                for (int i=0;i<despesa_dbs.size();i++){
                   Relatorio relatorio=new Relatorio(despesa_dbs.get(i).getDescricao(),DateFormat.getDateInstance().format(despesa_dbs.get(i).getDia()),despesa_dbs.get(i).getValor());
                   lista.add(relatorio);
                    v = v+despesa_dbs.get(i).getValor();

                }
                total=v;
                saldo.setText(total+" mzn");
            }

        }finally {

        }
        adapter = new AdapterRelatoriosDespesas(lista,Relatorio_de_Despesas_Activity.this);
        recyclerView.setAdapter(adapter);
    }

}
