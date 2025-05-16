package com.evadethefail.app.entidades;

import java.util.ArrayList;
import java.util.List;

import com.evadethefail.app.entidades.CartasPersonaje_;

import com.evadethefail.app.objectBox.ObjectBox;
import com.evadethefail.app.objectBox.Utilidades;
import com.evadethefail.app.vista.CombateFragment;
import io.objectbox.Box;
import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;
import io.objectbox.relation.ToOne;

@Entity
public class Personaje {
    @Id
    private long id;
    public ToOne<Clase> clase;

    private String nombre;
    private int nivel;
    private int energia;
    private int energiaRestante;
    private int vida;
    private int vidaRestante;
    private int ataque;
    private int defensa;
    private int bloqueo = 0;
    private float probabilidadCritico;
    private float danoCritico;

    private int ataqueReal;
    private int defensaReal;
    private double probabilidadCriticoReal;
    private double danoCriticoReal;
    private ToOne<Partida> partida;

    protected List<Efecto> efectos = new ArrayList<Efecto>();

    /*-------------------------------------------------------------------*/
    private int idPartida;

    // Relación con el personaje base del que proviene
    private ToOne<PersonajeBase> personajeBase;

    // Mazo de cartas del personaje en la partida
    @Backlink(to = "personaje")
    private ToMany<CartasPersonaje> mazo;

    public Personaje() {}

    public Personaje(int idPartida, PersonajeBase personajeBase, String nombre, int nivel, int energia, int vida, int ataque, int defensa, float probabilidadCritico, float danoCritico) {
        this.idPartida = idPartida;
        this.personajeBase.setTarget(personajeBase);
        this.nombre = nombre;
        this.nivel = nivel;
        this.energia = energia;
        this.energiaRestante = energia;
        this.vida = vida;
        this.vidaRestante = vida;
        this.ataque = ataque;
        this.defensa = defensa;
        this.probabilidadCritico = probabilidadCritico;
        this.danoCritico = danoCritico;
    }

    // Getters y Setters
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public Clase getClase() { return clase.getTarget(); }
    public void setClase(Clase clase) { this.clase.setTarget(clase); }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public int getNivel() { return nivel; }
    public void setNivel(int nivel) { this.nivel = nivel; }

    public int getEnergia() { return energia; }
    public void setEnergia(int energia) { this.energia = energia; }

    public int getEnergiaRestante() { return energiaRestante; }
    public void setEnergiaRestante(int energiaRestante) { this.energiaRestante = energiaRestante; }

    public int getVida() { return vida; }
    public void setVida(int vida) { this.vida = vida; }

    public int getVidaRestante() { return vidaRestante; }
    public void setVidaRestante(int vidaRestante) { this.vidaRestante = vidaRestante; }

    public int getAtaque() { return ataque; }
    public void setAtaque(int ataque) { this.ataque = ataque; this.ataqueReal = ataque;}

    public int getBloqueo() { return bloqueo; }
    public void setBloqueo(int bloqueo) { this.bloqueo = bloqueo; }

    public int getDefensa() { return defensa; }
    public void setDefensa(int defensa) { this.defensa = defensa; this.defensaReal = defensa;}

    public float getProbabilidadCritico() { return probabilidadCritico; }
    public void setProbabilidadCritico(float probabilidadCritico) { this.probabilidadCritico = probabilidadCritico; this.probabilidadCriticoReal = probabilidadCritico; }

    public float getDanoCritico() { return danoCritico; }
    public void setDanoCritico(float danoCritico) { this.danoCritico = danoCritico; this.danoCriticoReal = danoCritico;}
    public int getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(int idPartida) {
        this.idPartida = idPartida;
    }
    public ToOne<PersonajeBase> getPersonajeBase() { return personajeBase; }
    public void setPersonajeBase(PersonajeBase personajeBase) { this.personajeBase.setTarget(personajeBase); }



    public ToOne<Partida> getPartida() {
        return partida;
    }

    public void setClase(ToOne<Clase> clase) {
        this.clase = clase;
    }

    public void setPartida(ToOne<Partida> partida) {
        this.partida = partida;
    }

    public void setPersonajeBase(ToOne<PersonajeBase> personajeBase) {
        this.personajeBase = personajeBase;
    }

