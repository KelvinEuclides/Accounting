package com.anje.kelvin.aconting;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.app.LoaderManager.LoaderCallbacks;

import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.anje.kelvin.aconting.BaseDeDados.Conta;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via email/password.
 */
public class RegistrarActivity extends AppCompatActivity  {

     EditText telemovel,pin;
    Button registrar;
    Conta conta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        setupActionBar();
          pin=(EditText) findViewById(R.id.et_reg_pin);
          telemovel=(EditText) findViewById(R.id.et_numero_telefone);
          pin=(EditText) findViewById(R.id.et_reg_pi2);
          registrar=(Button) findViewById(R.id.bt_registrarr);



        registrar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                conta = new Conta();
                conta.setLoggado(true);
                conta.setPin(Integer.parseInt(pin.getText().toString()));
                conta.setTelemovel(Integer.parseInt(telemovel.getText().toString()));
                conta.setSaldo_conta(0);
                conta.setNomeConta("Conta");
                Realm realm=Realm.getDefaultInstance();
                realm.beginTransaction();
                realm.copyToRealm(conta);
                realm.commitTransaction();
                Intent itent =new Intent(RegistrarActivity.this,MainActivity.class);
                startActivity(itent);
                finish();
            }
        });

    }

    private void setupActionBar() {
    }
}

