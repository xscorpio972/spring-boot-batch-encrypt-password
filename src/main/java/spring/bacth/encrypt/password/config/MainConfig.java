/**
 * 
 */
package spring.bacth.encrypt.password.config;

import org.springframework.batch.core.launch.support.JvmSystemExiter;
import org.springframework.batch.core.launch.support.SystemExiter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;


/**
 * Main configuration for this batch jar.
 * @author JCZOBEIDE
 *
 *
 */
@Configuration
@Import({ JdbcConfig.class, BatchConfig.class })
public class MainConfig {

	/**
	 * @return the bean who interprets the property file specified in @PropertySource
	 *         parameter to fill the properties defined with a @Value
	 */
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		return new PropertySourcesPlaceholderConfigurer();
	}
	
	@Bean
	public SystemExiter systemExiter(){
		return new JvmSystemExiter();
	}
}
