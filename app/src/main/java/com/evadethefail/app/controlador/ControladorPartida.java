package com.evadethefail.app.controlador;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import com.evadethefail.app.entidades.Carta;
import com.evadethefail.app.entidades.Clase;
import com.evadethefail.app.entidades.Clase_;
import com.evadethefail.app.entidades.Enemigo;
import com.evadethefail.app.entidades.EnemigoBase;
import com.evadethefail.app.entidades.EnemigoBase_;
import com.evadethefail.app.entidades.EstadisticasPartida;
import com.evadethefail.app.entidades.EstadisticasPartida_;
import com.evadethefail.app.entidades.Partida;
import com.evadethefail.app.entidades.Partida_;
import com.evadethefail.app.entidades.Personaje;
import com.evadethefail.app.entidades.PersonajeBase;
import com.evadethefail.app.entidades.PersonajeBase_;
import com.evadethefail.app.entidades.Personaje_;
import com.evadethefail.app.modelo.PartidaActivity;
import com.evadethefail.app.objectBox.App;
import com.evadethefail.app.objectBox.ObjectBox;
import com.evadethefail.app.objectBox.Utilidades;
import com.evadethefail.app.vista.CombateFragment;
import com.evadethefail.app.vista.EstadisticasPartidaActivity;
import com.evadethefail.app.vista.LoginActivity;
import com.evadethefail.app.vista.MapaFragment;
import com.evadethefail.app.vista.MenuActivity;
import com.evadethefail.app.vista.NuevaPartidaActivity;
import com.evadethefail.app.vista.RecompensaDialog;
import io.objectbox.Box;
import io.objectbox.BoxStore;
/** @noinspection ALL*/
public class ControladorPartida {

    public static List<Integer> statsBase;
    public static Partida partida;
    public static boolean nuevaPartida = false;
    public CombateFragment combate = CombateFragment.instanciaCombate;
    private MapaFragment mapa = MapaFragment.instanciaMapa;
    private static final BoxStore boxStore = App.getBoxStore();
    private static final Box<Partida> partidaBox = boxStore.boxFor(Partida.class);
    private static final Box<PersonajeBase> personajeBaseBox = boxStore.boxFor(PersonajeBase.class);
    private static final Box<Personaje> personajeBox = boxStore.boxFor(Personaje.class);
    public static boolean esVictoria = false;

