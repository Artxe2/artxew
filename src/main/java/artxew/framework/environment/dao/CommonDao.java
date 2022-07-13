package artxew.framework.environment.dao;

import java.util.List;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public final class CommonDao {

    private final SqlSessionTemplate sqlSessionTemplate;

    public <T> T select(String sql) {
        log.info("select( {} )", sql);
        return sqlSessionTemplate.selectOne(sql);
    }
    public <T> T select(String sql, Object dto) {
        log.info("select( {} )", sql);
        return sqlSessionTemplate.selectOne(sql, dto);
    }

    public <T> List<T> selectList(String sql) {
        log.info("selectList( {} )", sql);
        return sqlSessionTemplate.selectList(sql);
    }
    public <T> List<T> selectList(String sql, Object dto) {
        log.info("selectList( {} )", sql);
        return sqlSessionTemplate.selectList(sql, dto);
    }


    public int insert(String sql) {
        log.info("insert( {} )", sql);
        return sqlSessionTemplate.insert(sql);
    }
    public int insert(String sql, Object dto) {
        log.info("insert( {} )", sql);
        return sqlSessionTemplate.insert(sql, dto);
    }

    public int update(String sql) {
        log.info("update( {} )", sql);
        return sqlSessionTemplate.update(sql);
    }
    public int update(String sql, Object dto) {
        log.info("update( {} )", sql);
        return sqlSessionTemplate.update(sql, dto);
    }

    public int delete(String sql) {
        log.info("delete( {} )", sql);
        return sqlSessionTemplate.update(sql);
    }
    public int delete(String sql, Object dto) {
        log.info("delete( {} )", sql);
        return sqlSessionTemplate.update(sql, dto);
    }

    public void procedure(String sql) {
        log.info("procedure( {} )", sql);
        sqlSessionTemplate.update(sql);
    }
    public void procedure(String sql, Object dto) {
        log.info("procedure( {} )", sql);
        sqlSessionTemplate.update(sql, dto);
    }
}