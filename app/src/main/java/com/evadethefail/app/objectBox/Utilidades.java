package com.evadethefail.app.objectBox;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.util.Log;

import com.evadethefail.app.controlador.ControladorPartida;
import com.evadethefail.app.entidades.Ataque;
import com.evadethefail.app.entidades.Carta;
import com.evadethefail.app.entidades.Efecto;
import com.evadethefail.app.entidades.EstadisticasPartida;
import com.evadethefail.app.entidades.Jugador;
import com.evadethefail.app.entidades.Marca;
import com.evadethefail.app.entidades.Modificador;
import com.evadethefail.app.entidades.Partida;
import com.evadethefail.app.entidades.Personaje;
import com.evadethefail.app.entidades.Tipo;
import com.evadethefail.app.modelo.PartidaActivity;
import com.evadethefail.app.objectBox.datos.EfectosDAO;
import com.evadethefail.app.objectBox.datos.EnemigoDAO;
import com.evadethefail.app.objectBox.datos.PersonajeDAO;
import com.evadethefail.app.objectBox.datos.ProgramaDAO;

import java.util.ArrayList;
import java.util.List;

import com.evadethefail.app.entidades.Ataque_;
import com.evadethefail.app.entidades.Carta_;

import com.evadethefail.app.entidades.EstadisticasJugador;

import com.evadethefail.app.entidades.Marca_;
import com.evadethefail.app.entidades.Modificador_;
import com.evadethefail.app.entidades.Tipo_;
import com.evadethefail.app.entidades.Jugador_;

import com.evadethefail.app.vista.MenuActivity;
import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;
/** @noinspection ALL*/
public class Utilidades {

    private static BoxStore boxStore;

    static {
        boxStore = ObjectBox.get();
    }

    private static final Box<Tipo> tipoBox = boxStore.boxFor(Tipo.class);
    private static Box<Jugador> jugadorBox = boxStore.boxFor(Jugador.class);
    private static Box<Modificador> modificadorBox = boxStore.boxFor(Modificador.class);
    private static Box<Marca> marcaBox = boxStore.boxFor(Marca.class);
    private static final Box<EstadisticasPartida> estadisticasPartidaBox = boxStore.boxFor(EstadisticasPartida.class);
    private static final Box<EstadisticasJugador> estadisticasJugadorBox = boxStore.boxFor(EstadisticasJugador.class);

    public static void revisaEIniciaDatos() {

        if (!hasData(jugadorBox)) {
            boxStore.removeAllObjects();
            Log.d("DatabaseManager", "Datos existentes borrados");

            // Insertamos nuevos datos
            insertaDatos();
        }
    }

    /**
     * Verifica si hay datos en la base de datos
     *
     * @param box Box de la entidad a comprobar
     * @return true si hay datos, false si está vacía
     */
    private static <T> boolean hasData(Box<T> box) {
        long count = box.count();
        Log.d("DatabaseManager", "Número de registros: " + count);
        return count > 0;
    }

    /**
     * Método para insertar datos iniciales
     */
    private static void insertaDatos() {
        Log.d("DatabaseManager", "Insertando datos iniciales...");
        EfectosDAO.crearDatosBase();
        PersonajeDAO.crearDatosBase();
        EnemigoDAO.crearDatosBase();
        ProgramaDAO.crearDatosBase();
    }

    public void close() {
        if (boxStore != null && !boxStore.isClosed()) {
            boxStore.close();
        }
    }

    public static boolean existeJugador(String nombre) {
        Query<Jugador> query = jugadorBox.query(
                        Jugador_.nombre.equal(nombre)
                )
                .build();
        boolean existe = query.count() > 0;
        query.close();
        return existe;
    }

    public static boolean login(String nombre, String contrasena) {
        Query<Jugador> query = jugadorBox.query(
                        Jugador_.nombre.equal(nombre)
                                .and(Jugador_.contrasena.equal(contrasena))
                )
                .build();
        boolean correcto = query.count() > 0;
        query.close();
        return correcto;
    }


    public static boolean registrarJugador(String nombre, String contrasena) {
        if (existeJugador(nombre)) {
            return false; // Ya existe
        }
        Jugador nuevoJugador = new Jugador(nombre, contrasena);
        jugadorBox.put(nuevoJugador); // Guarda en la base de datos
        return true;
    }

