package spring.bacth.encrypt.password.config;

import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author JCZOBEIDE
 *
 */
@Configuration
@Import({ JdbcConfig.class, BatchConfig.class })
@EnableAutoConfiguration
public class TestConfig {
	
	@Bean
	public JobLauncherTestUtils jobLauncherTestUtils() throws Exception{
		return new JobLauncherTestUtils();
	}
	
}
