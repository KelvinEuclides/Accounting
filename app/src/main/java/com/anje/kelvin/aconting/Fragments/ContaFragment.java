package com.anje.kelvin.aconting.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.BaseDeDados.Debito_automatico;
import com.anje.kelvin.aconting.BaseDeDados.Deposito_db;
import com.anje.kelvin.aconting.BaseDeDados.Despesa_db;
import com.anje.kelvin.aconting.BaseDeDados.Venda;
import com.anje.kelvin.aconting.Classes.Convertar_Datas;
import com.anje.kelvin.aconting.R;

import java.util.Date;
import java.util.List;

import io.realm.Realm;


public class ContaFragment extends Fragment {
    Realm realm=Realm.getDefaultInstance();
    Date inicio=new Date();

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
        TextView salddo = view.findViewById(R.id.fg_conta_tv_saldo);
        salddo.setText(contas.getSaldo_conta()+" MZN");
        TextView despesas = view.findViewById(R.id.fg_contas_despesas_m);
        despesas.setText(total_despesas()+" MZN");
        TextView vendas = view.findViewById(R.id.fg_conta_receitas_m);
        vendas.setText(total_vendas()+" MZN");
        Realm realm = Realm.getDefaultInstance();
        Convertar_Datas co=new Convertar_Datas();
        TextView totalvendas=(TextView) view.findViewById(R.id.n_vendas_mensais);
        try {
            Conta conta = realm.where(Conta.class).equalTo("loggado",true).findFirst();
            List<Venda> venda=realm.where(Venda.class).between("data",co.primeirodiadomes(inicio),inicio).findAll();
            List<Debito_automatico> debito_automaticoList =realm.where(Debito_automatico.class).equalTo("id_usuario",conta.getId_usuario()).findAll();
            TextView total_transacoes=(TextView) view.findViewById(R.id.total_trans_age);
            try {
                total_transacoes.setText(debito_automaticoList.size());
                totalvendas.setText(venda.size());
            }catch (Exception e){
            }
        }catch (IllegalArgumentException e){

        }

        return view;
    }

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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
