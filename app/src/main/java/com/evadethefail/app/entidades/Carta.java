package com.evadethefail.app.entidades;

import java.util.ArrayList;
import java.util.List;

import com.evadethefail.app.objectBox.Utilidades;
import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToMany;
import io.objectbox.relation.ToOne;

@Entity
public class Carta {
    @Id
    private long id;
    private long idCarta;

    private String nombre;

    private String descripcion;
    private int costo;

    // Relación con Clases
    public ToOne<Clase> clase;

    // Relación con Tipos (una carta puede tener varios tipos)
    private ToMany<Tipo> tipos;
    @Backlink(to = "carta")
    private ToMany<CartasPersonaje> cartasPersonaje;

    private int objetivo;
    private int potencia;
    private String elemento;

    // Relación con CartaEfecto (una carta puede aplicar varios efectos)
    @Backlink(to = "carta")
    private ToMany<CartaEfecto> cartaEfectos;

    // Constructor vacío para ObjectBox
    public Carta() {}

    public Carta(long idCarta, String nombre, String descripcion, int costo, int objetivo, int potencia, String elemento) {
        this.idCarta = idCarta;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.costo = costo;
        this.objetivo = objetivo;
        this.potencia = potencia;
        this.elemento = elemento;
    }


    /*
    // 🔹 Constructor con lista de tipos y lista de efectos
    public Carta(long idCarta, String nombre, String descripcion, int costo, Clase clase, List<Tipo> tipos, int objetivo, int potencia, String elemento, List<Efecto> efectos) {
        this.idCarta = idCarta;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.costo = costo;
        this.clase = new ToOne<>(this, Carta_.clase);
        this.clase.setTarget(clase);
        this.tipos.addAll(tipos);
        this.objetivo = objetivo;
        this.potencia = potencia;
        this.elemento = elemento;
        this.efectos.addAll(efectos);
    }

    // Constructor con un solo tipo y varios efectos
    public Carta(long idCarta, String nombre, String descripcion, int costo, Clase clase, Tipo tipo, int objetivo, int potencia, String elemento, List<Efecto> efectos) {
        this.idCarta = idCarta;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.costo = costo;
        this.clase = new ToOne<>(this, Carta_.clase);
        this.clase.setTarget(clase);
        this.tipos.add(tipo);
        this.objetivo = objetivo;
        this.potencia = potencia;
        this.elemento = elemento;
        this.efectos.addAll(efectos);
    }

    // Constructor con varios tipos y un solo efecto
    public Carta(long idCarta, String nombre, String descripcion, int costo, Clase clase, List<Tipo> tipos, int objetivo, int potencia, String elemento, Efecto efecto) {
        this.idCarta = idCarta;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.costo = costo;
        this.clase = new ToOne<>(this, Carta_.clase);
        this.clase.setTarget(clase);
        this.tipos.addAll(tipos);
        this.objetivo = objetivo;
        this.potencia = potencia;
        this.elemento = elemento;
        this.efectos.add(efecto);
    }
            //int, String, String, int, Clase, Tipo, int, int, String
    // 🔹 Constructor con un solo tipo y un solo efecto
    public Carta(long idCarta, String nombre, String descripcion, int costo, Clase clase, Tipo tipo, int objetivo, int potencia, String elemento, Efecto efecto) {
        this.idCarta = idCarta;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.costo = costo;
        this.clase = new ToOne<>(this, Carta_.clase);
        this.clase.setTarget(clase);
        this.tipos.add(tipo);
        this.objetivo = objetivo;
        this.potencia = potencia;
        this.elemento = elemento;
        this.efectos.add(efecto);
    }


    // Constructor con un solo tipo y sin efectos
    public Carta(long idCarta, String nombre, String descripcion, int costo, Clase clase, Tipo tipo, int objetivo, int potencia, String elemento) {
        this.idCarta = idCarta;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.costo = costo;
        this.clase = new ToOne<>(this, Carta_.clase);
        this.clase.setTarget(clase);
        this.tipos.add(tipo);
        this.objetivo = objetivo;
        this.potencia = potencia;
        this.elemento = elemento;
    }

    // Constructor con varios tipos y sin efectos
    public Carta(long idCarta, String nombre, String descripcion, int costo, Clase clase, List<Tipo> tipos, int objetivo, int potencia, String elemento) {
        this.idCarta = idCarta;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.costo = costo;
        this.clase = new ToOne<>(this, Carta_.clase);
        this.clase.setTarget(clase);
        this.tipos.addAll(tipos);
        this.objetivo = objetivo;
        this.potencia = potencia;
        this.elemento = elemento;
}
    */

