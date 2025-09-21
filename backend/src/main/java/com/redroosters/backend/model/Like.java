package com.redroosters.backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

// Entidad JPA que representa a un Like

@Entity
@Table(name = "likes")
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Usuario usuario;

    @ManyToOne
    private Cancion cancion;

    private LocalDateTime fecha;

    public Like() {
    }

    public Like(Long id, Usuario usuario, Cancion cancion, LocalDateTime fecha) {
        this.id = id;
        this.usuario = usuario;
        this.cancion = cancion;
        this.fecha = fecha;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Cancion getCancion() {
        return cancion;
    }

    public void setCancion(Cancion cancion) {
        this.cancion = cancion;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
}
