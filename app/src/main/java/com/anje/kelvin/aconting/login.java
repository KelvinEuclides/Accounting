package com.anje.kelvin.aconting;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.anje.kelvin.aconting.BaseDeDados.Conta;

import io.realm.Realm;

/**
 * Created by sala on 02-03-2018.
 */

public class login extends AppCompatActivity {
    Button bt_login;
    TextView registrar;
    EditText telemovel,pin;
    int pink=0000;
    int telk=842270587;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Realm realm= Realm.getDefaultInstance();
        if(realm.where(Conta.class).equalTo("loggado",true).findFirst() != null){
            Intent intent=new Intent(login.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        registrar=(TextView) findViewById(R.id.tv_reguistre_se);
        bt_login =(Button) findViewById(R.id.bt_login);
        telemovel=(EditText) findViewById(R.id.et_telefone_login);
         pin=(EditText) findViewById(R.id.et_pin);

        bt_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pink=Integer.parseInt(pin.getText().toString());
                telk=Integer.parseInt(telemovel.getText().toString());
                Realm realm= Realm.getDefaultInstance();
                try{
                    Conta conta=realm.where(Conta.class).equalTo("telemovel",telk).equalTo("pin",pink).findFirst();
                    if (telk>0 ){
                        realm.beginTransaction();
                        conta.setLoggado(true);
                        realm.commitTransaction();
                        Intent intent=new Intent(login.this,MainActivity.class);
                        startActivity(intent);
                    }else {
                        Snackbar.make(view, "Password Ou Numero de Telemovel Errados", Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();}
                }catch (Exception e){

                }


            }});


        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(login.this,RegistrarActivity.class);
                startActivity(intent);
            }
        });




    }
}
