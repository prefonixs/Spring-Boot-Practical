package com.multipledb.multipledb.config;

import java.util.HashMap;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import jakarta.persistence.EntityManagerFactory;
import lombok.Getter;
import lombok.Setter;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(entityManagerFactoryRef = "employeeEntityManagerFactory", transactionManagerRef = "employeeTransactionManager", basePackages = {
		"com.multipledb.multipledb.employee.repository" })
@ConfigurationProperties(prefix = "spring.employee.datasource")
@Getter
@Setter
public class EmployeeDBConfig {
	private String url;
	private String username;
	private String password;
	private String driverClassName;

	@Value("${spring.employee.jpa.database-platform}")
	private String dielect;

	@Bean(name = "employeeDatasource")
	public DataSource dataSource() {
		DataSource dataSource = DataSourceBuilder.create().url(url).username(username).password(password)
				.driverClassName(driverClassName).build();
		return dataSource;
	}

	@Bean(name = "employeeEntityManagerFactory")
	public LocalContainerEntityManagerFactoryBean employeeEntityManagerFactory(EntityManagerFactoryBuilder builder,
			@Qualifier("employeeDatasource") DataSource dataSource) {

		HashMap<String, Object> properties = new HashMap<>();
		properties.put("hibernate.hbm2ddl.auto", "update");
		properties.put("hibernate.dialect", dielect);

		return builder.dataSource(dataSource).properties(properties)
				.packages("com.multipledb.multipledb.employee.entity").persistenceUnit("employee").build();
	}

	@Bean(name = "employeeTransactionManager")
	public PlatformTransactionManager employeeTransactionManager(
			@Qualifier("employeeEntityManagerFactory") EntityManagerFactory employeeEntityManagerFactory) {
		return new JpaTransactionManager(employeeEntityManagerFactory);
	}

	@Primary
	@Bean
	public EntityManagerFactoryBuilder entityManagerFactoryBuilder() {
		return new EntityManagerFactoryBuilder(new HibernateJpaVendorAdapter(), new HashMap<>(), null);
	}
}
