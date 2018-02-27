package com.anje.kelvin.aconting.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anje.kelvin.aconting.Operacoes.Estoque_Activity;
import com.anje.kelvin.aconting.Operacoes.RelatorioActivity;
import com.anje.kelvin.aconting.Operacoes.Venda_Activity;
import com.anje.kelvin.aconting.Operacoes.TransicoesActivity;
import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.Operacoes.Adicionar_deposito_Activity;
import com.anje.kelvin.aconting.Operacoes.Adicionar_despesaActivity;
import com.anje.kelvin.aconting.R;

import io.realm.Realm;


public class MenuFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    public MenuFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static MenuFragment newInstance(int i, String menu) {
        MenuFragment fragment = new MenuFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



       final View view =inflater.inflate(R.layout.fragment_blank, container, false);
    Realm realm= Realm.getDefaultInstance();
        try {
            Conta contas = realm.where(Conta.class).equalTo("loggado",true).findFirst();

                final TextView saldo = (TextView) view.findViewById(R.id.tv_fg_menu_saldo_conta);
                saldo.setText(contas.getSaldo_conta() + " MZN");
                final TextView conta = (TextView) view.findViewById(R.id.tv_fg_menu_nome_conta);
                conta.setText(contas.getNomeConta());
                final TextView despesas = (TextView) view.findViewById(R.id.tv_saldos_despesas);
                despesas.setText(total_despesa() + "MZN");
                final TextView depositos = (TextView) view.findViewById(R.id.tv_saldos_despositos);
                depositos.setText(total_depositos() + "MZN");


        }finally {

        }
            final CardView depositos=view.findViewById(R.id.id_deposito);
       depositos.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(getActivity(),Adicionar_deposito_Activity.class);
               MenuFragment.this.startActivity(intent);
           }
       });
       final CardView despesa=view.findViewById(R.id.id_despesa);
       despesa.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(getActivity(),Adicionar_despesaActivity.class);
               MenuFragment.this.startActivity(intent);
           }

       });

       final CardView transicoes=view.findViewById(R.id.id_transacoes);
       transicoes.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(getActivity(),TransicoesActivity.class);
               MenuFragment.this.startActivity(intent);
           }
       });

       final CardView estoque=view.findViewById(R.id.id_estoque);
       estoque.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(getActivity(), Estoque_Activity.class);
               MenuFragment.this.startActivity(intent);
           }
       });

       final CardView venda=view.findViewById(R.id.id_venda);
       venda.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(getActivity(), Venda_Activity.class);
               startActivity(intent);
           }
       });

       final CardView relatorios=view.findViewById(R.id.id_relatorios);
       relatorios.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(getActivity(), RelatorioActivity.class);
               startActivity(intent);

           }
       });
        return view ;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public double total_depositos(){
        double total = 0;
        try {
            Realm realm=Realm.getDefaultInstance();
            Conta contas=realm.where(Conta.class).equalTo("loggado",true).findFirst();
            for(int i=0;i<contas.getDeposito_dbs().size();i++){
                total+=contas.getDeposito_dbs().get(i).getValor();
            }

        }catch (NullPointerException e){

        }

        return total;
    }
    public double total_despesa(){
        double total = 0;
        try {
            Realm realm=Realm.getDefaultInstance();
            Conta contas=realm.where(Conta.class).equalTo("loggado",true).findFirst();
            for(int i=0;i<contas.getDespesa_dbs().size();i++){
                total+=contas.getDespesa_dbs().get(i).getValor();
            }

        }catch (NullPointerException e){

        }

        return total;
    }
}
