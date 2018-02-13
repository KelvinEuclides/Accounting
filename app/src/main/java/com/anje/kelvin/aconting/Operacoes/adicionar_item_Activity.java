package com.anje.kelvin.aconting.Operacoes;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.BaseDeDados.Item;
import com.anje.kelvin.aconting.BaseDeDados.Transacao_db;
import com.anje.kelvin.aconting.MainActivity;
import com.anje.kelvin.aconting.R;
import com.anje.kelvin.aconting.item_stock_Activity;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmResults;

public class adicionar_item_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adicionar_item_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final EditText nome_item,preco_item,preco_venda,numero_itens;
        Spinner unidade_medida;
        Button salvar;
        nome_item=(EditText) findViewById(R.id.et_add_nomeitem);
        preco_item=(EditText) findViewById(R.id.et_add_valor_compra);
        preco_venda=(EditText) findViewById(R.id.et_add_preco_unidade);
        numero_itens=(EditText) findViewById(R.id.et_add_numitm);

        salvar=(Button) findViewById(R.id.bt_add_salvar);
        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Realm realm=Realm.getDefaultInstance();
                try {
                    RealmResults<Conta> contas=realm.where(Conta.class).findAll();
                    Item item=new Item();
                    item.setNome_Item(nome_item.getText().toString());
                    item.setPreco(Double.parseDouble(preco_item.getText().toString()));
                    item.setPrecoUnidade(Double.parseDouble(preco_venda.getText().toString()));
                    item.setNum_item(Integer.parseInt(numero_itens.getText().toString()));
                    Transacao_db transacao_db=new Transacao_db();
                    transacao_db.setDescricao("Compra de "+item.getNum_item() +"Unidades de "+item.getNome_Item());
                    transacao_db.setValor(item.getPreco());
                    transacao_db.setCategoria("Despesa");
                    transacao_db.setRecorrencia("Nenhuma");
                    transacao_db.setDia(new Date());
                    realm.beginTransaction();
                    contas.get(0).setStock(item);
                    contas.get(0).setTransacaoDbs(transacao_db);
                    realm.commitTransaction();

                }finally {

                }
                Intent intent=new Intent(adicionar_item_Activity.this,item_stock_Activity.class);
                startActivity(intent);
                finish();

            }
        });


    }

}
