package com.anje.kelvin.aconting.BaseDeDados;
import io.realm.RealmObject;

/**
 * Created by sala on 23-02-2018.
 */

public class Definicoes extends RealmObject {
    private  boolean notificacoes;
    private boolean alertas;
    private boolean registar_receitas;
    private boolean requisitar_pin;

    public boolean isNotificacoes() {
        return notificacoes;
    }

    public void setNotificacoes(boolean notificacoes) {
        this.notificacoes = notificacoes;
    }

    public boolean isAlertas() {
        return alertas;
    }

    public void setAlertas(boolean alertas) {
        this.alertas = alertas;
    }

    public boolean isRegistar_receitas() {
        return registar_receitas;
    }

    public void setRegistar_receitas(boolean registar_receitas) {
        this.registar_receitas = registar_receitas;
    }

    public boolean isRequisitar_pin() {
        return requisitar_pin;
    }

    public void setRequisitar_pin(boolean requisitar_pin) {
        this.requisitar_pin = requisitar_pin;
    }
}
