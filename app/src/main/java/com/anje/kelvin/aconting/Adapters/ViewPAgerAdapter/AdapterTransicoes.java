
package com.anje.kelvin.aconting.Adapters.ViewPAgerAdapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.anje.kelvin.aconting.Adapters.AdapterObjects.Transacao_itens;
import com.anje.kelvin.aconting.Classes.Convertar_Datas;
import com.anje.kelvin.aconting.R;

import java.util.List;

/**
 * Created by sala on 30-01-2018.
 */

public class AdapterTransicoes extends RecyclerView.Adapter<AdapterTransicoes.ViewHolder>{

   private List<Transacao_itens> mValues;
    private Context context;

    public AdapterTransicoes(List<Transacao_itens> mValues, Context context) {
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
        Transacao_itens transacao=mValues.get(position);
        holder.descricao.setText(transacao.getDescricao());
        holder.valor.setText(transacao.getValor()+"");
        Convertar_Datas convertar_datas = new Convertar_Datas();
        holder.data.setText(convertar_datas.datac(transacao.getData()));
        holder.icone.setImageResource(transacao.getIcone());


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
