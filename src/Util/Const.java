package Util;

/**
 * Created by user on 17/10/2017.
 */
public class Const {

    //----------Titan variables
    //Type of a titan
    public static final String TYPE_TITAN_NORMAL = "NORMAL";
    public static final String TYPE_TITAN_ECCENTRIC = "ECCENTRIC";
    public static final String TYPE_TITAN_INCONSTANT = "INCONSTANT";
    //State of a titan
    public static final String STATE_TITAN_FREE = "FREE";
    public static final String STATE_TITAN_CAPTURED = "CAPTURED";
    public static final String STATE_TITAN_DEAD = "DEAD";

    //-------------------------------------------------
    //-----------Message Json Const--------------------
    //-------------------------------------------------

    //----------Request keys
    public static final String REQ_TYPE = "type";
    public static final String REQ_CONTENT = "content";

    public static final String KEY_DISTRICT_REQUEST_PORT = "portDistrict";
    public static final String KEY_DISTRICT_REQUEST_IP = "ipDistrict";
    public static final String KEY_DISTRICT_MULTICAST_IP = "ipMulticast";
    public static final String KEY_DISTRICT_MULTICAST_PORT = "portMulticast";

    //----------Value of the request type

    //Request district server -> central server
    public static final String REQ_NEW_ID = "new_id";
    //Request client -> district server
    public static final String REQ_TITAN_LIST = "titan_list";
    public static final String REQ_KILL_TITAN = "kill_titan";
    public static final String REQ_CAPTURE_TITAN = "capture_titan";
    //Request client -> central server
    public static final String REQ_CHOSE_DISTRICT = "chose_district";
    public static final String REQ_DISTRICT_LIST = "district_list";

    //----------Value of a request district access content
    public static final String VALUE_ACCESS_REFUSED = "accessRefused";
    public static final String VALUE_ACCESS_ACCEPTED = "accessAccepted";
    public static final String VALUE_ACCESS_IMPOSSIBLE = "accessImpossible";

    //---------Value of a request to kill or capture a titan
    public static final String VALUE_REQUEST_ACCEPTED = "requestAccepted";
    public static final String VALUE_REQUEST_REFUSED = "requestRefused";

}
