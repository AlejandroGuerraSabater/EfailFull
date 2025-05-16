package com.evadethefail.app.entidades;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;
import io.objectbox.relation.ToOne;

@Entity
public class EstadisticasJugador {@Id
    private long id;
    private int vecesUsadoCaballero;
    private int vecesUsadoAsesino;
    private int vecesUsadoHechicera;
    private String personajeFavorito;
    private int enemigosDerrotados;
    private int vidaPerdida;
    private int danoCausado;
    private int danoRecibido;
    private int cartasUsadas;
    private int criticosCausadosJugador;
    private int criticosCausadosContraJugador;
    private int partidasJugadas;
    private int danoMaximo;

    @Backlink(to = "estadisticasJugador")
    public ToMany<EstadisticasPartida> estadisticasPartidas;

    // Relación con el jugador
    private ToOne<Jugador> jugador;

    public EstadisticasJugador() {}

    public EstadisticasJugador(Jugador jugador) {
        this.jugador.setTarget(jugador);
        this.vecesUsadoCaballero = 0;
        this.vecesUsadoAsesino = 0;
        this.vecesUsadoHechicera = 0;
        this.enemigosDerrotados = 0;
        this.vidaPerdida = 0;
        this.danoCausado = 0;
        this.danoRecibido = 0;
        this.cartasUsadas = 0;
        this.criticosCausadosJugador = 0;
        this.criticosCausadosContraJugador = 0;
        this.partidasJugadas = 0;
        this.danoMaximo = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getVecesUsadoCaballero() {
        return vecesUsadoCaballero;
    }

    public void setVecesUsadoCaballero(int vecesUsadoCaballero) {
        this.vecesUsadoCaballero = vecesUsadoCaballero;
    }

    public int getVecesUsadoAsesino() {
        return vecesUsadoAsesino;
    }

    public void setVecesUsadoAsesino(int vecesUsadoAsesino) {
        this.vecesUsadoAsesino = vecesUsadoAsesino;
    }

    public int getVecesUsadoHechicera() {
        return vecesUsadoHechicera;
    }

    public void setVecesUsadoHechicera(int vecesUsadoHechicera) {
        this.vecesUsadoHechicera = vecesUsadoHechicera;
    }

    public void setPersonajeFavorito() {

        this.personajeFavorito = "Todos";

        if (this.vecesUsadoCaballero == this.vecesUsadoAsesino && this.vecesUsadoAsesino == this.vecesUsadoHechicera) {
            this.personajeFavorito = "Todos";
        }

        if (this.vecesUsadoCaballero > this.vecesUsadoAsesino && this.vecesUsadoCaballero > this.vecesUsadoHechicera) {
            this.personajeFavorito = "Caballero";
        }

        if (this.vecesUsadoAsesino > this.vecesUsadoCaballero && this.vecesUsadoAsesino > this.vecesUsadoHechicera) {
            this.personajeFavorito = "Asesino";
        }

        if (this.vecesUsadoHechicera > this.vecesUsadoCaballero && this.vecesUsadoHechicera > this.vecesUsadoAsesino) {
            this.personajeFavorito = "Hechicera";
        }

        // Empates entre dos
        if (this.vecesUsadoCaballero == this.vecesUsadoAsesino || this.vecesUsadoCaballero == this.vecesUsadoHechicera) {
            this.personajeFavorito = "Caballero";
        }

    }

    public String getPersonajeFavorito() {
        return personajeFavorito;
    }

    public int getEnemigosDerrotados() {
        return enemigosDerrotados;
    }

    public void setEnemigosDerrotados(int enemigosDerrotados) {
        this.enemigosDerrotados = enemigosDerrotados;
    }

    public int getVidaPerdida() {
        return vidaPerdida;
    }

    public void setVidaPerdida(int vidaPerdida) {
        this.vidaPerdida = vidaPerdida;
    }

    public int getDanoCausado() {
        return danoCausado;
    }

    public void setDanoCausado(int danoCausado) {
        this.danoCausado = danoCausado;
    }

    public int getDanoRecibido() {
        return danoRecibido;
    }

    public void setDanoRecibido(int danoRecibido) {
        this.danoRecibido = danoRecibido;
    }

    public int getCartasUsadas() {
        return cartasUsadas;
    }

    public void setCartasUsadas(int cartasUsadas) {
        this.cartasUsadas = cartasUsadas;
    }

    public int getCriticosCausadosJugador() {
        return criticosCausadosJugador;
    }

    public void setCriticosCausadosJugador(int criticosCausadosJugador) {
        this.criticosCausadosJugador = criticosCausadosJugador;
    }

    public int getCriticosCausadosContraJugador() {
        return criticosCausadosContraJugador;
    }

    public void setCriticosCausadosContraJugador(int criticosCausadosContraJugador) {
        this.criticosCausadosContraJugador = criticosCausadosContraJugador;
    }

    public int getPartidasJugadas() {
        return partidasJugadas;
    }

    public void setPartidasJugadas(int partidasJugadas) {
        this.partidasJugadas = partidasJugadas;
    }

    public ToMany<EstadisticasPartida> getEstadisticasPartidas() {
        return estadisticasPartidas;
    }

    public void setEstadisticasPartidas(ToMany<EstadisticasPartida> estadisticasPartidas) {
        this.estadisticasPartidas = estadisticasPartidas;
    }

    public ToOne<Jugador> getJugador() {
        return jugador;
    }

    public void setJugador(ToOne<Jugador> jugador) {
        this.jugador = jugador;
    }

    public int getDanoMaximo() {
        return danoMaximo;
    }

    public void setDanoMaximo(int danoMaximo) {
        this.danoMaximo = danoMaximo;
    }

    public void vecesUsadoCaballeroSumar(int valor) {
        this.vecesUsadoCaballero += valor;
    }

    public void vecesUsadoAsesinoSumar(int valor) {
        this.vecesUsadoAsesino += valor;
    }

    public void vecesUsadoHechiceraSumar(int valor) {
        this.vecesUsadoHechicera += valor;
    }

    public void enemigosDerrotadosSumar(int valor) {
        this.enemigosDerrotados += valor;
    }

    public void vidaPerdidaSumar(int valor) {
        this.vidaPerdida += valor;
    }

    public void danoCausadoSumar(int valor) {
        this.danoCausado += valor;
    }

    public void danoRecibidoSumar(int valor) {
        this.danoRecibido += valor;
    }

    public void cartasUsadasSumar(int valor) {
        this.cartasUsadas += valor;
    }

    public void criticosCausadosJugadorSumar(int valor) {
        this.criticosCausadosJugador += valor;
    }

    public void criticosCausadosContraJugadorSumar(int valor) {
        this.criticosCausadosContraJugador += valor;
    }

    public void partidasJugadasSumar(int valor) {
        this.partidasJugadas += valor;
    }

}
