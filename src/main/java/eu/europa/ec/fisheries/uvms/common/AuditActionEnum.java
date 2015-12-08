package eu.europa.ec.fisheries.uvms.common;

public enum AuditActionEnum {
	
	DELETE("delete"),
	MODIFY("modify"),
	CREATE("create"),
    EXECUTE("execute"),
    SHARE("share"),
    UNDEFINED("undefined");
	
	private AuditActionEnum(String reportType) {
		this.reportType = reportType;
	}

	private String reportType;
	
	public String getAuditType() {
		return this.reportType;
	}
}
