package com.anje.kelvin.aconting.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.anje.kelvin.aconting.Adapters.AdapterDepesas;
import com.anje.kelvin.aconting.Adapters.Depositos_itens;
import com.anje.kelvin.aconting.BaseDeDados.Conta;
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
public class DespesaFragment extends Fragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    // TODO: Customize parameters
    private int mColumnCount = 1;
    private OnListFragmentInteractionListener mListener;
    public DespesaFragment() {
    }
    public static DespesaFragment newInstance(int columnCount) {
        DespesaFragment fragment = new DespesaFragment();
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
        View view = inflater.inflate(R.layout.fragment_item_list2, container, false);
            Context context = view.getContext();
        Realm realm=Realm.getDefaultInstance();
        List<Depositos_itens> lista;
        RecyclerView.Adapter adapter;
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rv_despesa_lista);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        lista = new ArrayList<Depositos_itens>();
        Conta conta = realm.where(Conta.class).equalTo("loggado",true).findFirst();
        if (conta.getDespesa_dbs().size()>0) {
            for (int i = 0; i < conta.getDespesa_dbs().size(); i++) {
                Depositos_itens transacao = new Depositos_itens(conta.getDespesa_dbs().get(i).getDescricao(),"Deposito",conta.getDespesa_dbs().get(i).getValor(),"Hoje");
                lista.add(transacao);
            }

        }
        adapter = new AdapterDepesas(lista,context);
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
