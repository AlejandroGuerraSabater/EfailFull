package com.evadethefail.app.entidades;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

@Entity
public class EstadisticasPartida {
    @Id
    private long id;
    private int enemigosDerrotados;
    private int vidaPerdida;
    private int danoCausado;
    private int danoRecibido;
    private int cartasUsadas;
    private int criticosCausadosJugador;
    private int criticosCausadosContraJugador;

    private ToOne<EstadisticasJugador> estadisticasJugador;

    // Relación con la partida
    private ToOne<Partida> partida;

    public EstadisticasPartida() {
        this.enemigosDerrotados = 0;
        this.vidaPerdida = 0;
        this.danoCausado = 0;
        this.danoRecibido = 0;
        this.cartasUsadas = 0;
        this.criticosCausadosJugador = 0;
        this.criticosCausadosContraJugador = 0;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getEnemigosDerrotados() {
        return enemigosDerrotados;
    }

    public void setEnemigosDerrotados(int enemigosDerrotados) {
        this.enemigosDerrotados = enemigosDerrotados;
    }

    public void enemigosDerrotadosSumar(int sum){
        this.enemigosDerrotados += sum;
    }

    public int getVidaPerdida() {
        return vidaPerdida;
    }

    public void setVidaPerdida(int vidaPerdida) {
        this.vidaPerdida = vidaPerdida;
    }

    public void vidaPerdidaSumar(int sum){
        this.vidaPerdida += sum;
    }

    public int getDanoCausado() {
        return danoCausado;
    }

    public void setDanoCausado(int danoCausado) {
        this.danoCausado = danoCausado;
    }

    public void danoCausadoSumar(int sum){
        this.danoCausado += sum;
    }

    public int getDanoRecibido() {
        return danoRecibido;
    }

    public void setDanoRecibido(int danoRecibido) {
        this.danoRecibido = danoRecibido;
    }

    public void danoRecibidoSumar(int sum){
        this.danoRecibido += sum;
    }

    public int getCartasUsadas() {
        return cartasUsadas;
    }

    public void setCartasUsadas(int cartasUsadas) {
        this.cartasUsadas = cartasUsadas;
    }

    public void cartasUsadasSumar(int sum){
        this.cartasUsadas += sum;
    }

    public int getCriticosCausadosJugador() {
        return criticosCausadosJugador;
    }

    public void setCriticosCausadosJugador(int criticosCausadosJugador) {
        this.criticosCausadosJugador = criticosCausadosJugador;
    }

    public void criticosCausadosJugadorSumar(int sum){
        this.criticosCausadosJugador += sum;
    }


    public int getCriticosCausadosContraJugador() {
        return criticosCausadosContraJugador;
    }

    public void setCriticosCausadosContraJugador(int criticosCausadosContraJugador) {
        this.criticosCausadosContraJugador = criticosCausadosContraJugador;
    }

    public void criticosCausadosContraJugadorSumar(int sum){
        this.criticosCausadosContraJugador += sum;
    }


    public ToOne<EstadisticasJugador> getEstadisticasJugador() {
        return estadisticasJugador;
    }

    public void setEstadisticasJugador(ToOne<EstadisticasJugador> estadisticasJugador) {
        this.estadisticasJugador = estadisticasJugador;
    }

    public ToOne<Partida> getPartida() {
        return partida;
    }

    public void setPartida(ToOne<Partida> partida) {
        this.partida = partida;
    }

}