    public ToMany<CartasPersonaje> getCartasPersonaje() {
        return cartasPersonaje;
    }

    // Getters y Setters


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getIdCarta() {
        return idCarta;
    }

    public void setIdCarta(long idCarta) {
        this.idCarta = idCarta;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCosto() {
        return costo;
    }

    public void setCosto(int costo) {
        this.costo = costo;
    }

    public ToOne<Clase> getClase() {
        return clase;
    }

    public void setClase(ToOne<Clase> clase) {
        this.clase = clase;
    }

    public ToMany<Tipo> getTipos() {
        return tipos;
    }

    public List<String> getTiposNombre() {

        List<String> nombreTipos = new ArrayList<String>();

        for (Tipo tipo : tipos){
            nombreTipos.add(tipo.getNombre());
        }

        return nombreTipos;
    }

    public List<String> getTiposCategoria() {

        List<String> nombreTipos = new ArrayList<String>();

        for (Tipo tipo : tipos){
            nombreTipos.add(tipo.getCategoria());
        }

        return nombreTipos;
    }

    public List<String> getTiposCompleto(){

        List<String> nombreTipos = new ArrayList<String>();

        nombreTipos.addAll(this.getTiposCategoria());
        nombreTipos.addAll(this.getTiposNombre());

        return nombreTipos;
    }

    public void setTipos(ToMany<Tipo> tipos) {
        this.tipos = tipos;
    }

    public void setPersonaje(ToMany<CartasPersonaje> cartasPersonaje) {
        this.cartasPersonaje = cartasPersonaje;
    }

    public int getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(int objetivo) {
        this.objetivo = objetivo;
    }

    public int getPotencia() {
        return potencia;
    }

    public void setPotencia(int potencia) {
        this.potencia = potencia;
    }

    public String getElemento() {
        return elemento;
    }

    public void setElemento(String elemento) {
        this.elemento = elemento;
    }

    public ToMany<CartaEfecto> getCartaEfectos() {
        return cartaEfectos;
    }

    public void setCartaEfectos(ToMany<CartaEfecto> cartaEfectos) {
        this.cartaEfectos = cartaEfectos;
    }

    public void setEfecto(CartaEfecto cartaEfecto) {
        this.getCartaEfectos().add(cartaEfecto);
    }

    public void asignaTipos(Tipo... tipos) {
        this.tipos.clear();
        for (Tipo tipo : tipos) {
            this.getTipos().add(tipo);
        }
    }

    public void asignaEfecto(CartaEfecto cartaEfecto, Efecto efecto) {
        cartaEfecto.getCarta().setTarget(this);
        cartaEfecto.getEfecto().setTarget(efecto);
        cartaEfecto.setIdEfecto(efecto.getIdEfecto());
        cartaEfecto.setNombreCarta(this.getNombre());
        cartaEfecto.setNombreEfecto(efecto.getNombre());
    }

    public List<Efecto> getEfectos() {
        List<Efecto> efectosList = new ArrayList<>();
        if (this.cartaEfectos != null) {
            List<Integer> idsEfectos = new ArrayList<Integer>();
            for (CartaEfecto ce : this.cartaEfectos) {
                if (!idsEfectos.contains((int) ce.getIdEfecto()))
                    idsEfectos.add((int) ce.getIdEfecto());
            }

            for (int idEfecto : idsEfectos) {
                efectosList.addAll(Utilidades.buscaEfectos(idEfecto));
            }

        }
        return efectosList;
    }



}
