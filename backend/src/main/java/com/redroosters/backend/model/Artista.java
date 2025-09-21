package com.redroosters.backend.model;

import jakarta.persistence.*;

import java.util.List;

// Entidad JPA que representa a un Artista

@Entity
@Table(name = "artistas")
public class Artista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Foto para nombre
    // Buscador global canciones albumes y artistas
    private String nombre;

    // Foto destacado de home
    private String urlNombre;

    private String descripcion;

    private String portadaUrl;  // URL a imagen

    private boolean destacado;

    @OneToMany(mappedBy = "artista", cascade = CascadeType.ALL)
    private List<Cancion> canciones;

    @OneToMany(mappedBy = "artista", cascade = CascadeType.ALL)
    private List<Album> albumes;


    public Artista() {
    }

    public Artista(Long id, String nombre, String urlNombre, String descripcion, String portadaUrl, boolean destacado, List<Cancion> canciones, List<Album> albumes) {
        this.id = id;
        this.nombre = nombre;
        this.urlNombre = urlNombre;
        this.descripcion = descripcion;
        this.portadaUrl = portadaUrl;
        this.destacado = destacado;
        this.canciones = canciones;
        this.albumes = albumes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getUrlNombre() {
        return urlNombre;
    }

    public void setUrlNombre(String urlNombre) {
        this.urlNombre = urlNombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
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

    public boolean isDestacado() {
        return destacado;
    }

    public void setDestacado(boolean destacado) {
        this.destacado = destacado;
    }

    public List<Cancion> getCanciones() {
        return canciones;
    }

    public void setCanciones(List<Cancion> canciones) {
        this.canciones = canciones;
    }

    public List<Album> getAlbumes() {
        return albumes;
    }

    public void setAlbumes(List<Album> albumes) {
        this.albumes = albumes;
    }
}
