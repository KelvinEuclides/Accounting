package com.anje.kelvin.aconting.Relatorios;

import android.annotation.SuppressLint;
import android.icu.text.DateFormat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.anje.kelvin.aconting.Adapters.ViewPAgerAdapter.AdapterTransicoes;
import com.anje.kelvin.aconting.Adapters.AdapterObjects.Transacao_itens;
import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.BaseDeDados.Receita;
import com.anje.kelvin.aconting.BaseDeDados.Transacao_db;
import com.anje.kelvin.aconting.BaseDeDados.Venda;
import com.anje.kelvin.aconting.Classes.Convertar_Datas;
import com.anje.kelvin.aconting.R;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import io.realm.Realm;
public class RelatoriodeactividadesActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    public List<Transacao_itens> lista;
    Date hoje=new Date();
    Date d=new Date((hoje.getTime()-24*24*600*1000)-6*24*3600*1000);
    TextView diainicio,diafim;
    private double total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        recyclerView = (RecyclerView) findViewById(R.id.rv_re_actividades);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        lista = new ArrayList<Transacao_itens>();
        Realm realm = Realm.getDefaultInstance();
        Conta conta = realm.where(Conta.class).equalTo("loggado",true).findFirst();
        List<Transacao_db>  items=realm.where(Transacao_db.class).equalTo("id_usuario",conta.getId_usuario()).findAll();
        diafim=(TextView) findViewById(R.id.tv_id_re_despesas_data_fim);
        diainicio=(TextView) findViewById(R.id.tv_id_re_despesas_datainicio);
        Convertar_Datas c=new Convertar_Datas();

        List<Venda> receita=realm.where(Venda.class).findAll();
        if (receita.size() > 0) {
            for (int i = 0; i < items.size(); i++) {

                Transacao_itens transacao = new Transacao_itens("Venda De "+receita.get(i).getItens_vendidos()+items,"venda",receita.get(i).getValor(),receita.get(i).getData());
                Realm realm1=Realm.getDefaultInstance();
                total=total+receita.get(i).getValor();
                lista.add(transacao);

            }

        }
        TextView totale=(TextView) findViewById(R.id.tv_id_total_despesas_ac);
        totale.setText(total+"");

        adapter = new AdapterTransicoes(lista,RelatoriodeactividadesActivity.this);
        recyclerView.setAdapter(adapter);
        Button button = (Button) findViewById(R.id.bt_relatorios_transacoes_exportar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(RelatoriodeactividadesActivity.this);
                builder.setMessage("Relatorio de venda gerado!").create().show();
            }
        });
    }
}
