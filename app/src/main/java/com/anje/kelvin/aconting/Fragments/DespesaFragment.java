package com.anje.kelvin.aconting.Fragments;

import android.content.Context;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.anje.kelvin.aconting.Adapters.RecyclerVIewAdapter.AdapterDepesas;
import com.anje.kelvin.aconting.Adapters.AdapterObjects.Depositos_itens;
import com.anje.kelvin.aconting.BaseDeDados.Conta;
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
        List<Despesa_db> despesa= realm.where(Despesa_db.class).equalTo("id_usuario",conta.getId_usuario()).findAll();
        if (despesa.size()>0) {
            for (int i = 0; i < despesa.size(); i++) {
                Depositos_itens transacao = new Depositos_itens(despesa.get(i).getDescricao(),"Deposito",despesa.get(i).getValor(),despesa.get(i).getDia());
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
