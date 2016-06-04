package eu.europa.ec.fisheries.uvms.common;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public abstract class ZipExtractor {

    public static void unZipFile(byte[] bytes, Path outputFolderPath) throws IOException {

        byte[] buffer = new byte[2048];

        File folder = outputFolderPath.toFile();
        if (!folder.exists()) {
            folder.mkdir();
        }

        ZipInputStream zis = new ZipInputStream(new ByteArrayInputStream(bytes));
        ZipEntry entry = zis.getNextEntry();

        while (entry != null) {
            Path outputFilePath = Paths.get(outputFolderPath + File.separator + entry.getName());

            File newFile = outputFilePath.toFile();

            new File(newFile.getParent()).mkdirs();

            FileOutputStream fos = new FileOutputStream(newFile);

            int len;
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }

            fos.close();
            entry = zis.getNextEntry();
        }

        zis.closeEntry();
        zis.close();

    }
}
