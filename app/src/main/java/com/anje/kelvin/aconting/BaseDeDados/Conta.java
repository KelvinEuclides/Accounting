package com.anje.kelvin.aconting.BaseDeDados;

import io.realm.Realm;
import io.realm.RealmList;
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

    private int pin;
    private RealmList<Despesa_db> despesa_dbs;
    private RealmList<Deposito_db> deposito_dbs;
    private RealmList<Transacao_db> transacaoDbs;
    private RealmList<Item> stock;
    private RealmList<Venda> vendas;

    public RealmList<Venda> getVendas() {
        return vendas;
    }

    public void setVendas(Venda venda) {
        vendas.add(venda);
    }

    public Conta(String nomeConta, double saldo_conta) {
        this.nomeConta = nomeConta;
        this.saldo_conta = saldo_conta;
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

    public Conta() {
    }

    public RealmList<Despesa_db> getDespesa_dbs() {
        return despesa_dbs;
    }

    public void setDespesa_dbs(Despesa_db despesa_db) {
        try {
            saldo_conta=saldo_conta-despesa_db.getValor();
            Transacao_db transacao_db=new Transacao_db();
            transacao_db.setCategoria("Despesa");
            transacao_db.setDescricao(despesa_db.getDescricao());
            transacao_db.setDia(despesa_db.getDia());
            transacao_db.setRecorrencia(despesa_db.getRecorrencia());
            transacao_db.setValor(despesa_db.getValor());
            transacaoDbs.add(transacao_db);
            despesa_dbs.add(despesa_db);
        }catch (NullPointerException e){

        }

    }

    public RealmList<Deposito_db> getDeposito_dbs() {
        return deposito_dbs;
    }

    public void setDeposito_dbs(Deposito_db deposito_db) {
        try{
            saldo_conta=saldo_conta+deposito_db.getValor();
            Transacao_db transacao_db=new Transacao_db();
            transacao_db.setCategoria("Deposito");
            transacao_db.setDescricao(deposito_db.getDescricao());
            transacao_db.setValor(deposito_db.getValor());
            transacao_db.getDia(deposito_db.getDia());
            transacao_db.getRecorrencia(deposito_db.getRecorrencia());
            transacaoDbs.add(transacao_db);
            deposito_dbs.add(deposito_db);
        }catch (NullPointerException e){

        }

    }

    public RealmList<Transacao_db> getTransacaoDbs() {
        return transacaoDbs;
    }


    public void setTransacaoDbs(Transacao_db transacaoDb) {
        transacaoDbs.add(transacaoDb);

    }

    public RealmList<Item> getStock() {
        return stock;
    }

    public void setStock(Item stocks) {
        saldo_conta=saldo_conta-stocks.getPreco();
        stock.add(stocks);
    }
}
