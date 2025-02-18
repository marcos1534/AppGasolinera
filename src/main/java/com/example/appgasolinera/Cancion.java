package com.example.appgasolinera;

public class Cancion {
    private int id;
    private String titulo;
    private String autor;
    private int duracion;
    private int imagen;
    private int audio;

    public Cancion(){}
    public Cancion(String titulo, String autor, int duracion, int imagen, int audio) {
        this.titulo = titulo;
        this.autor = autor;
        this.duracion = duracion;
        this.imagen = imagen;
        this.audio = audio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public int getAudio() {
        return audio;
    }

    public void setAudio(int audio) {
        this.audio = audio;
    }
}
