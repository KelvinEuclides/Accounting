package com.anje.kelvin.aconting.BaseDeDados;

import java.util.Date;
import io.realm.RealmObject;

/**
 * Created by sala on 06-02-2018.
 */

public class Deposito_db extends RealmObject {
    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    private  int id_usuario;
    private String descricao;
    private double Valor;
    private String categoria;
    private Date dia;
    private Date dia_fim;

    public Date getDia_fim() {
        return dia_fim;
    }

    public void setDia_fim(Date dia_fim) {
        this.dia_fim = dia_fim;
    }

    private String recorrencia;
    public Deposito_db() {
    }

    public String getDescricao() {
        return descricao;
    }


    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public double getValor() {
        return Valor;
    }

    public void setValor(double valor) {
        Valor = valor;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public Date getDia() {
        return dia;
    }

    public void setDia(Date dia) {
        this.dia = dia;
    }

    public String getRecorrencia() {
        return recorrencia;
    }

    public void setRecorrencia(String recorrencia) {
        this.recorrencia = recorrencia;
    }
}
