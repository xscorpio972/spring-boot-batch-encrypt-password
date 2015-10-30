/**
 * 
 */
package spring.bacth.encrypt.password;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

import spring.bacth.encrypt.password.config.MainConfig;

/**
 * @author JCZOBEIDE
 *
 */
@SpringBootApplication
@Import(value = MainConfig.class)
public class Application {

	private static Logger LOGGER = LogManager.getLogger(Application.class);

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		try {
			SpringApplication.run(Application.class, args);

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			System.exit(1);
		}

	}

}
