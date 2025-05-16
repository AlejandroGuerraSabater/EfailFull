package com.evadethefail.app.controlador;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.evadethefail.app.entidades.Ataque;
import com.evadethefail.app.entidades.Ataque_;
import com.evadethefail.app.entidades.Efecto;
import com.evadethefail.app.entidades.Marca;
import com.evadethefail.app.modelo.PartidaActivity;
import com.evadethefail.app.objectBox.App;
import com.evadethefail.app.objectBox.Utilidades;
import com.evadethefail.app.vista.CombateFragment;
import io.objectbox.Box;
import io.objectbox.BoxStore;
/** @noinspection ALL*/
public class MetodosMarcas {

    private CombateFragment combate = CombateFragment.instanciaCombate;
    private ControladorCombate controladorCombate = ControladorCombate.instanciaControlador;

    private static Map<String, Runnable> combinacionesElementales = new HashMap<>();
    private static boolean inicializado = false;

    private static final BoxStore boxStore = App.getBoxStore();
    private static final Box<Ataque> ataqueBox = boxStore.boxFor(Ataque.class);

    /**
     * Confusión se llama al final del turno. Existe 1/3 de probabilidades de golpearte a ti mismo y hacerte un daño en base a tu nivel.
     * @param esJugador
     */
    public void confusion(boolean esJugador) {

        Random random = new Random(); // Generador de números aleatorios
        int resultado = random.nextInt(3);
        if (resultado != 0) { // 1/3 de probabilidad (valores 0, 1, 2)

            if (esJugador){
                int potencia = 10 * PartidaActivity.personaje.getNivel();
                int dano = controladorCombate.calcularDano(
                        potencia,
                        PartidaActivity.personaje.getNivel(),
                        PartidaActivity.personaje.getAtaque(),
                        PartidaActivity.personaje.getDefensa(),
                        0, 0, null, esJugador
                );
                controladorCombate.mostrarMensaje("¡Estás tan confuso que te heriste a ti mismo!\n¡Te hiciste " + dano + " de Daño!", false);
                controladorCombate.danar(true, dano);
            } else {
                int potencia = 10 * PartidaActivity.enemigo.getNivel();
                int dano = controladorCombate.calcularDano(
                        potencia,
                        PartidaActivity.enemigo.getNivel(),
                        PartidaActivity.enemigo.getAtaqueReal(),
                        PartidaActivity.enemigo.getDefensaReal(),
                        0, 0, null, esJugador
                );
                controladorCombate.mostrarMensaje("¡Está tan confuso que se hirió a sí mismo!\n¡El enemigo se hizo " + dano + " de Daño!", true);
                controladorCombate.danar(false, dano);
            }

        } else {
            if (esJugador){
                controladorCombate.mostrarMensaje("¡Estás confundido!", false);
            } else {
                controladorCombate.mostrarMensaje("¡El enemigo está confuso!", true);
            }
        }
    }

    /**
     * Sangrado se llama en las reacciones. Al ser golpeado afecta con "sangrado" que baja un 10% ataque y defensa.
     * Como extra, si el ataque recibido es Cortante, realiza un daño extra.
     * @param esJugador Mide quien ES el que sangra y por ende se perjudica del efecto
     */
    public void sangrado(boolean esJugador) {

        if (esJugador){
            for (Efecto efecto : Utilidades.buscaEfectos(28)) {
            PartidaActivity.personaje.agregarEfecto(combate, efecto);
            }

            if (ControladorCombate.siguienteAtaque.getTiposCompleto().contains("Cortante")) {
                int potencia = 4 * PartidaActivity.enemigo.getNivel();
                int dano = controladorCombate.calcularDano(
                        potencia,
                        PartidaActivity.enemigo.getNivel(),
                        PartidaActivity.enemigo.getAtaqueReal(),
                        PartidaActivity.personaje.getDefensaReal(),
                        0, 0, null, esJugador
                );
                controladorCombate.mostrarMensaje("¡Estás sangrando!\n¡Te hiciste " + dano + " de Daño!", false);
                controladorCombate.danar(true, dano);
            }

        } else {
            for (Efecto efecto : Utilidades.buscaEfectos(28)) {
                PartidaActivity.enemigo.agregarEfecto(combate, efecto);
            }

            if (ControladorCombate.cartaUsada.getTiposCompleto().contains("Cortante")) {
                int potencia = 5 * PartidaActivity.enemigo.getNivel();
                int dano = controladorCombate.calcularDano(
                        potencia,
                        PartidaActivity.personaje.getNivel(),
                        PartidaActivity.personaje.getAtaqueReal(),
                        PartidaActivity.enemigo.getDefensaReal(),
                        0, 0, null, esJugador
                );
                controladorCombate.mostrarMensaje("¡El enemigo está sangrando!\n¡El enemigo se hizo " + dano + " de Daño!", true);
                controladorCombate.danar(false, dano);
            }

        }
    }

