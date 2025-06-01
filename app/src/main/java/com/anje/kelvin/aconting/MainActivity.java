package com.anje.kelvin.aconting;

import android.os.Bundle;
import androidx.activity.ComponentActivity;
import androidx.activity.compose.setContent;
import com.anje.kelvin.aconting.ui.PakitiniApp;

public class MainActivity extends ComponentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContent(() -> new PakitiniApp());
    }
}
