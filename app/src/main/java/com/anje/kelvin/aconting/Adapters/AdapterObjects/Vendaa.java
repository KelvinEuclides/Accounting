package com.anje.kelvin.aconting.Adapters.AdapterObjects;

import java.util.Date;

/**
 * Created by sala on 25-03-2018.
 */

public class Vendaa {
    private double valor;
    private double valor_iva;
    private String descricao = "Vendaa";
    private int itens_vendidos;
    private Date data;

    public Vendaa(double valor, String descricao, int itens_vendidos) {
        this.valor = valor;
        this.valor_iva = valor_iva;
        this.descricao = descricao;
        this.itens_vendidos = itens_vendidos;
        this.data = data;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getValor_iva() {
        return valor_iva;
    }

    public void setValor_iva(double valor_iva) {
        this.valor_iva = valor_iva;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getItens_vendidos() {
        return itens_vendidos;
    }

    public void setItens_vendidos(int itens_vendidos) {
        this.itens_vendidos = itens_vendidos;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
