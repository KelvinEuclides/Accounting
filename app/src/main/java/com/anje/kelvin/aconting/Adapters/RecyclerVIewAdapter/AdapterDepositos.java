
package com.anje.kelvin.aconting.Adapters.RecyclerVIewAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.icu.text.DateFormat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anje.kelvin.aconting.Adapters.AdapterObjects.Depositos_itens;
import com.anje.kelvin.aconting.R;

import java.util.List;

/**
 * Created by sala on 30-01-2018.
 */

public class AdapterDepositos extends RecyclerView.Adapter<AdapterDepositos.ViewHolder>{

   private List<Depositos_itens> mValues;
    private Context context;

    public AdapterDepositos(List<Depositos_itens> mValues, Context context) {
        this.mValues = mValues;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_transacao,parent,false);
        return new ViewHolder(v);
    }

    @SuppressLint("NewApi")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Depositos_itens transacao=mValues.get(position);
        holder.descricao.setText(transacao.getDescricao());
        holder.valor.setText(transacao.getValor()+"");
        holder.data.setText(DateFormat.getDateInstance().format(transacao.getData()));
        holder.icone.setImageResource(R.drawable.dinheiro_dento);


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

            descricao=(TextView) view.findViewById(R.id.fg_trans_tv_descricao);
            valor=(TextView) view.findViewById(R.id.trans_tv_valor);
            data=(TextView) view.findViewById(R.id.tv_trans_dat);
            icone=(ImageView) view.findViewById(R.id.iv_icon_transacao);

        }

        @Override
        public String toString() {
            return super.toString();
        }
    }


}
