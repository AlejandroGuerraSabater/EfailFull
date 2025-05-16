package com.evadethefail.app.objectBox.datos;

import android.util.Log;

import com.evadethefail.app.entidades.Carta;
import com.evadethefail.app.entidades.CartaEfecto;
import com.evadethefail.app.entidades.Clase;
import com.evadethefail.app.entidades.Efecto;
import com.evadethefail.app.entidades.PersonajeBase;
import com.evadethefail.app.entidades.Tipo;

import java.util.List;

import com.evadethefail.app.objectBox.ObjectBox;
import com.evadethefail.app.objectBox.Utilidades;
import io.objectbox.Box;
import io.objectbox.BoxStore;
/** @noinspection ALL*/
public class PersonajeDAO {

    private static final BoxStore boxStore = ObjectBox.get();
    private static final Box<PersonajeBase> personajeBox = boxStore.boxFor(PersonajeBase.class);
    private static final Box<Carta> cartaBox = boxStore.boxFor(Carta.class);
    private static final Box<CartaEfecto> cartaEfectoBox = boxStore.boxFor(CartaEfecto.class);
    private static final Box<Clase> clasesBox = boxStore.boxFor(Clase.class);

    public static void crearDatosBase() {
        // Evitar duplicados
        if (personajeBox.count() > 0) return;

        // 📌 Crear clases de personaje
        Clase caballeroClase = new Clase("Caballero");
        Clase asesinoClase = new Clase("Asesino");
        Clase hechiceraClase = new Clase("Hechicera");

        clasesBox.put(caballeroClase, asesinoClase, hechiceraClase);

        // 📌 Crear personajes jugables con su clase asignada
        PersonajeBase caballero = new PersonajeBase(caballeroClase, 3, 320, 180, 180, 5, 50);
        PersonajeBase asesino = new PersonajeBase(asesinoClase, 3, 215, 170, 190, 40, 100);
        PersonajeBase hechicera = new PersonajeBase(hechiceraClase, 4, 300, 200, 160, 20, 50);

        personajeBox.put(caballero, asesino, hechicera);

        // 📌 Obtener tipos desde la base de datos
        Tipo basico = Utilidades.getTipoPorNombre("Básico");
        Tipo defensaBasica = Utilidades.getTipoPorNombre("Defensa Básica");
        Tipo normal = Utilidades.getTipoPorNombre("Normal");
        Tipo potenciador = Utilidades.getTipoPorNombre("Potenciador");
        Tipo bloqueo = Utilidades.getTipoPorNombre("Bloqueo");
        Tipo cortante = Utilidades.getTipoPorNombre("Cortante");
        Tipo conjuroDeMejora = Utilidades.getTipoPorNombre("Conjuro de mejora");
        Tipo conjuroDeDesmejora = Utilidades.getTipoPorNombre("Conjuro de desmejora");
        Tipo magiaElemental = Utilidades.getTipoPorNombre("Magia elemental");
        Tipo demerito = Utilidades.getTipoPorNombre("Demérito");
        Tipo ataqueConDemerito = Utilidades.getTipoPorNombre("Ataque con demérito");
        Tipo heroico = Utilidades.getTipoPorNombre("Heroico");

        // 📌 Crear cartas iniciales y asignarlas a los personajes
        Carta[] cartasCaballero = {
                // Caballero
                new Carta(1, "Golpe", "Golpe básico y siempre fiable.", 1, 1, 50, "N"),
                new Carta(2, "Defender", "A veces la mejor estrategia es sobrevivir un día más.", 1, 0, 0, "N"),
                new Carta(3, "Pomazo", "Carta contundente con la empuñadura de tu espada.", 2, 1, 80, "N"),
                new Carta(4, "Espadazo", "Corte poderoso.", 1, 1, 70, "N"),
                new Carta(5, "Gran escudo", "Protección de grandes dimensiones.", 1, 0, 0, "N"),
                new Carta(6, "Calentamiento", "Realiza estiramientos para preparar tu próximo ataque.", 0, 0, 0, "N"),
                new Carta(7, "Carnicería", "Ataque tan despiadado como desesperado. Cruel pero efectivo.", 3, 1, 175, "N"),
                new Carta(8, "Atadura", "Movimiento técnico para limitar los movimientos del rival.", 2, 1, 80, "N"),
                new Carta(9, "Piel de hierro", "Resultado de todos tus años de entrenamiento y batallas anteriores.", 2, 0, 0, "N"),
                new Carta(10, "Preparacion", "Revisa tu equipo y estado y vuelve más fuerte a la pelea, repleto de energía.", 1, 0, 0, "N"),
                new Carta(11, "Golpe desarmado", "No todo son armas en esta vida.", 0, 1, 40, "N"),
                new Carta(12, "Descanso corto", "Has dormido en sitios peores que este campo de batalla. Recupera vida.", 0, 0, 0, "N"),
                new Carta(13, "Practicar", "Busca la mejor forma de lograr tu intrépido plan.", 1, 0, 0, "N"),
                new Carta(14, "Corte de furia", "Un ataque temerario con excelentes resultados que debilita ofensiva y defensiva del rival.", 2, 1, 120, "N"),
                new Carta(15, "Inspiración heroica", "Te sientes el protagonista, acaba con esto. Este es tu momento.", 2, 0, 0, "N"),
        };

        Carta[] cartasAsesino = {
                // Asesino
                new Carta(201, "Golpe", "Corte básico y siempre fiable.", 1, 1, 50, "N"),
                new Carta(202, "Cubrir", "Un asesino debe ser silencioso. Pero si te descubren es bueno tener un plan B.", 1, 0, 0, "N"),
                new Carta(203, "Puñalada", "Carta rastrero con un historial crítico impecable.", 1, 1, 50, "N"),
                new Carta(204, "Rajada", "Carta despiadada con consecuencias demoledoras.", 1, 1, 70, "N"),
                new Carta(205, "Maquinar", "Preparación de un plan perverso con alta probabilidad de crítico.", 0, 0, 0, "N"),
                new Carta(206, "Plan cruel", "Despreocupación total de la vida del enemigo.", 0, 0, 0, "N"),
                new Carta(207, "Cicatrices", "Si no hay puntos débiles, créalos con tu cuchillo.", 1, 1, 0, "N"),
                new Carta(208, "Rematar herida", "Apunta tus ataques cortantes a los puntos débiles del enemigo.", 1, 0, 0, "N"),
                new Carta(209, "Navaja", "Corte rápido e indoloro.", 0, 1, 40, "N"),
                new Carta(210, "Corte de Samurai", "Envaina, desenvaina. Envaina, corta.", 2, 1, 150, "N"),
                new Carta(211, "Puñalada venenosa", "Demostración de que los asesinos y el veneno son inseparables.", 1, 1, 60, "N"),
                new Carta(212, "Ataque sigiloso", "Ataque que rompe cualquier defensa enemiga.", 1, 1, 60, "N"),
                new Carta(213, "Entre las sombras", "Hazte uno con la oscuridad para ser prácticamente invencible.", 1, 0, 0, "N"),
                new Carta(214, "Actitud sádica", "Ver la sangre del enemigo te hace ignorar la tuya que brota por tus heridas.", 1, 0, 0, "N"),
                new Carta(215, "Navajazo", "Corte rápido no tan indoloro.", 0, 1, 60, "N"),
        };

        Carta[] cartasHechicera = {
                // Hechicera
                new Carta(401, "Golpe de bastón", "Bastonazo básico y siempre fiable.", 1, 1, 50, "N"),
                new Carta(402, "Magic Guard", "Libera una capa de protección mágica conocida como escudo.", 1, 0, 0, "N"),
                new Carta(403, "Fireball", "Una clásica y poderosa bola de fuego.", 2, 1, 100, "N"),
                new Carta(404, "Conjurar", "Recitales ancestrales e indescriptibles que potencian los hechizos.", 1, 0, 50, "N"),
                new Carta(405, "Torrente", "Lanzamiento de agua con la furia de la naturaleza.", 2, 1, 100, "N"),
                new Carta(406, "Rayo", "Ataque de rayo básico y poderoso.", 2, 1, 100, "N"),
                new Carta(407, "Granizo", "Recreación del clima más letal del norte.", 2, 1, 100, "N"),
                new Carta(408, "Magic Wall", "Barrera mágica casi impenetrable.", 1, 0, 0, "N"),
                new Carta(409, "Ritual de energia", "Conjuro mágico para ganar más energía al inicio del turno.", 3, 0, 0, "N"),
                new Carta(410, "Maldicion", "Maldice al enemigo para reducir su suerte.", 1, 0, 0, "N"),
                new Carta(411, "Oración natural", "Lanza una de las 4 cartas elementales básicas al azar.", 0, 1, 0, "N"),
                new Carta(412, "Ascuas", "Una débil pero versátil bola de fuego.", 1, 1, 50, "N"),
                new Carta(413, "Rápidos", "Corriente de agua de bajo coste.", 0, 1, 30, "N"),
                new Carta(414, "Trueno", "Magia de rayo más poderosa que ha visto la humanidad.", 3, 1, 170, "N"),
                new Carta(415, "Helada", "Conjuro de hielo de una fuerza inconmesurable.", 3, 1, 170, "N"),
        };

        cartaBox.put(cartasCaballero);
        cartaBox.put(cartasAsesino);
        cartaBox.put(cartasHechicera);

        // 📌 Asignar clases y tipos a cartas

        // CABALLERO
        asignarClase(cartasCaballero[0], caballeroClase); // ID 1
        asignarTipos(cartasCaballero[0], basico);

        asignarClase(cartasCaballero[1], caballeroClase); // ID 2
        asignarTipos(cartasCaballero[1], defensaBasica, bloqueo);
        asignarEfectos(cartasCaballero[1], 1);

        asignarClase(cartasCaballero[2], caballeroClase); // ID 3
        asignarTipos(cartasCaballero[2], normal);
        asignarEfectos(cartasCaballero[2], 2);

        asignarClase(cartasCaballero[3], caballeroClase); // ID 4
        asignarTipos(cartasCaballero[3], cortante);

        asignarClase(cartasCaballero[4], caballeroClase); // ID 5
        asignarTipos(cartasCaballero[4], bloqueo);
        asignarEfectos(cartasCaballero[4], 17);

        asignarClase(cartasCaballero[5], caballeroClase); // ID 6
        asignarTipos(cartasCaballero[5], potenciador);
        asignarEfectos(cartasCaballero[5], 18);

        asignarClase(cartasCaballero[6], caballeroClase); // ID 7
        asignarTipos(cartasCaballero[6]);

        asignarClase(cartasCaballero[7], caballeroClase); // ID 8
        asignarTipos(cartasCaballero[7], ataqueConDemerito);
        asignarEfectos(cartasCaballero[7], 4);

        asignarClase(cartasCaballero[8], caballeroClase); // ID 9
        asignarTipos(cartasCaballero[8], potenciador);
        asignarEfectos(cartasCaballero[8], 19);

        asignarClase(cartasCaballero[9], caballeroClase); // ID 10
        asignarTipos(cartasCaballero[9], potenciador);
        asignarEfectos(cartasCaballero[9], 20);

        asignarClase(cartasCaballero[10], caballeroClase); // ID 11
        asignarTipos(cartasCaballero[10], normal);

        asignarClase(cartasCaballero[11], caballeroClase); // ID 12
        asignarTipos(cartasCaballero[11], potenciador);
        asignarEfectos(cartasCaballero[11], 21);

        asignarClase(cartasCaballero[12], caballeroClase); // ID 13
        asignarTipos(cartasCaballero[12], potenciador);
        asignarEfectos(cartasCaballero[12], 42);

        asignarClase(cartasCaballero[13], caballeroClase); // ID 14
        asignarTipos(cartasCaballero[13], cortante, ataqueConDemerito);
        asignarEfectos(cartasCaballero[13], 23);

        asignarClase(cartasCaballero[14], caballeroClase); // ID 15
        asignarTipos(cartasCaballero[14], potenciador, heroico);
        asignarEfectos(cartasCaballero[14], 24);

        // ASESINO
        asignarClase(cartasAsesino[0], asesinoClase); // ID 201
        asignarTipos(cartasAsesino[0], basico);

        asignarClase(cartasAsesino[1], asesinoClase); // ID 202
        asignarTipos(cartasAsesino[1], defensaBasica);
        asignarEfectos(cartasAsesino[1], 1);

        asignarClase(cartasAsesino[2], asesinoClase); // ID 203
        asignarTipos(cartasAsesino[2], cortante);

        asignarClase(cartasAsesino[3], asesinoClase); // ID 204
        asignarTipos(cartasAsesino[3], cortante);

        asignarClase(cartasAsesino[4], asesinoClase); // ID 205
        asignarTipos(cartasAsesino[4], potenciador);
        asignarEfectos(cartasAsesino[4], 3);

        asignarClase(cartasAsesino[5], asesinoClase); // ID 206
        asignarTipos(cartasAsesino[5], potenciador);
        asignarEfectos(cartasAsesino[5], 25);

        asignarClase(cartasAsesino[6], asesinoClase); // ID 207
        asignarTipos(cartasAsesino[6], demerito, cortante);
        asignarEfectos(cartasAsesino[6], 26);

        asignarClase(cartasAsesino[7], asesinoClase); // ID 208
        asignarTipos(cartasAsesino[7], potenciador);
        asignarEfectos(cartasAsesino[7], 27);

        asignarClase(cartasAsesino[8], asesinoClase); // ID 209
        asignarTipos(cartasAsesino[8], cortante);

        asignarClase(cartasAsesino[9], asesinoClase); // ID 210
        asignarTipos(cartasAsesino[9], cortante);

        asignarClase(cartasAsesino[10], asesinoClase); // ID 211
        asignarTipos(cartasAsesino[10], ataqueConDemerito, cortante);
        asignarEfectos(cartasAsesino[10], 29);

        asignarClase(cartasAsesino[11], asesinoClase); // ID 212
        asignarTipos(cartasAsesino[11], ataqueConDemerito);

        asignarClase(cartasAsesino[12], asesinoClase); // ID 213
        asignarTipos(cartasAsesino[12], bloqueo);
        asignarEfectos(cartasAsesino[12], 17);

        asignarClase(cartasAsesino[13], asesinoClase); // ID 214
        asignarTipos(cartasAsesino[13], heroico, demerito);
        asignarEfectos(cartasAsesino[13], 30);

        asignarClase(cartasAsesino[14], asesinoClase); // ID 215
        asignarTipos(cartasAsesino[14], cortante);

        // HECHICERA
        asignarClase(cartasHechicera[0], hechiceraClase); // ID 401
        asignarTipos(cartasHechicera[0], basico);

        asignarClase(cartasHechicera[1], hechiceraClase); // ID 402
        asignarTipos(cartasHechicera[1], defensaBasica);
        asignarEfectos(cartasHechicera[1], 1);

        asignarClase(cartasHechicera[2], hechiceraClase); // ID 403
        asignarTipos(cartasHechicera[2], magiaElemental);
        asignarEfectos(cartasHechicera[2], 31);

        asignarClase(cartasHechicera[3], hechiceraClase); // ID 404
        asignarTipos(cartasHechicera[3], conjuroDeMejora);
        asignarEfectos(cartasHechicera[3], 16);

        asignarClase(cartasHechicera[4], hechiceraClase); // ID 405
        asignarTipos(cartasHechicera[4], magiaElemental);
        asignarEfectos(cartasHechicera[4], 32);

        asignarClase(cartasHechicera[5], hechiceraClase); // ID 406
        asignarTipos(cartasHechicera[5], magiaElemental);
        asignarEfectos(cartasHechicera[5], 33);

        asignarClase(cartasHechicera[6], hechiceraClase); // ID 407
        asignarTipos(cartasHechicera[6], magiaElemental);
        asignarEfectos(cartasHechicera[6], 34);

        asignarClase(cartasHechicera[7], hechiceraClase); // ID 408
        asignarTipos(cartasHechicera[7], bloqueo);
        asignarEfectos(cartasHechicera[7], 17);

        asignarClase(cartasHechicera[8], hechiceraClase); // ID 409
        asignarTipos(cartasHechicera[8], conjuroDeMejora);
        asignarEfectos(cartasHechicera[8], 38);

        asignarClase(cartasHechicera[9], hechiceraClase); // ID 410
        asignarTipos(cartasHechicera[9], conjuroDeDesmejora);
        asignarEfectos(cartasHechicera[9], 39);

        asignarClase(cartasHechicera[10], hechiceraClase); // ID 411
        asignarTipos(cartasHechicera[10], bloqueo);

        asignarClase(cartasHechicera[11], hechiceraClase); // ID 413
        asignarTipos(cartasHechicera[11], magiaElemental);
        asignarEfectos(cartasHechicera[11], 31);

        asignarClase(cartasHechicera[12], hechiceraClase); // ID 413
        asignarTipos(cartasHechicera[12], magiaElemental);
        asignarEfectos(cartasHechicera[12], 32);

        asignarClase(cartasHechicera[13], hechiceraClase); // ID 414
        asignarTipos(cartasHechicera[13], magiaElemental);
        asignarEfectos(cartasHechicera[13], 33);

        asignarClase(cartasHechicera[14], hechiceraClase); // ID 415
        asignarTipos(cartasHechicera[14], magiaElemental);
        asignarEfectos(cartasHechicera[14], 34);

        cartaBox.put(cartasCaballero);
        cartaBox.put(cartasAsesino);
        cartaBox.put(cartasHechicera);

    }

