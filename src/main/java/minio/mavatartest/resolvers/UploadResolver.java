//package minio.mavatartest.resolvers;
//
//import graphql.kickstart.servlet.context.DefaultGraphQLServletContext;
//import graphql.kickstart.tools.GraphQLMutationResolver;
//import graphql.schema.DataFetchingEnvironment;
//import io.minio.MinioClient;
//import io.minio.PutObjectArgs;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import java.util.UUID;
//
//@Slf4j
//@Component
//public class UploadResolver implements GraphQLMutationResolver {
//
//    @Autowired
//    private MinioClient minioClient;
//
//    @Value("${minio.bucket.name}")
//    private String bucket;
//
//
//    public UUID uploadFile(DataFetchingEnvironment environment) { //DFE should be the last parameter
//        log.info("Uploading file");
//
//        //access to the file
//        DefaultGraphQLServletContext context = environment.getContext();
//        //Object context = environment.getContext();
//
//        //List<Part> fileParts = context.getFileParts();
//        context.getFileParts().forEach(part -> {
//            try {
//                minioClient.putObject(PutObjectArgs.builder()
//                        .bucket(bucket)
//                        .object(part.getSubmittedFileName())
//                        .stream(part.getInputStream(), part.getSize(), -1)
//                        .build());
//            } catch (Exception e) {
//                log.error("Happened error when upload file: ", e);
//            }
//
//            //log.info("uploading: {} , size: {}", part.getSubmittedFileName(), part.getSize());
//        });
//
//        return UUID.randomUUID();
//    }
//
//}
