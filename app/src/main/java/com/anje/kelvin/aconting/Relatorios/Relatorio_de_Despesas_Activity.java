package com.anje.kelvin.aconting.Relatorios;

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import com.anje.kelvin.aconting.Adapters.AdapterObjects.Relatorio;
import com.anje.kelvin.aconting.Adapters.RecyclerVIewAdapter.AdapterRelatoriosDespesas;
import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.BaseDeDados.Deposito_db;
import com.anje.kelvin.aconting.BaseDeDados.Despesa_db;
import com.anje.kelvin.aconting.Classes.Convertar_Datas;
import com.anje.kelvin.aconting.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;

public class Relatorio_de_Despesas_Activity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    public List<Relatorio> lista;
    double total;
    double v;
    TextView datainicio, datafim;
    TextView saldo;
    Date hoje = new Date();
    Button button;
    Date d=new Date();
    Convertar_Datas c = new Convertar_Datas();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relatorio_de_despesas);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Spinner spinner = (Spinner) findViewById(R.id.sp_datas_relatorios);
            setSupportActionBar(toolbar);
        datainicio = (TextView) findViewById(R.id.tv_id_re_despesas_datainicio);
        saldo = findViewById(R.id.tv_id_total_despesas_ac);
        saldo.setText(total + " mzn");
        recyclerView = (RecyclerView) findViewById(R.id.rv_rrelatorio);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        lista = new ArrayList<Relatorio>();
        int k = spinner.getSelectedItemPosition();
        switch (k) {
            case 0:
                d = new Date((hoje.getTime() - 24 * 24 * 600 * 1000) - 6 * 24 * 3600 * 1000);
                datainicio.setText(c.datac(d));
                try {
                    Realm realm = Realm.getDefaultInstance();
                    Conta conta = realm.where(Conta.class).equalTo("loggado", true).findFirst();
                    List<Despesa_db> despesa_dbs = realm.where(Despesa_db.class).equalTo("id_usuario", conta.getId_usuario()).between("dia", d, hoje).findAll();
                    List<Deposito_db> deposito_dbs = realm.where(Deposito_db.class).equalTo("id_usuario", conta.getId_usuario()).between("dia", d, hoje).findAll();

                    if (despesa_dbs.size() > 0 || despesa_dbs.size() > 0) {
                        for (int i = 0; i < despesa_dbs.size(); i++) {
                            Relatorio relatorio = new Relatorio(despesa_dbs.get(i).getDescricao(), despesa_dbs.get(i).getDia(), despesa_dbs.get(i).getValor());
                            lista.add(relatorio);
                            v = v + despesa_dbs.get(i).getValor();

                        }
                        total = v;
                        saldo.setText(total + " mzn");
                    }

                } finally {

                }
                break;
            case 3:
                d = new Date(((hoje.getTime() - 24 * 24 * 600 * 1000) - 6 * 24 * 3600 * 1000) / 2);
                datainicio.setText(c.datac(d));

                try {
                    Realm realm = Realm.getDefaultInstance();
                    Conta conta = realm.where(Conta.class).equalTo("loggado", true).findFirst();
                    List<Despesa_db> despesa_dbs = realm.where(Despesa_db.class).equalTo("id_usuario", conta.getId_usuario()).between("dia", d, hoje).findAll();
                    List<Deposito_db> deposito_dbs = realm.where(Deposito_db.class).equalTo("id_usuario", conta.getId_usuario()).between("dia", d, hoje).findAll();

                    if (despesa_dbs.size() > 0 || despesa_dbs.size() > 0) {
                        for (int i = 0; i < despesa_dbs.size(); i++) {
                            Relatorio relatorio = new Relatorio(despesa_dbs.get(i).getDescricao(), despesa_dbs.get(i).getDia(), despesa_dbs.get(i).getValor());
                            lista.add(relatorio);
                            v = v + despesa_dbs.get(i).getValor();

                        }
                        total = v;
                        saldo.setText(total + " mzn");
                    }

                } finally {

                }
                break;
            case 1:
                d = new Date();
                datainicio.setText(c.datac(d));

                try {
                    Realm realm = Realm.getDefaultInstance();
                    Conta conta = realm.where(Conta.class).equalTo("loggado", true).findFirst();
                    List<Despesa_db> despesa_dbs = realm.where(Despesa_db.class).equalTo("id_usuario", conta.getId_usuario()).between("dia", d, hoje).findAll();
                    List<Deposito_db> deposito_dbs = realm.where(Deposito_db.class).equalTo("id_usuario", conta.getId_usuario()).between("dia", d, hoje).findAll();

                    if (despesa_dbs.size() > 0 || despesa_dbs.size() > 0) {
                        for (int i = 0; i < despesa_dbs.size(); i++) {
                            Relatorio relatorio = new Relatorio(despesa_dbs.get(i).getDescricao(), despesa_dbs.get(i).getDia(), despesa_dbs.get(i).getValor());
                            lista.add(relatorio);
                            v = v + despesa_dbs.get(i).getValor();

                        }
                        total = v;
                        saldo.setText(total + " mzn");
                    }

                } finally {

                }
                break;
            case 2:
                d = new Date(((hoje.getTime() - 24 * 24 * 600 * 1000) - 6 * 24 * 3600 * 1000) / 4);

                try {
                    Realm realm = Realm.getDefaultInstance();
                    Conta conta = realm.where(Conta.class).equalTo("loggado", true).findFirst();
                    List<Despesa_db> despesa_dbs = realm.where(Despesa_db.class).equalTo("id_usuario", conta.getId_usuario()).between("dia", d, hoje).findAll();
                    List<Deposito_db> deposito_dbs = realm.where(Deposito_db.class).equalTo("id_usuario", conta.getId_usuario()).between("dia", d, hoje).findAll();

                    if (despesa_dbs.size() > 0 || despesa_dbs.size() > 0) {
                        for (int i = 0; i < despesa_dbs.size(); i++) {
                            Relatorio relatorio = new Relatorio(despesa_dbs.get(i).getDescricao(), despesa_dbs.get(i).getDia(), despesa_dbs.get(i).getValor());
                            lista.add(relatorio);
                            v = v + despesa_dbs.get(i).getValor();

                        }
                        total = v;
                        saldo.setText(total + " mzn");
                    }

                } finally {

                }
                datainicio.setText(c.datac(d));
                break;

            case 4:
                d = new Date(((hoje.getTime() - 24 * 24 * 600 * 1000) - 6 * 24 * 3600 * 1000) / 4);

                try {
                    Realm realm = Realm.getDefaultInstance();
                    Conta conta = realm.where(Conta.class).equalTo("loggado", true).findFirst();
                    List<Despesa_db> despesa_dbs = realm.where(Despesa_db.class).equalTo("id_usuario", conta.getId_usuario()).between("dia", d, hoje).findAll();
                    List<Deposito_db> deposito_dbs = realm.where(Deposito_db.class).equalTo("id_usuario", conta.getId_usuario()).between("dia", d, hoje).findAll();

                    if (despesa_dbs.size() > 0 || despesa_dbs.size() > 0) {
                        for (int i = 0; i < despesa_dbs.size(); i++) {
                            Relatorio relatorio = new Relatorio(despesa_dbs.get(i).getDescricao(), despesa_dbs.get(i).getDia(), despesa_dbs.get(i).getValor());
                            lista.add(relatorio);
                            v = v + despesa_dbs.get(i).getValor();

                        }
                        total = v;
                        saldo.setText(total + " mzn");
                    }

                } finally {

                }
                datainicio.setText(c.datac(d));
                break;
            case 5:
                d = new Date(((hoje.getTime() - 24 * 24 * 600 * 1000) - 6 * 24 * 3600 * 1000) * 3);

                try {
                    Realm realm = Realm.getDefaultInstance();
                    Conta conta = realm.where(Conta.class).equalTo("loggado", true).findFirst();
                    List<Despesa_db> despesa_dbs = realm.where(Despesa_db.class).equalTo("id_usuario", conta.getId_usuario()).between("dia", d, hoje).findAll();
                    List<Deposito_db> deposito_dbs = realm.where(Deposito_db.class).equalTo("id_usuario", conta.getId_usuario()).between("dia", d, hoje).findAll();

                    if (despesa_dbs.size() > 0 || despesa_dbs.size() > 0) {
                        for (int i = 0; i < despesa_dbs.size(); i++) {
                            Relatorio relatorio = new Relatorio(despesa_dbs.get(i).getDescricao(), despesa_dbs.get(i).getDia(), despesa_dbs.get(i).getValor());
                            lista.add(relatorio);
                            v = v + despesa_dbs.get(i).getValor();

                        }
                        total = v;
                        saldo.setText(total + " mzn");
                    }

                } finally {

                }
                datainicio.setText(c.datac(d));

                try {
                    Realm realm = Realm.getDefaultInstance();
                    Conta conta = realm.where(Conta.class).equalTo("loggado", true).findFirst();
                    List<Despesa_db> despesa_dbs = realm.where(Despesa_db.class).equalTo("id_usuario", conta.getId_usuario()).between("dia", d, hoje).findAll();
                    List<Deposito_db> deposito_dbs = realm.where(Deposito_db.class).equalTo("id_usuario", conta.getId_usuario()).between("dia", d, hoje).findAll();

                    if (despesa_dbs.size() > 0 || despesa_dbs.size() > 0) {
                        for (int i = 0; i < despesa_dbs.size(); i++) {
                            Relatorio relatorio = new Relatorio(despesa_dbs.get(i).getDescricao(), despesa_dbs.get(i).getDia(), despesa_dbs.get(i).getValor());
                            lista.add(relatorio);
                            v = v + despesa_dbs.get(i).getValor();

                        }
                        total = v;
                        saldo.setText(total + " mzn");
                    }

                } finally {

                }
                break;
            case 6:
                d = new Date(((hoje.getTime() - 24 * 24 * 600 * 1000) - 6 * 24 * 3600 * 1000) * 6);

                try {
                    Realm realm = Realm.getDefaultInstance();
                    Conta conta = realm.where(Conta.class).equalTo("loggado", true).findFirst();
                    List<Despesa_db> despesa_dbs = realm.where(Despesa_db.class).equalTo("id_usuario", conta.getId_usuario()).between("dia", d, hoje).findAll();
                    List<Deposito_db> deposito_dbs = realm.where(Deposito_db.class).equalTo("id_usuario", conta.getId_usuario()).between("dia", d, hoje).findAll();

                    if (despesa_dbs.size() > 0 || despesa_dbs.size() > 0) {
                        for (int i = 0; i < despesa_dbs.size(); i++) {
                            Relatorio relatorio = new Relatorio(despesa_dbs.get(i).getDescricao(), despesa_dbs.get(i).getDia(), despesa_dbs.get(i).getValor());
                            lista.add(relatorio);
                            v = v + despesa_dbs.get(i).getValor();

                        }
                        total = v;
                        saldo.setText(total + " mzn");
                    }

                } finally {

                }
                datainicio.setText(c.datac(d));
                break;
            case 7:
                d = new Date(((hoje.getTime() - 24 * 24 * 600 * 1000) - 6 * 24 * 3600 * 1000) * 12);
                datainicio.setText(c.datac(d));

                try {
                    Realm realm = Realm.getDefaultInstance();
                    Conta conta = realm.where(Conta.class).equalTo("loggado", true).findFirst();
                    List<Despesa_db> despesa_dbs = realm.where(Despesa_db.class).equalTo("id_usuario", conta.getId_usuario()).between("dia", d, hoje).findAll();
                    List<Deposito_db> deposito_dbs = realm.where(Deposito_db.class).equalTo("id_usuario", conta.getId_usuario()).between("dia", d, hoje).findAll();

                    if (despesa_dbs.size() > 0 || despesa_dbs.size() > 0) {
                        for (int i = 0; i < despesa_dbs.size(); i++) {
                            Relatorio relatorio = new Relatorio(despesa_dbs.get(i).getDescricao(), despesa_dbs.get(i).getDia(), despesa_dbs.get(i).getValor());
                            lista.add(relatorio);
                            v = v + despesa_dbs.get(i).getValor();

                        }
                        total = v;
                        saldo.setText(total + " mzn");
                    }

                } finally {

                }
                break;
        }
            datafim = (TextView) findViewById(R.id.tv_id_re_despesas_data_fim);
            datainicio.setText(c.datac(d));
            datafim.setText(c.datac(hoje));
            ImageView iv = (ImageView) findViewById(R.id.vooltar_despesas);
            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });



            adapter = new AdapterRelatoriosDespesas(lista, Relatorio_de_Despesas_Activity.this);
            recyclerView.setAdapter(adapter);
        button=(Button) findViewById(R.id.bt_relatorios_despesas_exportar);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(Relatorio_de_Despesas_Activity.this);
                builder.setMessage("Relatorio de venda gerado!").create().show();
            }
        });
    }
}
