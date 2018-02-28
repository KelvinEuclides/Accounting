
package com.anje.kelvin.aconting.Adapters.RecyclerVIewAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anje.kelvin.aconting.Adapters.AdapterObjects.ReDespesa;
import com.anje.kelvin.aconting.R;

import java.util.List;

/**
 * Created by sala on 30-01-2018.
 */

public class AdapterDespesa extends RecyclerView.Adapter<AdapterDespesa.ViewHolder>{

    private List<ReDespesa> mValues;
    private Context context;

    public AdapterDespesa(List<ReDespesa> mValues, Context context) {
        this.mValues = mValues;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.relatorios_despesas,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ReDespesa reDespesa=mValues.get(position);
        holder.descricao.setText(reDespesa.getDescricao());
        holder.preco.setText(reDespesa.getPreco());
        holder.data.setText(reDespesa.getDate());


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView descricao,data, preco;


        public ViewHolder(View view) {
            super(view);
           descricao=(TextView) view.findViewById(R.id.tv_item_nome);
           data=(TextView) view.findViewById(R.id.tv_item_inicial);
           preco=(TextView) view.findViewById(R.id.tv_item_precoun);

        }



        @Override
        public String toString() {
            return super.toString();
        }
    }

}
