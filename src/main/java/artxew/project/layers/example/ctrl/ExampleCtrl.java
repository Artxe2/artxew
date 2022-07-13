package artxew.project.layers.example.ctrl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.validation.Valid;

import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import artxew.framework.decedent.ctrl.BaseController;
import artxew.framework.decedent.dto.ServerResponseDto;
import artxew.framework.environment.authcheck.AuthCheck;
import artxew.framework.layers.cmminq.svc.CmmInqSvc;
import artxew.framework.util.StringUtil;
import artxew.framework.util.MailSender;
import artxew.framework.util.StreamResponseWriter;
import artxew.project.layers.example.dto.req.BadRequestReqDto;
import artxew.project.layers.example.dto.req.DownloadZipReqDto;
import artxew.project.layers.example.dto.res.DownloadExcelResDto;
import artxew.project.layers.example.svc.ExampleSvc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Api(tags = { "Examples" })
@Profile("!prod")
@RequestMapping("api/example")
@RequiredArgsConstructor
@RestController
public class ExampleCtrl extends BaseController {

    private static final String FILE_PATH
            = new File("src/main/resources/public/upload").getAbsolutePath();

    private final Environment env;

    private final ExampleSvc exampleSvc;

    private final CmmInqSvc cmmInqSvc;



    @Operation(summary = "Query Status")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "code"
            , required = true
            , example = "200"
            , value = "Http Status"
            , dataTypeClass = Integer.class
        )
    })
    @GetMapping("status")
    public ServerResponseDto<String> status(
        @RequestParam int code
    ) {
        String time = cmmInqSvc.currentTime();

        return processResult(time, "status: " + code)
                .status(code);
    }



    @Operation(summary = "Query Exception")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "name"
            , required = true
            , example = "forbidden"
            , value = "Exception Name"
            , dataTypeClass = String.class
        )
    })
    @GetMapping("defined-exception/{name}")
    public ServerResponseDto<String> definedException(
        @PathVariable(required = true) String name
    ) {
        String time = cmmInqSvc.currentTime();

        exception(time, name);
        return processResult(null);
    }



    @Operation(summary = "Service Exception")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "name"
            , required = true
            , example = "forbidden"
            , value = "Exception Name"
            , dataTypeClass = String.class
        )
    })
    @GetMapping("service-exception/{name}")
    public ServerResponseDto<String> serviceException(
        @PathVariable(required = true) String name
    ) {
        exampleSvc.serviceException(name);
        return processResult(null);
    }



    @Operation(summary = "Bad Request")
    @AuthCheck({ "01", "02", "03" })
    @PostMapping("bad-request")
    public ServerResponseDto<String> badRequest(
        @Valid @RequestBody BadRequestReqDto reqDto
    ) {
        String time = cmmInqSvc.currentTime();

        return processResult(time);
    }



    @Operation(summary = "Query Hash")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "data"
            , required = true
            , example = "ASDF"
            , value = "Data"
            , dataTypeClass = String.class
        )
    })
    @GetMapping("hash")
    public ServerResponseDto<Object> hash(String data) {
        return processResult(StringUtil.toHash(data));
    }



    @Operation(summary = "Query Property")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "key"
            , required = true
            , example = "artxew.domain"
            , value = "Property Name"
            , dataTypeClass = String.class
        )
    })
    @GetMapping("property")
    public ServerResponseDto<Object> property(String key) {
        return processResult(env.getProperty(key));
    }



    @Operation(summary = "Send Mail")
    @PostMapping(
        value = "send-mail"
        , consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ServerResponseDto<String> sendMail(
        String to
        , String cc
        , String bcc
        , @RequestPart(required = false) MultipartFile[] files
    ) {
        String time = cmmInqSvc.currentTime();

        MailSender.send(
            "ARTXEW EMAIL"
            , "<h1 style=\"color:green\">Test Pass " + time + "</h1>"
            , to, cc, bcc
            , files
        );
        return processResult(time);
    }



    @Operation(summary = "Upload File")
    @PostMapping(
        value = "file"
        , consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public void uploadFiles(
        @RequestPart(required = true) MultipartFile[] files
    ) {
        for (MultipartFile file : files) {
            File f = new File(FILE_PATH, UUID.randomUUID().toString());

            f.mkdirs();
            try {
                file.transferTo(f);
                log.info("{} -> {}", file.getOriginalFilename(), f.getName());
            } catch (Exception e) {
                exception("upload-files-error", e);
            }
        }
    }



    @Operation(summary = "Download File")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "filePath"
            , required = true
            , example = "lorem_ipsum.txt"
            , value = "File Path"
            , dataTypeClass = String.class
        )
    })
    @GetMapping("file")
    public void downloadFile(String filePath) {
        StreamResponseWriter.file(filePath, null);
    }



    @Operation(summary = "Download Zip")
    @PostMapping("zip")
    public void downloadZip(@RequestBody DownloadZipReqDto reqDto) {
        StreamResponseWriter.zip(reqDto.getZipName(), reqDto.getFileList());
    }



    @Operation(summary = "Download Excel")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "template"
            , required = true
            , example = "/excel/example/example.xlsx"
            , value = "Template Path"
            , dataTypeClass = String.class
        )
    })
    @GetMapping("excel")
    public void downloadExcel(String template) {
        Map<String, Object> model = new HashMap<>();
        List<DownloadExcelResDto> list = new ArrayList<>();
        String time = cmmInqSvc.currentTime();

        model.put("currTime", time);
        model.put("list", list);
        for (int i = 0; i < 10; i++) {
            DownloadExcelResDto row = new DownloadExcelResDto();

            row.setRno(i + 1);
            row.setUuid(UUID.randomUUID().toString());
            row.setHash(StringUtil.toHash(row.getUuid()));
            list.add(row);
        }
        StreamResponseWriter.excel(model, template, null);
    }



    @Operation(summary = "Async")
    @PostMapping("async")
    public ServerResponseDto<String> async() {
        String time = exampleSvc.async();
        
        return processResult(time);
    }
}