    /**
     * setupInicial() tiene dos funciones principales. <br>
     * En primer lugar, crea en la base de datos la fila correspondiente al
     * personaje, jugador y partida que se hayan elegido.<br>
     * La fila quedaría de la siguiente manera:<br>
     * <br>
     *
     * Id:1 <br>
     * IdPartida:1 <br>
     * IdPersonaje:1 <br>
     * Nombre:Alexio <br>
     * Nivel:1 <br>
     * Energía:3 <br>
     * EnergíaRestante:3 <br>
     * Vida:45 <br>
     * VidaRestante:45 <br>
     * Ataque:32 <br>
     * Defensa:27 <br>
     * Bloqueo:0 <br>
     * ProbabilidadCritico:5.00 <br>
     * DanioCritico:50.00 <br>
     * <br>
     *
     * Además, inicializa con esos datos el objeto PartidaActivity.personaje, que
     * representará al jugador en los combates.<br>
     * Esto lo hace mediante el método setupPersonaje().
     */
    public static void setupInicial() {

        // 1. Crear nueva partida
        partida = new Partida();
        partidaBox.put(partida);
        partida.setCasilla(1);
        partida.setJugadorId((int) LoginActivity.jugador.getId());
        partida.getPersonaje().setTarget(PartidaActivity.personaje);
        partidaBox.put(partida);

        // 3. Obtener datos del personaje seleccionado
        Clase clase = ObjectBox.get()
                .boxFor(Clase.class)
                .query(Clase_.nombre.equal(NuevaPartidaActivity.personajeSeleccionado))
                .build()
                .findFirst();

        PersonajeBase personajeBase = personajeBaseBox.query(PersonajeBase_.claseId.equal(clase.getId()))
                .build()
                .findFirst();

        if (personajeBase == null) {
            return; // Error: el personaje no existe en la BD
        }

        statsBase = new ArrayList<Integer>(Arrays.asList(
                personajeBase.getVida(), personajeBase.getVidaRestante(), personajeBase.getAtaque(), personajeBase.getDefensa()
        ));

        // 4. Crear el personaje en la partida
        Personaje personaje = new Personaje();
        personaje.setIdPartida((int) partida.getId());
        personaje.setPersonajeBase(personajeBase);
        personaje.setClase(personajeBase.getClase());
        personaje.setNombre(LoginActivity.jugador.getNombre());
        personaje.setNivel(1);
        personaje.setEnergia(personajeBase.getEnergia());
        personaje.setEnergiaRestante(personajeBase.getEnergiaRestante());
        personaje.setVida(personajeBase.getVida());
        personaje.setVidaRestante(personajeBase.getVidaRestante());
        personaje.setAtaque(personajeBase.getAtaque());
        personaje.setDefensa(personajeBase.getDefensa());
        personaje.setProbabilidadCritico(personajeBase.getProbabilidadCritico());
        personaje.setDanoCritico(personajeBase.getDanoCritico());

        PartidaActivity.personaje = personaje;
        personajeBox.put(personaje);

        // 5. Asignar el mazo inicial al jugador
        List<Carta> mazoInicial = new ArrayList<>();

        switch (personaje.getClase().getNombre()) {
            case "Caballero": // Caballero
                PartidaActivity.personajeSeleccionado = 1;
                break;
            case "Asesino": // Asesino
                PartidaActivity.personajeSeleccionado = 2;
                break;
            case "Hechicera": // Hechicera
                PartidaActivity.personajeSeleccionado = 3;
                break;
        }

        /*
        //CARTAS QUE SE AÑADEN EN LA VERSIÓN FINAL
        int baseCartaId = (idClase - 1) * 200;

        for (int i = 0; i < 4; i++) {
            personaje.addCarta(baseCartaId + 1);
            PartidaActivity.mazo.add((baseCartaId + 1) + "");
        }
        for (int i = 0; i < 4; i++) {
            personaje.addCarta(baseCartaId + 2);
            PartidaActivity.mazo.add((baseCartaId + 2) + "");
        }
        personaje.addCarta(baseCartaId + 3);
        PartidaActivity.mazo.add((baseCartaId + 3) + "");
        personaje.addCarta(baseCartaId + 4);
        PartidaActivity.mazo.add((baseCartaId + 4) + "");
         */

        //CARTAS QUE SE AÑADEN EN LA VERSIÓN ENTREGADA (de prueba)
        int baseCartaId = (PartidaActivity.personajeSeleccionado - 1) * 200;

        for (int i = 0; i < 2; i++) {
            personaje.addCarta(baseCartaId + 1);
            PartidaActivity.mazo.add((baseCartaId + 1) + "");
        }
        for (int i = 0; i < 2; i++) {
            personaje.addCarta(baseCartaId + 2);
            PartidaActivity.mazo.add((baseCartaId + 2) + "");
        }

        switch (NuevaPartidaActivity.personajeSeleccionado){
            case "Caballero": // Caballero
                for (int i = 3; i != 11; i++) {
                    personaje.addCarta(baseCartaId + i);
                    PartidaActivity.mazo.add((baseCartaId + i) + "");
                }
                break;
            case "Asesino": // Asesino

                personaje.addCarta(baseCartaId + 2);
                PartidaActivity.mazo.add((baseCartaId + 2) + "");
                for (int i = 3; i != 9; i++) {
                    personaje.addCarta(baseCartaId + i);
                    PartidaActivity.mazo.add((baseCartaId + i) + "");
                }
                personaje.addCarta(baseCartaId + 11);
                PartidaActivity.mazo.add((baseCartaId + 11) + "");
                break;
            case "Hechicera": // Hechicera
                for (int i = 3; i != 8; i++) {
                    personaje.addCarta(baseCartaId + i);
                    PartidaActivity.mazo.add((baseCartaId + i) + "");
                }
                personaje.addCarta(baseCartaId + 9);
                PartidaActivity.mazo.add((baseCartaId + 9) + "");
                personaje.addCarta(baseCartaId + 12);
                PartidaActivity.mazo.add((baseCartaId + 12) + "");
                personaje.addCarta(baseCartaId + 13);
                PartidaActivity.mazo.add((baseCartaId + 13) + "");
                break;
        }

        // 7. Configurar personaje en la partida y ajustes iniciales
        setupPersonaje();
        ajuste(5, statsBase);

        PartidaActivity.estadisticas = new EstadisticasPartida();
        Utilidades.guardarEstadisticasPartida();
        partida.getPersonaje().setTarget(PartidaActivity.personaje);
        partida.getEstadisticas().setTarget(PartidaActivity.estadisticas);
        partidaBox.put(partida);
    }


