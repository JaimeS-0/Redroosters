package com.redroosters.backend.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

// Entidad JPA que representa a una Escucha

@Entity
@Table(name = "escuchas")
public class Escucha {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private Usuario usuario;

    @ManyToOne(optional = false)
    private Cancion cancion;

    private Long vecesEscuchada = 1L;

    private LocalDateTime ultimaEscucha;

    public Escucha(){}

    public Escucha(Long id, Usuario usuario, Cancion cancion, Long vecesEscuchada, LocalDateTime ultimaEscucha) {
        this.id = id;
        this.usuario = usuario;
        this.cancion = cancion;
        this.vecesEscuchada = vecesEscuchada;
        this.ultimaEscucha = ultimaEscucha;
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

    public long getVecesEscuchada() {
        return vecesEscuchada;
    }

    public void setVecesEscuchada(Long vecesEscuchada) {
        this.vecesEscuchada = vecesEscuchada;
    }

    public LocalDateTime getUltimaEscucha() {
        return ultimaEscucha;
    }

    public void setUltimaEscucha(LocalDateTime ultimaEscucha) {
        this.ultimaEscucha = ultimaEscucha;
    }
}
