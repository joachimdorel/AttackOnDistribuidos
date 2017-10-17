package Server;

import Creature.Titans;
import Util.*;

public class District {
    String name;
    String ipMulticast;
    String puertoMulticast;
    String ipPeticiones;
    String puertoPeticiones;

    public District(String name, String ipMulticast, String puertoMulticast, String ipPeticiones, String puertoPeticiones) {
        this.name = name;
        this.ipMulticast = ipMulticast;
        this.puertoMulticast = puertoMulticast;
        this.ipPeticiones = ipPeticiones;
        this.puertoPeticiones = puertoPeticiones;
    }

    private void CreateNewTitan() {
        ID_generator id_g = new ID_generator();
        Integer id = id_g.newID();
        RandomEnum r = new RandomEnum();
        Const.Type type = r.randomEnum(Const.Type.class);
        Const.Titan_name name = r.randomEnum(Const.Titan_name.class);
        Titans newTitan = new Titans(name, id, type);
    }

    //TODO: lancer une creation de titan regulierement, timer ??
}