    public static void setupPersonaje() {

        if (PartidaActivity.personaje != null) {
            PartidaActivity.personaje.setNivel(5);
            personajeBox.put(PartidaActivity.personaje); // Guarda los cambios en la base de datos
        }
    }

    public static List<Integer> sacarStatsBasePj(PersonajeBase personaje) {
        List<Integer> statsBase = new ArrayList<Integer>();
        statsBase.add(personaje.getVida());
        statsBase.add(personaje.getVidaRestante());
        statsBase.add(personaje.getAtaque());
        statsBase.add(personaje.getDefensa());

        return statsBase;

    }

    public static List<Integer> sacarStatsBaseEnemigo(EnemigoBase enemigoBase) {
        List<Integer> statsBase = new ArrayList<Integer>();
        statsBase.add(enemigoBase.getVida());
        statsBase.add(enemigoBase.getVidaRestante());
        statsBase.add(enemigoBase.getAtaque());
        statsBase.add(enemigoBase.getDefensa());

        return statsBase;

    }

    /**
     * Ajusta los stats del personaje jugable respecto a su base en el nivel
     * indicado.
     *
     * @param nivel
     * @param statsBase
     */
    public static void ajuste(int nivel, List<Integer> statsBase) {
        if (PartidaActivity.personaje != null && statsBase.size() >= 3) {
            PartidaActivity.personaje.setVida((nivel + 5) * statsBase.get(0) / 50);
            PartidaActivity.personaje.setVidaRestante((nivel + 5) * statsBase.get(0) / 50);
            PartidaActivity.personaje.setAtaque((nivel + 5) * statsBase.get(2) / 50);
            PartidaActivity.personaje.setDefensa((nivel + 5) * statsBase.get(3) / 50);
            PartidaActivity.personaje.setNivel(nivel);

            personajeBox.put(PartidaActivity.personaje); // Guarda los cambios en la base de datos
        }
    }

    /**
     * Ajusta los stats del enemigo respecto a su base en el nivel indicado.
     *
     * @param nivel
     * @param statsBase
     */
    public static void ajusteEnemigo(int nivel, List<Integer> statsBase) {

        PartidaActivity.enemigo.setVida((nivel + 5) * statsBase.get(0) / 50);
        PartidaActivity.enemigo.setVidaRestante((nivel + 5) * statsBase.get(0) / 50);
        PartidaActivity.enemigo.setAtaque((nivel + 5) * statsBase.get(2) / 50);
        PartidaActivity.enemigo.setDefensa((nivel + 5) * statsBase.get(3) / 50);

    }

    public static void actualizarStats(List<String> statsNuevos) {

        PartidaActivity.personaje.setVida(Integer.parseInt(statsNuevos.get(0)));
        PartidaActivity.personaje.setAtaque(Integer.parseInt(statsNuevos.get(2)));
        PartidaActivity.personaje.setDefensa(Integer.parseInt(statsNuevos.get(3)));

    }