    /**
     * Vampirismo se llama en las acciones. Cura al afectado una porción del daño realizado.
     * @param esJugador Mide quién se BENEFICIA de Vampirismo
     */
    public void vampirismo(boolean esJugador) {

        if (esJugador){

            if (ControladorCombate.siguienteAtaque.getTiposCompleto().contains("Cortante")) {
                int potencia = ControladorCombate.cartaUsada.getPotencia()/5;
                int dano = controladorCombate.calcularDano(
                        potencia,
                        PartidaActivity.personaje.getNivel(),
                        PartidaActivity.personaje.getAtaqueReal(),
                        PartidaActivity.enemigo.getDefensaReal(),
                        0, 0, null, esJugador
                );

                PartidaActivity.personaje.curar(dano);
                if (dano != 0)
                    controladorCombate.mostrarMensaje("¡Te curaste " + dano + " de vida!", true);
            }

        } else {

            if (ControladorCombate.cartaUsada.getTiposCompleto().contains("Cortante")) {
                int potencia = ControladorCombate.siguienteAtaque.getPotencia()/2;
                int dano = controladorCombate.calcularDano(
                        potencia,
                        PartidaActivity.enemigo.getNivel(),
                        PartidaActivity.enemigo.getAtaqueReal(),
                        PartidaActivity.personaje.getDefensaReal(),
                        0, 0, null, esJugador
                );


                PartidaActivity.enemigo.curar(dano);
                if (dano != 0)
                    controladorCombate.mostrarMensaje("¡El enemigo se curó " + dano + " de vida!", false);
            }

        }
    }

    public void veneno(boolean esJugador) {

        if (esJugador){
            controladorCombate.mostrarMensaje("¡Estás envenenado!\n¡Pierdes " + 7 + " puntos de vida!", false);
            controladorCombate.danar(true, 7);
        } else {

            controladorCombate.mostrarMensaje("¡El enemigo está envenenado!\n¡El enemigo pierde " + 7 + " puntos de vida!", true);
            controladorCombate.danar(false, 7);
        }

    }


    public static double revisaTraits(boolean esJugador, List<String> traitsPersonaje,  List<String> traitsEnemigo, List<String> tipos){
        double multiplicadorFinal = 1;

        List<String> traitsOfensivos = esJugador ? traitsPersonaje : traitsEnemigo;
        List<String> traitsDefensivos = esJugador ? traitsEnemigo : traitsPersonaje;

        for ( String trait : traitsOfensivos ){
            for ( String tipo : tipos){
                if ((trait).equals(tipo + " +")) multiplicadorFinal += 0.2;
            }
        }

        for ( String trait : traitsDefensivos ){
            for ( String tipo : tipos){
                if ((trait).equals(tipo + " -")) multiplicadorFinal -= 0.2;
            }
        }

        if (traitsDefensivos.contains("Elemento") && tipos.contains("Magia elemental")){
            String elemento1 = buscaElementoDefensivo(!esJugador);
            String elemento2 = buscaElementoOfensivo(esJugador);

            if (!inicializado){
                inicializado = true;

                // Registrar combinaciones
                combinacionesElementales.put("agua+agua", () -> osmosis(esJugador));
                combinacionesElementales.put("agua+fuego", () -> ebullicion(esJugador));
                combinacionesElementales.put("agua+hielo", () -> congelacion(esJugador));
                combinacionesElementales.put("agua+rayo", () -> electrocucion(esJugador));

                combinacionesElementales.put("fuego+fuego", () -> combustion(esJugador));
                combinacionesElementales.put("fuego+hielo", () -> vapor(esJugador));
                combinacionesElementales.put("fuego+rayo", () -> tormentaSolar(esJugador));

                combinacionesElementales.put("hielo+hielo", () -> ceroAbsoluto(esJugador));
                combinacionesElementales.put("hielo+rayo", () -> tormentaHelada(esJugador));

                combinacionesElementales.put("rayo+rayo", () -> sobrecarga(esJugador));
            }

            ControladorCombate.reaccion = true;
            ejecutarCombinacion(elemento1, elemento2);
        }

        return multiplicadorFinal;
    }

