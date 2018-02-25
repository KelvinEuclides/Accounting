package com.anje.kelvin.aconting.Relatorios;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.anje.kelvin.aconting.Adapters.AdapterDespesa;
import com.anje.kelvin.aconting.Adapters.AdapterTransicoes;
import com.anje.kelvin.aconting.Adapters.ReDespesa;
import com.anje.kelvin.aconting.Adapters.Transacao_itens;
import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class Relatorio_de_Despesas_Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    public List<ReDespesa> lista;
    double total;
    TextView datainicio,datafim;
    TextView saldo;
    Date hoje =new Date();
    Date diainicial=new Date();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_de_despesas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        datainicio= (TextView) findViewById(R.id.tv_id_re_despesas_datainicio);
        datafim=(TextView) findViewById(R.id.tv_id_re_despesas_data_fim);
        saldo=findViewById(R.id.tv_id_total_despesas);
        saldo.setText(total+" mzn");
        recyclerView = (RecyclerView) findViewById(R.id.rv_redespesass);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        lista =new ArrayList<ReDespesa>();

        try {
            Realm realm = Realm.getDefaultInstance();
            Conta conta = realm.where(Conta.class).equalTo("loggado",true).findFirst();
            if (conta.getDespesa_dbs().size()>0){
                for (int i=0;i<conta.getDespesa_dbs().size();i++){
                    ReDespesa reDespesa =new ReDespesa(conta.getDespesa_dbs().get(i).getDescricao(),conta.getDespesa_dbs().get(i).getValor()+"",conta.getDespesa_dbs().get(i).getDia());

                }
                total=conta.getDespesa_dbs().sum("Valor").doubleValue();
            }

        }finally {

        }
        adapter = new AdapterDespesa(lista,Relatorio_de_Despesas_Activity.this);
        recyclerView.setAdapter(adapter);
    }

}
