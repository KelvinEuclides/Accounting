
package com.anje.kelvin.aconting.Adapters.RecyclerVIewAdapter;
import android.content.Context;
import android.icu.text.DateFormat;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anje.kelvin.aconting.Adapters.AdapterObjects.Depositos_itens;
import com.anje.kelvin.aconting.Classes.Convertar_Datas;
import com.anje.kelvin.aconting.R;

import java.util.List;

/**
 * Created by kelvin euclides on 30-01-2018.
 */

public class AdapterDepesas extends RecyclerView.Adapter<AdapterDepesas.ViewHolder>{

   private List<Depositos_itens> mValues;
    private Context context;

    public AdapterDepesas(List<Depositos_itens> mValues, Context context) {
        this.mValues = mValues;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_transacao,parent,false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Depositos_itens transacao=mValues.get(position);
        holder.descricao.setText(transacao.getDescricao());
        holder.valor.setText(transacao.getValor()+"");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.data.setText(DateFormat.getDateInstance().format(transacao.getData()));
        }else {
            Convertar_Datas c =new Convertar_Datas();
            holder.data.setText(c.datac(transacao.getData()));
        }
        holder.icone.setImageResource(R.drawable.dinheiro_fora);


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView descricao;
        public TextView valor;
        public TextView data;
        public ImageView icone;

        public ViewHolder(View view) {
            super(view);

            descricao = view.findViewById(R.id.fg_trans_tv_descricao);
            valor = view.findViewById(R.id.trans_tv_valor);
            data = view.findViewById(R.id.tv_trans_dat);
            icone = view.findViewById(R.id.iv_icon_transacao);

        }

        @Override
        public String toString() {
            return super.toString();
        }
    }


}
