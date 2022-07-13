package artxew.framework.layers.cmminq.svc;

import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import artxew.framework.decedent.svc.BaseService;
import artxew.framework.environment.dao.CommonDao;
import artxew.framework.layers.cmminq.dto.CommonCodeDto;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CmmInqSvc extends BaseService {
    private static final String CMMCD_NAMESPACE = "framework.postgresql.cmminq.";

    private final CommonDao commonDao;


    
    @Transactional(readOnly = true)
    public String currentTime() {
        return commonDao.select(CMMCD_NAMESPACE + "currentTime");
    }



    @Cacheable(cacheNames = "queryCommonCodeList")
    @Transactional(readOnly = true)
    public CommonCodeDto[] queryCommonCodeList() {
        List<CommonCodeDto> list = commonDao.selectList(
            CMMCD_NAMESPACE + "queryCommonCodeList"
        );
        CommonCodeDto[] array = new CommonCodeDto[list.size()];

        return list.toArray(array);
    }

    @PostConstruct
    private void loadCache() {
        queryCommonCodeList();
    }
}