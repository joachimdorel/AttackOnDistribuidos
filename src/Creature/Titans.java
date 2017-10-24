package Creature;

import Util.*;

/**
 * Titans class
 */
public class Titans {
    private String name;
    private Integer ID;
    private Const.Type type;
    private Const.State status;
    private String district;

    public Titans(String name, Integer id, Const.Type type, String district) {
        this.name = name;
        this.ID = id;
        this.type = type;
        this.status = Const.State.FREE;
        this.district = district;
    }

    public Const.State getStatus() {
        return status;
    }

    public Integer getID() {
        return ID;
    }

    public Const.Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDistrict() {return  district;}

    public void setStatus(Const.State status) {this.status = status;}
}

