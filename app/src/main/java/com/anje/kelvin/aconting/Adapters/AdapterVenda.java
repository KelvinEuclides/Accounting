
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

import com.anje.kelvin.aconting.R;

import java.util.List;

/**
 * Created by sala on 30-01-2018.
 */

public class AdapterVenda extends RecyclerView.Adapter<AdapterVenda.ViewHolder>{

    private List<Stock> mValues;
    private Context context;

    public AdapterVenda(List<Stock> mValues, Context context) {
        this.mValues = mValues;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_venda,parent,false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final Stock stock=mValues.get(position);
        holder.descricao.setText(stock.getNomeItem());
        holder.preco.setText(stock.getPreco()+"");
        holder.itensDispo.setText(stock.getNumitemdisp());
        holder.numItens.setText(stock.getNumitem());
        holder.adicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Digite a quantidade que deseja vender");
                builder.create();
                builder.show();
                Toast toast=Toast.makeText(context,"Adicionou "+stock.getNomeItem()+"  A venda",Toast.LENGTH_LONG);
                toast.show();
            }
        });


    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView descricao,numItens,itensDispo, preco;
        public Button adicionar;

        public ViewHolder(View view) {
            super(view);
           descricao=(TextView) view.findViewById(R.id.tv_vender_nome);
           itensDispo=(TextView) view.findViewById(R.id.tv_item_venda_item);
           numItens=(TextView) view.findViewById(R.id.tv_venda_item_dispo);
           preco=(TextView) view.findViewById(R.id.tv_item_precoun);
           adicionar=(Button) view.findViewById(R.id.bt_item_vendr);
        }



        @Override
        public String toString() {
            return super.toString();
        }
    }

}
