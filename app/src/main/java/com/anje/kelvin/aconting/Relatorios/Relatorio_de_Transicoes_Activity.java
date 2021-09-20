package com.anje.kelvin.aconting.Relatorios;

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.anje.kelvin.aconting.Adapters.AdapterObjects.Relatorio;
import com.anje.kelvin.aconting.Adapters.RecyclerVIewAdapter.AdapterRelatoriosDespesas;
import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.BaseDeDados.Transacao_db;
import com.anje.kelvin.aconting.Classes.Convertar_Datas;
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
    Date d=new Date((hoje.getTime()-24*24*600*1000)-6*24*3600*1000);
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_de_transicoes);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        datainicio= (TextView) findViewById(R.id.tv_id_re_despesas_datainicio);
        datafim=(TextView) findViewById(R.id.tv_id_re_despesas_data_fim);
        Convertar_Datas c=new Convertar_Datas();
        datainicio.setText(c.datac(d));
        datafim.setText(c.datac(hoje));
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
            List<Transacao_db> transacao =realm.where(Transacao_db.class).findAll();

            if (transacao.size()>0 ){
                for (int i=0;i<transacao.size();i++){
                    Transacao_db item1=transacao.get(i);
                    Relatorio relatorio=new Relatorio(item1.getDescricao(),item1.getDia(),item1.getValor());
                    lista.add(relatorio);
                    v = v+transacao.get(i).getValor();

                }
                total=v;
                saldo.setText(total+" mzn");
            }

        }finally {

        }
        adapter = new AdapterRelatoriosDespesas(lista,Relatorio_de_Transicoes_Activity.this);
        recyclerView.setAdapter(adapter);
        button=(Button) findViewById(R.id.bt_relatorios_transacoes_exportar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(Relatorio_de_Transicoes_Activity.this);
                builder.setMessage("Relatorio de venda gerado!").create().show();
            }
        });



    }

}
