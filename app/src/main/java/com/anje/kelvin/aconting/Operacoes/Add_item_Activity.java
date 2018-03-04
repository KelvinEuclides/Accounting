package com.anje.kelvin.aconting.Operacoes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.BaseDeDados.Despesa_db;
import com.anje.kelvin.aconting.BaseDeDados.Item;
import com.anje.kelvin.aconting.BaseDeDados.Transacao_db;
import com.anje.kelvin.aconting.R;

import java.util.Date;

import io.realm.Realm;

public class Add_item_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final EditText descricao=(EditText) findViewById(R.id.et_nome_item);
        final EditText preco=(EditText) findViewById(R.id.et_item_preco);
        RadioGroup unidades=(RadioGroup) findViewById(R.id.rg_unidade_medida);
        final EditText precovenda=(EditText) findViewById(R.id.item_preco_venda);
        final EditText quantidade=(EditText) findViewById(R.id.et_item_quantidade);
        Button add=(Button) findViewById(R.id.bt_item_adicionar);
        final Realm realm=Realm.getDefaultInstance();
        final Conta conta=realm.where(Conta.class).equalTo("loggado",true).findFirst();
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Item item=new Item();
                item.setNome_Item(descricao.getText().toString());
                item.setUnidade_de_Medida("Unidade");
                item.setNum_item(Integer.parseInt(quantidade.getText().toString()));
                item.setItens_disponiveis(Integer.parseInt(quantidade.getText().toString()));
                item.setPreco(Double.parseDouble(preco.getText().toString()));
                item.setPrecoUnidade(Double.parseDouble(precovenda.getText().toString()));
                Despesa_db despesa_db=new Despesa_db();
                despesa_db.setCategoria("Compra");
                String medida;
                if (item.getUnidade_de_Medida().equals("Kg")){
                    medida="kilogramas";
                }
                if (item.getUnidade_de_Medida().equals("Litros")){
                    medida="litros";
                }
                else{
                    medida="Unidades";
                }
                despesa_db.setDescricao("Compra de "+item.getNum_item()+" "+medida+" de "+item.getNome_Item());
                despesa_db.setValor(item.getPreco());
                despesa_db.setDia(new Date());
                Transacao_db transacao_db=new Transacao_db();
                transacao_db.setValor(despesa_db.getValor());
                transacao_db.setDescricao(despesa_db.getDescricao());
                transacao_db.setDia(despesa_db.getDia());
                transacao_db.setCategoria("Compra");
                realm.beginTransaction();
                conta.adicionar_item(Double.parseDouble(preco.getText().toString()));
                realm.copyToRealm(item);
                realm.copyToRealm(despesa_db);
                realm.copyToRealm(transacao_db);
                realm.commitTransaction();
                Intent intent=new Intent(Add_item_Activity.this,Estoque_Activity.class);
                startActivity(intent);
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

}
