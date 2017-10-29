package Creature;

import Util.*;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * Titans class
 */

//TODO : check si status et type exist !! add a list in const
public class Titans implements Serializable{
    private String name;
    private Integer ID;
    private String type;
    private String status;
    private String district;
    //Static variable, allows to have a unique ID for each titan
    private static Integer generator_ID = 1;

    public Titans(String name, String type, String district) {
        this.name = name;
        this.ID = generator_ID++;
        this.type = type;
        this.status = Const.STATE_TITAN_FREE;
        this.district = district;
    }

    private Titans(String name, Integer id, String type, String status, String district) {
        this.name = name;
        this.ID = id;
        this.type = type;
        this.status = status;
        this.district = district;
    }

    public String getStatus() {
        return status;
    }

    public Integer getID() {
        return ID;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDistrict() {return  district;}

    public void setStatus(String status) {this.status = status;}

    /**
     * Static function allowing to recover the titan given into the linkedHashMap.
     * When a titan is serialized, it's converted to a LinkedHashMap
     *
     * @param lhm a linkedHashMap representing a Titans
     * @return the Titan corresponding to the linkedHashMap parameter
     */
    public static Titans valueOf (LinkedHashMap<String, Object> lhm){
        return new Titans(
                (String) lhm.get("name"),
                (Integer) lhm.get("id"),
                (String) lhm.get("type"),
                (String) lhm.get("status"),
                (String) lhm.get("district"));
    }
}

