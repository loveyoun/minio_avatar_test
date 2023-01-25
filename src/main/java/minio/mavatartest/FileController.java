package minio.mavatartest;

import io.minio.messages.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    /*@GetMapping(value = "/**")
    public ResponseEntity<Object> getFile(HttpServletRequest request) throws IOException {
        String pattern = (String) request.getAttribute(BEST_MATCHING_PATTERN_ATTRIBUTE);
        String filename = new AntPathMatcher().extractPathWithinPattern(pattern, request.getServletPath());
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(IOUtils.toByteArray(minioService.getObject(filename)));
    }*/

    @PostMapping(value = "/file/upload")
    public ResponseEntity<Object> upload(@ModelAttribute FileDTO request) {
        return ResponseEntity.ok().body(minioService.uploadFile(request));
    }

}
