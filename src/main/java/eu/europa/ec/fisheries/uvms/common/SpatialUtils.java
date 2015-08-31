package eu.europa.ec.fisheries.uvms.common;

public class SpatialUtils {

    public static final int DEFAULT_CRS = 4326;

    private SpatialUtils(){

    }

    public static boolean isDefaultCrs(int crs){
        return crs == DEFAULT_CRS;
    }
}
