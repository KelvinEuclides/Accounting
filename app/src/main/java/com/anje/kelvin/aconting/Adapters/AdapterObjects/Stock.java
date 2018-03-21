package com.anje.kelvin.aconting.Adapters.AdapterObjects;

/**
 * Created by sala on 02-02-2018.
 */

public class Stock {
    private String nomeItem;
   private int numitem,numitemdisp;
   private Double preco;

    public Stock(String nomeItem, int numitem, int numitemdisp, Double preco) {
        this.nomeItem = nomeItem;
        this.numitem = numitem;
        this.numitemdisp = numitemdisp;
        this.preco = preco;
    }

    public String getNomeItem() {
        return nomeItem;
    }

    public void setNomeItem(String nomeItem) {
        this.nomeItem = nomeItem;
    }

    public int getNumitem() {
        return numitem;
    }

    public void setNumitem(int numitem) {
        this.numitem = numitem;
    }

    public int getNumitemdisp() {
        return numitemdisp;
    }

    public void setNumitemdisp(int numitemdisp) {
        this.numitemdisp = numitemdisp;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }
}
