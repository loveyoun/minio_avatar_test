package minio.mavatartest;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileDTO implements Serializable {

    private static final long serialVersionUID = 232836038145089522L;

    private String title;
    private String description;

    @SuppressWarnings("java:S1948")
    private MultipartFile file;
    private String url; //http://localhost:8080/{{filename}}
    private Long size;
    private String filename;    // {{folder name}}/{{file name}}.jpg

}
