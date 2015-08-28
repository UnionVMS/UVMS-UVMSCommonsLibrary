package eu.europa.ec.fisheries.uvms.rest.dto;


public enum ResponseCode  {

    OK("200"),
    ERROR("500");

    private final String code;

    private ResponseCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

}
