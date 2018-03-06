package com.anje.kelvin.aconting.Adapters.AdapterObjects;


import com.anje.kelvin.aconting.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by sala on 02-02-2018.
 */

public class Transacao_itens {
    String descricao,tipo;
    double valor;
    Date data;
    int icone;

    public Transacao_itens(String descricao, String tipo, double valor,Date data) {
        this.descricao = descricao;
        this.tipo = tipo;
        this.valor = valor;
        this.data = data;
        this.icone =R.drawable.transferencias;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public double getValor() {
        return valor;
    }

    public Date getData() {
        return data;
    }


    public int getIcone() {
        return icone;
    }

    public int escolher_icone(String descricao){
        if(tipo.equals("Transferencia")){
            icone= R.drawable.transferencias;
        }
        if (tipo.equals("Deposito")){
            icone=R.drawable.dinheiro_dento;
        }
        if (tipo.equals("Despesa")){
            icone=R.drawable.dinheiro_fora;
        }
        else{
            icone= R.drawable.transferencias;
        }
        return icone;
    }

}
