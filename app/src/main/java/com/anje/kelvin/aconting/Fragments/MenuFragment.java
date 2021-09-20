package com.anje.kelvin.aconting.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.anje.kelvin.aconting.BaseDeDados.Deposito_db;
import com.anje.kelvin.aconting.BaseDeDados.Despesa_db;
import com.anje.kelvin.aconting.Operacoes.Gerir_estoque;
import com.anje.kelvin.aconting.Operacoes.RelatorioActivity;
import com.anje.kelvin.aconting.Operacoes.Venda_Activity;
import com.anje.kelvin.aconting.Operacoes.TransicoesActivity;
import com.anje.kelvin.aconting.BaseDeDados.Conta;
import com.anje.kelvin.aconting.Operacoes.Adicionar_deposito_Activity;
import com.anje.kelvin.aconting.Operacoes.Adicionar_despesaActivity;
import com.anje.kelvin.aconting.R;
import com.takusemba.spotlight.OnSpotlightEndedListener;
import com.takusemba.spotlight.OnSpotlightStartedListener;
import com.takusemba.spotlight.OnTargetStateChangedListener;
import com.takusemba.spotlight.SimpleTarget;
import com.takusemba.spotlight.Spotlight;

import java.util.List;

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
        SimpleTarget depositostarget =new SimpleTarget.Builder(getActivity()).setPoint(depositos).setRadius(80f).setTitle("Depositos")
                .setDescription("Faça a inserçao de todas  as receitas do seu negocio")
                .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                    @Override
                    public void onStarted(SimpleTarget target) {

                    }

                    @Override
                    public void onEnded(SimpleTarget target) {

                    }
                }).build();
        depositos.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(getActivity(),Adicionar_deposito_Activity.class);
               MenuFragment.this.startActivity(intent);
           }
       });
       final CardView despesa=view.findViewById(R.id.id_despesa);
       SimpleTarget despesatarget=new SimpleTarget.Builder(getActivity()).setPoint(despesa).setTitle("Despesa")
               .setDescription("Adicione as Despesas Mensais")
               .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                   @Override
                   public void onStarted(SimpleTarget target) {

                   }

                   @Override
                   public void onEnded(SimpleTarget target) {

                   }
               }).build();

       despesa.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent=new Intent(getActivity(),Adicionar_despesaActivity.class);
               MenuFragment.this.startActivity(intent);
           }

       });

       final CardView transicoes=view.findViewById(R.id.id_transacoes);
       SimpleTarget transacoestarget = new SimpleTarget.Builder(getActivity()).setPoint(transicoes).setDescription("Vizualize todas as transacoes efectuadas no Mes Corrente").setTitle("Transacoes ")
               .setOnSpotlightStartedListener(new OnTargetStateChangedListener<SimpleTarget>() {
                   @Override
                   public void onStarted(SimpleTarget target) {

                   }

                   @Override
                   public void onEnded(SimpleTarget target) {

                   }
               }).build();
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
               Intent intent=new Intent(getActivity(), Gerir_estoque.class);
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
        final Spotlight spotlight = Spotlight.with(getActivity()).setAnimation(new DecelerateInterpolator(2f)).setDuration(1000L).setTargets(depositostarget,despesatarget,transacoestarget)
                .setOnSpotlightStartedListener(new OnSpotlightStartedListener() {
                    @Override
                    public void onStarted() {

                    }
                }).setOnSpotlightEndedListener(new OnSpotlightEndedListener() {
                    @Override
                    public void onEnded() {

                    }
                });
        spotlight.start();
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
            List<Deposito_db> deposito=realm.where(Deposito_db.class).equalTo("id_usuario",contas.getId_usuario()).findAll();
            for(int i=0;i<deposito.size();i++){
                total+=deposito.get(i).getValor();
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
            List<Despesa_db> despesa=realm.where(Despesa_db.class).equalTo("id_usuario",contas.getId_usuario()).findAll();
            for(int i=0;i<despesa.size();i++){
                total+=despesa.get(i).getValor();
            }

        }catch (NullPointerException e){

        }

        return total;
    }
}
