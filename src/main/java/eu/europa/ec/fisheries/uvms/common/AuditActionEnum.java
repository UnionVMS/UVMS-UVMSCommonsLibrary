package eu.europa.ec.fisheries.uvms.common;

public enum AuditActionEnum {
	
	DELETE("delete"),
	MODIFY("modify"),
	CREATE("create"),
	UNDEFINED("undefined");
	
	private AuditActionEnum(String reportType) {
		this.reportType = reportType;
	}

	private String reportType;
	
	public String getAuditType() {
		return this.reportType;
	}
}
