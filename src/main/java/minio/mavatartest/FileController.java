package minio.mavatartest;

import io.minio.messages.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static org.springframework.web.servlet.HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE;

@Slf4j
@RestController
public class FileController {

    @Autowired
    private MinioService minioService;


    @GetMapping(path="/buckets")
    public List<Bucket> listBuckets(){
        return minioService.getAllBuckets();
    }

    @GetMapping(value="/file")
    public ResponseEntity<Object> getFiles() {
        return ResponseEntity.ok(minioService.getListObjects());
    }

    //파일 객체 하나 가져오기(Download)
    @GetMapping(value = "/file/download/**")  //경로의 모든 하위 디렉토리 매핑. '/*'는 경로 바로 하위에 있는 모든경로 매핑
    public ResponseEntity<Object> getFile(HttpServletRequest request) throws IOException {
        String pattern = (String) request.getAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE);
        String filename = new AntPathMatcher().extractPathWithinPattern(pattern, request.getServletPath());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(IOUtils.toByteArray(minioService.getObject(filename)));
    }

//    @GetMapping(path = "/download")
//    public ResponseEntity<ByteArrayResource> uploadFile(@RequestParam(value = "file") String file) throws IOException {
//        MinioService ms = new MinioService();
//        byte[] bytes = new byte[1024];
//        bytes = ms.getFile(file); //ERROR//Non static method cannot be referenced from a static context //bytes[] bytes=로 바로 했을 떄.
//        ByteArrayResource resource = new ByteArrayResource(bytes);
//
//        return ResponseEntity
//                .ok()
//                .contentLength(bytes.length)
//                .header("Content-type", "application/octet-stream")
//                .header("Content-disposition", "attachment; filename=\"" + file + "\"")
//                .body(resource);
//    }

    @PostMapping(value = "/file/upload")
    public ResponseEntity<Object> upload(@ModelAttribute FileDTO request) {
        return ResponseEntity.ok().body(minioService.uploadFile(request));
    }

}
