/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it 
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of 
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; 
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more 
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


package eu.europa.ec.fisheries.uvms.commons.geometry.utils;

import com.vividsolutions.jts.geom.*;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.linearref.LengthIndexedLine;
import eu.europa.ec.fisheries.uvms.commons.geometry.mapper.GeometryMapper;
import eu.europa.ec.fisheries.uvms.commons.service.exception.ServiceException;
import org.apache.commons.collections.CollectionUtils;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.measure.Latitude;
import org.geotools.measure.Longitude;
import org.geotools.referencing.CRS;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.Math.toRadians;

public final class GeometryUtils {

    private static final Logger LOG = LoggerFactory.getLogger(GeometryUtils.class);

    private static final String EPSG = "EPSG:";

    public static final int DEFAULT_EPSG_SRID = 4326;
    public static GeometryFactory geometryFactory = new GeometryFactory();

    /**
     * private constructor to avoid class instantiation
     */
    private GeometryUtils() {
    }

    /**
     * Transforms the geometry from its current crs to {@link org.geotools.referencing.crs.DefaultGeographicCRS#WGS84}. If the
     * specified geometry is already in WGS84, then it is returned unchanged.
     *
     * @param geom
     *            The geometry to transform.
     * @param crs
     *            The CRS the geometry is currently in.
     * @return The geometry transformed to be in {@link org.geotools.referencing.crs.DefaultGeographicCRS#WGS84}.
     * @throws ServiceException
     *             If at least one coordinate can't be transformed.
     */
    public static Geometry toGeographic(Geometry geom, Integer crs) throws ServiceException {
        try {
            CoordinateReferenceSystem decode = CRS.decode(EPSG + Integer.toString(crs), true);
            return JTS.toGeographic(geom, decode);
        } catch (TransformException  | FactoryException e) {
            throw new ServiceException(e.getMessage(), e);
        }
    }

    /**
     * This method returns a Point in WGS84 for a given latitude and longitude and Coordinate reference System.
     * It will check the parameters for inconsistencies and translate the point if necessary.
     *
     * @param y The latitude value in <strong>decimal degrees</strong>.
     * @param x The longitude value in <strong>decimal degrees</strong>.
     * @param crs
     *            The CRS the geometry is currently in.
     * @return The Point in WGS84.
     */
    public static Geometry toGeographic(Double y, Double x, Integer crs) throws ServiceException {

        Point point = geometryFactory.createPoint(new Coordinate(x, y));

        if (!isDefaultEpsgSRID(crs)) {
            point = (Point) toGeographic(point, crs);
        }

        point.setSRID(DEFAULT_EPSG_SRID);
        double x1 = point.getX();
        double y1 = point.getY();
        checkLatitude(y1);
        checkLongitude(x1);
        return point;
    }

    /**
     * Returns the centroid of a given geometry as WKT
     *
     * @param wkt a WKT
     * @return wkt the centroid as WKT
     */
    public static String wktToCentroidWkt(final String wkt){

        String theWktString = null;

        try {
            Point centroid = GeometryMapper.INSTANCE.wktToGeometry(wkt).getValue().getCentroid();
            theWktString = GeometryMapper.INSTANCE.geometryToWkt(centroid).getValue();

        } catch (ParseException e) {
            LOG.error(e.getMessage(), e);
        }

        return theWktString;
    }

    /**
     * Checks the longitude validity. The argument {@code longitude} should be
     * greater or equal than -180 degrees and lower or equals than +180 degrees. As
     * a convenience, this method returns the longitude in radians.
     *
     * @param  longitude The longitude value in <strong>decimal degrees</strong>.
     * @return The longitude value in <strong>radians</strong>.
     * @throws IllegalArgumentException if {@code longitude} is not between -180 and +180 degrees.
     */
    private static double checkLongitude(final double longitude) throws IllegalArgumentException {
        if (longitude >= Longitude.MIN_VALUE && longitude <= Longitude.MAX_VALUE) {
            return toRadians(longitude);
        }
        throw new IllegalArgumentException(Errors.format(ErrorKeys.LONGITUDE_OUT_OF_RANGE_$1, new Longitude(longitude)));
    }

    /**
     * Checks the latitude validity. The argument {@code latitude} should be
     * greater or equal than -90 degrees and lower or equals than +90 degrees. As
     * a convenience, this method returns the latitude in radians.
     *
     * @param  latitude The latitude value in <strong>decimal degrees</strong>.
     * @return The latitude value in <strong>radians</strong>.
     * @throws IllegalArgumentException if {@code latitude} is not between -90 and +90 degrees.
     */
    private static double checkLatitude(final double latitude) throws IllegalArgumentException {
        if (latitude >= Latitude.MIN_VALUE && latitude <= Latitude.MAX_VALUE) {
            return toRadians(latitude);
        }
        throw new IllegalArgumentException(Errors.format(
                ErrorKeys.LATITUDE_OUT_OF_RANGE_$1, new Latitude(latitude)));
    }

