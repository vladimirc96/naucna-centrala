package com.upp.naucnacentrala.config;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.net.InetAddress;
import java.net.UnknownHostException;


@Configuration
@EnableElasticsearchRepositories(basePackages = "com.upp.naucnacentrala.repository.elasticsearch")
@EnableJpaRepositories(basePackages = "com.upp.naucnacentrala.repository.jpa")
public class ElasticSearchConfiguration {

    @Bean
    public Client client(){
        TransportClient transportClient = null;

        final Settings settings = Settings.builder()
                .put("client.transport.sniff", true)
                .put("transport.host", "0.0.0.0")
                .put("cluster.name", "my-magazine").build();
        transportClient = new PreBuiltTransportClient(settings);
        try {
            transportClient.addTransportAddress(new TransportAddress(InetAddress.getByName("localhost"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        return transportClient;
    }

}
