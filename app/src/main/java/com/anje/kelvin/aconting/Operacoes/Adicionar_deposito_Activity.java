package com.anje.kelvin.aconting.Operacoes;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.BaseDeDados.Deposito_db;
import com.anje.kelvin.aconting.BaseDeDados.Transacao_db;
import com.anje.kelvin.aconting.Classes.Convertar_Datas;
import com.anje.kelvin.aconting.Fragments.MenuFragment;
import com.anje.kelvin.aconting.MainActivity;
import com.anje.kelvin.aconting.R;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class Adicionar_deposito_Activity extends AppCompatActivity {
    String recorrencia="Nenhuma";
    boolean cliclou=false;
    Date date = new Date();
    Date dia=new Date();
    EditText descricao,valor,datainicio;
    ImageView data_inicio,data_fim_iv;
    Button salvaar;
    Deposito_db deposito_dp = new Deposito_db();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_deposito_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        descricao=(EditText) findViewById(R.id.et_descricao_despodito);
       valor=(EditText) findViewById(R.id.et_valor_deposito);
       datainicio=(EditText) findViewById(R.id.et_data_inicio_despesa);
            final Convertar_Datas c =new Convertar_Datas();
            datainicio.setHint(c.datac(date));
            deposito_dp.setDia(new Date());



        data_inicio=(ImageView) findViewById(R.id.iv_data_inicio);
       data_inicio.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               DatePickerDialog.OnDateSetListener date=new DatePickerDialog.OnDateSetListener() {
                   @Override
                   public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                    Calendar calendar=Calendar.getInstance();
                    calendar.set(Calendar.YEAR,i);
                       calendar.set(Calendar.MONTH,i1);
                       calendar.set(Calendar.DAY_OF_MONTH,i2);


                   }
               };

           }
       });
       salvaar=(Button) findViewById(R.id.bt_salvar);
        salvaar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {


                    Realm realm = Realm.getDefaultInstance();
                    try {
                        Conta conta = realm.where(Conta.class).equalTo("loggado", true).findFirst();
                        deposito_dp.setDescricao(descricao.getText().toString());
                        deposito_dp.setId_usuario(conta.getId_usuario());
                        deposito_dp.setCategoria("Deposito");
                        deposito_dp.setRecorrencia("Nenhuma");
                        if (valor.getText().toString().equals("")) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(Adicionar_deposito_Activity.this);
                            builder.setTitle("Aviso").setMessage("Nao pode Adicionar depositos com Valores nulos").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                }
                            });

                            builder.show();

                        } else {
                            deposito_dp.setValor(Double.parseDouble(valor.getText().toString()));
                            if (recorrencia.equals("Fixa")) {
                                deposito_dp.setDia_fim(new Date());
                            }
                            Transacao_db transacao_db = new Transacao_db();
                            transacao_db.setId_usuario(deposito_dp.getId_usuario());
                            transacao_db.setDescricao(deposito_dp.getDescricao());
                            transacao_db.setDia(deposito_dp.getDia());
                            transacao_db.setValor(deposito_dp.getValor());
                            realm.beginTransaction();
                            conta.adicionar_deposito(Double.parseDouble(valor.getText().toString()));
                            realm.copyToRealm(deposito_dp);
                            realm.copyToRealm(transacao_db);
                            realm.commitTransaction();
                            AlertDialog.Builder sair = new AlertDialog.Builder(Adicionar_deposito_Activity.this);
                            sair.setMessage("Deposito efectuado Com Sucesso!").setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent(Adicionar_deposito_Activity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                            sair.show();

                        }
                    } finally {
                        realm.close();
                    }

                }catch (Exception e){

                }
            }
        });




    }
    public static void reiniciar(Activity activity){
        activity.recreate();
    }


}
