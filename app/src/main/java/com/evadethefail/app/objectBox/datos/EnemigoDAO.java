package com.evadethefail.app.objectBox.datos;


import android.util.Log;

import com.evadethefail.app.entidades.Ataque;
import com.evadethefail.app.entidades.AtaqueEfecto;
import com.evadethefail.app.entidades.Efecto;
import com.evadethefail.app.entidades.EnemigoBase;
import com.evadethefail.app.entidades.Tipo;

import java.util.List;

import com.evadethefail.app.objectBox.ObjectBox;
import com.evadethefail.app.objectBox.Utilidades;
import io.objectbox.Box;
import io.objectbox.BoxStore;
/** @noinspection ALL*/
public class EnemigoDAO {
    private static final BoxStore boxStore = ObjectBox.get();
    private static final Box<EnemigoBase> enemigoBox = boxStore.boxFor(EnemigoBase.class);
    private static final Box<Ataque> ataqueBox = boxStore.boxFor(Ataque.class);
    private static final Box<AtaqueEfecto> ataqueEfectoBox = boxStore.boxFor(AtaqueEfecto.class);

    public static void crearDatosBase(){
        inicializarAtaques();
        inicializarEnemigosBase();
    }

    public static void inicializarEnemigosBase() {

        // 📌 Crear enemigos base
        EnemigoBase esqueletoPruebas = new EnemigoBase("EsqueletoPruebas", "Neutro", 0, 80, 80, 80, 80, 0, 5.00, 50.00);
        EnemigoBase bossPruebas = new EnemigoBase("bossPruebas", "Neutro", 0, 80, 80, 80, 80, 0, 5.00, 50.00);
        EnemigoBase slimePruebas = new EnemigoBase("SlimePruebas", "Neutro", 0, 25, 25, 25, 25, 0, 5.00, 50.00);
        EnemigoBase limo = new EnemigoBase("Limo", "Neutro", 1, 410, 410, 150, 80, 0, 5.00, 50.00);
        EnemigoBase murcielago = new EnemigoBase("Murciélago", "Neutro", 1, 240, 240, 190, 100, 0, 10.00, 40.00);
        EnemigoBase parasito = new EnemigoBase("Parásito", "Neutro", 1, 210, 210, 150, 100, 0, 5.00, 50.00);
        EnemigoBase pillo1 = new EnemigoBase("Pillo", "Neutro", 1, 270, 270, 200, 100, 0, 10.00, 75.00);
        EnemigoBase pillo2 = new EnemigoBase("Pillo", "Neutro", 2, 270, 270, 200, 100, 0, 10.00, 75.00);
        EnemigoBase sabueso = new EnemigoBase("Sabueso", "Neutro", 2, 300, 300, 260, 100, 0, 5.00, 50.00);
        EnemigoBase criaturaMar = new EnemigoBase("Criatura del mar", "Neutro", 2, 410, 410, 220, 80, 0, 5.00, 50.00);
        EnemigoBase esqueleto = new EnemigoBase("Esqueleto", "Neutro", 2, 380, 380, 260, 105, 0, 20.00, 80.00);
        EnemigoBase esqueleto2 = new EnemigoBase("Esqueleto", "Neutro", 3, 380, 380, 260, 105, 0, 20.00, 80.00);
        EnemigoBase prisionero = new EnemigoBase("Prisionero", "Neutro", 3, 400, 400, 220, 130, 0, 10.00, 100.00);
        EnemigoBase magoOscuro = new EnemigoBase("Mago Oscuro", "Neutro", 3, 420, 420, 200, 120, 0, 5.00, 50.00);
        EnemigoBase heroeCaido = new EnemigoBase("Héroe Caído", "Neutro", 4, 1000, 1000, 170, 110, 0, 40.00, 125.00);
        EnemigoBase reyHonrado = new EnemigoBase(        "Rey", "Neutro", 4, 1000, 1000, 200, 200, 0, 10.00, 100.00);
        EnemigoBase reyMago = new EnemigoBase(      "Rey Mago", "Neutro", 4, 1000, 1000, 200, 160, 0, 20.00, 100.00);

        enemigoBox.put(
                esqueletoPruebas, bossPruebas, slimePruebas, limo, murcielago,
                parasito, pillo1, pillo2, sabueso, criaturaMar, esqueleto, esqueleto2,
                magoOscuro, heroeCaido /*, reyMago, reyHonrado*/
        );

        // 📌 Asignar ataques a cada enemigo
        asignarAtaques(esqueletoPruebas, 3L, 4L, 5L);
        asignarAtaques(bossPruebas, 1L, 4L, 6L);
        asignarAtaques(slimePruebas, 1L, 4L, 6L);
        asignarAtaques(limo, 1L, 4L, 5L);
        asignarAtaques(murcielago, 3L, 6L, 7L);
        asignarAtaques(parasito, 8L, 9L, 10L);
        asignarAtaques(pillo1, 1L, 11L, 12L);
        asignarAtaques(pillo2, 1L, 11L, 12L);
        asignarAtaques(sabueso, 6L, 13L, 14L);
        asignarAtaques(criaturaMar, 2L, 15L, 16L, 17L);
        asignarAtaques(esqueleto, 11L, 18L, 19L);
        asignarAtaques(esqueleto2, 11L, 18L, 19L);
        asignarAtaques(prisionero, 20L, 21L, 22L, 23L);
        asignarAtaques(magoOscuro, 25L, 26L, 27L, 28L);
        asignarAtaques(heroeCaido, 35L, 36L, 37L, 38L, 39L);

        // 📌 Guardar en la base de datos
        enemigoBox.put(
                esqueletoPruebas, bossPruebas, slimePruebas, limo, murcielago,
                parasito, pillo1, pillo2, sabueso, criaturaMar, esqueleto, esqueleto2,
                prisionero, magoOscuro, heroeCaido /*, reyMago, reyHonrado*/
        );
    }

