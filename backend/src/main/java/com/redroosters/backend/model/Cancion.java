package com.redroosters.backend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "canciones")
public class Cancion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String descripcion;

    private String duracion;

    private String portada;

    private String urlAudio;

    @ManyToOne
    @JoinColumn(name = "artista_id")
    private Artista artista;

    @ManyToOne
    private Album album;

    public Cancion() {
    }

    public Cancion(Long id, String titulo, String descripcion, String duracion, String portada, String urlAudio, Artista artista, Album album) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.duracion = duracion;
        this.portada = portada;
        this.urlAudio = urlAudio;
        this.artista = artista;
        this.album = album;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public String getPortada() {
        return portada;
    }

    public void setPortada(String portada) {
        this.portada = portada;
    }

    public String getUrlAudio() {
        return urlAudio;
    }

    public void setUrlAudio(String urlAudio) {
        this.urlAudio = urlAudio;
    }

    public Artista getArtista() {
        return artista;
    }

    public void setArtista(Artista artista) {
        this.artista = artista;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }
}
