package com.anje.kelvin.aconting.BaseDeDados;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by sala on 20-03-2018.
 */

public class Receita extends RealmObject {
    String descricao;
    Double valor;
    Date data;

    public Receita() {
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
