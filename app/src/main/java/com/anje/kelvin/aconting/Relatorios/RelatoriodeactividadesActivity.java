package com.anje.kelvin.aconting.Relatorios;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.anje.kelvin.aconting.Adapters.AdapterTransicoes;
import com.anje.kelvin.aconting.Adapters.Transacao_itens;
import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.R;
import com.anje.kelvin.aconting.Transicoes_Activity;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class RelatoriodeactividadesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    public List<Transacao_itens> lista;
    private double total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relatoriodeactividades);
        recyclerView = (RecyclerView) findViewById(R.id.rv_re_actividades);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        lista = new ArrayList<Transacao_itens>();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<Conta> contas = realm.where(Conta.class).findAll();
        if (contas.get(0).getStock().size() > 0) {
            for (int i = 0; i < contas.get(0).getTransacaoDbs().size(); i++) {
                Conta conta = contas.get(0);
                Transacao_itens transacao = new Transacao_itens(conta.getTransacaoDbs().get(i).getDescricao(), conta.getTransacaoDbs().get(i).getCategoria(), conta.getTransacaoDbs().get(i).getValor());
                total=total+conta.getTransacaoDbs().get(i).getValor();
                lista.add(transacao);
            }

        }
        TextView totale=(TextView) findViewById(R.id.tv_rda_total);
        totale.setText(total+"");

        adapter = new AdapterTransicoes(lista,RelatoriodeactividadesActivity.this);
        recyclerView.setAdapter(adapter);
    }
}
