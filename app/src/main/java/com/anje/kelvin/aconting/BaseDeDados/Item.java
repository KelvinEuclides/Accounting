package com.anje.kelvin.aconting.BaseDeDados;

import io.realm.RealmObject;

/**
 * Created by sala on 07-02-2018.
 */

public class Item extends RealmObject {
    String nome_Item;
    Double preco,precoUnidade;
    String unidade_de_Medida;
    int num_item,itens_disponiveis;

    public Item() {
    }

    public String getNome_Item() {
        return nome_Item;
    }

    public void setNome_Item(String nome_Item) {
        this.nome_Item = nome_Item;
    }

    public Double getPreco() {
        return preco;
    }

    public void setPreco(Double preco) {
        this.preco = preco;
    }

    public Double getPrecoUnidade() {
        return precoUnidade;
    }

    public void setPrecoUnidade(Double precoUnidade) {
        this.precoUnidade = precoUnidade;
    }

    public String getUnidade_de_Medida() {
        return unidade_de_Medida;
    }

    public void setUnidade_de_Medida(String unidade_de_Medida) {
        this.unidade_de_Medida = unidade_de_Medida;
    }

    public int getNum_item() {
        return num_item;
    }

    public void setNum_item(int num_item) {
        this.num_item = num_item;
    }

    public int getItens_disponiveis() {
        return itens_disponiveis;
    }

    public void setItens_disponiveis(int itens_disponiveis) {
        this.itens_disponiveis = itens_disponiveis;
    }
}
