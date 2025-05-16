package com.evadethefail.app.objectBox.datos;

import com.evadethefail.app.entidades.Efecto;
import com.evadethefail.app.entidades.Marca;
import com.evadethefail.app.entidades.Modificador;
import com.evadethefail.app.entidades.Tipo;
import com.evadethefail.app.objectBox.ObjectBox;
import io.objectbox.Box;
import io.objectbox.BoxStore;
/** @noinspection ALL*/
public class EfectosDAO {

    private static final BoxStore boxStore = ObjectBox.get();
    private static final Box<Tipo> tipoBox = boxStore.boxFor(Tipo.class);
    private static final Box<Efecto> efectoBox = boxStore.boxFor(Efecto.class);
    private static final Box<Modificador> modificadorBox = boxStore.boxFor(Modificador.class);
    private static final Box<Marca> marcaBox = boxStore.boxFor(Marca.class);

    public static void crearDatosBase(){
        inicializarTipos();
        inicializarEfectos();
    }

    public static void inicializarTipos() {
        // Evitar duplicados
        if (tipoBox.count() > 0) return;

        // 📌 Insertar tipos en la base de datos
        Tipo[] tipos = {
                new Tipo("Estado", "Defensa Básica"),
                new Tipo("Estado", "Potenciador Básico"),
                new Tipo("Estado", "Potenciador"),
                new Tipo("Estado", "Demérito"),
                new Tipo("Estado", "Bloqueo"),
                new Tipo("Estado", "Heroico"),
                new Tipo("Físico", "Básico"),
                new Tipo("Físico", "Normal"),
                new Tipo("Físico", "Cortante"),
                new Tipo("Físico", "Ataque con demérito"),
                new Tipo("Mágico", "Conjuro de mejora"),
                new Tipo("Mágico", "Conjuro de desmejora"),
                new Tipo("Mágico", "Magia elemental")
        };

        tipoBox.put(tipos);
    }

