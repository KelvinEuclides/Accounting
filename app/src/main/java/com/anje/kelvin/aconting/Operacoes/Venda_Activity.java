package com.anje.kelvin.aconting.Operacoes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.anje.kelvin.aconting.BaseDeDados.Venda;
import com.anje.kelvin.aconting.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import io.realm.Realm;

public class Venda_Activity extends AppCompatActivity {
    Venda venda;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    public List<preco> lista;
    String vid;
    TextView saldo, itens;
    Realm realm= Realm.getDefaultInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venda_);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        saldo = (TextView) findViewById(R.id.tv_venda_valor_venda);
        itens = (TextView) findViewById(R.id.tv_venda_itens_vendidos);
        venda = new Venda();
        Intent i=getIntent();
        if (i != null){
            vid =  i.getStringExtra("id");
            venda=realm.where(Venda.class).equalTo("venda",vid).findFirst();
        }else {

            Date hoje = new Date();
            vid="Venda" + hoje.getDate() + "" + hoje.getYear() + "" + hoje.getMonth();
            venda.setVenda(vid);
            realm.beginTransaction();
            realm.copyToRealm(venda);
            realm.commitTransaction();
        }
        double v=0;
        int a=0;
        try{
            a=venda.getItens_vendidos();
            v=venda.getValor();
        }catch (Exception e){

        }
        recyclerView = (RecyclerView) findViewById(R.id.rv_vendas);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        lista = new ArrayList<preco>();
        try {
            Realm realm = Realm.getDefaultInstance();
            try {
                Venda q = realm.where(Venda.class).equalTo("venda",vid).findFirst();
                       if (q != null && q.getItems().size()>0){
                           for (int ia = 0; ia < q.getItems().size(); ia++) {
                               preco pre = new preco(q.getItems().get(ia).getNum_item(), q.getItems().get(ia).getNome_Item(), q.getItems().get(ia).getPreco());
                               lista.add(pre);
                           }
                       }


            } finally {

            }


            adapter = new Vendas(lista,this);
            recyclerView.setAdapter(adapter);


            Button adicionar_item = (Button) findViewById(R.id.bt_adicionar_producto);
            adicionar_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(Venda_Activity.this, Itens_venda_Activity.class);
                    intent.putExtra("id",vid);
                    startActivity(intent);
                }
            });
        } finally {


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
            holder.peco.setText(p.getQuantidade());
            holder.icone.setImageResource(R.drawable.dinheiro_fora);


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
}

