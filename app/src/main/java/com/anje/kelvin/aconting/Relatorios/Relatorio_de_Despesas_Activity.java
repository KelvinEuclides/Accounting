package com.anje.kelvin.aconting.Relatorios;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.anje.kelvin.aconting.Adapters.AdapterDespesa;
import com.anje.kelvin.aconting.Adapters.AdapterTransicoes;
import com.anje.kelvin.aconting.Adapters.ReDespesa;
import com.anje.kelvin.aconting.Adapters.Transacao_itens;
import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.R;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class Relatorio_de_Despesas_Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    public List<ReDespesa> lista;
    double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_de_despesas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView = (RecyclerView) findViewById(R.id.rv_redespesa);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        lista =new ArrayList<ReDespesa>();
        try {
            Realm realm = Realm.getDefaultInstance();
            RealmResults<Conta> contas = realm.where(Conta.class).findAll();
            if (contas.get(0).getDespesa_dbs().size()>0){
                for (int i=0;i<contas.get(0).getDespesa_dbs().size();i++);
                int s =contas.get(0).getDespesa_dbs().size();
                ReDespesa reDespesa =new ReDespesa(contas.get(0).getDespesa_dbs().get(s-1).getDescricao(),contas.get(0).getDespesa_dbs().get(s-1).getValor()+"",contas.get(0).getDespesa_dbs().get(s-1).getDia());
                total=total+contas.get(0).getDespesa_dbs().get(s-1).getValor();
            }

        }finally {

        }
        adapter = new AdapterDespesa(lista,Relatorio_de_Despesas_Activity.this);
        recyclerView.setAdapter(adapter);
    }

}
