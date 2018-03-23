package com.anje.kelvin.aconting.Operacoes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.BaseDeDados.Item;
import com.anje.kelvin.aconting.BaseDeDados.ItemVendido;
import com.anje.kelvin.aconting.BaseDeDados.Receita;
import com.anje.kelvin.aconting.BaseDeDados.Transacao_db;
import com.anje.kelvin.aconting.BaseDeDados.Venda;
import com.anje.kelvin.aconting.MainActivity;
import com.anje.kelvin.aconting.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;

public class Venda_Activity extends AppCompatActivity {
    public List<preco> lista;
    Venda venda;
    String vid;
    TextView saldo, itens;
    Item item1;
    double totalvendas;
    int quantidadea;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder=new AlertDialog.Builder(Venda_Activity.this);
        builder.setTitle("Deseja Realmente Sair?").setMessage("Ira perder todo progresso!");
        builder.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Realm realm=Realm.getDefaultInstance();
                final List<ItemVendido> itemVendidos=realm.where(ItemVendido.class).equalTo("vid",vid).findAll();
                finish();

            }
        });
        builder.setNegativeButton("Nao", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create();
        builder.show();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venda_);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Date hoje = new Date();

        vid = "Venda" + hoje.getDate() + "" + hoje.getYear() + "" + hoje.getMonth() + "" + hoje.getTime();
        saldo = findViewById(R.id.tv_venda_valor_venda);
        Realm realm = Realm.getDefaultInstance();
        List<ItemVendido> itemVendidoRealmResults = realm.where(ItemVendido.class).equalTo("vid", vid).findAll();
        totalvendas = 0;
        for (int i = 0; i < itemVendidoRealmResults.size(); i++) {
            totalvendas = totalvendas + itemVendidoRealmResults.get(i).getValor();
        }

        saldo.setText(totalvendas + "Mzn");
        itens = findViewById(R.id.tv_venda_itens_vendidos);
        quantidadea = realm.where(ItemVendido.class).equalTo("vid", vid).findAll().sum("quantidade").intValue();
        itens.setText(quantidadea + "");
        venda = new Venda();
        recyclerView = findViewById(R.id.rv_vendas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        lista = new ArrayList<preco>();
        try {
            try {
                List<ItemVendido> iv = realm.where(ItemVendido.class).equalTo("nomeitem", vid).findAll();
                for (int ia = 0; ia < iv.size(); ia++) {
                    preco pre = new preco(iv.get(ia).getQuantidade(), iv.get(ia).getNomeitem(), iv.get(ia).getValor());
                    lista.add(pre);
                }
            } finally {

            }


            adapter = new Vendas(lista, this);
            recyclerView.setAdapter(adapter);
            List<ItemVendido> itemVendidoRealmResults1 = realm.where(ItemVendido.class).equalTo("nomeitem", vid).findAll();
            Button adicionar_item = findViewById(R.id.bt_adicionar_producto);
            if (itemVendidoRealmResults1.isEmpty()) {
                adicionar_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final Realm realm = Realm.getDefaultInstance();
                        Item man = realm.where(Item.class).findFirst();
                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Venda_Activity.this, android.R.layout.select_dialog_singlechoice);
                        final Conta conta = realm.where(Conta.class).equalTo("loggado", true).findFirst();
                        final List<Item> item = realm.where(Item.class).equalTo("id_usuario", conta.getId_usuario()).findAll();
                        for (int i = 0; i < item.size(); i++) {
                            arrayAdapter.add(item.get(i).getNome_Item());
                        }
                        final Dialog builder = new Dialog(Venda_Activity.this);
                        builder.setContentView(R.layout.itensvenda);
                        builder.setTitle("Sececione Item Para venda");
                        final ListView listView = builder.findViewById(R.id.listaitens);
                        Button concluir = builder.findViewById(R.id.bt_concluir);
                        concluir.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                builder.cancel();
                            }
                        });
                        listView.setAdapter(arrayAdapter);
                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                final Dialog builder = new Dialog(Venda_Activity.this);
                                Realm realm = Realm.getDefaultInstance();
                                Conta conta = realm.where(Conta.class).equalTo("loggado", true).findFirst();
                                final Item item = realm.where(Item.class).equalTo("id_usuario", conta.getId_usuario()).findFirst();
                                builder.setTitle(item.getNome_Item());
                                builder.setCancelable(true);
                                builder.setContentView(R.layout.dialogoitemavender);
                                TextView nome = builder.findViewById(R.id.et_vender_nome);
                                TextView preco = builder.findViewById(R.id.et_vender_preco);
                                TextView quantidade = builder.findViewById(R.id.quantidade_txto);
                                Button vender_items = builder.findViewById(R.id.bt_adicionar_item_va);
                                item1 = realm.where(Item.class).equalTo("nome_Item", arrayAdapter.getItem(position)).findFirst();
                                final int p = position;
                                try {
                                    nome.setText(item1.getNome_Item());
                                    preco.setText(item1.getPrecoUnidade() + " MZN");
                                    quantidade.setText("Qtd:" + item1.getItens_disponiveis());

                                } catch (Exception e) {

                                }
                                vender_items.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        EditText qtd = builder.findViewById(R.id.et_vender_quantidade);
                                        Realm realm = Realm.getDefaultInstance();
                                        ItemVendido itemVendido = new ItemVendido();
                                        itemVendido.setNomeitem(item1.getNome_Item());
                                        itemVendido.setValor((Double.parseDouble(item1.getPrecoUnidade().toString()) * Double.parseDouble(qtd.getText().toString())));
                                        itemVendido.setVid(vid);
                                        itemVendido.setData(new Date());
                                        itemVendido.setQuantidade(Integer.parseInt(qtd.getText().toString()));
                                        realm.beginTransaction();
                                        realm.copyToRealm(itemVendido);
                                        realm.commitTransaction();
                                        preco pre = new preco(itemVendido.getQuantidade(), itemVendido.getNomeitem(), itemVendido.getValor());
                                        lista.add(pre);
                                        adapter = new Vendas(lista, getApplicationContext());
                                        recyclerView.setAdapter(adapter);
                                        List<ItemVendido> item = realm.where(ItemVendido.class).equalTo("vid", vid).findAll();
                                        quantidadea = 0;
                                        for (int i = 0; i < item.size(); i++) {
                                            quantidadea = quantidadea + item.get(i).getQuantidade();
                                        }
                                        itens.setText(quantidadea + "");
                                        totalvendas = 0;
                                        for (int i = 0; i < item.size(); i++) {
                                            totalvendas = totalvendas + item.get(i).getValor();
                                        }
                                        saldo.setText(totalvendas + "Mzn");
                                        builder.cancel();
                                    }
                                });

                                builder.show();


                            }
                        });
                        builder.show();
                    }
                });
            } else {
                adicionar_item.setText("Concluir");
                ItemVendido itemV = realm.where(ItemVendido.class).equalTo("vid", vid).findFirst();
                if (itemV != null) {
                    Receita receita = new Receita();
                    receita.setDescricao("Venda de " + quantidadea + " Itens");
                    receita.setValor(totalvendas);
                    receita.setData(new Date());
                    Conta conta1 = realm.where(Conta.class).equalTo("loggado", true).findFirst();
                    Transacao_db transacao_db = new Transacao_db();
                    transacao_db.setDescricao("Venda de " + quantidadea + " Itens");
                    Conta conta = realm.where(Conta.class).equalTo("loggado", true).findFirst();
                    transacao_db.setId_usuario(conta.getId_usuario());
                    transacao_db.setValor(totalvendas);
                    transacao_db.setDia(new Date());
                    realm.beginTransaction();
                    realm.copyToRealm(transacao_db);
                    conta1.adicionar_deposito(totalvendas);
                    realm.copyToRealm(receita);
                    realm.commitTransaction();
                    List<ItemVendido> itemVendido = realm.where(ItemVendido.class).equalTo("vid", vid).findAll();
                    for (int i = 0; i < itemVendido.size(); i++) {
                        Item item2 = realm.where(Item.class).equalTo("nome_Item", itemVendido.get(i).getNomeitem()).findFirst();
                        try {
                            realm.beginTransaction();
                            item2.vender(itemVendido.get(i).getQuantidade());
                            realm.commitTransaction();
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "Item nao encontrado", Toast.LENGTH_LONG).show();
                        }
                    }

                } else {
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Venda_Activity.this);
                    builder1.setMessage("Nao e possivel efectuar a venda sem itens, Adicione alguns Itens A venda!");
                    builder1.setTitle("Aviso");
                    builder1.create();
                    builder1.show();

                }
                finish();
                Intent intent = new Intent(Venda_Activity.this, MainActivity.class);
                startActivity(intent);
                AlertDialog.Builder builder1 = new AlertDialog.Builder(Venda_Activity.this);
                builder1.setMessage("Venda Efectuada Com Susseso !");
            }
        } finally {
        }

    }


}
     class Vendas extends RecyclerView.Adapter<Vendas.ViewHolder>{

        private List<preco> mValues;
        private Context context;

        public Vendas(List<preco> mValues, Context context) {
            this.mValues = mValues;
            this.context = context;
        }

        @Override
        public Vendas.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.itens_vendidos,parent,false);
            return new Vendas.ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(Vendas.ViewHolder holder, int position) {
            preco p=mValues.get(position);
            holder.descricao.setText(p.getNome());
            holder.valor.setText(p.getPreco()+"");
            holder.peco.setText(p.getQuantidade()+"");


        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }
        public class ViewHolder extends RecyclerView.ViewHolder {
            public TextView descricao;
            public TextView valor;
            public TextView peco;
            public ImageView icone;

            public ViewHolder(View view) {
                super(view);

                descricao = view.findViewById(R.id.itens_vendidos_nome);
                valor = view.findViewById(R.id.itens_vendidos_preco);
                peco = view.findViewById(R.id.itens_vendidos_quantidade);
                icone = view.findViewById(R.id.imageView4);

            }

            @Override
            public String toString() {
                return super.toString();
            }
        }


    }

    class preco{
        private int quantidade;
        private String nome;
        private double preco;

        public preco(int quantidade, String nome, double preco) {
            this.quantidade = quantidade;
            this.nome = nome;
            this.preco = preco;
        }

        public int getQuantidade() {
            return quantidade;
        }

        public void setQuantidade(int quantidade) {
            this.quantidade = quantidade;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public double getPreco() {
            return preco;
        }

        public void setPreco(double preco) {
            this.preco = preco;
        }
    }


