package com.anje.kelvin.aconting.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

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
        CardView pin=(CardView) view.findViewById(R.id.cardpin);
       pin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Realm realm=Realm.getDefaultInstance();
                final Conta conta=realm.where(Conta.class).equalTo("loggado",true).findFirst();
                Dialog dialog=new Dialog(getContext());
                dialog.setContentView(R.layout.dialogoalterarpin);
                final EditText pin1=(EditText)  dialog.findViewById(R.id.pin1);
                final EditText pin2=(EditText) dialog.findViewById(R.id.npin1);
                final EditText pin3=(EditText) dialog.findViewById(R.id.npin2);
                Button editar=(Button)  dialog.findViewById(R.id.bt_alterar_pin);
                editar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(conta.getPin()==Integer.parseInt(pin1.getText().toString())){
                            if(Integer.parseInt(pin2.getText().toString())==Integer.parseInt(pin3.getText().toString())){
                                final Realm realm=Realm.getDefaultInstance();
                                final Conta conta=realm.where(Conta.class).equalTo("loggado",true).findFirst();
                                realm.beginTransaction();
                                conta.setPin(Integer.parseInt(pin2.getText().toString()));
                                realm.commitTransaction();
                            }else {
                                AlertDialog.Builder bu=new AlertDialog.Builder(getContext());
                                bu.setMessage("O pin1 nao e igual ao pin2");
                                bu.setMessage("Aviso");
                                bu.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                    }
                                });
                            }

                        }else {
                            AlertDialog.Builder bu=new AlertDialog.Builder(getContext());
                            bu.setMessage("Pin Incorrecto ! ,Digite o pin utilizado no login");
                            bu.setMessage("Aviso");
                            bu.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                        }
                    }
                });

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
