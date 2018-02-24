package com.anje.kelvin.aconting;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.R;

import io.realm.Realm;
import io.realm.RealmResults;

public class Login_Activity extends AppCompatActivity {
    Button login,registrar;
    int pink=0;
    int telk=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Realm realm= Realm.getDefaultInstance();
        if(realm.where(Conta.class).equalTo("loggado",true).findFirst() != null){
            Intent intent=new Intent(Login_Activity.this,MainActivity.class);
            startActivity(intent);
            finish();
        }
        registrar=(Button) findViewById(R.id.bt_registre_se);
        login=(Button) findViewById(R.id.bt_login);
        EditText telemovel=(EditText) findViewById(R.id.et_telefone_login);
        EditText pin=(EditText) findViewById(R.id.et_pin);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Realm realm= Realm.getDefaultInstance();
                     if (pink>0 && telk>0){
                         Conta conta=realm.where(Conta.class).equalTo("telemovel",telk).equalTo("pin",pink).findFirst();
                         if (conta!=null){
                             realm.beginTransaction();
                             conta.setLoggado(true);
                             realm.commitTransaction();
                             Intent intent=new Intent(Login_Activity.this,MainActivity.class);
                             startActivity(intent);
                         }else {
                             Toast toast=Toast.makeText(Login_Activity.this,"Telemovel ou Pin Errado",Toast.LENGTH_LONG);
                         }

                     }}});


        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login_Activity.this,RegistrarActivity.class);
                startActivity(intent);
            }
        });




    }

}
