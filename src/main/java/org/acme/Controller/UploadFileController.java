//package com.example.fileupload;
//
//import com.mongodb.client.MongoClient;
//import com.mongodb.client.gridfs.GridFSBuckets;
//import com.mongodb.client.gridfs.GridFSBucket;
//import com.mongodb.client.gridfs.model.GridFSUploadOptions;
//import io.quarkus.mongodb.MongoClient;
//import jakarta.ws.rs.Consumes;
//import jakarta.ws.rs.POST;
//import jakarta.ws.rs.Path;
//import jakarta.ws.rs.core.MediaType;
//import jakarta.ws.rs.core.Response;
//import org.bson.types.ObjectId;
//import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
//import org.jboss.resteasy.reactive.MultipartForm;
//
//import javax.inject.Inject;
//import javax.ws.rs.Consumes;
//import javax.ws.rs.POST;
//import javax.ws.rs.Path;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import java.io.InputStream;
//
//@Path("/files")
//public class FileUploadResource {
//
//    @Inject
//    MongoClient mongoClient;
//
//    @POST
//    @Path("/upload")
//    @Consumes(MediaType.MULTIPART_FORM_DATA)
//    public Response uploadFile(@MultipartForm FileUploadForm form) {
//        try {
//            GridFSBucket gridFSBucket = GridFSBuckets.create(mongoClient.getDatabase("mydatabase"));
//            GridFSUploadOptions options = new GridFSUploadOptions();
//
//            ObjectId fileId = gridFSBucket.uploadFromStream(form.fileName, form.fileData, options);
//            return Response.ok("File uploaded with ID: " + fileId.toString()).build();
//        } catch (Exception e) {
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("File upload failed: " + e.getMessage()).build();
//        }
//    }
//
//    public static class FileUploadForm {
//        @org.jboss.resteasy.annotations.providers.multipart.PartType(MediaType.TEXT_PLAIN)
//        public String fileName;
//
//        @org.jboss.resteasy.annotations.providers.multipart.PartType(MediaType.APPLICATION_OCTET_STREAM)
//        public InputStream fileData;
//    }
//}
