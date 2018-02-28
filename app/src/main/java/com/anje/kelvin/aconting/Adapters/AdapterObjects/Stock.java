package com.anje.kelvin.aconting.Adapters.AdapterObjects;

/**
 * Created by sala on 02-02-2018.
 */

public class Stock {
    private String nomeItem,numitem,numitemdisp,preco;

    public Stock(String nomeItem, String numitem, String numitemdisp, String preco) {
        this.nomeItem = nomeItem;
        this.numitem = numitem;
        this.numitemdisp = numitemdisp;
        this.preco = preco;
    }

    public String getNomeItem() {
        return nomeItem;
    }

    public String getNumitem() {
        return numitem;
    }

    public String getNumitemdisp() {
        return numitemdisp;
    }

    public String getPreco() {
        return preco;
    }
}
