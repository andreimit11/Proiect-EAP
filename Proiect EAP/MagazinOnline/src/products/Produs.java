package products;

import db.DBFunctions;

import java.sql.Connection;

public class Produs {
    private String id_produs;
    private String nume_produs;
    private Double pret_produs;
    private Integer cantitate_produs;

    public Produs(String id_produs, String nume_produs, Double pret_produs, Integer cantitate_produs) {
        this.id_produs = id_produs;
        this.nume_produs = nume_produs;
        this.pret_produs = pret_produs;
        this.cantitate_produs = cantitate_produs;
    }

    public Produs() {
    }

    public String getId_produs() {
        return id_produs;
    }

    public String getNume_produs() {
        return nume_produs;
    }

    public Double getPret_produs() {
        return pret_produs;
    }

    public Integer getCantitate_produs() {
        return cantitate_produs;
    }

}
