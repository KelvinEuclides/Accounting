package com.anje.kelvin.aconting.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anje.kelvin.aconting.Adapters.AdapterObjects.Transacao_itens;
import com.anje.kelvin.aconting.Adapters.RecyclerVIewAdapter.AdapterDepositos;
import com.anje.kelvin.aconting.Adapters.AdapterObjects.Depositos_itens;
import com.anje.kelvin.aconting.Adapters.ViewPAgerAdapter.AdapterTransicoes;
import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.BaseDeDados.Deposito_db;
import com.anje.kelvin.aconting.BaseDeDados.Despesa_db;
import com.anje.kelvin.aconting.BaseDeDados.Transacao_db;
import com.anje.kelvin.aconting.BaseDeDados.Venda;
import com.anje.kelvin.aconting.R;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;


public class ContaFragment extends Fragment {
    Realm realm=Realm.getDefaultInstance();
    Conta contas=realm.where(Conta.class).equalTo("loggado",true).findFirst();

    private OnFragmentInteractionListener mListener;

    public ContaFragment() {
        // Required empty public constructor
    }

    public static ContaFragment newInstance() {
        ContaFragment fragment = new ContaFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view =inflater.inflate(R.layout.fragment_conta, container, false);
        TextView salddo=(TextView) view.findViewById(R.id.fg_conta_tv_saldo);
        salddo.setText(contas.getSaldo_conta()+" MZN");
        TextView depositos =(TextView) view.findViewById(R.id.fg_depositos_m);
                depositos.setText(total_depositos()+" MZN");
        TextView despesas=(TextView) view.findViewById(R.id.fg_contas_despesas_m);
        despesas.setText(total_despesas()+" MZN");
        TextView vendas=(TextView) view.findViewById(R.id.fg_conta_receitas_m);
        vendas.setText(total_vendas()+" MZN");
         RecyclerView recyclerView;
        RecyclerView.Adapter adapter;
        List<Transacao_itens> lista;
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_despesas_recentes);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(false);
        lista = new ArrayList<Transacao_itens>();
        Realm realm = Realm.getDefaultInstance();
        Conta conta = realm.where(Conta.class).equalTo("loggado",true).findFirst();
        List<Transacao_db> transacao_db=realm.where(Transacao_db.class).equalTo("id_usuario",conta.getId_usuario()).findAll();
        if (transacao_db.size()>5) {
            for (int i = 0; i <transacao_db.size(); i++) {
                Transacao_itens transacaoa = new Transacao_itens(lista.get(i).getDescricao(),lista.get(i).getDescricao(),lista.get(i).getValor(),lista.get(i).getData());
                lista.add(transacaoa);
            }

        }


        adapter = new AdapterTransicoes(lista,getContext());
        recyclerView.setAdapter(adapter);

        return view;
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
            List<Deposito_db> despesa_dbs=realm.where(Deposito_db.class).equalTo("id_usuario",contas.getId_usuario()).findAll();
            for(int i=0;i<despesa_dbs.size();i++){
                total+=despesa_dbs.get(i).getValor();
            }

        }catch (NullPointerException e){

        }

        return total;
    }

    public double total_despesas(){

        double total = 0;
        try {
            Realm realm=Realm.getDefaultInstance();
            Conta contas=realm.where(Conta.class).equalTo("loggado",true).findFirst();
            List<Despesa_db> despesa= realm.where(Despesa_db.class).equalTo("id_usuario",contas.getId_usuario()).findAll();
            for(int i=0;i<despesa.size();i++){
                total+=despesa.get(i).getValor();
            }

        }catch (NullPointerException e){

        }

        return total;

    }

    public double total_vendas(){
        double total=0;
        try {
            Realm realm=Realm.getDefaultInstance();
            List<Venda> vendaList=realm.where(Venda.class).findAll();
            for (int i=0;i<vendaList.size();i++) {
                total += vendaList.get(i).getValor();
            }
        }catch (NullPointerException e){

        }
        return total;
    }
}