    public static void subirNivel(int nivelesSubidos, List<String> statsBase) {
        Personaje personaje = PartidaActivity.personaje;

        int vidaExtra = nivelesSubidos * Integer.parseInt(statsBase.get(0)) / 50;
        int ataqueExtra = nivelesSubidos * Integer.parseInt(statsBase.get(2)) / 50;
        int defensaExtra = nivelesSubidos * Integer.parseInt(statsBase.get(3)) / 50;

        String mensaje = "Vida: " + personaje.getVida() + " + " + vidaExtra + "\n" +
                "Ataque: " + personaje.getAtaque() + " + " + ataqueExtra + "\n" +
                "Defensa: " + personaje.getDefensa() + " + " + defensaExtra;

        AlertDialog.Builder builder = new AlertDialog.Builder(CombateFragment.instanciaCombate.getContext());
        builder.setTitle("¡Subiste de nivel!")
                .setMessage(mensaje)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();

        personaje.setVida(personaje.getVida() + vidaExtra);
        personaje.setAtaque(personaje.getAtaque() + ataqueExtra);
        personaje.setDefensa(personaje.getDefensa() + defensaExtra);
        personaje.aumentaNivel(nivelesSubidos);
    }

    public static void eligeEnemigo() {
        // Obtener la instancia de la base de datos de EnemigoBase
        Box<EnemigoBase> enemigoBox = ObjectBox.get().boxFor(EnemigoBase.class);

        // Consultar los enemigos que están en la misma casilla
        List<EnemigoBase> enemigos = enemigoBox.query()
                .equal(EnemigoBase_.casilla, MapaFragment.casilla)
                .build()
                .find();

        if (enemigos.isEmpty()) {
            System.out.println("No hay enemigos en esta casilla.");
            return;
        }

        EnemigoBase enemigoSeleccionado;
        if (MapaFragment.casilla == 4){
            enemigoSeleccionado = enemigos.get(0);
        } else{
            // Elegir un enemigo aleatorio
            Random random = new Random();
            enemigoSeleccionado = enemigos.get(random.nextInt(3));
        }

        // Llamar a creaEnemigo con el ID seleccionado
        creaEnemigo((int) enemigoSeleccionado.getId());
    }

    private static void creaEnemigo(int id) {
        // Obtener el Box de EnemigoBase
        Box<EnemigoBase> enemigoBaseBox = ObjectBox.get().boxFor(EnemigoBase.class);

        // Buscar el EnemigoBase en ObjectBox
        EnemigoBase enemigoBase = enemigoBaseBox.get(id);

        if (enemigoBase == null) {
            System.out.println("No se encontró un enemigo con ID: " + id);
            return;
        }

        // Generar nivel aleatorio dentro de un rango de ±2 niveles respecto al personaje * la casilla
        int nivelEnemigo = (int) (Math.random() * 5 + PartidaActivity.personaje.getNivel() - 2) + MapaFragment.casilla;

        // Crear el enemigo actual copiando los valores del EnemigoBase
        PartidaActivity.enemigo = new Enemigo(enemigoBase, nivelEnemigo);
        PartidaActivity.enemigo.setDanioCriticoReal(PartidaActivity.enemigo.getDanioCritico());
        PartidaActivity.enemigo.setProbabilidadCriticoReal(PartidaActivity.enemigo.getProbabilidadCritico());
        ajusteEnemigo(nivelEnemigo, sacarStatsBaseEnemigo(
                ObjectBox.get().boxFor(EnemigoBase.class).get(PartidaActivity.enemigo.getIdEnemigoBase())
        ));

    }


    public static List<Integer> eligeRecompensa() {
        List<Integer> recompensas = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            int id = obtenerIdCartaRecompensa();
            recompensas.add(id);
        }

