package eu.europa.ec.fisheries.uvms.common;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Random;

public class MockingUtils {

    private MockingUtils(){

    }

    public static int randInt(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive

        return rand.nextInt((max - min) + 1) + min;
    }

    public static int randIntOdd(int min, int max) {

        // NOTE: Usually this should be a field rather than a method
        // variable so that it is not re-seeded every call.
        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive

        return (rand.nextInt((max - min) + 1) + min)& ~1;
    }

}
