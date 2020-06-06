package br.com.lar.server.config;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

import javax.naming.ConfigurationException;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.io.FileUtils;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.querydsl.sql.H2Templates;
import com.querydsl.sql.PostgreSQLTemplates;
import com.querydsl.sql.SQLTemplates;

import br.com.sysdesc.util.classes.CryptoUtil;
import br.com.sysdesc.util.constants.MensagemConstants;
import br.com.sysdesc.util.resources.Configuracoes;
import br.com.sysdesc.util.resources.Resources;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableJpaRepositories(basePackages = { "br.com.lar.server.repository" })
@EnableTransactionManagement
public class DatasourceConfig {

	@Bean
	public DataSource getDataSource() throws ConfigurationException {

		Properties propriedades = buscarPropertiesConexao(getConfiguracaoBanco());

		DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();

		dataSourceBuilder.driverClassName(propriedades.getProperty("javax.persistence.jdbc.driver", ""));
		dataSourceBuilder.url(propriedades.getProperty("javax.persistence.jdbc.url"));
		dataSourceBuilder.username(propriedades.getProperty("javax.persistence.jdbc.user"));
		dataSourceBuilder.password(propriedades.getProperty("javax.persistence.jdbc.password"));

		return dataSourceBuilder.build();
	}

	@Bean
	public SQLTemplates getSQLTemplates() throws ConfigurationException {

		Properties propriedades = buscarPropertiesConexao(getConfiguracaoBanco());

		if (propriedades.getProperty("javax.persistence.jdbc.driver", "").equals("org.postgresql.Driver")) {

			return PostgreSQLTemplates.DEFAULT;
		}

		return H2Templates.DEFAULT;

	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() throws ConfigurationException {

		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(true);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		factory.setPackagesToScan("br.com.lar.server.repository");
		factory.setDataSource(getDataSource());

		return factory;
	}

	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {

		JpaTransactionManager txManager = new JpaTransactionManager();

		txManager.setEntityManagerFactory(entityManagerFactory);

		return txManager;
	}

	private static File getConfiguracaoBanco() throws ConfigurationException {

		if (!isconfigured()) {

			throw new ConfigurationException("Configuração de banco de dados não encontrada");
		}

		return new File(Configuracoes.CONEXAO);
	}

	private static boolean isconfigured() {

		return new File(Configuracoes.CONEXAO).exists();
	}

	private static Properties buscarPropertiesConexao(File configuracaoBanco) throws ConfigurationException {

		try {
			String arquivoConfiguracao = CryptoUtil
					.fromBlowfish(FileUtils.readFileToString(configuracaoBanco, StandardCharsets.UTF_8));

			if (arquivoConfiguracao == null) {
				throw new ConfigurationException("Configuração de conexão inválida");
			}

			Properties properties = new Properties();

			properties.load(new StringReader(arquivoConfiguracao));

			return properties;

		} catch (IOException e) {

			log.error(Resources.translate(MensagemConstants.MENSAGEM_LOG_PROPRIEDADES_CONEXAO), e);

			throw new ConfigurationException("Arquivo de configuração não encontrado");
		}
	}
}