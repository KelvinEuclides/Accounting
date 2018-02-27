package com.anje.kelvin.aconting.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anje.kelvin.aconting.Adapters.AdapterTransicoes;
import com.anje.kelvin.aconting.Adapters.Transacao_itens;
import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.R;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

public class TransacaoitensFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    public TransacaoitensFragment() {
    }
    @SuppressWarnings("unused")
    public static TransacaoitensFragment newInstance(int columnCount) {
        TransacaoitensFragment fragment = new TransacaoitensFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mColumnCount = getArguments().getInt(ARG_COLUMN_COUNT);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_transacaoitens, container, false);
        Realm realm=Realm.getDefaultInstance();
        List<Transacao_itens> lista;
        RecyclerView.Adapter adapter;
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.frag_rv_transicoes_a);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            lista = new ArrayList<Transacao_itens>();
            Conta conta = realm.where(Conta.class).equalTo("loggado",true).findFirst();
            if (conta.getTransacaoDbs().size()>0) {
                for (int i = 0; i < conta.getTransacaoDbs().size(); i++) {
                    Transacao_itens transacao = new Transacao_itens(conta.getTransacaoDbs().get(i).getDescricao(), conta.getTransacaoDbs().get(i).getCategoria(), conta.getTransacaoDbs().get(i).getValor());
                    lista.add(transacao);
                }

            }
            adapter = new AdapterTransicoes(lista,context);
            recyclerView.setAdapter(adapter);


        return view;
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

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction();
    }
}
