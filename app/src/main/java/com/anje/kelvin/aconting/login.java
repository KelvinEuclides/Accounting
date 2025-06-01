package com.anje.kelvin.aconting;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.ComponentActivity;
import androidx.activity.compose.setContent;
import com.google.android.material.snackbar.Snackbar;
import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.ui.LoginScreen;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class login extends ComponentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Initialize Realm
        Realm.init(this);
        RealmConfiguration realmConfig = new RealmConfiguration.Builder()
                .name("Contablilidade")
                .schemaVersion(0)
                .build();
        Realm.setDefaultConfiguration(realmConfig);
        
        // Check if user is already logged in
        Realm realm = Realm.getDefaultInstance();
        if (realm.where(Conta.class).equalTo("loggado", true).findFirst() != null) {
            Intent intent = new Intent(login.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }
        
        // Set Compose content
        setContent(
            () -> new LoginScreen(
                // Login button click handler
                (phone, pin) -> {
                    try {
                        int phoneNum = Integer.parseInt(phone);
                        int pinNum = Integer.parseInt(pin);
                        loginUser(phoneNum, pinNum);
                    } catch (NumberFormatException e) {
                        showError("Please enter valid numbers");
                    }
                    return null;
                },
                // Register button click handler
                () -> {
                    navigateToRegister();
                    return null;
                }
            )
        );
    }
    
    private void loginUser(int phone, int pin) {
        Realm realm = Realm.getDefaultInstance();
        try {
            Conta conta = realm.where(Conta.class)
                    .equalTo("telemovel", phone)
                    .equalTo("pin", pin)
                    .findFirst();
                    
            if (conta != null) {
                realm.beginTransaction();
                conta.setLoggado(true);
                realm.commitTransaction();
                
                Intent intent = new Intent(login.this, MainActivity.class);
                startActivity(intent);
                finish();
            } else {
                showError("Password Ou Numero de Telemovel Errados");
            }
        } catch (Exception e) {
            showError("An error occurred during login");
        }
    }
    
    private void navigateToRegister() {
        Intent intent = new Intent(login.this, RegistrarActivity.class);
        startActivity(intent);
    }
    
    private void showError(String message) {
        // Show error message
        Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG).show();
    }
}