    public List<CartasPersonaje> getMazo() { return mazo; }

    public List<Carta> getMazoReal() {
        List<Carta> mazoReal = new ArrayList<>();

        Box<CartasPersonaje> cpBox = ObjectBox.get().boxFor(CartasPersonaje.class);

        List<CartasPersonaje> cartasAsociadas = cpBox.query()
                .equal(CartasPersonaje_.personajeId, this.id)
                .build()
                .find();

        for (CartasPersonaje cp : cartasAsociadas) {
            for (int i = 0; i < cp.cantidad; i++) {
                mazoReal.add(cp.carta.getTarget());
            }
        }

        return mazoReal;
    }


    public void setMazo(List<CartasPersonaje> mazo) {
        this.mazo.clear();
        this.mazo.addAll(mazo);
    }

    public void addCarta(long idCarta) {
        Carta carta = Utilidades.buscaCarta(idCarta);
        if (carta == null) return;

        Box<CartasPersonaje> cpBox = ObjectBox.get().boxFor(CartasPersonaje.class);

        CartasPersonaje cartaPersonaje = cpBox.query()
                .equal(CartasPersonaje_.personajeId, this.id)
                .equal(CartasPersonaje_.cartaId, carta.getId())
                .build()
                .findFirst();

        if (cartaPersonaje != null) {
            cartaPersonaje.cantidad++;
            cpBox.put(cartaPersonaje);
        } else {
            cartaPersonaje = new CartasPersonaje();
            cpBox.put(cartaPersonaje);
            cartaPersonaje.personaje.setTarget(this);
            cartaPersonaje.carta.setTarget(carta);
            cartaPersonaje.cantidad = 1;
            cpBox.put(cartaPersonaje);
        }
    }

    /*
    public void eliminarCarta(long idCarta) {
        Carta carta = Utilidades.buscaCarta(idCarta);
        if (carta != null) {
            mazo.remove(carta);
        }
    }
    */

    public int getAtaqueReal() {
        return ataqueReal;
    }

    public void setAtaqueReal(int ataqueReal) {
        this.ataqueReal = ataqueReal;
    }

    public int getDefensaReal() {
        return defensaReal;
    }

    public void setDefensaReal(int defensaReal) {
        this.defensaReal = defensaReal;
    }

    public double getProbabilidadCriticoReal() {
        return probabilidadCriticoReal;
    }

    public void setProbabilidadCriticoReal(double probabilidadCriticoReal) {
        this.probabilidadCriticoReal = probabilidadCriticoReal;
    }

    public double getDanoCriticoReal() {
        return danoCriticoReal;
    }

    public void setDanoCriticoReal(double danoCriticoReal) {
        this.danoCriticoReal = danoCriticoReal;
    }

    public List<Efecto> getEfectos() {
        return this.efectos;
    }

    public void setEfectos(List<Efecto> efectos) {
        this.efectos = efectos;
    }

