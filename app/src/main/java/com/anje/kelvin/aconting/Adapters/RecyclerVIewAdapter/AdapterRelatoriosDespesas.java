package com.anje.kelvin.aconting.Adapters.RecyclerVIewAdapter;

import android.content.Context;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anje.kelvin.aconting.Adapters.AdapterObjects.Relatorio;
import com.anje.kelvin.aconting.Classes.Convertar_Datas;
import com.anje.kelvin.aconting.R;

import java.util.List;

/**
 * Created by sala on 28-02-2018.
 */

public class AdapterRelatoriosDespesas extends RecyclerView.Adapter<AdapterRelatoriosDespesas.ViewHolder> {
    List<Relatorio> mValues;
    Context context;

   public AdapterRelatoriosDespesas(List<Relatorio> mValues, Context context) {
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
        Relatorio relatorio=mValues.get(position);
        Convertar_Datas c =new Convertar_Datas();
        holder.data.setText(c.datac(relatorio.getDate()));
        holder.discricao.setText(relatorio.getDescricao());
        holder.preco.setText(relatorio.getPreco()+"MZN");
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView preco,discricao,data;
        public ViewHolder(View itemView) {
            super(itemView);
            preco=(TextView) itemView.findViewById(R.id.valor);
            discricao=(TextView) itemView.findViewById(R.id.discricaodespesa);
            data=(TextView) itemView.findViewById(R.id.datadespesa);
        }
    }
}
