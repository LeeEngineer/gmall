package com.atguigu.gmall.pms.config;

import com.zaxxer.hikari.HikariDataSource;
import io.seata.rm.datasource.DataSourceProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;


/**
 * @author Lee_engineer
 * @create 2020-07-24 20:43
 */
@Configuration
public class DataSourceProxyConfig {


    @Bean
    @Primary
    public DataSourceProxy dataSourceProxy(@Value("${spring.datasource.url}")String url,
                                           @Value("${spring.datasource.driver-class-name}")String driverClassName,
                                           @Value("${spring.datasource.username}")String username,
                                           @Value("${spring.datasource.password}")String password) {
        HikariDataSource hikariDataSource = new HikariDataSource();
        hikariDataSource.setJdbcUrl(url);
        hikariDataSource.setDriverClassName(driverClassName);
        hikariDataSource.setUsername(username);
        hikariDataSource.setPassword(password);
        return new DataSourceProxy(hikariDataSource);
    }

}
