package artxew.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Configuration
@EnableCaching
public class EhcacheConfig {

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean(
        ApplicationContext applicationContext
    ) {
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean
                = new EhCacheManagerFactoryBean();
        Resource resource = applicationContext.getResource(
            "classpath:config/ehcache/ehcache-config.xml"
        );
        
        ehCacheManagerFactoryBean.setConfigLocation(resource);
        ehCacheManagerFactoryBean.setShared(true);
        return ehCacheManagerFactoryBean;
    }

    @Bean
    public EhCacheCacheManager ehCacheCacheManager(
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean
    ) {
        EhCacheCacheManager ehCacheCacheManager = new EhCacheCacheManager();

        ehCacheCacheManager.setCacheManager(ehCacheManagerFactoryBean.getObject());
        return ehCacheCacheManager;
    }
}
