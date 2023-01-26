package minio.mavatartest;

import io.minio.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class MinioService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket.name}")
    private String bucket;

    /*
    * 기능적 요구사항
    * 1. 지정된 bucket에 이미지 올리기
    * 2. 이미지 bytes 받아와서 -> 뿌려주기
    * 3. 이미지 파일 다운받기
    */

    public List<Bucket> getAllBuckets() {
        try{
            return minioClient.listBuckets();
        } catch(Exception e){
            throw new RuntimeException(e.getMessage());
        }
    }

    //from bucket, 목록 객체 가져옴
    public List<FileDTO> getListObjects(){
        List<FileDTO> objects = new ArrayList<>();
        try {
            //해당 bucket에서 objects list 가져오기
            Iterable<Result<Item>> result = minioClient.listObjects(ListObjectsArgs.builder()
                    .bucket(bucket)
                    .recursive(true)
                    .build());
            //loop 돌면서
            for (Result<Item> item : result) {
                objects.add(FileDTO.builder()
                        .filename(item.get().objectName())
                        .size(item.get().size())
                        .url(getPreSignedUrl(item.get().objectName()))
                        .build());
            }
            return objects;
        } catch (Exception e) {
            log.error("Happened error when get list objects from minio: ", e);
        }

        return objects;
    }

    public InputStream getObject(String filename) {
        InputStream inputStream;
        try {
            inputStream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucket)
                    .object(filename)
                    .build());

            Image img = ImageIO.read(inputStream);
            File file = new File("test.jpg");
            ImageIO.write((RenderedImage) img, "jpg", file);

        } catch (Exception e) {
            log.error("Happened error when get list objects from minio: ", e);
            return null;
        }


        return inputStream;
    }

//    public byte[] getFile(String filename) {
//        try {
//            //InputStream obj = minioClient.getObject(bucket, defaultBaseFolder + "/" + key);
//            InputStream obj = minioClient.getObject(GetObjectArgs.builder()
//                    .bucket(bucket)
//                    .object(filename)
//                    .build());
//
//            byte[] content = IOUtils.toByteArray(obj);
//            obj.close();
//            return content;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }


    public Object uploadFile(FileDTO request) {
        try {
            minioClient.putObject(PutObjectArgs.builder()
                    .bucket(bucket)
                    .object(request.getFile().getOriginalFilename())
                    .stream(request.getFile().getInputStream(), request.getFile().getSize(), -1)
                    .build());
        } catch (Exception e) {
            log.error("Happened error when upload file: ", e);
        }
        return FileDTO.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .size(request.getFile().getSize())
                .url(getPreSignedUrl(request.getFile().getOriginalFilename()))
                .filename(request.getFile().getOriginalFilename())
                .build();
    }


    private String getPreSignedUrl(String filename){
        return "http://localhost:9000/browser/".concat(filename);
    }

}
