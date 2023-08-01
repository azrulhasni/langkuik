/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.azrul.langkuik.framework.dao.config;

import java.util.Properties;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author azrul
 */
@Configuration
@EnableTransactionManagement
public class PersistenceSearchConfig {
    
    @Value("${application.lgDatabaseDriverClassName}")
    private String dbDriverClassName;
      
    @Value("${application.lgDatabaseUsername}")
    private String dbUsername;
        
    @Value("${application.lgDatabasePassword}")
    private String dbPassword;
          
    @Value("${application.lgJdbcURL}")
    private String dbJdbcURL;
    
//    @Value("${application.lgHibernateSearchDirectoryProvider}")
//    private String hibernateSearchDirectoryProvider;
//            
//    @Value("${application.lgHibernateSearchIndexBase}")
//    private String hibernateSearchIndexBase;
    
    @Value("${application.lgModelPackageName}")
    private String modelPackageName;
    
    @Value("${application.lgHibernateDialect}")
    private String hibernateDialect;
    
    @Value("${application.lgDDLCreationMode}")
    private String ddlCreationMode;
    
    @Value("${application.lgEnableAudit}")
    private String enableAudit;
    
     @Value("${application.lgEsUsername}")
    private String esUsername;
     
     @Value("${application.lgEsPassword}")
    private String esPassword;
     
     @Value("${application.lgEsServicePrefix}")
    private String esServicePrefix;
     
     @Value("${application.lgEsDiscoveryEnabled}")
    private String esDiscoverEnaled;
     
     @Value("${application.lgEsDiscoveryInterval}")
    private String esDiscoverInterval;
     
     @Value("${application.lgEsStrategy}")
    private String esStrategy;
     
     @Value("${application.lgEsURIs}")
    private String esURIs;
     
    @Value("${application.lgSearchConfigurerLocation:class:com.azrul.langkuik.framework.dao.config.SearchConfigurer}")
    private  String searchConfigurer;
    

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        LocalContainerEntityManagerFactoryBean em
                = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(new String[]{
            modelPackageName,
            "com.azrul.langkuik.framework.entity",
            "com.azrul.langkuik.custom.attachment",
            "com.azrul.langkuik.custom.lookupchoice",
            "com.azrul.langkuik.custom.comment",
            "com.azrul.langkuik.custom.approval",
            "com.azrul.langkuik.custom.bizConfig",
            "com.azrul.langkuik.framework.audit",
            "com.azrul.langkuik.framework.user"
        });

        JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaProperties(additionalProperties());
        
        return em;
    }

    // ...
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(dbDriverClassName);
        dataSource.setUsername(dbUsername);
        dataSource.setPassword(dbPassword);
        dataSource.setUrl(dbJdbcURL);

        return dataSource;
    }

    @Bean
    public PlatformTransactionManager transactionManager() {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());

        return transactionManager;
    }

    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
        return new PersistenceExceptionTranslationPostProcessor();
    }

    Properties additionalProperties() {
        Properties properties = new Properties();
        //properties.setProperty("hibernate.hbm2ddl.auto", "create-drop");
        properties.setProperty("hibernate.dialect", hibernateDialect);
        properties.setProperty("hibernate.cache.provider_class", "org.hibernate.cache.NoCacheProvider");
        properties.setProperty("hibernate.hbm2ddl.auto", ddlCreationMode);
        //properties.setProperty("hibernate.hbm2ddl.auto", "create");
        //properties.setProperty("hibernate.search.default.directory_provider", hibernateSearchDirectoryProvider);
        //properties.setProperty("hibernate.search.default.indexBase", hibernateSearchIndexBase);
        
//        properties.setProperty("hibernate.search.default.indexmanager","elasticsearch");
//        properties.setProperty("hibernate.search.default.elasticsearch.host","http://localhost:9200");
        properties.setProperty("hibernate.search.backend.type","elasticsearch");
        properties.setProperty("hibernate.search.backend.username",esUsername/*"elastic"*/);
        properties.setProperty("hibernate.search.backend.password",esPassword/*"1qazZAQ!"*/);
        properties.setProperty("hibernate.search.backend.path_prefix",esServicePrefix/*"/"*/);
        properties.setProperty("hibernate.search.backend.discovery.enabled",esDiscoverEnaled/*"false"*/);
        properties.setProperty("hibernate.search.backend.discovery.refresh_interval",esDiscoverInterval/*"10"*/);
        properties.setProperty("hibernate.search.schema_management.strategy",esStrategy/*"create-or-update"*/);
//        properties.setProperty("hibernate.search.default.elasticsearch.required_index_status","yellow");
        properties.setProperty("hibernate.search.backend.uris",esURIs/*"http://localhost:9200"*/);
       // properties.setProperty("org.hibernate.search.elasticsearch.request","true");
 //       properties.setProperty("hibernate.search.backend.log.json_pretty_printing","true");
//        properties.setProperty("hibernate.search.backend.directory.type","local-filesystem");
//        properties.setProperty("hibernate.search.backend.directory.root",hibernateSearchIndexBase);
//        properties.setProperty("hibernate.search.backend.directory.filesystem_access.strategy","auto");
        properties.setProperty("hibernate.format_sql","true");
        properties.setProperty("hibernate.show_sql", "false");
        properties.setProperty("hibernate.current_session_context_class", "thread");
        //properties.setProperty("hibernate.search.default.exclusive_index_use", "false");
        properties.setProperty("org.hibernate.envers.global_with_modified_flag ", "true");
        properties.setProperty("javax.persistence.validation.mode","none");
        properties.setProperty("org.hibernate.envers.audit_strategy", "org.hibernate.envers.strategy.ValidityAuditStrategy");
        properties.setProperty("org.hibernate.envers.audit_strategy", "org.hibernate.envers.strategy.DefaultAuditStrategy");
        properties.setProperty("hibernate.cache.use_second_level_cache","true");
        properties.setProperty("hibernate.cache.use_query_cache","true");
        properties.setProperty("hibernate.cache.region.factory_class","org.hibernate.cache.ehcache.EhCacheRegionFactory");
        properties.setProperty("hibernate.integration.envers.enabled",enableAudit);
        properties.setProperty("hibernate.search.backend.analysis.configurer",searchConfigurer);
        return properties;
    }

}
