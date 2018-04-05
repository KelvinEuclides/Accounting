package com.anje.kelvin.aconting.BaseDeDados;

import java.util.Date;

import io.realm.RealmObject;

/**
 * Created by sala on 19-03-2018.
 */

public class Debito_automatico extends RealmObject {
    private  int id_usuario;
    private String Descricao;
    private double valor;

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public Boolean getMensal() {
        return mensal;
    }

    public void setMensal(Boolean mensal) {
        this.mensal = mensal;
    }

    private Date DataFim;
    private Boolean mensal=false;

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
