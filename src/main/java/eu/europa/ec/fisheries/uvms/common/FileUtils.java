package eu.europa.ec.fisheries.uvms.common;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public abstract class FileUtils {

    public static void saveByteArrayToFile(byte[] content, Path targetPath) throws IOException {
        File file = targetPath.toFile();

        if (!file.exists()) {
            file.createNewFile();
        }

        FileOutputStream fop = new FileOutputStream(file);

        InputStream is = new  ByteArrayInputStream(content);
        byte[] buf = new byte[4096];
        int bytesRead;
        while ((bytesRead = is.read(buf)) != -1) {
            fop.write(buf, 0, bytesRead);
        }

        fop.close();
    }

}
