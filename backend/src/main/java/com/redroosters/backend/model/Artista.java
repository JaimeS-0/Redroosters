package com.redroosters.backend.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "artistas")
public class Artista {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String descripcion;

    private String portada;  // URL a imagen

    private boolean destacado;

    @OneToMany(mappedBy = "artista", cascade = CascadeType.ALL)
    private List<Cancion> canciones;

    @OneToMany(mappedBy = "artista", cascade = CascadeType.ALL)
    private List<Album> albumes;


    public Artista() {
    }

    public Artista(Long id, String nombre, String descripcion, String portada, boolean destacado, List<Cancion> canciones, List<Album> albumes) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.portada = portada;
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

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPortada() {
        return portada;
    }

    public void setPortada(String portada) {
        this.portada = portada;
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