    public static void guardarEstadisticasPartida(){
        estadisticasPartidaBox.put(PartidaActivity.estadisticas);
    }

    public static void guardarEstadisticasJugador(){
        estadisticasJugadorBox.put(MenuActivity.estadisticas);
    }

    public static Carta buscaCarta(long idCarta) {
        Box<Carta> cartaBox = boxStore.boxFor(Carta.class);
        return cartaBox.query(Carta_.idCarta.equal(idCarta)).build().findFirst();
    }

    public static Carta buscaCartaPersonaje(long idCarta, long idPersonaje) {
        Box<Carta> cartaBox = boxStore.boxFor(Carta.class);
        return cartaBox.query(Carta_.idCarta.equal(idCarta)).build().findFirst();
    }

    public static Ataque buscaAtaque(long idAtaque) {
        Box<Ataque> ataqueBox = boxStore.boxFor(Ataque.class);
        return ataqueBox.query(Ataque_.idAtaque.equal(idAtaque)).build().findFirst();
    }

    public static Efecto buscaEfecto(long idEfecto) {
        Modificador mod = modificadorBox.query(Modificador_.idEfecto.equal(idEfecto)).build().findFirst();
        if (mod != null) {
            return mod;
        }

        Marca marca = marcaBox.query(Marca_.idEfecto.equal(idEfecto)).build().findFirst();
        if (marca != null) {
            return marca;
        }

        return null; // No se encontró ni modificador ni marca
    }

    public static List<Efecto> buscaEfectos(long idEfecto) {
        List<Efecto> efectos = new ArrayList<>();

        List<Modificador> mods = modificadorBox.query(Modificador_.idEfecto.equal(idEfecto)).build().find();
        efectos.addAll(mods);

        List<Marca> marcas = marcaBox.query(Marca_.idEfecto.equal(idEfecto)).build().find();
        efectos.addAll(marcas);

        return efectos;
    }


    public static Efecto buscaEfectoDirecto(long idEfecto) {

        Modificador mod = modificadorBox.query(Modificador_.idEfecto.equal(idEfecto)).build().findFirst();
        if (mod != null) {
            return mod;
        }

        Marca marca = marcaBox.query(Marca_.idEfecto.equal(idEfecto)).build().findFirst();
        if (marca != null) {
            return marca;
        }

        return null;
    }


    public static void guardarPartida(Context context) {
        // Obtener las cajas (DAOs de ObjectBox)
        Box<Partida> partidaBox = boxStore.boxFor(Partida.class);
        Box<Personaje> personajeBox = boxStore.boxFor(Personaje.class);

        // Inicializar la partida actual
        partidaBox.put(ControladorPartida.partida);

        ControladorPartida.partida.getPersonaje().setTarget(PartidaActivity.personaje);
        ControladorPartida.partida.getEstadisticas().setTarget(PartidaActivity.estadisticas);
        ControladorPartida.partida.setCasilla(ControladorPartida.partida.getCasilla());

        // Guardar la partida actual
        partidaBox.put(ControladorPartida.partida);

        PartidaActivity.personaje.getPartida().setTarget(ControladorPartida.partida);
        PartidaActivity.estadisticas.getPartida().setTarget(ControladorPartida.partida);

        // Obtener el personaje actual
        personajeBox.put(PartidaActivity.personaje);
        estadisticasPartidaBox.put(PartidaActivity.estadisticas);

    }

    public static void mensajeError(final Context context, final Exception e) {
        // Asegurar que se ejecuta en el hilo principal
        if (Looper.myLooper() != Looper.getMainLooper()) {
            new android.os.Handler(Looper.getMainLooper()).post(() -> mensajeError(context, e));
            return;
        }

        String mensajeError = (e != null) ? e.getMessage() : "Ha ocurrido un error inesperado.";

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error")
                .setMessage(mensajeError)
                .setCancelable(false)
                .setPositiveButton("Cerrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static Tipo getTipoPorNombre(String nombre) {
        Tipo tipo = tipoBox.query(Tipo_.nombre.equal(nombre)).build().findFirst();

        return tipo;
    }

    // Sobrecarga para permitir un mensaje personalizado sin excepción
    public static void mensajeError(final Context context, final String mensaje) {
        mensajeError(context, mensaje != null ? new Exception(mensaje) : null);
    }
}