    public static boolean isDefaultEpsgSRID(int crs) {
        return crs == DEFAULT_EPSG_SRID;
    }

    public static Geometry transform(Double tx, Double ty, Geometry geometry) {

        AffineTransform translate= AffineTransform.getTranslateInstance(tx, ty);
        Coordinate[] source = geometry.getCoordinates();
        Coordinate[] target = new Coordinate[source.length];

        for (int i= 0; i < source.length; i++){
            Coordinate sourceCoordinate = source[i];
            Point2D p = new Point2D.Double(sourceCoordinate.x,sourceCoordinate.y);
            Point2D transform = translate.transform(p, null);
            target[i] = new Coordinate(transform.getX(), transform.getY());
        }

        GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory();
        Geometry targetGeometry;

        if (geometry instanceof Point){
            targetGeometry = geometryFactory.createPoint(target[0]);
        }
        else if (geometry instanceof Polygon){
            targetGeometry = geometryFactory.createPolygon(target);
        }
        else {
            throw new UnsupportedOperationException("GEOMETRY TYPE NOT SUPPORTED");
        }

        return targetGeometry;
    }

    public static Geometry calculateIntersectingPoint(LengthIndexedLine lengthIndexedLine, Double index) throws ServiceException {
        Coordinate coordinate = lengthIndexedLine.extractPoint(index);
        return GeometryUtils.createPoint(coordinate);
    }

    public static LengthIndexedLine createLengthIndexedLine(String wkt1, String wkt2) throws ServiceException {
        Geometry lineString = createLineString(wkt1, wkt2);
        return new LengthIndexedLine(lineString);
    }

    public static Geometry createPoint(Double longitude, Double latitude) {
        if(null == longitude || null == latitude){
            return null;
        }
        GeometryFactory geometryFactory = new GeometryFactory();
        Coordinate coordinate = new Coordinate(longitude, latitude);
        Geometry point = geometryFactory.createPoint(coordinate);
        point.setSRID(DEFAULT_EPSG_SRID);
        return point;
    }

    public static Geometry createPoint(Coordinate coordinate) {
        GeometryFactory geometryFactory = new GeometryFactory();
        Geometry point = geometryFactory.createPoint(coordinate);
        point.setSRID(DEFAULT_EPSG_SRID);
        return point;
    }

    public static Geometry createMultipoint(List<Geometry> geometries) {
        if (CollectionUtils.isEmpty(geometries)) {
            return null;
        }
        GeometryFactory geometryFactory = new GeometryFactory();
        Set<Coordinate> coordinates = new HashSet<>();
        for (Geometry geom : geometries) {
            coordinates.add(geom.getCoordinate());
        }
        Geometry multiPoint = geometryFactory.createMultiPoint(coordinates.toArray(new Coordinate[coordinates.size()]));
        multiPoint.setSRID(DEFAULT_EPSG_SRID);
        return multiPoint;
    }

    public static Geometry createLineString(String wkt1, String wkt2) throws ServiceException {
        LineString line;

        try {
            Geometry point1 = GeometryMapper.INSTANCE.wktToGeometry(wkt1).getValue();
            Geometry point2 = GeometryMapper.INSTANCE.wktToGeometry(wkt2).getValue();
            GeometryFactory geometryFactory = new GeometryFactory();
            List<Coordinate> coordinates = new ArrayList<>();
            coordinates.add(point1.getCoordinate());
            coordinates.add(point2.getCoordinate());
            line = geometryFactory.createLineString(coordinates.toArray(new Coordinate[coordinates.size()]));
            line.setSRID(DEFAULT_EPSG_SRID);

        } catch (ParseException e) {
            throw new ServiceException(e.getMessage(), e);
        }

        return line;
    }

    public static CoordinateReferenceSystem toDefaultCoordinateReferenceSystem() throws ServiceException {
        try {
            return CRS.decode(EPSG + DEFAULT_EPSG_SRID);
        } catch (FactoryException e) {
            LOG.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
    }

    public static CoordinateReferenceSystem toCoordinateReferenceSystem(Integer srid) throws ServiceException {
        try {
            return CRS.decode(EPSG + srid);
        } catch (FactoryException e) {
            LOG.error(e.getMessage(), e);
            throw new ServiceException(e.getMessage(), e);
        }
    }
}