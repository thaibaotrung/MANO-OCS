package org.acme.Controller;

import java.io.*;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;


@Path("/file")
public class UploadFileService {

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)


    public Response uploadFile(@FormDataParam("file") InputStream uploadedInputStream) throws IOException, InterruptedException {
        // Đường dẫn tới ổ D
        String path = "D:/";
        String name = "TOSCA";
        String outputDir = path + name + "/";
        // Tạo đường dẫn và tên tệp tin theo ngẫu nhiên
        String uploadedFileLocation = path + name + ".zip";

        // Lưu file
        writeToFile(uploadedInputStream, uploadedFileLocation);
        TimeUnit.SECONDS.sleep(5);
        unzipFile(uploadedFileLocation, outputDir);
        String output = "File uploaded to: " + uploadedFileLocation;

        return Response.status(200).entity(output).build();
    }

    // save uploaded file to new location
    private void writeToFile(InputStream uploadedInputStream,
                             String uploadedFileLocation) {

        try {
            OutputStream out = new FileOutputStream(new File(
                    uploadedFileLocation));
            int read = 0;
            byte[] bytes = new byte[1024];

            out = new FileOutputStream(new File(uploadedFileLocation));
            while ((read = uploadedInputStream.read(bytes)) != -1) {
                out.write(bytes, 0, read);
            }
            out.flush();
            out.close();
        } catch (IOException e) {

            e.printStackTrace();
        }

    }


    public class UploadForm {
        @FormDataParam("file")
        private InputStream file;

        @FormDataParam("file")
        private FormDataContentDisposition fileDetail;

        // getters và setters

        public InputStream getFile() {
            return file;
        }

        public void setFile(InputStream file) {
            this.file = file;
        }

        public FormDataContentDisposition getFileDetail() {
            return fileDetail;
        }

        public void setFileDetail(FormDataContentDisposition fileDetail) {
            this.fileDetail = fileDetail;
        }
    }

    private void unzipFile(String zipFilePath, String destDir) throws IOException {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if (!dir.exists()) dir.mkdirs();
        FileInputStream fis = new FileInputStream(zipFilePath);
        ZipInputStream zis = new ZipInputStream(fis);
        ZipEntry ze = zis.getNextEntry();
        while (ze != null) {
            String fileName = ze.getName();
            File newFile = new File(destDir + File.separator + fileName);
            System.out.println("Unzipping to " + newFile.getAbsolutePath());
            // create directories for sub directories in zip
            new File(newFile.getParent()).mkdirs();
            FileOutputStream fos = new FileOutputStream(newFile);
            int len;
            byte[] buffer = new byte[1024];
            while ((len = zis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            // close this ZipEntry
            zis.closeEntry();
            ze = zis.getNextEntry();
        }
        // close last ZipEntry
        zis.closeEntry();
        zis.close();
        fis.close();
    }


}
