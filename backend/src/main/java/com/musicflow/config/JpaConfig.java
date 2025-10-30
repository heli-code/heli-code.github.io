package com.musicflow.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
    basePackages = "com.musicflow.repository",
    entityManagerFactoryRef = "entityManagerFactory",
    transactionManagerRef = "transactionManager"
)
public class JpaConfig {

    /**
     * 数据源配置 - 使用HikariCP连接池
     */
    @Bean
    @ConfigurationProperties("spring.datasource.hikari")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .build();
    }

    /**
     * 实体管理器工厂配置
     */
    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan("com.musicflow.entity");
        
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        
        // JPA属性配置
        Map<String, Object> properties = new HashMap<>();
        
        // 数据库方言
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        
        // 显示SQL（开发环境）
        properties.put("hibernate.show_sql", true);
        properties.put("hibernate.format_sql", true);
        
        // 二级缓存配置
        properties.put("hibernate.cache.use_second_level_cache", true);
        properties.put("hibernate.cache.region.factory_class", 
            "org.hibernate.cache.jcache.JCacheRegionFactory");
        properties.put("hibernate.javax.cache.provider", 
            "org.ehcache.jsr107.EhcacheCachingProvider");
        properties.put("hibernate.cache.use_query_cache", true);
        
        // 批量操作优化
        properties.put("hibernate.jdbc.batch_size", 50);
        properties.put("hibernate.order_inserts", true);
        properties.put("hibernate.order_updates", true);
        properties.put("hibernate.batch_versioned_data", true);
        
        // 连接池配置
        properties.put("hibernate.hikari.maximumPoolSize", 20);
        properties.put("hibernate.hikari.minimumIdle", 5);
        properties.put("hibernate.hikari.idleTimeout", 300000); // 5分钟
        properties.put("hibernate.hikari.connectionTimeout", 30000); // 30秒
        properties.put("hibernate.hikari.maxLifetime", 1800000); // 30分钟
        
        // 查询优化
        properties.put("hibernate.query.in_clause_parameter_padding", true);
        properties.put("hibernate.query.plan_cache_max_size", 2048);
        properties.put("hibernate.query.plan_parameter_metadata_max_size", 128);
        
        em.setJpaPropertyMap(properties);
        
        return em;
    }

    /**
     * 事务管理器配置
     */
    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
        return transactionManager;
    }
}