    public static void inicializarEfectos() {

        if (efectoBox.count() > 0) return;

        Efecto bloqueo = new Efecto(1, "Bloqueo", 1, 0, 0, 0);
        Efecto dfDown2t = new Efecto(2, "Defensa -", 2, 0, 0, 0);
        Efecto critRateUp3t = new Efecto(3, "Probabilidad de crítico +", 3, 0, 0, 0);
        Efecto atDown2t = new Efecto(4, "Ataque -", 2, 0, 0, 0);
        Efecto atUp3t = new Efecto(5, "Ataque +", 3, 0, 0, 0);
        Efecto baba2t = new Efecto(6, "Baba", 2, 0, 0, 0);
        Efecto confundido2t = new Efecto(7, "Confundido", 3, 0, 0, 0);
        Efecto mudar = new Efecto(8, "Mudar", 1, 0, 0, 0);
        Efecto atDown3t = new Efecto(9, "Ataque -", 3, 0, 0, 0);
        Efecto dfDown2t2 = new Efecto(10, "Defensa -", 2, 0, 0, 0);
        Efecto critDmgDown3t = new Efecto(11, "Daño Crítico -", 3, 0, 0, 0);
        Efecto talasofobia2t = new Efecto(12, "Talasofobia", 2, 0, 0, 0);
        Efecto critRateDown1t = new Efecto(13, "Probabilidad de Crítico -", 2, 0, 0, 0);
        Efecto humedecer2t = new Efecto(14, "Humedecer", 2, 0, 0, 0);
        Efecto intimidado1t = new Efecto(15, "Intimidado", 2, 0, 0, 0);
        Efecto magicosUp3t = new Efecto(16, "Mágico +", 3, 0, 0, 0);
        Efecto bloqueo2 = new Efecto(17, "Bloqueo", 1, 0, 0, 0);
        Efecto atUp1At = new Efecto(18, "Ataque +", 0, 1, 0, 0);
        Efecto critRateUp1At = new Efecto(18, "Probabilidad de Crítico +", 0, 1, 0, 0);
        Efecto dfUp2t = new Efecto(19, "Defensa +", 2, 0, 0, 0);
        Efecto enUp1t = new Efecto(20, "Energía +", 1, 0, 0, 0);
        Efecto hpUp = new Efecto(21, "Vida +", 1, 0, 0, 0);
        Efecto critRateUp1At2 = new Efecto(22, "Probabilidad de Crítico +", 0, 1, 0, 0);
        Efecto atDown1t = new Efecto(23, "Ataque -", 1, 0, 0, 0);
        Efecto dfDown1t = new Efecto(23, "Defensa -", 1, 0, 0, 0);
        Efecto critRateUp2t = new Efecto(24, "Probabilidad de Crítico +", 2, 0, 0, 0);
        Efecto critDmgUp2t = new Efecto(24, "Daño Crítico +", 2, 0, 0, 0);
        Efecto critDmgUp3t = new Efecto(25, "Daño crítico +", 3, 0, 0, 0);
        Efecto cicatrices = new Efecto(26, "Cicatrices", 2, 0, 0, 0);
        Efecto cortantesUp3t = new Efecto(27, "Cortante +", 3, 0, 0, 0);
        Efecto sangrado1t = new Efecto(28, "Sangrado", 1, 0, 0, 0);
        Efecto veneno3t = new Efecto(29, "Veneno", 3, 0, 0, 0);
        Efecto vampirismo2t = new Efecto(30, "Vampirismo", 2, 0, 0, 0);
        Efecto fuego1ar = new Efecto(31, "Elemento", 0, 0, 2, 0);
        Efecto agua1ar = new Efecto(32, "Elemento", 0, 0, 2, 0);
        Efecto rayo1ar = new Efecto(33, "Elemento", 0, 0, 2, 0);
        Efecto hielo1ar = new Efecto(34, "Elemento", 0, 0, 2, 0);
        Efecto dfDown3t = new Efecto(35, "Defensa -", 3, 0, 0, 0);
        Efecto critRateDown3t = new Efecto(36, "Probabilidad de Crítico -", 3, 0, 0, 0);
        Efecto dfUp3t = new Efecto(37, "Defensa +", 3, 0, 0, 0);
        Efecto enUpxt = new Efecto(38, "Ritual de energía", 3, 0, 0, 0);
        Efecto maldicion2t = new Efecto(39, "Maldicion", 2, 0, 0, 0);
        Efecto ritualDeSangre = new Efecto(40, "Ritual de Sangre", 1, 0, 0, 0);
        Efecto oportunista = new Efecto(41, "Oportunista", 100, 0, 0, 0);
        Efecto critRateUp2t2 = new Efecto(42, "Probabilidad de Crítico +", 2, 0, 0, 0);
        Efecto bloqueo3 = new Efecto(43, "Bloqueo", 1, 0, 0, 0);
        Efecto desmembrar2t = new Efecto(44, "Corte profundo", 2, 0, 0, 0);
        Efecto fuego2t = new Efecto(45, "Elemento", 2, 0, 0, 0);
        Efecto agua2t = new Efecto(46, "Elemento", 2, 0, 0, 0);
        Efecto rayo2t = new Efecto(47, "Elemento", 2, 0, 0, 0);
        Efecto hielo2t = new Efecto(48, "Elemento", 2, 0, 0, 0);


        // Insertar efectos en la base de datos
        efectoBox.put(
                bloqueo, dfDown2t, critRateUp3t, atDown2t, atUp3t, baba2t, confundido2t, mudar,
                atDown3t, dfDown2t2, critDmgDown3t, critRateDown1t, talasofobia2t, humedecer2t,
                intimidado1t, magicosUp3t, bloqueo2, atUp1At, critRateUp1At, dfUp2t, enUp1t, atDown1t,
                dfDown1t, critRateUp2t, critDmgUp2t, critDmgUp3t, cicatrices, cortantesUp3t, sangrado1t,
                veneno3t, vampirismo2t, fuego1ar, agua1ar, rayo1ar, hielo1ar, dfDown3t, critRateDown3t,
                dfUp3t, enUpxt, maldicion2t, oportunista, critRateUp2t2, bloqueo3, desmembrar2t, fuego2t,
                rayo2t, agua2t, hielo2t
        );

        // 📌 Insertar modificadores con referencia a los efectos
        Modificador[] modificadores = {
                new Modificador(bloqueo, "Bloqueo", 0.00, 8),
                new Modificador(dfDown2t, "Defensa", -50.00, 0),
                new Modificador(atDown2t, "Ataque", -25.00, 0),
                new Modificador(atDown3t, "Ataque", -33.00, 0),
                new Modificador(atUp3t, "Ataque", 25.00, 0),
                new Modificador(baba2t, "Ataque", -10.00, -3),
                new Modificador(baba2t, "Defensa", -10.00, -3),
                new Modificador(mudar, "Vida Restante", 0.00, 15),
                new Modificador(critRateUp3t, "Probabilidad de Crítico", 0.00, 30),
                new Modificador(dfDown2t2, "Defensa", -33.00, 0),
                new Modificador(critDmgDown3t, "Daño Crítico", 0.00, -50),
                new Modificador(talasofobia2t, "Defensa", -33.00, 0),
                new Modificador(talasofobia2t, "Ataque", -33.00, 0),
                new Modificador(critRateDown1t, "Probabilidad de Crítico", -50.00, 0),
                new Modificador(humedecer2t, "Defensa", 50.00, 15),
                new Modificador(humedecer2t, "Ataque", 50.00, 15),
                new Modificador(bloqueo2, "Bloqueo", 0, 12),
                new Modificador(atUp1At, "Ataque", 50, 0),
                new Modificador(critRateUp1At, "Probabilidad de Crítico", 0, 90),
                new Modificador(dfUp2t, "Defensa", 40, 10),
                new Modificador(enUp1t, "Energía", 0, 2),
                new Modificador(hpUp, "Vida Restante", 30, 0),
                new Modificador(critRateUp1At2, "Probabilidad de Crítico", 0, 90),
                new Modificador(dfDown1t, "Defensa", -50.00, 0),
                new Modificador(atDown1t, "Ataque", -50.00, 0),
                new Modificador(critRateUp2t, "Probabilidad de Crítico", 0, 100),
                new Modificador(critDmgUp2t, "Daño Crítico", 0, 100),
                new Modificador(critDmgUp3t, "Daño Crítico", 0.00, 50),
                new Modificador(sangrado1t, "Ataque", -10.00, 0),
                new Modificador(sangrado1t, "Defensa", -10.00, 0),
                new Modificador(dfDown3t, "Defensa", -33.00, -10),
                new Modificador(critRateDown3t, "Probabilidad de Crítico", 0, -50),
                new Modificador(dfUp3t, "Defensa", 50, 15),
                new Modificador(maldicion2t, "Probabilidad de Crítico", 0, -15),
                new Modificador(maldicion2t, "Daño Crítico", 0, -30),
                new Modificador(ritualDeSangre, "Vida Restante", 20, 20),
                new Modificador(critRateUp2t2, "Probabilidad de Crítico", 0, 30),
                new Modificador(bloqueo3, "Bloqueo", 0, 25),
                new Modificador(desmembrar2t, "Ataque", -25.00, -10),
                new Modificador(desmembrar2t, "Defensa", -25.00, -10),
        };

        modificadorBox.put(modificadores);

        // 📌 Insertar marcas con referencia a los efectos
        Marca[] marcas = {
                new Marca(confundido2t, "Confusión", "¡Estás tan confuso que te heriste a ti mismo!", "finTurno", "confusion"),
                new Marca(intimidado1t, "Intimidación", "¡Qué miedo da ese enemigo! No debería atacarle...", "preaccion", "intimidacion"),
                new Marca(magicosUp3t, "Conjuros mejorados", "Los ataques mágicos serán más efectivos", "trait", ""),
                new Marca(cicatrices, "Cicatrices", "¡Las heridas están visibles y al rojo vivo!", "reaccion", "sangrado"),
                new Marca(cortantesUp3t, "Rematar Heridas", "Apuntar a las heridas es una estrategia muy cruel, y completamente de tu gusto.", "trait", ""),
                new Marca(veneno3t, "Envenenamiento", "El tipo de muerte más sutil que la historia ha conocido.", "finTurno", "veneno"),
                new Marca(vampirismo2t, "Vampirismo", "El disfrute de ver las heridas de otros te reconforta y alivia tu dolor.", "accion", "vampirismo"),
                new Marca(fuego1ar, "Fuego", "Objetivo marcado con poderosa magia de fuego.", "trait", ""),
                new Marca(rayo1ar, "Rayo", "Objetivo marcado con poderosa magia de rayo.", "trait", ""),
                new Marca(agua1ar, "Agua", "Objetivo marcado con poderosa magia de agua.", "trait", ""),
                new Marca(hielo1ar, "Hielo", "Objetivo marcado con poderosa magia de hielo.", "trait", ""),
                new Marca(fuego2t, "Fuego", "Objetivo marcado con poderosa magia de fuego.", "trait", ""),
                new Marca(rayo2t, "Rayo", "Objetivo marcado con poderosa magia de rayo.", "trait", ""),
                new Marca(agua2t, "Agua", "Objetivo marcado con poderosa magia de agua.", "trait", ""),
                new Marca(hielo2t, "Hielo", "Objetivo marcado con poderosa magia de hielo.", "trait", ""),
                new Marca(enUpxt, "Ritual", "Gana energía extra mientras tengas el favor de tus dioses.", "inicioTurno", "gana2Energia"),
                new Marca(oportunista, "Oportunista", "El Rey Asesino sabe cuándo debe atacar.", "inicioTurno", "ataqueDeOportunidad"),
        };

        marcaBox.put(marcas);
    }

}