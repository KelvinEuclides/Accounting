package com.anje.kelvin.aconting.Relatorios;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.anje.kelvin.aconting.Adapters.ViewPAgerAdapter.AdapterTransicoes;
import com.anje.kelvin.aconting.Adapters.AdapterObjects.Transacao_itens;
import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.R;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;


public class RelatoriodeDespesas extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    public List<Transacao_itens> lista;
    double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatoriode_despesas);
        recyclerView = (RecyclerView) findViewById(R.id.rv_re_actividades);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        lista = new ArrayList<Transacao_itens>();
        /**Realm realm = Realm.getDefaultInstance();
        RealmResults<Conta> contas = realm.where(Conta.class).findAll();
        if (contas.get(0).getStock().size() > 0) {
            for (int i = 0; i < contas.get(0).getTransacaoDbs().size(); i++) {
                Conta conta = contas.get(0);
                Transacao_itens transacao = new Transacao_itens(conta.getDespesa_dbs().get(i).getDescricao(), conta.getDespesa_dbs().get(i).getCategoria(), conta.getDespesa_dbs().get(i).getValor());
                total=total+conta.getTransacaoDbs().get(i).getValor();
                lista.add(transacao);
            }

        }
        TextView totale=(TextView) findViewById(R.id.tv_rda_total);
        totale.setText(total+" MZN");

        adapter = new AdapterTransicoes(lista,RelatoriodeDespesas.this);
        recyclerView.setAdapter(adapter);**/
    }
}
