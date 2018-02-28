package com.anje.kelvin.aconting.Adapters.AdapterObjects;

/**
 * Created by sala on 28-02-2018.
 */

public class Relatorio {
    String Descricao;
    String Date;
    double preco;

    public Relatorio(String descricao, String date, double preco) {
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

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
}
