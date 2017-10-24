package Util;

/**
 * Created by user on 17/10/2017.
 */
public class Const {

    //----------Titan variables
    public enum Type {NORMAL, ECCENTRIC, INCONSTANT}
    public enum State {FREE, CAPTURED, DEAD};

    //----------Request value
    public static final String REQ_TYPE = "type";
    public static final String REQ_CONTENT = "content";

    //----------Request type

    //Request client -> district server
    public static final String REQ_TITAN_LIST = "titan_list";
    public static final String REQ_KILL_TITAN = "kill_titan";
    public static final String REQ_CAPTURE_TITAN = "capture_titan";
    //Request client -> central server
    public static final String REQ_CHOSE_DISTRICT = "chose_district";
    public static final String REQ_DISTRICT_LIST = "district_list";
}
