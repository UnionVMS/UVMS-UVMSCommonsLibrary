/*
Developed by the European Commission - Directorate General for Maritime Affairs and Fisheries @ European Union, 2015-2016.

This file is part of the Integrated Fisheries Data Management (IFDM) Suite. The IFDM Suite is free software: you can redistribute it
and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of
the License, or any later version. The IFDM Suite is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for more
details. You should have received a copy of the GNU General Public License along with the IFDM Suite. If not, see <http://www.gnu.org/licenses/>.

 */


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

    public static void renameFiles(Path absolutePath, String newFileName) {
        if (absolutePath != null) {
            for (final File file : absolutePath.toFile().listFiles()) {
                String[] fileNameSplits = file.getName().split("\\.");
                int extensionIndex = fileNameSplits.length - 1; // extension is assumed to be the last part
                File newFile = new File(file.getParent() + File.separator + newFileName + "." + fileNameSplits[extensionIndex]);
                boolean status = file.renameTo(newFile);
            }
        }
    }
}
