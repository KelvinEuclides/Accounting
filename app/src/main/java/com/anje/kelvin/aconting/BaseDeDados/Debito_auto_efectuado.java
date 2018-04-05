package com.anje.kelvin.aconting.BaseDeDados;

import java.util.Date;
import io.realm.RealmObject;

public class Debito_auto_efectuado extends RealmObject {
    private  int id_usuario;
    private String Descricao;
    private Date Data;
    Boolean feito=false;

    public Debito_auto_efectuado() {
    }

    public String getDescricao() {
        return Descricao;
    }

    public void setDescricao(String descricao) {
        Descricao = descricao;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public Date getData() {
        return Data;
    }

    public void setData(Date data) {
        Data = data;
    }

    public Boolean getFeito() {
        return feito;
    }

    public void setFeito(Boolean feito) {
        this.feito = feito;
    }
}
