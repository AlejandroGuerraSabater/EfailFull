package com.evadethefail.app.objectBox.datos;

import com.evadethefail.app.entidades.Jugador;
import com.evadethefail.app.objectBox.ObjectBox;
import io.objectbox.Box;
import io.objectbox.BoxStore;

public class ProgramaDAO {

    private static final BoxStore boxStore = ObjectBox.get();
    private static final Box<Jugador> jugadorBox = boxStore.boxFor(Jugador.class);

    public static void crearDatosBase(){
        crearJugadoresBase();
    }

    public static void crearJugadoresBase(){
        Jugador alexio = new Jugador("Alexio", "1234");

        jugadorBox.put(alexio);
    }



}
