//package net.blaklizt.streets.restapi.spring;
//
//import com.google.common.base.Preconditions;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.*;
//import org.springframework.core.env.Environment;
//import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.jdbc.datasource.DriverManagerDataSource;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.transaction.annotation.EnableTransactionManagement;
//
//import javax.sql.DataSource;
//import java.util.Properties;
//
////@Configuration
//////@EnableTransactionManagement
//////@PropertySource({ "classpath:persistence-${envTarget:mysql}.properties" })
////@ComponentScan({ "net.blaklizt.symbiosis.*" })
////@ImportResource("classpath*:spring-context.xml")
//////@EnableJpaRepositories(basePackages = "net.blaklizt.streets.rest-api.persistence.dao")
//
////@Configuration
////@EnableTransactionManagement
//////@PropertySource({ "classpath*:properties/database/application.properties" })
////@ComponentScan({ "net.blaklizt.symbiosis.sym_authentication" })
////@ImportResource("classpath*:sym_*-spring-context.xml")
////@EnableJpaRepositories(basePackages = "net.blaklizt.symbiosis.sym_persistence")
//public class PersistenceConfig {
//
////    @Autowired
////    private Environment env;
////
////    public PersistenceConfig() {
////        super();
////    }
////
////    @Bean
////    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
////        final LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
////        em.setDataSource(dataSource());
////        em.setPackagesToScan(new String[] { "net.blaklizt.symbiosis.sym_persistence" });
////
////        final HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
////        // vendorAdapter.set
////        em.setJpaVendorAdapter(vendorAdapter);
////        em.setJpaProperties(additionalProperties());
////
////        return em;
////    }
////
////    @Bean
////    public DataSource dataSource() {
////        final DriverManagerDataSource dataSource = new DriverManagerDataSource();
////        dataSource.setDriverClassName(Preconditions.checkNotNull(env.getProperty("jdbc.driverClassName")));
////        dataSource.setUrl(Preconditions.checkNotNull(env.getProperty("hibernate.connection.url")));
////        dataSource.setUsername(Preconditions.checkNotNull(env.getProperty("jdbc.user")));
////        dataSource.setPassword(Preconditions.checkNotNull(env.getProperty("jdbc.pass")));
////
////        return dataSource;
////    }
////
////    @Bean
////    public PlatformTransactionManager transactionManager() {
////        final JpaTransactionManager transactionManager = new JpaTransactionManager();
////        transactionManager.setEntityManagerFactory(entityManagerFactory().getObject());
////
////        return transactionManager;
////    }
////
////    @Bean
////    public PersistenceExceptionTranslationPostProcessor exceptionTranslation() {
////        return new PersistenceExceptionTranslationPostProcessor();
////    }
////
////    final Properties additionalProperties() {
////        final Properties hibernateProperties = new Properties();
////        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
////        hibernateProperties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
////        // hibernateProperties.setProperty("hibernate.globally_quoted_identifiers", "true");
////        return hibernateProperties;
////    }
//
//}