package artxew.framework.layers.cmminq.ctrl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import artxew.framework.decedent.ctrl.BaseController;
import artxew.framework.decedent.dto.ServerResponseDto;
import artxew.framework.layers.cmminq.dto.CommonCodeDto;
import artxew.framework.layers.cmminq.svc.CmmInqSvc;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.GetMapping;


@Api(tags = { "Common Inquery" })
@RequestMapping("api/cmminq")
@RestController
@RequiredArgsConstructor
public class CmmInqCtrl extends BaseController {

    private final CmmInqSvc cmmInqSvc;



    @GetMapping("current-time")
    public ServerResponseDto<String> currentTime() {
        String currentTime = cmmInqSvc.currentTime();
        
        return processResult(currentTime);
    }
    


    @Operation(summary = "Service Exception")
    @ApiImplicitParams({
        @ApiImplicitParam(
            name = "cdGrp"
            , required = true
            , example = "USR_AUTH"
            , value = "Code Group Id"
            , dataTypeClass = String.class
        )
    })
    @GetMapping("code")
    public ServerResponseDto<CommonCodeDto[]> queryCommonCode(String cdGrp) {
        CommonCodeDto[] array = cmmInqSvc.queryCommonCodeList();

        array = filterCommonCode(array, cdGrp);
        return processResult(array);
    }

    private CommonCodeDto[] filterCommonCode(CommonCodeDto[] array, String cdGrp) {
        int start = findStart(array, cdGrp);

        if (start == array.length) {
            return new CommonCodeDto[0];
        }

        int end = findEnd(array, cdGrp, start);
        int length = end - start;
        CommonCodeDto[] result = new CommonCodeDto[length];

        System.arraycopy(array, start, result, 0, length);
        return result;
    }

    private int findStart(CommonCodeDto[] array, String cdGrp) {
        int length = array.length;
        int start = 0;
        int mid;
        int end = length;
        String temp;

        while (start < end) {
            mid = (start + end - 1) / 2;
            temp = array[mid].getCdGrp();
            if (cdGrp.compareTo(temp) <= 0) {
                end = mid;
            } else {
                start = mid + 1;
            }
        }
        if (end == length) {
            return length;
        }
        temp = array[end].getCdGrp();
        if (!cdGrp.equals(temp)) {
            return length;
        }
        return end;
    }

    private int findEnd(CommonCodeDto[] array, String cdGrp, int start) {
        int mid;
        int end = array.length;
        String temp;

        while (start < end) {
            mid = (start + end - 1) / 2;
            temp = array[mid].getCdGrp();
            if (cdGrp.compareTo(temp) < 0) {
                end = mid;
            } else {
                start = mid + 1; 
            }
        }
        return start;
    }
}