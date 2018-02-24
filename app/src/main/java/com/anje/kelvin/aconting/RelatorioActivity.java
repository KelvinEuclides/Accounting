package com.anje.kelvin.aconting;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.anje.kelvin.aconting.Relatorios.Relatorio_de_Despesas_Activity;
import com.anje.kelvin.aconting.Relatorios.RelatoriodeactividadesActivity;
import java.util.Date;
public class RelatorioActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Date date=new Date();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorios);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final CardView relatorio =(CardView) findViewById(R.id.relatorios);
        relatorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RelatorioActivity.this, RelatoriodeactividadesActivity.class);
                startActivity(intent);
            }
        });
        final CardView despesa =(CardView) findViewById(R.id.reladorio_de_despesa_diaria);
        despesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(RelatorioActivity.this, Relatorio_de_Despesas_Activity.class);
                startActivity(intent);
            }
        });


    }

}
