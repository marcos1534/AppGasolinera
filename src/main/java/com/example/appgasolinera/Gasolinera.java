package com.example.appgasolinera;

import java.io.Serializable;

public class Gasolinera implements Serializable {
    private int id;
    public String gasolinera;
    public String precios;
    public int gasImage;

    public Gasolinera(int id, String gasolinera, String precios, int gasolineraImg) {
        this.id = id;
        this.gasolinera = gasolinera;
        this.precios = precios;
        this.gasImage = gasolineraImg;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGasolinera() {
        return gasolinera;
    }

    public void setGasolinera(String gasolinera) {
        this.gasolinera = gasolinera;
    }

    public String getPrecios() {
        return precios;
    }

    public void setPrecios(String precios) {
        this.precios = precios;
    }

    public int getGasImage() {
        return gasImage;
    }

    public void setGasImage(int gasImage) {
        this.gasImage = gasImage;
    }
}
