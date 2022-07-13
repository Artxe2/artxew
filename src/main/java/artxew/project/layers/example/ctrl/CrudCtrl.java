package artxew.project.layers.example.ctrl;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;

import artxew.framework.decedent.ctrl.BaseController;
import artxew.framework.decedent.dto.ServerResponseDto;
import artxew.project.layers.example.dto.req.CreateExampleReqDto;
import artxew.project.layers.example.dto.req.QueryExampleListReqDto;
import artxew.project.layers.example.dto.req.ModifyExampleReqDto;
import artxew.project.layers.example.dto.res.CreateExampleResDto;
import artxew.project.layers.example.dto.res.QueryExampleInfoResDto;
import artxew.project.layers.example.dto.res.QueryExampleListResDto;
import artxew.project.layers.example.svc.ExampleSvc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@Api(tags = { "CRUD Example" })
@Profile("!prod")
@RequestMapping("api/example/crud")
@RequiredArgsConstructor
@RestController
public class CrudCtrl extends BaseController {

    private final ExampleSvc exampleSvc;



    @Operation(summary = "Query Example List")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "page"
            , required = true
            , example = "3"
            , value = "Current Page"
            , dataTypeClass = Integer.class
        )
        , @ApiImplicitParam(
            name = "size"
            , required = true
            , example = "12345"
            , value = "Page Size"
            , dataTypeClass = Integer.class
        )
    })
    @GetMapping
    public ServerResponseDto<QueryExampleListResDto> queryExampleList(
        QueryExampleListReqDto reqDto
    ) {
        QueryExampleListResDto resDto = exampleSvc.queryExampleList(reqDto);

        return processResult(resDto);
    }



    @Operation(summary = "Query Example Info")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "eid"
            , required = true
            , example = "3"
            , value = "Example Id"
            , dataTypeClass = Integer.class
        )
    })
    @GetMapping("{eid}")
    public ServerResponseDto<QueryExampleInfoResDto> queryExampleInfo(long eid) {
        QueryExampleInfoResDto resDto = exampleSvc.queryExampleInfo(eid);

        return processResult(resDto);
    }



    @Operation(summary = "Create Example")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServerResponseDto<CreateExampleResDto> createExample(
        @RequestBody(required = true) CreateExampleReqDto reqDto
    ) {
        exampleSvc.createExample(reqDto);

        CreateExampleResDto resDto = new CreateExampleResDto();

        resDto.setEid(reqDto.getEid());
        return processResult(resDto);
    }



    @Operation(summary = "Update Example")
    @PutMapping("{eid}")
    public ServerResponseDto<QueryExampleInfoResDto> modifyExample(
        @PathVariable long eid
        , @RequestBody(required = true) ModifyExampleReqDto reqDto
    ) {
        reqDto.setEid(eid);
        exampleSvc.modifyExample(reqDto);

        QueryExampleInfoResDto resDto = exampleSvc.queryExampleInfo(eid);
        return processResult(resDto);
    }



    @Operation(summary = "Delete Example")
    @DeleteMapping("{eid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteExample(
        @PathVariable long eid
    ) {
        exampleSvc.deleteExample(eid);
    }
}