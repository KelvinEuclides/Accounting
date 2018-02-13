package com.anje.kelvin.aconting.BaseDeDados;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by sala on 06-02-2018.
 */

public class Usuario extends RealmObject {

    public Usuario() {
    }

    public String username;
    public int password;
    public int telefone;
    public int id_usuario;

    public Usuario(String username, int password, int telefone) {
        this.username = username;
        this.password = password;
        this.telefone = telefone;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getPassword() {
        return password;
    }

    public void setPassword(int password) {
        this.password = password;
    }

    public int getTelefone() {
        return telefone;
    }

    public void setTelefone(int telefone) {
        this.telefone = telefone;
    }
}