    private static void asignarClase(Carta carta, Clase clase) {
        if (carta != null && clase != null) {
            carta.clase.setTarget(clase);
        } else {
            Log.e("AsignarClase", "Carta o clase es null");
        }
    }

    private static void asignarTipos(Carta carta, Tipo... tipos) {
        if (carta != null && tipos != null) {
            carta.asignaTipos(tipos);
        } else {
            Log.e("AsignarTipos", "Carta o tipos son null");
        }
    }

    private static void asignarEfectos(Carta carta, long idEfecto) {
        if (carta != null) {
            List<Efecto> efectos = Utilidades.buscaEfectos(idEfecto);
            Box<CartaEfecto> cartaEfectoBox = boxStore.boxFor(CartaEfecto.class);
            for (Efecto efecto : efectos) {
                if (efecto != null) {
                    // 1. Crear instancia vacía
                    CartaEfecto ce = new CartaEfecto();
                    // 2. Guardar para que ce.id tenga valor
                    cartaEfectoBox.put(ce);
                    // 3. Delegar al método de la entidad
                    carta.asignaEfecto(ce, efecto);
                    // 4. Guardar el cartaEfecto con los datos finales
                    cartaEfectoBox.put(ce);
                    // 5. Guardar el cartaEfecto final en la carta
                    carta.setEfecto(ce);
                }
            }
        } else {
            Log.e("AsignarEfectos", "Carta es null");
        }
    }
}