    public static void ejecutarCombinacion(String e1, String e2) {
        List<String> elementos = Arrays.asList(e1.toLowerCase(), e2.toLowerCase());
        Collections.sort(elementos); // Asegura que "agua+fuego" y "fuego+agua" se lean igual
        String clave = elementos.get(0) + "+" + elementos.get(1);

        Runnable accion = combinacionesElementales.get(clave);
        if (accion != null) {
            accion.run();
        } else {
            System.out.println("Sin combinación válida para: " + clave);
        }
    }

    /* ---------------------- EN LOS METODOS ELEMENTALES esJugador SIEMPRE ES EL QUE ATACA -----------------------*/

    private static void osmosis(boolean esJugador) {
        if (esJugador){

            int potencia = ControladorCombate.cartaUsada.getPotencia() / 2;
            int dano = ControladorCombate.instanciaControlador.calcularDano(
                    potencia,
                    PartidaActivity.personaje.getNivel(),
                    PartidaActivity.personaje.getAtaqueReal(),
                    PartidaActivity.enemigo.getDefensaReal(),
                    0, 0, null, esJugador
            );

            PartidaActivity.personaje.curar(dano/2);
            ControladorCombate.instanciaControlador.danar(false, dano);
            if (dano != 0)
                ControladorCombate.instanciaControlador.mostrarMensaje("¡Explosión de vida, robaste " + dano/2 + " puntos de vida e hiciste " + dano + " de daño!", true);


        } else {

            int potencia = ControladorCombate.siguienteAtaque.getPotencia() / 2;
            int dano = ControladorCombate.instanciaControlador.calcularDano(
                    potencia,
                    PartidaActivity.enemigo.getNivel(),
                    PartidaActivity.enemigo.getAtaqueReal(),
                    PartidaActivity.personaje.getDefensaReal(),
                    0, 0, null, esJugador
            );


            PartidaActivity.enemigo.curar(dano);
            ControladorCombate.instanciaControlador.danar(true, dano);
            if (dano != 0)
                ControladorCombate.instanciaControlador.mostrarMensaje("¡Explosión de vida, te robaron " + dano + " puntos de vida!", false);
        }
    }

    private static void ebullicion(boolean esJugador) {
        if (esJugador){
            PartidaActivity.enemigo.agregarEfecto(CombateFragment.instanciaCombate, Utilidades.buscaEfecto(35));
            ControladorCombate.instanciaControlador.mostrarMensaje("¡El enemigo siente dolor por el cambio de temperatura! ¡Baja su defensa!", true);
        } else {
            PartidaActivity.personaje.agregarEfecto(CombateFragment.instanciaCombate, Utilidades.buscaEfecto(35));
            ControladorCombate.instanciaControlador.mostrarMensaje("¡Sientes dolor por el cambio de temperatura! ¡Baja tu defensa!", false);
        }
    }

    private static void congelacion(boolean esJugador) {
        if (esJugador){
            PartidaActivity.enemigo.agregarEfecto(CombateFragment.instanciaCombate, Utilidades.buscaEfecto(9));
            ControladorCombate.instanciaControlador.mostrarMensaje("¡El enemigo no se mueve bien por el frío! ¡Baja su ataque!", true);
        } else {
            PartidaActivity.personaje.agregarEfecto(CombateFragment.instanciaCombate, Utilidades.buscaEfecto(9));
            ControladorCombate.instanciaControlador.mostrarMensaje("¡No te mueves bien por el frío! ¡Baja tu ataque!", false);
        }
    }

