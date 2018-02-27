package com.anje.kelvin.aconting.Operacoes;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.MainActivity;
import com.anje.kelvin.aconting.R;

import io.realm.Realm;

public class Login_Activity extends AppCompatActivity {
    Button login,registrar;
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
                     try{
                         Conta conta=realm.where(Conta.class).equalTo("telemovel",telk).equalTo("pin",pink).findFirst();
                         if (telk>0 ){
                             realm.beginTransaction();
                             conta.setLoggado(true);
                             realm.commitTransaction();
                             Intent intent=new Intent(Login_Activity.this,MainActivity.class);
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
                Intent intent=new Intent(Login_Activity.this,RegistrarActivity.class);
                startActivity(intent);
            }
        });




    }

}
