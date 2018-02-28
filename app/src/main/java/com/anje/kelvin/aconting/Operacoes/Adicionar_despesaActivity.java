package com.anje.kelvin.aconting.Operacoes;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.BaseDeDados.Deposito_db;
import com.anje.kelvin.aconting.BaseDeDados.Despesa_db;
import com.anje.kelvin.aconting.MainActivity;
import com.anje.kelvin.aconting.R;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class Adicionar_despesaActivity extends AppCompatActivity {
    String recorrencia="Nenhuma";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_despesa);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final TextView data_fim_tv;
        final EditText descricao,valor,datainicio,datafim;
        final RadioButton fixa,nehhuma;
        final ImageView data_inicio,data_fim_iv;
        final Button salvaar;
        data_fim_tv =(TextView) findViewById(R.id.tv_data_fim);
        data_fim_tv.setVisibility(View.GONE);
        data_fim_iv=(ImageView) findViewById(R.id.iv_data_fim_despesa);
        data_fim_iv.setVisibility(View.GONE);
        datafim=(EditText) findViewById(R.id.et_data_fim_despesa);
        datafim.setVisibility(View.GONE);
        descricao=(EditText) findViewById(R.id.et_descricao_despesa);
        fixa=(RadioButton) findViewById(R.id.rb_despesa_fixa);
        nehhuma=(RadioButton) findViewById(R.id.rb_nenhuma);
       nehhuma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recorrencia="Nenhuma";
                data_fim_tv.setVisibility(View.GONE);
                data_fim_iv.setVisibility(View.GONE);
                datafim.setVisibility(View.GONE);

            }
        });
        fixa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recorrencia="Fixa";
                data_fim_tv.setVisibility(View.VISIBLE);
                data_fim_iv.setVisibility(View.VISIBLE);
                datafim.setVisibility(View.VISIBLE);

            }
        });
        valor=(EditText) findViewById(R.id.et_despesa_valor);
        datainicio=(EditText) findViewById(R.id.et_data_inicio_despesa);
        salvaar=(Button) findViewById(R.id.bt_salvar_despesa);
        salvaar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Realm realm=Realm.getDefaultInstance();
                try {

                    RealmResults<Conta> contas=realm.where(Conta.class).findAll();
                    Despesa_db despesa_db=new Despesa_db();
                    if (descricao.getText().toString().equals("")){
                        AlertDialog.Builder despesa=new AlertDialog.Builder(Adicionar_despesaActivity.this);
                        despesa.setMessage("Nao pode Adicionar despesa sem Descricao").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        despesa.show();
                    }else {
                        despesa_db.setDescricao(descricao.getText().toString());
                    }
                    despesa_db.setCategoria("Deposito");
                    despesa_db.setDia(new Date());
                    despesa_db.setRecorrencia(recorrencia);
                    if(valor.getText().toString().equals("")){
                        AlertDialog.Builder valor_d=new AlertDialog.Builder(Adicionar_despesaActivity.this);
                        valor_d.setMessage("Nao pode Adicionar despesa sem Descricao").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        valor_d.show();
                    }
                    else{
                        despesa_db.setValor(Double.parseDouble(valor.getText().toString()));
                    }

                    if (recorrencia.equals("Fixa")){

                    }
                    realm.beginTransaction();
                    contas.get(0).setDespesa_dbs(despesa_db);
                    realm.commitTransaction();
                    AlertDialog.Builder sair=new AlertDialog.Builder(Adicionar_despesaActivity.this);
                    sair.setMessage("Deposito efectuado Com Sucesso!").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent intent=new Intent(Adicionar_despesaActivity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                    sair.show();
                }finally {
                    realm.close();
                }

            }
        });
    }

}