    // Método para agregar un efecto
    public void agregarEfecto(CombateFragment combate, Efecto efecto) {
        boolean esBloqueo = false;
        if (efecto instanceof Modificador) {
            Modificador mod = (Modificador) efecto;
            if (mod.getEstadistica().equals("Bloqueo")) {
                this.setBloqueo((int) (this.getBloqueo() + mod.getVariacionPlana()));
                combate.txtBloqueoJugador.setText((this.getBloqueo() == 0) ? "" : this.getBloqueo() + "");

            } else {
                switch (mod.getEstadistica()) {

                    case "Vida":
                        this.setVida((int) (this.getVida() - mod.getVariacionPlana()));

                        this.setVidaRestante((int) (this.getVidaRestante() + mod.getVariacionPlana()));
                        this.setVidaRestante((int) (this.getVidaRestante() + this.getVida() * mod.getPorcentaje()/100));
                        break;

                    case "Vida Restante":

                        this.setVidaRestante((int) (this.getVidaRestante() + mod.getVariacionPlana()));
                        this.setVidaRestante((int) (this.getVidaRestante() + this.getVidaRestante() * mod.getPorcentaje()/100));
                        if (this.getVidaRestante() > this.getVida()){
                            this.setVidaRestante(this.getVida());
                        } else if (this.getVidaRestante() < 0) {
                            this.setVidaRestante(0);
                        }
                        break;

                    case "Defensa":
                        this.setDefensaReal((int) (this.getDefensaReal() + mod.getVariacionPlana()));
                        this.setDefensaReal((int) (this.getDefensaReal() + this.getDefensa() * mod.getPorcentaje()/100));
                        break;

                    case "Ataque":
                        this.setAtaqueReal((int) (this.getAtaqueReal() + mod.getVariacionPlana()));
                        this.setAtaqueReal((int) (this.getAtaqueReal() + this.getAtaque() * mod.getPorcentaje()/100));
                        break;

                    case "Probabilidad de Crítico":
                        this.setProbabilidadCriticoReal((int) (this.getProbabilidadCriticoReal() + mod.getVariacionPlana()));
                        this.setProbabilidadCriticoReal((int) (this.getProbabilidadCriticoReal() + this.getProbabilidadCritico() * mod.getPorcentaje()/100));
                        break;

                    case "Daño Crítico":
                        this.setDanoCriticoReal((int) (this.getDanoCriticoReal() + mod.getVariacionPlana()));
                        this.setDanoCriticoReal((int) (this.getDanoCriticoReal() + this.getDanoCritico() * mod.getPorcentaje()/100));
                        break;

                    case "Energía":
                        this.setEnergiaRestante((int) (this.getEnergiaRestante() + mod.getVariacionPlana()));
                        break;

                }
            }
        }
        if (!esBloqueo) {
            this.efectos.add(efecto);
        }
    }

    // Método para remover un efecto
    public void removerEfecto(CombateFragment combate, Efecto efecto) {
        boolean esBloqueo = false;
        if (efecto instanceof Modificador) {
            Modificador mod = (Modificador) efecto;
            if (mod.getEstadistica().equals("Bloqueo")) {
            } else {
                switch (mod.getEstadistica()) {

                    case "Vida":
                        this.setVida((int) (this.getVida() - mod.getVariacionPlana()));

                        this.setVidaRestante((int) (this.getVidaRestante() - mod.getVariacionPlana()));
                        this.setVidaRestante((int) (this.getVidaRestante() - this.getVida() * mod.getPorcentaje()/100));
                        break;

                    case "VidaRestante":

                        this.setVidaRestante((int) (this.getVidaRestante() - mod.getVariacionPlana()));
                        this.setVidaRestante((int) (this.getVidaRestante() - this.getVidaRestante() * mod.getPorcentaje()/100));
                        break;

                    case "Defensa":
                        this.setDefensaReal((int) (this.getDefensaReal() - mod.getVariacionPlana()));
                        this.setDefensaReal((int) (this.getDefensaReal() - this.getDefensa() * mod.getPorcentaje()/100));
                        break;

                    case "Ataque":
                        this.setAtaqueReal((int) (this.getAtaqueReal() - mod.getVariacionPlana()));
                        this.setAtaqueReal((int) (this.getAtaqueReal() - this.getAtaque() * mod.getPorcentaje()/100));
                        break;

                    case "Probabilidad de Crítico":
                        this.setProbabilidadCriticoReal((int) (this.getProbabilidadCriticoReal() - mod.getVariacionPlana()));
                        this.setProbabilidadCriticoReal((int) (this.getProbabilidadCriticoReal() - this.getProbabilidadCritico() * mod.getPorcentaje()/100));
                        break;

                    case "Daño Crítico":
                        this.setDanoCriticoReal((int) (this.getDanoCriticoReal() - mod.getVariacionPlana()));
                        this.setDanoCriticoReal((int) (this.getDanoCriticoReal() - this.getDanoCritico() * mod.getPorcentaje()/100));
                        break;

                }
            }
        }
        if (!esBloqueo) {
            this.efectos.remove(efecto);
            combate.pintaEfectos(true);
        }
    }

    public void limpiaEfectos(){
        efectos.clear();
        ataqueReal = ataque;
        defensaReal = defensa;
        probabilidadCriticoReal = probabilidadCritico;
        danoCriticoReal = danoCritico;
        bloqueo = 0;
    }

    public void curar(int cantidad) {
        this.vidaRestante += cantidad;

        if (this.vidaRestante > this.vida) {
            this.vidaRestante = this.vida;
        }
    }

    public void aumentaNivel(int cantidad) {
        this.nivel += cantidad;
    }

}
