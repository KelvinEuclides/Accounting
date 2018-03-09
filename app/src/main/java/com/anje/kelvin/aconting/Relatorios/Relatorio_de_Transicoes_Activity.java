package com.anje.kelvin.aconting.Relatorios;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.anje.kelvin.aconting.Adapters.AdapterObjects.Relatorio;
import com.anje.kelvin.aconting.Adapters.RecyclerVIewAdapter.AdapterRelatoriosDespesas;
import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.BaseDeDados.Transacao_db;
import com.anje.kelvin.aconting.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;

public class Relatorio_de_Transicoes_Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    public List<Relatorio> lista;
    double total;
    double v;
    TextView datainicio,datafim;
    TextView saldo;
    Date hoje =new Date();
    Date diainicial=new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_de_transicoes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        datainicio= (TextView) findViewById(R.id.tv_id_re_despesas_datainicio);
        datafim=(TextView) findViewById(R.id.tv_id_re_despesas_data_fim);
        saldo=findViewById(R.id.tv_id_total_despesas_ac);
        saldo.setText(total+" mzn");
        recyclerView = (RecyclerView) findViewById(R.id.rv_transicoees);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
          recyclerView.setHasFixedSize(false);
        lista =new ArrayList<Relatorio>();

        try {
            Realm realm = Realm.getDefaultInstance();
            Conta conta = realm.where(Conta.class).equalTo("loggado",true).findFirst();
            List<Transacao_db> item=realm.where(Transacao_db.class).equalTo("id_usuario",conta.getId_usuario()).findAll();
            if (item.size()>0){
                for (int i=0;i<item.size();i++){
                    Relatorio relatorio=new Relatorio(item.get(i).getDescricao(),"28/02/2018",item.get(i).getValor());
                    lista.add(relatorio);
                    v = v+item.get(i).getValor();

                }
                total=v;
                saldo.setText(total+" mzn");
            }

        }finally {

        }
        adapter = new AdapterRelatoriosDespesas(lista,Relatorio_de_Transicoes_Activity.this);
        recyclerView.setAdapter(adapter);




    }

}
