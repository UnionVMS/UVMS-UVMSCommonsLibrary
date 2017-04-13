package eu.europa.ec.fisheries.uvms.message;

/**
 * Created by sanera on 13/04/2017.
 */
public class TextMessageProperties {
    private String adVal;
    private String dfVal;
    private String arVal;
    private String businessUUId;
    private String creationDate;

    public TextMessageProperties(){

    }

    public TextMessageProperties(String adVal, String dfVal, String arVal, String businessUUId, String creationDate) {
        this.adVal = adVal;
        this.dfVal = dfVal;
        this.arVal = arVal;
        this.businessUUId = businessUUId;
        this.creationDate = creationDate;
    }

    public String getAdVal() {
        return adVal;
    }

    public void setAdVal(String adVal) {
        this.adVal = adVal;
    }

    public String getDfVal() {
        return dfVal;
    }

    public void setDfVal(String dfVal) {
        this.dfVal = dfVal;
    }

    public String getArVal() {
        return arVal;
    }

    public void setArVal(String arVal) {
        this.arVal = arVal;
    }

    public String getBusinessUUId() {
        return businessUUId;
    }

    public void setBusinessUUId(String businessUUId) {
        this.businessUUId = businessUUId;
    }

    public String getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(String creationDate) {
        this.creationDate = creationDate;
    }
}
