package artxew.config;
import javax.sql.DataSource;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.SpringBootVFS;
import org.mybatis.spring.transaction.SpringManagedTransactionFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.lang.NonNull;
import org.springframework.transaction.TransactionManager;
import com.zaxxer.hikari.HikariDataSource;

/**
 * @author Artxe2
 */
@Configuration
public class MybatisConfig {

	/**
	 * @author Artxe2
	*/
	@Bean("dataSourceCommand")
    @ConfigurationProperties(prefix = "spring.datasource-command")
	public DataSource dataSourceCommand() {
		return DataSourceBuilder.create().build();
	}

	@Bean
	public TransactionManager transactionManager(
		@NonNull @Qualifier("dataSourceCommand")
		DataSource dataSourceCommand
	) {
		return new DataSourceTransactionManager(dataSourceCommand);
	}

	/**
	 * @author Artxe2
	 */
	@Bean("sqlSessionCommand")
	public SqlSessionTemplate sqlSessionCommand(
		@Qualifier("dataSourceCommand") DataSource dataSourceCommand
		, ApplicationContext applicationContext
	) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:config/mybatis/mybatis-config.xml"));
		sqlSessionFactoryBean.setDataSource(dataSourceCommand);
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:artxew/*/layers/*/sql/*.xml"));
		sqlSessionFactoryBean.setTransactionFactory(new SpringManagedTransactionFactory());
		sqlSessionFactoryBean.setVfs(SpringBootVFS.class);
		HikariDataSource hikari = (HikariDataSource) dataSourceCommand;
		hikari.setConnectionTimeout(8000); // SELECT GLOBAL_VALUE FROM INFORMATION_SCHEMA.SYSTEM_VARIABLES WHERE VARIABLE_NAME = 'connect_timeout'
		hikari.setMaximumPoolSize(4);
		hikari.setMaxLifetime(180000); // SELECT GLOBAL_VALUE FROM INFORMATION_SCHEMA.SYSTEM_VARIABLES WHERE VARIABLE_NAME = 'wait_timeout'
		hikari.setAutoCommit(false);
		return new SqlSessionTemplate(sqlSessionFactoryBean.getObject());
	}

	/**
	 * @author Artxe2
	 */
	@Bean("dataSourceQuery")
    @ConfigurationProperties(prefix = "spring.datasource-query")
    public DataSource dataSourceQuery() {
        return DataSourceBuilder.create().build();
    }

	/**
	 * @author Artxe2
	 */
	@Bean("sqlSessionQuery")
	public SqlSessionTemplate sqlSessionQuery(
		@Qualifier("dataSourceQuery") DataSource dataSourceQuery
		, ApplicationContext applicationContext
	) throws Exception {
		SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
		sqlSessionFactoryBean.setConfigLocation(applicationContext.getResource("classpath:config/mybatis/mybatis-config.xml"));
		sqlSessionFactoryBean.setDataSource(dataSourceQuery);
		sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources("classpath:artxew/*/layers/*/sql/*.xml"));
		sqlSessionFactoryBean.setVfs(SpringBootVFS.class);
		HikariDataSource hikari = (HikariDataSource) dataSourceQuery;
		hikari.setConnectionTimeout(8000); // SELECT GLOBAL_VALUE FROM INFORMATION_SCHEMA.SYSTEM_VARIABLES WHERE VARIABLE_NAME = 'connect_timeout'
		hikari.setMaximumPoolSize(20);
		hikari.setMaxLifetime(180000); // SELECT GLOBAL_VALUE FROM INFORMATION_SCHEMA.SYSTEM_VARIABLES WHERE VARIABLE_NAME = 'wait_timeout'
		hikari.setReadOnly(true);
		return new SqlSessionTemplate(sqlSessionFactoryBean.getObject());
	}
}