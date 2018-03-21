package com.anje.kelvin.aconting.Operacoes;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import com.anje.kelvin.aconting.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

public class Venda_Activity extends AppCompatActivity {
    Venda venda;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    public List<preco> lista;
    String vid;
    TextView saldo, itens;
    Item item1;
    double sa;
    int quantidadea;

    Realm realm= Realm.getDefaultInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venda_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Date hoje = new Date();

        vid="Venda" + hoje.getDate() + "" + hoje.getYear() + "" + hoje.getMonth()+""+hoje.getTime();
        saldo = (TextView) findViewById(R.id.tv_venda_valor_venda);
        Realm realm=Realm.getDefaultInstance();
        sa=realm.where(ItemVendido.class).equalTo("vid",vid).sum("Valor").doubleValue();
        saldo.setText(sa+"Mzn");
        itens = (TextView) findViewById(R.id.tv_venda_itens_vendidos);
        quantidadea=realm.where(ItemVendido.class).equalTo("vid",vid).sum("quantidade").intValue();
        itens.setText(quantidadea+"");
        venda = new Venda();
        recyclerView = (RecyclerView) findViewById(R.id.rv_vendas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        lista = new ArrayList<preco>();
        try {
            try {
               List<ItemVendido> iv=realm.where(ItemVendido.class).equalTo("nomeitem",vid).findAll();
                for (int ia = 0; ia <iv.size(); ia++) {
                               preco pre = new preco(iv.get(ia).getQuantidade(),iv.get(ia).getNomeitem(),iv.get(ia).getValor());
                               lista.add(pre);
                           }
            } finally {

            }


            adapter = new Vendas(lista,this);
            recyclerView.setAdapter(adapter);


            Button adicionar_item = (Button) findViewById(R.id.bt_adicionar_producto);
            adicionar_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final Realm realm = Realm.getDefaultInstance();
                    Item man=realm.where(Item.class).findFirst();
                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(Venda_Activity.this, android.R.layout.select_dialog_singlechoice);
                    final Conta conta = realm.where(Conta.class).equalTo("loggado",true).findFirst();
                    final List<Item> item=realm.where(Item.class).equalTo("id_usuario",conta.getId_usuario()).findAll();
                    for(int i=0;i<item.size();i++){
                        arrayAdapter.add(item.get(i).getNome_Item());
                    }
                    final Dialog builder=new Dialog(Venda_Activity.this);
                    builder.setContentView(R.layout.itensvenda);
                    builder.setTitle("Sececione Item Para venda");
                    final ListView listView=(ListView) builder.findViewById(R.id.listaitens);
                    listView.setAdapter(arrayAdapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            final Dialog builder=new Dialog(Venda_Activity.this);
                            Realm realm = Realm.getDefaultInstance();
                            Conta conta = realm.where(Conta.class).equalTo("loggado",true).findFirst();
                            final Item item=realm.where(Item.class).equalTo("id_usuario",conta.getId_usuario()).findFirst();
                            builder.setTitle(item.getNome_Item());
                            builder.setCancelable(true);
                            builder.setContentView(R.layout.dialogoitemavender);
                            TextView nome=(TextView) builder.findViewById(R.id.et_vender_nome);
                            TextView preco=(TextView) builder.findViewById(R.id.et_vender_preco);

                            final TextView quantidade=(TextView) builder.findViewById(R.id.quantidade_txto);
                            Button vender_items=(Button) builder.findViewById(R.id.bt_adicionar_item_va);
                            item1=realm.where(Item.class).equalTo("nome_Item",arrayAdapter.getItem(position)).findFirst();
                            final int p=position;
                            try{
                                nome.setText(item1.getNome_Item());
                                preco.setText(item1.getPreco()+" MZN");
                                quantidade.setText(item1.getItens_disponiveis());

                            }catch (Exception e){

                            }
                            vender_items.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    EditText qtd=(EditText) builder.findViewById(R.id.et_vender_quantidade);
                                    Realm realm=Realm.getDefaultInstance();
                                    quantidadea=realm.where(ItemVendido.class).equalTo("vid",vid).sum("quantidade").intValue();
                                    itens.setText(quantidadea+"");
                                    sa=realm.where(ItemVendido.class).equalTo("vid",vid).sum("Valor").doubleValue();
                                    saldo.setText(sa+"Mzn");
                                       ItemVendido itemVendido=new ItemVendido();
                                       itemVendido.setNomeitem(item1.getNome_Item());
                                       itemVendido.setValor((Double.parseDouble(item1.getPreco().toString())+Double.parseDouble(qtd.getText().toString())));
                                       itemVendido.setVid(vid);
                                       itemVendido.setData(new Date());
                                       itemVendido.setQuantidade(Integer.parseInt(qtd.getText().toString()));
                                        realm.beginTransaction();
                                        realm.copyToRealm(itemVendido);
                                        realm.commitTransaction();
                                    preco pre = new preco(itemVendido.getQuantidade(),itemVendido.getNomeitem(),itemVendido.getValor());
                                    lista.add(pre);
                                    adapter = new Vendas(lista,getApplicationContext());
                                    recyclerView.setAdapter(adapter);
                                    builder.cancel();
                                }
                            });

                            builder.show();





                        }
                    });
                    builder.show();
                    FloatingActionButton floatingActionButton=(FloatingActionButton) findViewById(R.id.fab_venda);
                    floatingActionButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ItemVendido itemV=realm.where(ItemVendido.class).equalTo("vid",vid).findFirst();
                            if(itemV!=null) {
                                Receita receita = new Receita();
                                receita.setDescricao("Venda de " + quantidadea + " Itens");
                                receita.setValor(sa);
                                receita.setData(new Date());
                                Conta conta1 = realm.where(Conta.class).equalTo("loggado", true).findFirst();
                                Transacao_db transacao_db = new Transacao_db();
                                transacao_db.setDescricao("Venda de " + quantidadea + " Itens");
                                transacao_db.setId_usuario(conta.getId_usuario());
                                transacao_db.setValor(sa);
                                transacao_db.setDia(new Date());
                                realm.beginTransaction();
                                realm.copyToRealm(transacao_db);
                                conta1.adicionar_deposito(sa);
                                realm.copyToRealm(receita);
                                List<ItemVendido> itemVendido = realm.where(ItemVendido.class).equalTo("vid", vid).findAll();
                                int i = 0;
                                while (i < itemVendido.size()) {
                                    try {
                                        Item item2 = realm.where(Item.class).equalTo("nome_Item", itemVendido.get(i).getNomeitem()).findFirst();
                                        realm.beginTransaction();
                                        item2.vender(itemVendido.get(i).getQuantidade());
                                        realm.commitTransaction();

                                    } catch (Exception e) {
                                        Toast.makeText(getApplicationContext(), "Item nao encontrado", Toast.LENGTH_LONG).show();
                                    }

                                }
                                finish();
                            }else {
                                AlertDialog.Builder builder1=new AlertDialog.Builder(Venda_Activity.this);
                                builder1.setMessage("Nao e possivel efectuar a venda sem itens, Adicione alguns Itens A venda!");
                                builder1.setTitle("Aviso");
                                builder1.create();
                                builder1.show();

                            }

                        }
                    });

                }

            });
        } finally{


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

                descricao=(TextView) view.findViewById(R.id.itens_vendidos_nome);
                valor=(TextView) view.findViewById(R.id.itens_vendidos_preco);
                peco=(TextView) view.findViewById(R.id.itens_vendidos_quantidade);
                icone=(ImageView) view.findViewById(R.id.imageView4);

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


