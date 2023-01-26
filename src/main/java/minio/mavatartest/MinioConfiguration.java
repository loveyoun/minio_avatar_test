package minio.mavatartest;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class MinioConfiguration {

    @Value("${minio.access.key}")
    private String accessKey;

    @Value("${minio.access.secret}")
    private String secretKey;

    //Minio server console URL
    @Value("${minio.url}")
    private String minioUrl;

    @Bean
    @Primary
    public MinioClient minioClient(){
//        try{
//            MinioClient client = new MinioClient(minioUrl, accessKey, secretKey);
//            return client;
//        }catch(Exception e){
//            throw new RuntimeException(e.getMessage());
//        }
        //서버에 접근할 수 있도록 Minio Client 객체 생성
        try {
            MinioClient client = new MinioClient.Builder()
                .credentials(accessKey, secretKey)
                .endpoint(minioUrl)
                .build();
            return client;
        } catch(Exception e){
            throw new RuntimeException(e.getMessage());
            }

    }

}