    /**
     * Método auxiliar para asignar ataques a un enemigo
     */
    private static void asignarAtaques(EnemigoBase enemigoBase, Long... idAtaques) {
        for (Long id : idAtaques) {
            Ataque ataque = Utilidades.buscaAtaque(id);
            if (ataque != null) {
                enemigoBase.getAtaques().add(ataque);
            }
        }
    }
    public static void inicializarAtaques() {
        // Evitar duplicados
        if (ataqueBox.count() > 0) return;

        // 📌 Obtener tipos desde la base de datos
        Tipo basico = Utilidades.getTipoPorNombre("Básico");
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

        // 📌 Insertar ataques
        Ataque[] ataques = {
                new Ataque(1, "Golpe", 1, 20, 40),
                new Ataque(2, "Golpe", 1, 25, 40),
                new Ataque(3, "Golpe", 1, 34, 40),
                new Ataque(4, "Atormentar", 1, 33, 0),
                new Ataque(5, "Baba", 1, 47, 0),
                new Ataque(6, "Mordisco", 1, 33, 70),
                new Ataque(7, "Supersónico", 1, 33, 0),
                new Ataque(8, "Golpe", 1, 40, 50),
                new Ataque(9, "Enrollarse", 0, 30, 0),
                new Ataque(10, "Mudar", 0, 30, 0),
                new Ataque(11, "Afilar", 0, 40, 0),
                new Ataque(12, "Puñalada", 1, 40, 70),
                new Ataque(13, "Ladrar", 1, 33, 0),
                new Ataque(14, "Mordisquear", 1, 34, 40),
                new Ataque(15, "Mojar", 1, 25, 75),
                new Ataque(16, "Talasofobia", 1, 25, 0),
                new Ataque(17, "Humedecer", 0, 25, 0),
                new Ataque(18, "Ataque de no muerto", 1, 30, 100),
                new Ataque(19, "Mordisquear", 1, 30, 40),
                new Ataque(20, "Afilar", 0, 25, 0),
                new Ataque(21, "Atraco", 1, 25, 100),
                new Ataque(22, "Intimidación", 1, 25, 0),
                new Ataque(23, "Abusar", 1, 25, 60),
                new Ataque(24, "Aprovechar", 1, 0, 0),
                new Ataque(25, "Conjurar", 0, 20, 0),
                new Ataque(26, "Blasfemia", 1, 20, 60),
                new Ataque(27, "Oración natural negra", 1, 40, 0),
                new Ataque(28, "Ritual de sangre", 0, 20, 0),
                new Ataque(29, "Fireball", 1, 0, 120),
                new Ataque(30, "Torrente", 1, 0, 120),
                new Ataque(31, "Rayo", 1, 0, 120),
                new Ataque(32, "Granizo", 1, 0, 120),

                //Héroe Caído
                new Ataque(33, "Buscar debilidades", 0, 0, 0),
                new Ataque(34, "Ataque de oportunidad", 1, 0, 60),
                new Ataque(35, "Mimetizar con la oscuridad", 0, 20, 0),
                new Ataque(36, "Espadazo del pasado", 1, 30, 150),
                new Ataque(37, "Desmembrar", 1, 30, 100),
                new Ataque(38, "Plan perfecto", 0, 10, 0),
                new Ataque(39, "Plan retorcido", 0, 10, 0),
        };

        // 📌 Guardar cambios en la base de datos
        ataqueBox.put(ataques);

        // 📌 Asignar tipos a ataques
        asignarTipos(ataques[0], basico);
        asignarTipos(ataques[1], basico);
        asignarTipos(ataques[2], basico);
        asignarTipos(ataques[3], demerito);
        asignarTipos(ataques[4], demerito);
        asignarTipos(ataques[5], normal);
        asignarTipos(ataques[6], demerito);
        asignarTipos(ataques[7], basico);
        asignarTipos(ataques[8], potenciador);
        asignarTipos(ataques[9], potenciador);
        asignarTipos(ataques[10], potenciador);
        asignarTipos(ataques[11], cortante);
        asignarTipos(ataques[12], demerito);
        asignarTipos(ataques[13], normal);
        asignarTipos(ataques[14], ataqueConDemerito);
        asignarTipos(ataques[15], demerito);
        asignarTipos(ataques[16], potenciador);
        asignarTipos(ataques[17], ataqueConDemerito);
        asignarTipos(ataques[18], normal);
        asignarTipos(ataques[19], potenciador);
        asignarTipos(ataques[20], normal);
        asignarTipos(ataques[21], ataqueConDemerito);
        asignarTipos(ataques[22], cortante, ataqueConDemerito);
        asignarTipos(ataques[23], cortante);
        asignarTipos(ataques[24], conjuroDeMejora);
        asignarTipos(ataques[25], conjuroDeDesmejora, ataqueConDemerito);
        asignarTipos(ataques[26], conjuroDeMejora);
        asignarTipos(ataques[27], potenciador, demerito);
        asignarTipos(ataques[28], magiaElemental);
        asignarTipos(ataques[29], magiaElemental);
        asignarTipos(ataques[30], magiaElemental);
        asignarTipos(ataques[30], magiaElemental);

        //Héroe caído
        asignarTipos(ataques[32], potenciador);
        asignarTipos(ataques[33], cortante);
        asignarTipos(ataques[34], bloqueo);
        asignarTipos(ataques[35], cortante);
        asignarTipos(ataques[36], cortante, ataqueConDemerito);
        asignarTipos(ataques[37], potenciador);
        asignarTipos(ataques[38], potenciador);


        // 📌 Asignar efectos a ataques
        asignarEfectos(ataques[3], 4);
        asignarEfectos(ataques[4], 6);
        asignarEfectos(ataques[6], 7);
        asignarEfectos(ataques[8], 1);
        asignarEfectos(ataques[9], 8);
        asignarEfectos(ataques[10], 3);
        asignarEfectos(ataques[12], 9);
        asignarEfectos(ataques[13], 10);
        asignarEfectos(ataques[14], 11);
        asignarEfectos(ataques[15], 12);
        asignarEfectos(ataques[16], 14);
        asignarEfectos(ataques[17], 13);
        asignarEfectos(ataques[19], 3);
        asignarEfectos(ataques[18], 9);
        asignarEfectos(ataques[21], 15);
        asignarEfectos(ataques[22], 10);
        asignarEfectos(ataques[24], 16);
        asignarEfectos(ataques[25], 10);
        asignarEfectos(ataques[25], 4);
        asignarEfectos(ataques[27], 40);
        asignarEfectos(ataques[28], 45);
        asignarEfectos(ataques[29], 46);
        asignarEfectos(ataques[30], 47);
        asignarEfectos(ataques[31], 48);

        //Héroe caído
        asignarEfectos(ataques[32], 41);
        asignarEfectos(ataques[34], 43);
        asignarEfectos(ataques[36], 44);
        asignarEfectos(ataques[37], 42);
        asignarEfectos(ataques[38], 24);
        //Rey honesto


        //Rey mago

        ataqueBox.put(ataques);

    }

