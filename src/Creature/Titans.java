package Creature;

import Util.*;

/**
 * Titans class
 */
public class Titans {
    private Const.Titan_name name;
    private Integer ID;
    private Const.Type type;
    private Const.State status;

    public Titans(Const.Titan_name name, Integer id, Const.Type type) {
        this.name = name;
        this.ID = id;
        this.type = type;
        this.status = Const.State.LIBRE;
    }

    public Const.State getStatus() {
        return status;
    }

    public Integer getID() {
        return ID;
    }

    public Const.Type getTypo() {
        return type;
    }

    public Const.Titan_name getName() {
        return name;
    }
}

