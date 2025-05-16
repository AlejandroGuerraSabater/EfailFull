package com.evadethefail.app.objetos;

public class MensajeCombate {
    public final String texto;
    public final boolean esDelJugador; // true = verde, false = rojo
    public long timestamp; // para controlar el desvanecimiento

    public MensajeCombate(String texto, boolean esDelJugador) {
        this.texto = texto;
        this.esDelJugador = esDelJugador;
        this.timestamp = System.currentTimeMillis();
    }
}
