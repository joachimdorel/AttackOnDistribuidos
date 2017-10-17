package Util;

/**
 * Generate unique ID
 */
public class ID_generator {
    private Integer ID = 0;

    public Integer newID(){
        return ID++;
    }
}
