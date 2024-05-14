package org.acme.Model;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@ApplicationScoped
public class FileService {

    private static final Logger LOGGER = Logger.getLogger(FileService.class);

    @Inject
    @ConfigProperty(name = "store.location") // Path to the storage directory
    String storageLocation;

    public void uploadFile(MultipartFile file) throws IOException {
        File file1 = new File(storageLocation + File.separator + file.getOriginalFilename());
        if (file1.createNewFile()) {
            System.out.println("File is created!" + file1.getAbsolutePath());
        } else {
            System.out.println("File already exists.");
        }

        try (BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(file1))) {
            stream.write(file.getBytes());
        }
    }

    public Response downloadFile(String fileName) {
        String filePath = storageLocation + File.separator + fileName;
        File file = new File(filePath);

        if (!file.exists()) {
            throw new WebApplicationException("File not found", Response.Status.NOT_FOUND);
        }

        StreamingOutput stream = outputStream -> {
            try (InputStream inputStream = new FileInputStream(file)) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                outputStream.flush();
            } catch (IOException e) {
                LOGGER.error("Error while streaming the file", e);
                throw new WebApplicationException("Error while streaming the file", e, Response.Status.INTERNAL_SERVER_ERROR);
            }
        };

        return Response.ok(stream, MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=\"" + fileName + "\"")
                .build();
    }
}