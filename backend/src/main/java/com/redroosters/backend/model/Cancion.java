package com.redroosters.backend.model;

import jakarta.persistence.*;

// Entidad JPA que representa a un Cancion

@Entity
@Table(name = "canciones")
public class Cancion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String descripcion;

    private Integer duracionSegundos = 0;

    private String portada;

    private String urlAudio;

    @ManyToOne
    @JoinColumn(name = "artista_id")
    private Artista artista;

    @ManyToOne
    private Album album;

    @OneToMany(
            mappedBy = "cancion",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private java.util.List<Escucha> escuchas = new java.util.ArrayList<>();


    public Cancion() {
    }

    public Cancion(Long id, String titulo, String descripcion, Integer duracionSegundos, String portada, String urlAudio, Artista artista, Album album) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.duracionSegundos = duracionSegundos;
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

    public Integer getDuracionSegundos() {
        return duracionSegundos;
    }

    public void setDuracionSegundos(Integer duracionSegundos) {
        this.duracionSegundos = duracionSegundos;
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
