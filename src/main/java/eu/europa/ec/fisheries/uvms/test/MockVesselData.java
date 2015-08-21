package eu.europa.ec.fisheries.uvms.test;

import eu.europa.ec.fisheries.wsdl.vessel.types.CarrierSource;
import eu.europa.ec.fisheries.wsdl.vessel.types.Vessel;
import eu.europa.ec.fisheries.wsdl.vessel.types.VesselId;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class MockVesselData {

    public static Vessel getVesselDto(Integer id) {
        Vessel dto = new Vessel();

        dto.setCfr("CFR" + id);
        dto.setCountryCode("SWE" + id);
        dto.setExternalMarking("MARKING" + 1);
        dto.setGrossTonnage(BigDecimal.valueOf(1.2));
        dto.setHasIrcs(true);
        dto.setHasLicense(true);
        dto.setHomePort("PORT" + id);

        VesselId vesselId = new VesselId();
        vesselId.setValue(id.toString());
        dto.setVesselId(vesselId);
        dto.setIrcs("IRCS-" + id);
        dto.setLengthBetweenPerpendiculars(BigDecimal.valueOf(MockingUtils.randInt(0, 3) + id));
        dto.setLengthOverAll(BigDecimal.valueOf(MockingUtils.randInt(0,10) + id));
        dto.setName("VESSEL-" + id);
        dto.setOtherGrossTonnage(BigDecimal.valueOf(MockingUtils.randInt(0,30) + id));
        dto.setPowerAux(BigDecimal.valueOf(MockingUtils.randInt(0,999) + id));
        dto.setPowerMain(BigDecimal.valueOf(MockingUtils.randInt(0,999) + id));
        dto.setSafetyGrossTonnage(BigDecimal.valueOf(MockingUtils.randInt(0,100) + id));
        dto.setSource(CarrierSource.LOCAL);
        dto.setActive(true);

        if (id % 3 == 0) {
            dto.setSource(CarrierSource.LOCAL);
            dto.setActive(true);
        }
        if (id % 2 == 0) {
            dto.setSource(CarrierSource.NATIONAL);
            dto.setActive(false);
        }
        if (id % 5 == 0) {
            dto.setSource(CarrierSource.XEU);
            dto.setActive(true);
            dto.setVesselType("VESSEL-TYPE: " + id);
        }
        dto.setVesselType("VESSEL-TYPE: " + id);
        return dto;
    }

    public static List<Vessel> getVesselDtoList(Integer amount) {
        List<Vessel> dtoList = new ArrayList<>();
        for (int i = 0; i < amount; i++) {
            dtoList.add(getVesselDto(i));
        }
        return dtoList;
    }
}
