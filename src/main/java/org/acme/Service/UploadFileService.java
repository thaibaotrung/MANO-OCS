package org.acme.Service;

import java.io.*;
import java.nio.file.*;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.media.multipart.*;

@Path("/upload")
public class UploadFileService {

    private static final String UPLOAD_DIR = "D:/";

    public Response uploadFile(
            @FormDataParam("file") InputStream uploadedInputStream,
            @FormDataParam("file") FormDataContentDisposition fileDetail
    ) throws IOException, InterruptedException {
        String fileName = fileDetail.getFileName();
        String uploadedFileLocation = UPLOAD_DIR + fileName;

        // Lưu file
        saveToFile(uploadedInputStream, uploadedFileLocation);
        TimeUnit.SECONDS.sleep(5);

        // Giải nén file
        String outputDir = UPLOAD_DIR + fileName.substring(0, fileName.lastIndexOf('.'));
        unzipFile(uploadedFileLocation, outputDir);

        String output = "File uploaded and extracted to: " + outputDir;

        return Response.status(200).entity(output).build();
    }

    private void saveToFile(InputStream uploadedInputStream, String uploadedFileLocation) throws IOException {
        Files.copy(uploadedInputStream, Paths.get(uploadedFileLocation), StandardCopyOption.REPLACE_EXISTING);
    }

    private void unzipFile(String zipFilePath, String outputDir) throws IOException {
        try (ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath))) {
            ZipEntry entry = zipInputStream.getNextEntry();
            while (entry != null) {
                String filePath = outputDir + File.separator + entry.getName();
                if (!entry.isDirectory()) {
                    extractFile(zipInputStream, filePath);
                } else {
                    createDirectory(filePath);
                }
                zipInputStream.closeEntry();
                entry = zipInputStream.getNextEntry();
            }
        }
    }

    private void extractFile(ZipInputStream zipInputStream, String filePath) throws IOException {
        byte[] buffer = new byte[1024];
        try (BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(filePath))) {
            int bytesRead;
            while ((bytesRead = zipInputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }

    private void createDirectory(String dirPath) {
        File dir = new File(dirPath);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}