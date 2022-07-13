package artxew.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Value("${elastic.ip}")
    private String ip;
    
    @Value("${elastic.port}")
    private int port;

    @Value("${elastic.protocol}")
    private String protocol;

    @Bean RestClient restClient() {
        HttpHost httpHost = new HttpHost(ip, port, protocol);
        return RestClient.builder(httpHost).build();
    }
}