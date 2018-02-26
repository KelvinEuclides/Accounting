package com.anje.kelvin.aconting.Adapters;

/**
 * Created by sala on 26-02-2018.
 */

public class Depositos_itens {
    String descricao,tipo;
    double valor;
    String data;

    public Depositos_itens(String descricao, String tipo, double valor, String data) {
        this.descricao = descricao;
        this.tipo = tipo;
        this.valor = valor;
        this.data = data;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