        return recompensas;
    }

    private static int obtenerIdCartaRecompensa() {
        int casilla = MapaFragment.casilla;
        Random random = new Random();
        int probabilidad = random.nextInt(99) + 1 ; // Número entre 1 y 100

        /*
         * Eleccion de recompensa en base a la casilla donde el jugador está. Si casilla
         * es inferior a 5, la probabilidad de que cada id será: Si la casilla es
         * inferior a 5: id del 4 al 90: 82% id del 91 al 150: 16% id del 151 al 199: 2%
         *
         * Si casilla es entre 5 y 10, 5 incluido: id del 4 al 90: 70% id del 91 al 150:
         * 25% id del 151 al 199: 5%
         *
         * Si casilla es igual o superior a 10: id del 4 al 90: 55% id del 91 al 150:
         * 30% id del 151 al 199: 15%
         *
         * Se entiende que las cartas de mayor valor tendrán un id más alto.
         * En la version de prueba entregada, se juega con cartas hasta id = 15
         *
         if (casilla < 5) {
            if (probabilidad < 82)
                return random.nextInt(87) + 4 + (PartidaActivity.personajeSeleccionado - 1) * 200; // 4 - 90
            if (probabilidad < 98)
                return random.nextInt(60) + 91 + (PartidaActivity.personajeSeleccionado - 1) * 200; // 91 - 150
            return random.nextInt(49) + 151 + (PartidaActivity.personajeSeleccionado - 1) * 200; // 151 - 199
        }

        if (casilla <= 10) {
            if (probabilidad < 70)
                return random.nextInt(87) + 4 + (PartidaActivity.personajeSeleccionado - 1) * 200; // 4 - 90
            if (probabilidad < 95)
                return random.nextInt(60) + 91 + (PartidaActivity.personajeSeleccionado - 1) * 200; // 91 - 150
            return random.nextInt(49) + 151 + (PartidaActivity.personajeSeleccionado - 1) * 200; // 151 - 199
        }

        if (probabilidad < 55)
            return random.nextInt(87) + 4; // 4 - 90
        if (probabilidad < 85)
            return random.nextInt(60) + 91; // 91 - 150
        return random.nextInt(49) + 151; // 151 - 199
         */

        if (casilla < 5) {
            //Añadido para aumentar la probabilidad de obtener cartas raras un 5, 10, 15 y 20%
            probabilidad += casilla * 5;
            int offset = (PartidaActivity.personajeSeleccionado - 1) * 200;

            if (probabilidad < 25)
                return random.nextInt(4) + 1 + offset; // 1 - 4

            if (probabilidad < 75)
                return random.nextInt(9) + 4 + offset; // 4 - 12

            return random.nextInt(4) + 12 + offset; // 12 - 15
        }
        return 15 + (PartidaActivity.personajeSeleccionado - 1) * 200;
    }

    public static void victoria(Context context) {

        if (MapaFragment.casilla == MapaFragment.totalCasillas){
            esVictoria = true;
            PartidaActivity.estadisticas.enemigosDerrotadosSumar(1);
            MenuActivity.estadisticas.enemigosDerrotadosSumar(1);
            Utilidades.guardarEstadisticasPartida();
            Utilidades.guardarEstadisticasJugador();
            juegoCompletado(context);
            return;
        }

        PartidaActivity.personaje.limpiaEfectos();
        PartidaActivity.personaje.setEnergiaRestante(PartidaActivity.personaje.getEnergia());

        int nivelesSubidos = calcularNivelesSubidos();

        PersonajeBase personajeBase = personajeBaseBox.query(PersonajeBase_.claseId.equal(PartidaActivity.personaje.getClase().getId()))
                .build()
                .findFirst();

        int vidaRestante = PartidaActivity.personaje.getVidaRestante();
        int diferenciaDeVida = PartidaActivity.personaje.getVida();
        ajuste(PartidaActivity.personaje.getNivel() + nivelesSubidos, sacarStatsBasePj(personajeBase));
        diferenciaDeVida = PartidaActivity.personaje.getVida() - diferenciaDeVida;
        PartidaActivity.personaje.setVidaRestante(vidaRestante + diferenciaDeVida);

        RecompensaDialog dialog = new RecompensaDialog(CombateFragment.instanciaCombate.getContext(), eligeRecompensa());

        dialog.show();
    }

    private static void juegoCompletado(Context contexto) {
        PartidaActivity.personaje.setVidaRestante(0);
        personajeBox.put(PartidaActivity.personaje);
        Intent intent = new Intent(contexto, EstadisticasPartidaActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        contexto.startActivity(intent);
    }

    public static int calcularNivelesSubidos() {
        double random = Math.random() * 100; // Número entre 0 y 100

        /* Probabilidades de la version final
        if (random < 10) {
            return 0; // 10% de no subir niveles
        } else if (random < 50) { // 10% + 40%
            return 1; // 40% de subir 1 nivel
        } else if (random < 85) { // 10% + 40% + 35%
            return 2; // 35% de subir 2 niveles
        } else if (random < 95) { // 10% + 40% + 35% + 10%
            return 3; // 10% de subir 3 niveles
        } else {
            return 4; // 5% de subir 4 niveles
        }
        */

        //Probabilidades version de prueba
        if (random < 10) {
            return 4; // 10% de no subir niveles
        } else if (random < 50) { // 10% + 40%
            return 6; // 40% de subir 1 nivel
        } else if (random < 85) { // 10% + 40% + 35%
            return 8; // 35% de subir 2 niveles
        } else if (random < 95) { // 10% + 40% + 35% + 10%
            return 10; // 10% de subir 3 niveles
        } else {
            return 15; // 5% de subir 4 niveles
        }

    }

    public static void derrota(Context context) {
        establecerVidaCero();

        Utilidades.guardarEstadisticasPartida();
        Utilidades.guardarEstadisticasJugador();
        juegoCompletado(context);
    }

    public static void establecerVidaCero() {
        PartidaActivity.personaje.setVidaRestante(0);
    }

    public static void guardarProgreso(Context context) {

        Utilidades.guardarPartida(context);

    }

    public static void cargarPartida(Context context) {
        // Obtener la última partida guardada del usuario
        long jugadorId = LoginActivity.jugador.getId();

        partida = ObjectBox.get()
                .boxFor(Partida.class)
                .query(Partida_.jugadorId.equal(jugadorId))
                .orderDesc(Partida_.id)
                .build()
                .findFirst();

        if (partida == null) {
            //No hay partida guardada
            return;
        }

        // Establecer la casilla actual del mapa
        MapaFragment.casilla = partida.getCasilla();

        // Obtener el personaje de la partida
        PartidaActivity.personaje = ObjectBox.get()
                .boxFor(Personaje.class)
                .query(Personaje_.partidaId.equal(partida.getId()))
                .build()
                .findFirst();

        // Obtener las estadísticas de la partida
        PartidaActivity.estadisticas = ObjectBox.get()
                .boxFor(EstadisticasPartida.class)
                .query(EstadisticasPartida_.partidaId.equal(partida.getId()))
                .build()
                .findFirst();

        switch (PartidaActivity.personaje.getClase().getNombre()) {
            case "Caballero": // Caballero
                PartidaActivity.personajeSeleccionado = 1;
                NuevaPartidaActivity.personajeSeleccionado = "Caballero";
                break;
            case "Asesino": // Asesino
                PartidaActivity.personajeSeleccionado = 2;
                NuevaPartidaActivity.personajeSeleccionado = "Asesino";
                break;
            case "Hechicera": // Hechicera
                PartidaActivity.personajeSeleccionado = 3;
                NuevaPartidaActivity.personajeSeleccionado = "Hechicera";
                break;
        }

        if (PartidaActivity.personaje != null) {

            for (Carta carta : PartidaActivity.personaje.getMazoReal()) {
                PartidaActivity.mazo.add(String.valueOf(carta.getIdCarta()));
            }

        } else {
            //ERROR
        }

        Intent intent = new Intent(context, PartidaActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }


}
