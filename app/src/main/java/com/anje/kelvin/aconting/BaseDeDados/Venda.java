package com.anje.kelvin.aconting.BaseDeDados;

import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmObject;

/**
 * Created by sala on 13-02-2018.
 */

public class Venda extends RealmObject {
    private Double valor;
    private int itens_vendidos;
    private Date data;
    private RealmList<Item> items;

    public Double getValor() {
        return valor;
    }

    public void setValor(Double valor) {
        this.valor = valor;
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

    public RealmList<Item> getItems() {
        return items;
    }

    public void setItems(Item item,int numunidade,double valo) {
        itens_vendidos=itens_vendidos+item.getNum_item();
        valor=valor+valo;
        items.add(item);
    }
}