    private static void electrocucion(boolean esJugador) {
        if (esJugador){
            PartidaActivity.personaje.agregarEfecto(CombateFragment.instanciaCombate, Utilidades.buscaEfecto(3));
            ControladorCombate.instanciaControlador.mostrarMensaje("¡El enemigo no se puede defender! ¡Es la hora de los críticos!", true);
        } else {
            PartidaActivity.enemigo.agregarEfecto(CombateFragment.instanciaCombate, Utilidades.buscaEfecto(3));
            ControladorCombate.instanciaControlador.mostrarMensaje("¡No te puedes defender! ¡Cuidado con los críticos!", false);
        }
    }

    private static void combustion(boolean esJugador) {
        if (esJugador){

            int potencia = ControladorCombate.cartaUsada.getPotencia();
            int dano = ControladorCombate.instanciaControlador.calcularDano(
                    potencia,
                    PartidaActivity.personaje.getNivel(),
                    PartidaActivity.personaje.getAtaqueReal(),
                    PartidaActivity.enemigo.getDefensaReal(),
                    0, 0, null, esJugador
            );

            ControladorCombate.instanciaControlador.danar(false, dano);
            if (dano != 0)
                ControladorCombate.instanciaControlador.mostrarMensaje("¡Explosión! ¡Infliges " + dano + " puntos de daño extra!", true);


        } else {

            int potencia = ControladorCombate.siguienteAtaque.getPotencia();
            int dano = ControladorCombate.instanciaControlador.calcularDano(
                    potencia,
                    PartidaActivity.enemigo.getNivel(),
                    PartidaActivity.enemigo.getAtaqueReal(),
                    PartidaActivity.personaje.getDefensaReal(),
                    0, 0, null, esJugador
            );

            ControladorCombate.instanciaControlador.danar(true, dano);
            if (dano != 0)
                ControladorCombate.instanciaControlador.mostrarMensaje("¡Explosión! ¡Te infligen " + dano + " puntos de daño extra!", false);
        }
    }

    private static void vapor(boolean esJugador) {
        if (esJugador){
            PartidaActivity.enemigo.agregarEfecto(CombateFragment.instanciaCombate, Utilidades.buscaEfecto(36));
            ControladorCombate.instanciaControlador.mostrarMensaje("¡El enemigo no ve nada! ¡No te preocupes por sus ataques más fuertes!", true);
        } else {
            PartidaActivity.personaje.agregarEfecto(CombateFragment.instanciaCombate, Utilidades.buscaEfecto(36));
            ControladorCombate.instanciaControlador.mostrarMensaje("¡No ves nada! ¡No es el mejor momento para los críticos!", false);
        }
    }

    private static void tormentaSolar(boolean esJugador) {
        if (esJugador){

            int potencia = (int) (ControladorCombate.cartaUsada.getPotencia() * 1.5);
            int dano = ControladorCombate.instanciaControlador.calcularDano(
                    potencia,
                    PartidaActivity.personaje.getNivel(),
                    PartidaActivity.personaje.getAtaqueReal(),
                    PartidaActivity.enemigo.getDefensaReal(),
                    0, 0, null, esJugador
            );

            ControladorCombate.instanciaControlador.danar(false, dano);
            ControladorCombate.instanciaControlador.danar(true, dano/3);
            if (dano != 0)
                ControladorCombate.instanciaControlador.mostrarMensaje("¡Explosión descontrolada! ¡Infliges " + dano + " puntos de daño extra, pero también " + dano/3 + " a ti mismo!", true);

        } else {

            int potencia = (int) (ControladorCombate.siguienteAtaque.getPotencia() * 1.5);
            int dano = ControladorCombate.instanciaControlador.calcularDano(
                    potencia,
                    PartidaActivity.enemigo.getNivel(),
                    PartidaActivity.enemigo.getAtaqueReal(),
                    PartidaActivity.personaje.getDefensaReal(),
                    0, 0, null, esJugador
            );

            ControladorCombate.instanciaControlador.danar(true, dano);
            ControladorCombate.instanciaControlador.danar(false, dano/2);
            if (dano != 0)
                ControladorCombate.instanciaControlador.mostrarMensaje("¡Explosión descontrolada! ¡Te infligen " + dano + " puntos de daño extra, pero también " + dano/2 + " a él mismo!", false);
        }
    }

