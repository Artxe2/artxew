package artxew.project.layers.example.svc;

import java.util.List;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import artxew.framework.decedent.svc.BaseService;
import artxew.framework.environment.dao.CommonDao;
import artxew.framework.environment.elastic.CommonRepository;
import artxew.framework.layers.cmminq.svc.CmmInqSvc;
import artxew.project.layers.example.async.ExampleAsync;
import artxew.project.layers.example.dto.req.CreateExampleReqDto;
import artxew.project.layers.example.dto.req.QueryExampleListReqDto;
import artxew.project.layers.example.dto.req.ModifyExampleReqDto;
import artxew.project.layers.example.dto.res.QueryExampleInfoResDto;
import artxew.project.layers.example.dto.res.QueryExampleListResDto;
import lombok.RequiredArgsConstructor;

@Profile("!prod")
@RequiredArgsConstructor
@Service
public class ExampleSvc extends BaseService {

    private static final String SAMPLE_NAMESPACE = "project.postgresql.example.";

    private final CommonDao commonDao;

    private final CommonRepository commonRepository;

    private final ExampleAsync exampleAsync;

    private final CmmInqSvc cmmInqSvc;

    @Transactional(readOnly = true)
    public QueryExampleListResDto queryExampleList(QueryExampleListReqDto reqDto) {
        QueryExampleListResDto resDto = new QueryExampleListResDto();
        int count = commonDao.select(
            SAMPLE_NAMESPACE + "selectExampleListCount"
            , reqDto
        );
        List<QueryExampleListResDto.QueryItem> dataList = commonDao.selectList(
            SAMPLE_NAMESPACE + "selectExampleList"
            , reqDto
        );

        resDto.setCount(count);
        resDto.setDataList(dataList);
        return resDto;
    }

    @Transactional(readOnly = true)
    public QueryExampleInfoResDto queryExampleInfo(long eid) {
        return commonDao.select(
            SAMPLE_NAMESPACE + "selectExampleInfo"
            , eid
        );
    }

    @Transactional
    public int createExample(CreateExampleReqDto reqDto) {
        return commonDao.insert(
            SAMPLE_NAMESPACE + "insertExample"
            , reqDto
        );
    }

    @Transactional
    public int modifyExample(ModifyExampleReqDto reqDto) {
        return commonDao.insert(
            SAMPLE_NAMESPACE + "updateExample"
            , reqDto
        );
    }

    @Transactional
    public int deleteExample(long eid) {
        return commonDao.insert(
            SAMPLE_NAMESPACE + "deleteExample"
            , eid
        );
    }

    @Transactional(readOnly = true)
    public void serviceException(String name) {
        String time = cmmInqSvc.currentTime();

        try {
            int i = 1 / 0;
        } catch (Exception e) {
            exception(time, name, e);
        }
    }

    @Transactional(readOnly = true)
    public String async() {
        String time = cmmInqSvc.currentTime();

        exampleAsync.async(time);
        return time;
    }

    public Object esSelect(String index, String id) {
        return commonRepository.select(index, id, Object.class);
    }

    public Object esInsert(String index, Object data) {
        return commonRepository.insert(index, data);
    }

    public Object esInsert(String index, String id, Object data) {
        return commonRepository.insert(index, id, data);
    }

    public Object esMerge(String index, String id, Object data) {
        return commonRepository.merge(index, id, data);
    }

    public Object esUpdate(String index, String id, Object data) {
        return commonRepository.update(index, id, data);
    }

    public Object esDelete(String index, String id) {
        return commonRepository.delete(index, id);
    }

    public Object esSearch(String query) {
        return commonRepository.search(query);
    }
}