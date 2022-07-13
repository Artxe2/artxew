package artxew.config;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.transaction.TransactionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class MybatisConfig {

    @Bean
    public SqlSessionFactory sqlSessionFactory(
        DataSource dataSource
        , ApplicationContext applicationContext
    ) throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();

        sqlSessionFactoryBean.setDataSource(dataSource);

        Resource resource = applicationContext.getResource(
            "classpath:config/mybatis/mybatis-config.xml"
        );

        sqlSessionFactoryBean.setConfigLocation(resource);

        Resource[] resources = applicationContext.getResources(
            "classpath:sql/**/*.xml"
        );

        sqlSessionFactoryBean.setMapperLocations(resources);

        TransactionFactory transactionFactory = new SpringManagedTransactionFactory();

        sqlSessionFactoryBean.setTransactionFactory(transactionFactory);
        sqlSessionFactoryBean.afterPropertiesSet();
        return sqlSessionFactoryBean.getObject();
    }
}