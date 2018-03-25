package com.anje.kelvin.aconting.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.anje.kelvin.aconting.Adapters.AdapterObjects.Vendaa;
import com.anje.kelvin.aconting.Adapters.RecyclerVIewAdapter.AdapterItemVenda;
import com.anje.kelvin.aconting.BaseDeDados.Item;
import com.anje.kelvin.aconting.BaseDeDados.Venda;
import com.anje.kelvin.aconting.R;


import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;


public class FragmentVendas extends Fragment {

    private static final String ARG_COLUMN_COUNT = "column-count";

    private int mColumnCount = 1;

    public FragmentVendas() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static FragmentVendas newInstance(int columnCount) {
        FragmentVendas fragment = new FragmentVendas();
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
        View view = inflater.inflate(R.layout.fragment_item_venda, container, false);
        Realm realm=Realm.getDefaultInstance();
        List<Vendaa> lista;
        RecyclerView.Adapter adapter;
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.list_vendas);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        lista = new ArrayList<Vendaa>();
        List<Venda> vendas=realm.where(Venda.class).findAll();
        for (int i=0;i<vendas.size();i++){
            Vendaa vendaa=new Vendaa(vendas.get(i).getValor(),vendas.get(i).getVenda(),vendas.get(i).getItens_vendidos());
            lista.add(vendaa);
        }

        adapter = new AdapterItemVenda(lista);
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
    }
}
