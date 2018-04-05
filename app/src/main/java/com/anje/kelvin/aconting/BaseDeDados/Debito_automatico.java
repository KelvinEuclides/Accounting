package com.anje.kelvin.aconting.BaseDeDados;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by sala on 19-03-2018.
 */

public class Debito_automatico extends RealmObject {

    private String Descricao;
    private double valor;
    private Date DataFim;

    public Debito_automatico() {
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public Date getDataFim() {
        return DataFim;
    }

    public void setDataFim(Date dataFim) {
        DataFim = dataFim;
    }
}
