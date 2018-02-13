package com.anje.kelvin.aconting.BaseDeDados;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmObject;

/**
 * Created by sala on 06-02-2018.
 */

public class Transacao_db extends RealmObject {
    protected String descricao;
    protected double Valor;
    protected String categoria;
    protected Date dia;
    protected String recorrencia;

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return Valor;
    }

    public void setValor(double valor) {
        Valor = valor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Date getDia(Date dia) {
        return this.dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public String getRecorrencia(String recorrencia) {
        return this.recorrencia;
    }

    public void setRecorrencia(String recorrencia) {
        this.recorrencia = recorrencia;
    }
}
