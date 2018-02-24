package com.anje.kelvin.aconting.Adapters;

import java.util.Date;

/**
 * Created by sala on 23-02-2018.
 */

public class ReDespesa {
    String descricao,preco;
    Date date;

    public ReDespesa(String descricao, String preco, Date date) {
        this.descricao = descricao;
        this.preco = preco;
        this.date = date;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPreco() {
        return preco;
    }

    public void setPreco(String preco) {
        this.preco = preco;
    }

    public String getDate() {
        String dt;
        return date.getDay()+"/"+date.getMonth()+"/"+date.getYear();
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
