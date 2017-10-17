package Util;


import java.util.Random;

/**
 * Created by user on 17/10/2017.
 */
public class RandomEnum {
    private static final Random random = new Random();

    public <T extends Enum<?>> T randomEnum(Class<T> clazz){
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }
}
