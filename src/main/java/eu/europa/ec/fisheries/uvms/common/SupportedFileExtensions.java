package eu.europa.ec.fisheries.uvms.common;

public enum SupportedFileExtensions {
    DBF("dbf"),
    SHP("shp"),
    SHX("shx");

    private final String ext;

    SupportedFileExtensions(String ext) {
        this.ext = ext;
    }

    public String getExt() {
        return ext;
    }

    static SupportedFileExtensions fromValue(String ext) {
        for (SupportedFileExtensions extension : values()) {
            if (extension.getExt().equalsIgnoreCase(ext)) {
                return extension;
            }
        }
        return null;
    }

}