
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.anje.kelvin.aconting.Adapters.AdapterObjects.Stock;
import com.anje.kelvin.aconting.R;

import java.util.List;

/**
 * Created by sala on 30-01-2018.
 */

public class AdapterStock extends RecyclerView.Adapter<AdapterStock.ViewHolder>{

    private List<Stock> mValues;
    private Context context;
    private long timestamp1=001;


    public AdapterStock(List<Stock> mValues, Context context) {
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
        holder.modificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder =new AlertDialog.Builder(context);
                LayoutInflater inflater;
                inflater = LayoutInflater.from(context);
                final View dialogView=inflater.inflate(R.layout.dialogomodificar,null);
                builder.setView(dialogView);
                final Button editar=(Button) dialogView.findViewById(R.id.bt_dialog_editar);
                final Button apagar=(Button) dialogView.findViewById(R.id.bt_dialog_apagar);
                editar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder2 =new AlertDialog.Builder(context);
                        LayoutInflater inflater1=LayoutInflater.from(context);
                        final View dalogView1=inflater1.inflate(R.layout.dialogoeditar,null);
                        builder2.setView(dalogView1);

                        final EditText nomeitem1=(EditText) dalogView1.findViewById(R.id.et_dialog_editar_nomep);
                        nomeitem1.setText(stock.getNomeItem());
                        final EditText precoUnidade1=(EditText) dalogView1.findViewById(R.id.et_dialog_preco_venda);
                        precoUnidade1.setText(stock.getPreco());
                        final TextView Categoria=(TextView) dalogView1.findViewById(R.id.tv_dialogo_unidademedida);
                        final Spinner categoria1=(Spinner) dalogView1.findViewById(R.id.sp_dialogo_unidademedida);

                        builder2.setPositiveButton("Aplicar Alteracoes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder2.create();
                        builder2.show();


                    }
                });
                apagar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AlertDialog.Builder builder1=new AlertDialog.Builder(context);
                        builder1.setTitle("Aviso").setMessage("Tem a certeza que deseja Apagar  "+stock.getNomeItem()+" Da lista de itens");
                        builder1.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Alforitimo para apagar item
                                Toast toast=Toast.makeText(context,"Item "+stock.getNomeItem()+"apagado",Toast.LENGTH_LONG);
                                toast.show();
                            }
                        }).setNegativeButton("Nao", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        });
                        builder1.create();
                        builder1.show();

                    }
                });

                builder.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                builder.setTitle("Selecione a Opçao");

                AlertDialog b =builder.create();
                b.show();

                Toast toast=Toast.makeText(context,"Clicou em modificar  "+stock.getNomeItem(),Toast.LENGTH_LONG);
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