    private static void ceroAbsoluto(boolean esJugador) {
        if (esJugador){
            PartidaActivity.personaje.agregarEfecto(CombateFragment.instanciaCombate, Utilidades.buscaEfecto(37));
            ControladorCombate.instanciaControlador.mostrarMensaje("¡Eres duro como un iceberg! ¡Ganas una enorme defensa!", true);
        } else {
            PartidaActivity.enemigo.agregarEfecto(CombateFragment.instanciaCombate, Utilidades.buscaEfecto(37));
            ControladorCombate.instanciaControlador.mostrarMensaje("¡El enemigo es duro como un iceberg! ¡Gana una enorme defensa!", false);
        }
    }

    private static void tormentaHelada(boolean esJugador) {
        if (esJugador){
            PartidaActivity.personaje.agregarEfecto(CombateFragment.instanciaCombate, Utilidades.buscaEfecto(25));
            ControladorCombate.instanciaControlador.mostrarMensaje("¡Las partículas inestables del ambiente potencian tus ataques!", true);
        } else {
            PartidaActivity.enemigo.agregarEfecto(CombateFragment.instanciaCombate, Utilidades.buscaEfecto(25));
            ControladorCombate.instanciaControlador.mostrarMensaje("¡Las partículas inestables del ambiente potencian los ataques enemigos!", false);
        }
    }

    private static void sobrecarga(boolean esJugador) {
        if (esJugador){
            PartidaActivity.personaje.agregarEfecto(CombateFragment.instanciaCombate, Utilidades.buscaEfecto(18));
            ControladorCombate.instanciaControlador.mostrarMensaje("¡Tienes que descargar energía, tu siguiente ataque será muy poderoso!", true);
        } else {
            PartidaActivity.enemigo.agregarEfecto(CombateFragment.instanciaCombate, Utilidades.buscaEfecto(18));
            ControladorCombate.instanciaControlador.mostrarMensaje("¡El enemigo está cargado de energía, cuidado con su siguiente ataque!", false);
        }
    }

    public static String buscaElementoDefensivo(boolean esJugador){
        // Seleccionar la lista de efectos según el booleano
        List<Efecto> efectos = esJugador ? PartidaActivity.personaje.getEfectos() : PartidaActivity.enemigo.getEfectos();

        for (int i = efectos.size() - 1; i >= 0; i--) {
            Efecto efecto = efectos.get(i);

            if (efecto instanceof Marca) {
                Marca mar = (Marca) efecto;
                if (mar.getNombre().equals("Elemento")){
                    return mar.getMarcaNombre();
                }

            }
        }

        return "";
    }

    public static String buscaElementoOfensivo(boolean esJugador){
        // Seleccionar la lista de efectos según el booleano
        List<Efecto> efectos = esJugador ? ControladorCombate.cartaUsada.getEfectos() : ControladorCombate.siguienteAtaque.getEfectos();

        for (int i = efectos.size() - 1; i >= 0; i--) {
            Efecto efecto = efectos.get(i);

            if (efecto instanceof Marca) {
                Marca mar = (Marca) efecto;
                if (mar.getNombre().equals("Elemento")){
                    return mar.getMarcaNombre();
                }

            }
        }
        return "";
    }

    public static void limpiaElementos(boolean esJugador){
        // Seleccionar la lista de efectos según el booleano
        List<Efecto> efectos = esJugador ? PartidaActivity.personaje.getEfectos() : PartidaActivity.enemigo.getEfectos();

        for (int i = efectos.size() - 1; i >= 0; i--) {
            Efecto efecto = efectos.get(i);

            if (efecto instanceof Marca) {
                Marca mar = (Marca) efecto;
                if (mar.getNombre().equals("Elemento")){
                    efectos.remove(mar);
                }

            }
        }
        CombateFragment.instanciaCombate.pintaEfectos(!esJugador);
    }

    public static void gana2Energia(boolean esJugador){
        PartidaActivity.personaje.setEnergiaRestante(PartidaActivity.personaje.getEnergiaRestante() + 2);
    }

    public static void ataqueDeOportunidad(boolean esJugador){
        ControladorCombate.instanciaControlador.mostrarMensaje("¡Esta es la mía!", false);
        ControladorCombate.instanciaControlador.atacar(ataqueBox.query(Ataque_.idAtaque.equal(34)).build().findFirst());
    }

    public static void intimidacion(boolean esJugador){
        ControladorCombate.instanciaControlador.mostrarMensaje("¡Qué miedo!", false);
        if (esJugador)
            ControladorCombate.inconveniente = true;
    }

}
