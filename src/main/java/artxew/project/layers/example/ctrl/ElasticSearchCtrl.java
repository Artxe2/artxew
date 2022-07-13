package artxew.project.layers.example.ctrl;

import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import artxew.framework.decedent.ctrl.BaseController;
import artxew.framework.decedent.dto.ServerResponseDto;
import artxew.project.layers.example.svc.ExampleSvc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

@Api(tags = { "Elastic Search Example" })
@Profile("!prod")
@RequestMapping("api/example/es")
@RequiredArgsConstructor
@RestController
public class ElasticSearchCtrl extends BaseController {

    private final ExampleSvc exampleSvc;



    @Operation(summary = "Elasticsearch Select")
    @GetMapping("select/{index}/{id}")
    public ServerResponseDto<Object> esSelect(
        @PathVariable(required = true) String index
        , @PathVariable(required = true) String id
    ) {
        Object resDto = exampleSvc.esSelect(index, id);
        
        return processResult(resDto);
    }



    @Operation(summary = "Elasticsearch Insert")
    @PostMapping("insert/{index}")
    public ServerResponseDto<Object> esInsert(
        @PathVariable(required = true) String index
        , @RequestBody(required = true) Object data
    ) {
        Object resDto = exampleSvc.esInsert(index, data);

        return processResult(resDto);
    }



    @Operation(summary = "Elasticsearch Insert")
    @PostMapping("insert/{index}/{id}")
    public ServerResponseDto<Object> esInsert(
        @PathVariable(required = true) String index
        , @PathVariable(required = true) String id
        , @RequestBody(required = true) Object data
    ) {
        Object resDto = exampleSvc.esInsert(index, id, data);

        return processResult(resDto);
    }



    @Operation(summary = "Elasticsearch Merge")
    @PutMapping("merge/{index}/{id}")
    public ServerResponseDto<Object> esMerge(
        @PathVariable(required = true) String index
        , @PathVariable(required = true) String id
        , @RequestBody(required = true) Object data
    ) {
        Object resDto = exampleSvc.esMerge(index, id, data);

        return processResult(resDto);
    }



    @Operation(summary = "Elasticsearch Update")
    @PutMapping("update/{index}/{id}")
    public ServerResponseDto<Object> esUpdate(
        @PathVariable(required = true) String index
        , @PathVariable(required = true) String id
        , @RequestBody(required = true) Object data
    ) {
        Object resDto = exampleSvc.esUpdate(index, id, data);

        return processResult(resDto);
    }



    @Operation(summary = "Elasticsearch Delete")
    @DeleteMapping("delete/{index}/{id}")
    public ServerResponseDto<Object> esDelete(
        @PathVariable(required = true) String index
        , @PathVariable(required = true) String id
    ) {
        Object resDto = exampleSvc.esDelete(index, id);

        return processResult(resDto);
    }



    @Operation(summary = "Elasticsearch Search")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "query"
            , required = true
            , example = "{\"query\":{\"match_all\":{}}}"
            , value = "Search Query"
            , dataTypeClass = String.class
        )
    })
    @GetMapping("search")
    public ServerResponseDto<Object> esSearch(String query) {
        Object resDto = exampleSvc.esSearch(query);

        return processResult(resDto);
    }
}
