package eu.europa.ec.fisheries.uvms.test;

import eu.europa.ec.fisheries.schema.movement.mobileterminal.v1.MobileTerminalId;
import eu.europa.ec.fisheries.schema.movement.search.v1.ListCriteria;
import eu.europa.ec.fisheries.schema.movement.search.v1.ListPagination;
import eu.europa.ec.fisheries.schema.movement.search.v1.MovementListQuery;
import eu.europa.ec.fisheries.schema.movement.search.v1.SearchKey;
import eu.europa.ec.fisheries.schema.movement.v1.MessageType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementBaseType;
import eu.europa.ec.fisheries.schema.movement.v1.MovementPoint;
import eu.europa.ec.fisheries.schema.movement.v1.MovementSourceType;
import org.apache.commons.lang.RandomStringUtils;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

public class MockMovementData {

    public static MovementBaseType getDto(Long id) {
        MovementBaseType dto = new MovementBaseType();
        dto.setId(id.toString());
        dto.setConnectId(UUID.randomUUID().toString());
        dto.setCalculatedSpeed(BigDecimal.valueOf(MockingUtils.randInt(0, 50) + id));
        dto.setCourse(MockingUtils.randInt(1, 20));
        dto.setMeasuredSpeed(BigDecimal.valueOf(MockingUtils.randInt(0, 50) + id));
        dto.setMessageType(MessageType.ENT);
        dto.setMobileTerminal(getMobTermId());
        dto.setPosition(getMovementPoint());
        dto.setSource(MovementSourceType.INMARSAT_C);
        dto.setStatus(RandomStringUtils.random(MockingUtils.randInt(5, 30)));
        dto.setPositionTime(getPositionTime());
        return dto;
    }

    public static MovementPoint getMovementPoint() {
        MovementPoint point = new MovementPoint();
        point.setLatitude(MockingUtils.randInt(-90,90));
        point.setLongitude(MockingUtils.randInt(-180,180));
        return point;
    }

    public static MobileTerminalId getMobTermId() {
        MobileTerminalId id = new MobileTerminalId();
        id.setId("ABC-80+");
        return id;
    }

    public static MovementListQuery getQuery() {
        MovementListQuery query = new MovementListQuery();
        query.getMovementSearchCriteria().add(getListCtieria());
        query.setPagination(getListPagination());
        return query;
    }

    public static ListPagination getListPagination() {
        ListPagination criteria = new ListPagination();
        criteria.setListSize(BigInteger.valueOf(10L));
        criteria.setPage(BigInteger.valueOf(1L));
        return criteria;
    }

    public static ListCriteria getListCtieria() {
        ListCriteria criteria = new ListCriteria();
        criteria.setKey(SearchKey.CONNECT_ID);
        criteria.setValue("value");
        return criteria;
    }

    public static List<MovementBaseType> getDtoList(Integer amount) {
        List<MovementBaseType> dtoList = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            dtoList.add(getDto(Long.valueOf(i)));
        }
        return dtoList;
    }

    public static XMLGregorianCalendar getPositionTime() {
        GregorianCalendar c = new GregorianCalendar();
        c.setTime(new Date());
        try {
            return DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
        } catch (DatatypeConfigurationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
