package Util;

/**
 * Created by user on 17/10/2017.
 */
public class Const {

    //----------Titan variables
//    public enum Type {NORMAL, ECCENTRIC, INCONSTANT}
//    public enum State {FREE, CAPTURED, DEAD};
    //TODO add a list of states and types
    public static final String TYPE_TITAN_NORMAL = "NORMAL";
    public static final String TYPE_TITAN_ECCENTRIC = "ECCENTRIC";
    public static final String TYPE_TITAN_INCONSTANT = "INCONSTANT";
    public static final String STATE_TITAN_FREE = "FREE";
    public static final String STATE_TITAN_CAPTURED = "CAPTURED";
    public static final String STATE_TITAN_DEAD = "DEAD";

    //----------Request value
    public static final String REQ_TYPE = "type";
    public static final String REQ_CONTENT = "content";

    //----------Request type

    //Request district server -> central server
    public static final String REQ_NEW_ID = "new_id";
    //Request client -> district server
    public static final String REQ_TITAN_LIST = "titan_list";
    public static final String REQ_KILL_TITAN = "kill_titan";
    public static final String REQ_CAPTURE_TITAN = "capture_titan";
    //Request client -> central server
    public static final String REQ_CHOSE_DISTRICT = "chose_district";
    public static final String REQ_DISTRICT_LIST = "district_list";
}
