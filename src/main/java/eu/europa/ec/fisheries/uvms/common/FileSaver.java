package eu.europa.ec.fisheries.uvms.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

public class FileSaver {

    public void saveContentToFile(byte[] content, Path fileNamePath) throws IOException {
        File file = fileNamePath.toFile();

        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream fop = new FileOutputStream(file);

        fop.write(content);
        fop.flush();
        fop.close();
    }

}
