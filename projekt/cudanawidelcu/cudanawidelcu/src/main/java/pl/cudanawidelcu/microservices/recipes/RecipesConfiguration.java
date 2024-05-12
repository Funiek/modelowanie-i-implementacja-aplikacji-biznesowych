package pl.cudanawidelcu.microservices.recipes;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

/**
 * The accounts Spring configuration.
 * 
 * @author Paul Chapman
 */
@Configuration
@ComponentScan
@EntityScan("pl.cudanawidelcu.microservices.recipes")
@EnableJpaRepositories("pl.cudanawidelcu.microservices.recipes")
@PropertySource("classpath:application.properties")
public class RecipesConfiguration {
	protected Logger logger;
	public RecipesConfiguration() {
		logger = Logger.getLogger(getClass().getName());
	}

	@Bean
	public DataSource dataSource() {
		logger.info("dataSource() invoked");

		DataSource dataSource = (new EmbeddedDatabaseBuilder()).addScript("classpath:testdb/schema.sql")
				.addScript("classpath:testdb/data.sql").build();

		logger.info("dataSource = " + dataSource);

		// Sanity check
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Map<String, Object>> recipes = jdbcTemplate.queryForList("SELECT count(*) FROM recipe");
		logger.info("System has " + recipes.size() + " recipes");

		return dataSource;
	}
}
