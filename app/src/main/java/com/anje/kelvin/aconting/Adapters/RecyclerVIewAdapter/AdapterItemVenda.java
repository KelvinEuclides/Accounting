package com.anje.kelvin.aconting.Adapters.RecyclerVIewAdapter;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anje.kelvin.aconting.Adapters.AdapterObjects.Vendaa;
import com.anje.kelvin.aconting.BaseDeDados.Venda;
import com.anje.kelvin.aconting.Classes.Convertar_Datas;
import com.anje.kelvin.aconting.R;

import java.util.List;

public class AdapterItemVenda extends RecyclerView.Adapter<AdapterItemVenda.ViewHolder> {

    private final List<Vendaa> mValues;

    public AdapterItemVenda(List<Vendaa> items) {
        mValues = items;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_venda, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.descricao.setText(mValues.get(position).getDescricao());
        holder.valor.setText(mValues.get(position).getValor()+"MZN");
        Convertar_Datas c=new Convertar_Datas();
        holder.data.setText("");
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
         TextView descricao,valor,data;
         CardView cardView;
        public ViewHolder(View view) {
            super(view);
            descricao = (TextView) view.findViewById(R.id.fg_trans_tv_descricao_venda);
            valor = (TextView) view.findViewById(R.id.trans_tv_valor_venda);
            data=(TextView) view.findViewById(R.id.tv_trans_dat_venda);
            cardView=(CardView) view.findViewById(R.id.caard);
        }

        @Override
        public String toString() {
            return "";
        }
    }
}
