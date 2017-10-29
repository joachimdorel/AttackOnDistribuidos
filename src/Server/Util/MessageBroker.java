package Server.Util;

import Creature.Titans;
import java.lang.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.*;

/**
 * Allow JSON strings parsing into the equivalent key-value map
 * and vice versa (map serialization into JSON string)
 */

//TODO : rajouter pour le traitement des listes et des objets (liste de titan)

public class MessageBroker {
    /**
     * A encapsulated map representing some message
     */
    private Map<String, Object> map;
    private static ObjectMapper mapper = new ObjectMapper();

    public MessageBroker() {
        map = new HashMap<String, Object>();
    }

    /**
     * Build a map from a given JSON string
     *
     * @param content a given JSON string
     */
    public MessageBroker(String content) {
        parse(content);
    }

    /**
     * Constructor with a pair key-value to add to the map object at construction
     *
     * @param key   any given key
     * @param value any given value
     */
    public MessageBroker(String key, Object value) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put(key, value);
        this.map = map;
    }


    /**
     * Standard getter
     *
     * @param key   any given key
     * @return the value for the key
     */
    public Object get(String key) {
        return map.get(key);
    }

    /**
     * Getter to retrieve the value of a given key as a String
     * If the value is a String, it is returned
     * Otherwise, we assume it is a map and its JSON serialization is returned
     *
     * @param key any given key
     * @return the String representation of the value
     */
    public String getStringValue(String key) {
        Object value = map.get(key);
        if (value instanceof String)
            return (String) value;

        StringWriter sw = new StringWriter();
        try {
            mapper.writeValue(sw, value);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sw.toString();
    }

    /**
     * Getter to retrieve the value of a given key as a Integer
     * If the value is a Integer, it is returned
     * Otherwise, we assume it is a map and its JSON serialization is returned
     *
     * @param key any given key
     * @return the String representation of the value
     */
    public int getIntegerValue(String key) {
        Object value = map.get(key);
        try {
            return (Integer) value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public Titans getTitansValue(String key) {
        Object value = map.get(key);
        //TODO check the cast
        return Titans.valueOf((LinkedHashMap<String, Object>) value);
    }

    public ArrayList<Titans> getListTitansValue(String key) {
        Object value = map.get(key);

        //noinspection unchecked
        List<Object> obj = (List<Object>) value;
        if (obj == null)
            return null;
        ArrayList<Titans> res = new ArrayList<Titans>();
        for (Object o : obj) {
            //TODO check the cast
            if (o instanceof java.util.LinkedHashMap) {
                res.add(Titans.valueOf((LinkedHashMap<String, Object>) o));
            }
        }
        return res;
    }

    /**
     * Standard setter: add a pair key-value to the encapsulated map
     *
     * @param key   any given key
     * @param value any given value
     */
    public void put(String key, Serializable value) {
        map.put(key, value);
    }

    /**
     * Get the internal map
     *
     * @return the map of the ContentBroker instance
     */
    public Map<String, Object> getMap() {
        return map;
    }

    /**
     * Parse a JSON string into the map of the ContentBroker instance
     *
     * @param content a given JSON string
     */
    private void parse(String content) {
        try {
            //noinspection unchecked
            map = mapper.readValue(content, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Serialize the map object into a JSON String representation
     *
     * @return the resulting JSON string
     */
    public String toJson() {
        StringWriter sw = new StringWriter();
        try {
            mapper.writeValue(sw, map);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sw.toString();
    }
}