    /**
     * Método auxiliar para asignar efectos a un ataque
     */
    private static void asignarEfectos(Ataque ataque, long idEfecto) {
        if (ataque != null) {
            List<Efecto> efectos = Utilidades.buscaEfectos(idEfecto);
            Box<AtaqueEfecto> ataqueEfectoBox = boxStore.boxFor(AtaqueEfecto.class);
            for (Efecto efecto : efectos) {
                if (efecto != null) {
                    // 1. Crear instancia vacía
                    AtaqueEfecto ae = new AtaqueEfecto();
                    // 2. Guardar para que ae.id tenga valor
                    ataqueEfectoBox.put(ae);
                    // 3. Delegar al método de la entidad
                    ataque.asignaEfecto(ae, efecto);
                    // 4. Guardar el ataqueEfecto con los datos finales
                    ataqueEfectoBox.put(ae);
                    // 5. Añadirlo a la lista interna del Ataque
                    ataque.setEfecto(ae);
                }
            }
        } else {
            Log.e("AsignarEfectos", "Ataque es null");
        }
    }



    private static void asignarTipos(Ataque ataque, Tipo... tipos) {
        if (ataque != null && tipos != null) {
            ataque.asignaTipos(tipos);
        } else {
            Log.e("AsignarTipos", "Ataque o tipos nulos al intentar asignar tipos");
        }
    }

}
