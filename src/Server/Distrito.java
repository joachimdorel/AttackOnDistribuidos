package Server;

import Creature.Titanes;
import Util.*;

public class Distrito {

    String nombre;
    String ipMulticast;
    String puertoMulticast;
    String ipPeticiones;
    String puertoPeticiones;

    public Distrito(){}

    public Distrito(String nombre, String ipMulticast, String puertoMulticast, String ipPeticiones, String puertoPeticiones) {
        this.nombre = nombre;
        this.ipMulticast = ipMulticast;
        this.puertoMulticast = puertoMulticast;
        this.ipPeticiones = ipPeticiones;
        this.puertoPeticiones = puertoPeticiones;
    }

    private void CreateNewTitan() {
        ID_generator id_g = new ID_generator();
        Integer id = id_g.newID();
        RandomEnum r = new RandomEnum();
        Const.Tipo tipo = r.randomEnum(Const.Tipo.class);
        Const.Titan_name name = r.randomEnum(Const.Titan_name.class);
        Titanes newTitan = new Titanes(name, id, tipo);
    }

    //TODO: lancer une creation de titan regulierement
}
