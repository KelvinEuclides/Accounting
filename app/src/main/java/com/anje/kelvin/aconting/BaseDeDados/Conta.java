package com.anje.kelvin.aconting.BaseDeDados;
import io.realm.RealmObject;

/**
 * Created by sala on 06-02-2018.
 */

public class Conta extends RealmObject {

    private  int id_usuario;
    private String nomeConta;
    private double saldo_conta=0;
    private boolean loggado=false;
    private int telemovel;
    private int pin;
    private Boolean notificacoes=true;
    private Boolean Alertas=true;

    public Boolean getNotificacoes() {
        return notificacoes;
    }

    public void setNotificacoes(Boolean notificacoes) {
        this.notificacoes = notificacoes;
    }

    public Boolean getAlertas() {
        return Alertas;
    }

    public void setAlertas(Boolean alertas) {
        Alertas = alertas;
    }

    public Conta() {
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getNomeConta() {
        return nomeConta;
    }

    public void setNomeConta(String nomeConta) {
        this.nomeConta = nomeConta;
    }

    public double getSaldo_conta() {
        return saldo_conta;
    }

    public void setSaldo_conta(double saldo_conta) {
        this.saldo_conta = saldo_conta;
    }

    public boolean isLoggado() {
        return loggado;
    }

    public void setLoggado(boolean loggado) {
        this.loggado = loggado;
    }

    public int getTelemovel() {
        return telemovel;
    }

    public void setTelemovel(int telemovel) {
        this.telemovel = telemovel;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public void adicionar_item(double val){
        saldo_conta-=val;
    }
    public void adicionar_deposito(double val){
        saldo_conta+=val;
    }
    public void adicionar_despesa(double val){
        saldo_conta-=val;
    }
}
