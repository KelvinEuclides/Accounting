
package com.anje.kelvin.aconting.Adapters.RecyclerVIewAdapter;

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
import android.widget.TextView;

import com.anje.kelvin.aconting.Adapters.AdapterObjects.Stock;
import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.BaseDeDados.Venda;
import com.anje.kelvin.aconting.R;

import java.util.List;

import io.realm.Realm;

/**
 * Created by sala on 30-01-2018.
 */

public class AdapterVenda extends RecyclerView.Adapter<AdapterVenda.ViewHolder>{

    private List<Stock> mValues;
    private Context context;
    String codico;
    private long timestamp1=001;


    public AdapterVenda(List<Stock> mValues, Context context,String codico) {
        this.mValues = mValues;
        this.context = context;
        this.codico = codico;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_venda,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Stock stock=mValues.get(position);
        holder.descricao.setText(stock.getNomeItem());
        holder.preco.setText(stock.getPreco()+"");
        holder.itensDispo.setText(stock.getNumitemdisp());
        holder.numItens.setText(stock.getNumitem());
        holder.modificar.setText("Vender");
        holder.modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Quantidade");
                final EditText editText=new EditText(context);
                editText.setHint("0");
                builder.setView(editText);
                builder.setPositiveButton("Vender", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Realm realm=Realm.getDefaultInstance();
                       Conta conta=realm.where(Conta.class).equalTo("loggado",true).findFirst();
                       Venda ve=realm.where(Venda.class).equalTo("venda",codico).findFirst();
                        try {
                            realm.beginTransaction();
                            ve.setItems(conta.getStock().get(position),Integer.parseInt(editText.getText().toString()),conta.getStock().get(position).getPreco());
                            realm.commitTransaction();
                        }catch (Exception e){

                        }

                    }
                }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.create().show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView descricao,numItens,itensDispo, preco;
        public Button modificar;
        public LinearLayout linearLayout;

        public ViewHolder(View view) {
            super(view);
           descricao=(TextView) view.findViewById(R.id.tv_item_nome);
           itensDispo=(TextView) view.findViewById(R.id.tv_item_dispo);
           numItens=(TextView) view.findViewById(R.id.tv_item_inicial);
           preco=(TextView) view.findViewById(R.id.tv_item_precoun);
           modificar=(Button) view.findViewById(R.id.bt_item_modificar);
        }



        @Override
        public String toString() {
            return super.toString();
        }
    }

}
