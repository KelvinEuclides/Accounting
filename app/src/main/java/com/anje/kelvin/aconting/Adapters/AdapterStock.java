
package com.anje.kelvin.aconting.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.anje.kelvin.aconting.MainActivity;
import com.anje.kelvin.aconting.Operacoes.Adicionar_deposito_Activity;
import com.anje.kelvin.aconting.R;

import java.util.List;

/**
 * Created by sala on 30-01-2018.
 */

public class AdapterStock extends RecyclerView.Adapter<AdapterStock.ViewHolder>{

    private List<Stock> mValues;
    private Context context;

    public AdapterStock(List<Stock> mValues, Context context) {
        this.mValues = mValues;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_stock,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Stock stock=mValues.get(position);
        holder.descricao.setText(stock.getNomeItem());
        holder.preco.setText(stock.getPreco()+" MZN");
        holder.numItens.setText(stock.getNumitem());
        holder.itensDispo.setText(stock.getNumitemdisp());


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView descricao,numItens,itensDispo, preco;
        public LinearLayout linearLayout;

        public ViewHolder(View view) {
            super(view);
           descricao=(TextView) view.findViewById(R.id.tv_id_descricao);
           numItens=(TextView) view.findViewById(R.id.tv_id_items);
           itensDispo=(TextView) view.findViewById(R.id.tv_id_itensDisp);
           preco=(TextView) view.findViewById(R.id.tv_id_preco);

        }



        @Override
        public String toString() {
            return super.toString();
        }
    }
}
