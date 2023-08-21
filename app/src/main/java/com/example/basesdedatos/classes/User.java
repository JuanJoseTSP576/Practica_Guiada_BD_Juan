package com.example.basesdedatos.classes;

public class User {
    // Declaramos variables
    private int document;
    private String user;
    private String names;
    private String lastNames;
    private String pass;
    private int status;


    // Constructores
    public User(int document, String user, String names, String lastNames, String pass, int status) {
        this.document = document;
        this.user = user;
        this.names = names;
        this.lastNames = lastNames;
        this.pass = pass;
        this.status = status;
    }

    public User() {

    }

    // Getters y Setters
    public int getDocument() {
        return document;
    }

    public void setDocument(int document) {
        this.document = document;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public String getLastNames() {
        return lastNames;
    }

    public void setLastNames(String lastNames) {
        this.lastNames = lastNames;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public int getStatus() { // Getter para status
        return status;
    }

    public void setStatus(int status) { // Setter para status
        this.status = status;
    }

    // Método ToString, se ve mejor sin las llaves porque antes daba la apariencia como de un JSON
    @Override
    public String toString() {
        return "Documento: " + document + "\n" + //Usamos el \n como salto de línea
                "Usuario: " + user + "\n" +
                "Nombres: " + names + "\n" +
                "Apellidos: " + lastNames +  "\n" +
                "Contraseña: "+ pass + "\n" +
                "Estado: "+ (status == 1 ? "Activo" : "Inactivo"); // Aquí se muestra "Activo" si status es 1, de lo contrario "Inactivo"
    }
}

