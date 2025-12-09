package com.redroosters.backend.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

// Entidad JPA que representa a un Album

@Entity
@Table(name = "albumes")
public class Album {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    private String descripcion;

    private String portadaUrl;

    @ManyToOne(optional = false)
    private Artista artista;

    @OneToMany(
            mappedBy = "album",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Cancion> canciones = new ArrayList<>();


    public Album(){}

    public Album(Long id, String titulo, String descripcion, String portadaUrl, Artista artista, List<Cancion> canciones) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.portadaUrl = portadaUrl;
        this.artista = artista;
        this.canciones = canciones;
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

    public String getPortadaUrl() {
        return portadaUrl;
    }

    public void setPortadaUrl(String portadaUrl) {
        this.portadaUrl = portadaUrl;
    }

    public Artista getArtista() {
        return artista;
    }

    public void setArtista(Artista artista) {
        this.artista = artista;
    }

    public List<Cancion> getCanciones() {
        return canciones;
    }

    public void setCanciones(List<Cancion> canciones) {
        this.canciones = canciones;
    }
}
