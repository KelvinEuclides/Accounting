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

import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.R;
import com.anje.kelvin.aconting.login;

import io.realm.Realm;

public class DefinicoesFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    public DefinicoesFragment() {
        // Required empty public constructor
    }
    public static DefinicoesFragment newInstance(String param1, String param2) {
        DefinicoesFragment fragment = new DefinicoesFragment();
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
        View view=inflater.inflate(R.layout.fragment_definicoes, container, false);
        CardView logout=(CardView) view.findViewById(R.id.id_terminar_sessao);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Realm realm=Realm.getDefaultInstance();
                Conta conta=realm.where(Conta.class).equalTo("loggado",true).findFirst();
                Intent intent =new Intent(view.getContext(),login.class);
                realm.beginTransaction();
                conta.setLoggado(false);
                realm.commitTransaction();
                startActivity(intent);
            }
        });
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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
