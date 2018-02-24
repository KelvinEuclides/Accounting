
package com.anje.kelvin.aconting.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.BaseDeDados.Venda;
import com.anje.kelvin.aconting.Operacoes.Venda_Activity;
import com.anje.kelvin.aconting.R;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by sala on 30-01-2018.
 */

public class AdapterVenda extends RecyclerView.Adapter<AdapterVenda.ViewHolder>{

    private List<Stock> mValues;
    private Context context;
    Venda venda;
    Realm realm = Realm.getDefaultInstance();
    RealmResults<Conta> contas = realm.where(Conta.class).findAll();

    public AdapterVenda(Context context,Venda venda) {
        this.venda=venda;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vender_item,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        try {
            holder.nome_item.setText(contas.get(0).getStock().get(position).getNome_Item());
            holder.preco.setText(contas.get(0).getStock().get(position).getPreco()+"");
            holder.itensdispo.setText(contas.get(0).getStock().get(position).getItens_disponiveis()+"");
            holder.itens.setText(contas.get(0).getStock().get(position).getNum_item()+"");
            holder.vender.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int quantidadew;
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context).setTitle("QUANDIDADE").setMessage("Escreva A Quantidade" +
                            " que deseja vender");
                    final EditText quantidade = new EditText(context);
                    quantidade.setHint("0");
                    builder.setView(quantidade);
                    quantidadew = Integer.parseInt(quantidade.getText().toString());
                    builder.setPositiveButton("Vender", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (quantidadew <= 0) {
                                android.support.v7.app.AlertDialog.Builder builder1 = new android.support.v7.app.AlertDialog.Builder(context);
                                builder1.setMessage("Nao pode Adicionar Productos com valores nulos!").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).create().show();
                            } else {
                                venderItem(i, contas.get(0).getStock().get(i).getUnidade_de_Medida(), quantidadew);

                            }
                        }
                    }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    }).create().show();

                }
            });
        }catch (Exception e){

        }


    }

    @Override
    public int getItemCount() {
        return contas.get(0).getStock().size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nome_item;
        TextView itensdispo;
        TextView itens;
        TextView preco;
        Button vender;


        public ViewHolder(View view) {
            super(view);
             nome_item = (TextView) view.findViewById(R.id.tv_vender_nome);
            itensdispo = (TextView) view.findViewById(R.id.tv_item_dispo);
            itens = (TextView) view.findViewById(R.id.tv_item_venda_item);
            preco = (TextView) view.findViewById(R.id.tv_venda_item_precoun);
            vender = (Button) view.findViewById(R.id.bt_item_vendr);
        }



        @Override
        public String toString() {
            return super.toString();
        }
    }

    public void venderItem(int i, String medida, int numunidade) {
        double valor = contas.get(0).getStock().get(i).getPrecoUnidade();
        if (medida.equals("kg") || medida.equals("KG")) {
            valor = valor * numunidade;
        }
        if (medida.equals("UNIDADE") || medida.equals("unidade")) {
            valor = valor * numunidade;
        } else {
            valor = valor * numunidade;
        }
        venda.setItems(contas.get(0).getStock().get(i), numunidade, valor);
    }
}

