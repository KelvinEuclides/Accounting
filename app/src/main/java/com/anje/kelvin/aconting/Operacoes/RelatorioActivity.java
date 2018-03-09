package com.anje.kelvin.aconting.Operacoes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.anje.kelvin.aconting.R;
import com.anje.kelvin.aconting.Relatorios.Relatorio_de_Transicoes_Activity;
import com.anje.kelvin.aconting.Relatorios.Relatorio_de_Despesas_Activity;
import com.anje.kelvin.aconting.Relatorios.Relatorio_de_renda_Activity;

import java.util.Date;
public class RelatorioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Date date=new Date();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       CardView relatorio =(CardView) findViewById(R.id.relatorios);
        relatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RelatorioActivity.this, Relatorio_de_Transicoes_Activity.class);
                startActivity(intent);
            }
        });
        CardView despesa =(CardView) findViewById(R.id.reladorio_de_despesa_diaria);
        despesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RelatorioActivity.this, Relatorio_de_Despesas_Activity.class);
                startActivity(intent);
            }
        });
        CardView renda=(CardView) findViewById(R.id.relatorio_de_renda);
        renda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RelatorioActivity.this,Relatorio_de_renda_Activity.class);
                startActivity(intent);
            }
        });


    }

}
