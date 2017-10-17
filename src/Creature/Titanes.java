package Creature;

import Util.*;

/**
 * Titanes class
 */
public class Titanes {
    Const.Titan_name nombre;
    Integer ID;
    Const.Tipo tipo;
    Const.Estado estado;

    public Titanes(Const.Titan_name nombre, Integer id, Const.Tipo tipo) {
        this.nombre = nombre;
        this.ID = id;
        this.tipo = tipo;
        this.estado = Const.Estado.LIBRE;
    }
}

