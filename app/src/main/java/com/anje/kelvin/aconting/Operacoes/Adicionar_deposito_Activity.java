package com.anje.kelvin.aconting.Operacoes;

import android.app.Activity;
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
import com.anje.kelvin.aconting.Fragments.MenuFragment;
import com.anje.kelvin.aconting.MainActivity;
import com.anje.kelvin.aconting.R;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class Adicionar_deposito_Activity extends AppCompatActivity {
    String recorrencia="Nenhuma";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_deposito_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final EditText descricao,valor,datainicio;
        final ImageView data_inicio,data_fim_iv;
        final Button salvaar;
        descricao=(EditText) findViewById(R.id.et_descricao_despodito);
       valor=(EditText) findViewById(R.id.et_valor_deposito);
       datainicio=(EditText) findViewById(R.id.et_data_inicio_despesa);

       salvaar=(Button) findViewById(R.id.bt_salvar);
        salvaar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Realm realm=Realm.getDefaultInstance();
                try {
                    RealmResults<Conta> contas=realm.where(Conta.class).findAll();
                    Deposito_db deposito_dp=new Deposito_db();
                    deposito_dp.setDescricao(descricao.getText().toString());
                    deposito_dp.setCategoria("Deposito");
                    deposito_dp.setDia(new Date());
                    deposito_dp.setRecorrencia("Nenhuma");
                    if(valor.getText().toString().equals("")){
                        AlertDialog.Builder builder=new AlertDialog.Builder(Adicionar_deposito_Activity.this);
                        builder.setTitle("Aviso").setMessage("Nao pode Adicionar depositos com Valores nulos").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });

                        builder.show();

                    }else {
                        deposito_dp.setValor(Double.parseDouble(valor.getText().toString()));
                        if (recorrencia.equals("Fixa")) {
                            deposito_dp.setDia_fim(new Date());
                        }
                        realm.beginTransaction();
                        contas.get(0).setDeposito_dbs(deposito_dp);
                        realm.commitTransaction();
                        AlertDialog.Builder sair=new AlertDialog.Builder(Adicionar_deposito_Activity.this);
                        sair.setMessage("Deposito efectuado Com Sucesso!").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent=new Intent(Adicionar_deposito_Activity.this,MainActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        });
                        sair.show();

                    }
                }finally {
                    realm.close();
                }


            }
        });




    }
    public static void reiniciar(Activity activity){
        activity.recreate();
    }


}
