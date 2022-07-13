package artxew.project.layers.example.dto.req;

import java.util.List;

import artxew.framework.util.StreamResponseWriter;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DownloadZipReqDto {

    @ApiModelProperty(example = "example.zip")
    private String zipName;

    @ApiModelProperty(example =
        "[{\"filePath\": \"lorem_ipsum.txt\"},{\"filePath\": \"spring-logo.svg\"}]"
    )
    private List<StreamResponseWriter.FileDto> fileList;
}