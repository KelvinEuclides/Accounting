package com.anje.kelvin.aconting.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anje.kelvin.aconting.Adapters.RecyclerVIewAdapter.AdapterDepositos;
import com.anje.kelvin.aconting.Adapters.AdapterObjects.Depositos_itens;
import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.BaseDeDados.Deposito_db;
import com.anje.kelvin.aconting.BaseDeDados.Despesa_db;
import com.anje.kelvin.aconting.R;


import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class DespositoFragment extends Fragment {
    private static final String ARG_COLUMN_COUNT = "column-count";
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;

    public DespositoFragment() {
    }

    @SuppressWarnings("unused")
    public static DespositoFragment newInstance(int columnCount) {
        DespositoFragment fragment = new DespositoFragment();
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
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
            Context context = view.getContext();
        Realm realm=Realm.getDefaultInstance();
        List<Depositos_itens> lista;
        RecyclerView.Adapter adapter;
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_deposito_lista);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        lista = new ArrayList<Depositos_itens>();
        Conta conta = realm.where(Conta.class).equalTo("loggado",true).findFirst();
        List<Deposito_db> despesa_dbs=realm.where(Deposito_db.class).equalTo("id_usuario",conta.getId_usuario()).findAll();

        if (despesa_dbs.size()>0) {
            for (int i = 0; i < despesa_dbs.size(); i++) {
                Depositos_itens transacao = new Depositos_itens(despesa_dbs.get(i).getDescricao(),"Deposito",despesa_dbs.get(i).getValor(),"Hoje");
                lista.add(transacao);
            }

        }
        adapter = new AdapterDepositos(lista,context);
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
        // TODO: Update argument type and name
        void onListFragmentInteraction();
    }
}
