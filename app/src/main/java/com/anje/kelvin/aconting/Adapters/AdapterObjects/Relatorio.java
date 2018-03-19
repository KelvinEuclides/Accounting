package com.anje.kelvin.aconting.Adapters.AdapterObjects;

import java.util.Date;

/**
 * Created by sala on 28-02-2018.
 */

public class Relatorio {
    String Descricao;
    Date Date;
    double preco;
    private Date dia;

    public Relatorio(String descricao, java.util.Date date, double preco) {
        Descricao = descricao;
        Date = date;
        this.preco = preco;
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public Date getDate() {
        return Date;
    }

    public void setDate(Date date) {
        Date = date;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public Date getDia() {
        return dia;
    }

    public void setDia(java.util.Date dia) {
        this.dia = dia;
    }
}
