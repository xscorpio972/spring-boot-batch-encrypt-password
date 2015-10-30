package spring.bacth.encrypt.password.config;

import java.util.List;

import javax.sql.DataSource;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.batch.core.ItemReadListener;
import org.springframework.batch.core.ItemWriteListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.support.MapJobRepositoryFactoryBean;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.support.transaction.ResourcelessTransactionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;

import spring.bacth.encrypt.password.UserBean;
import spring.bacth.encrypt.password.utils.PasswordUtils;

/**
 * Batch configuration class.
 * 
 * @author JCZOBEIDE
 *
 */
@Configuration
@EnableBatchProcessing
public class BatchConfig {

	private static final Logger LOGGER = LogManager.getLogger(BatchConfig.class);

	@Autowired
	private DataSource dataSource;

	@Autowired
	private JobBuilderFactory jobs;

	@Autowired
	private StepBuilderFactory stepBuilderFactory;
	
	@Bean
	public JobBuilderFactory jobs() throws Exception{
		return new JobBuilderFactory(new MapJobRepositoryFactoryBean(new ResourcelessTransactionManager()).getObject());
	}

	/**
	 * Bean for reading users from DB.
	 * @return
	 */
	@Bean
	public ItemReader<UserBean> itemReader() {
		JdbcCursorItemReader<UserBean> itemReader = new JdbcCursorItemReader<UserBean>();
		itemReader.setDataSource(dataSource);
		itemReader.setSql("select LOGIN, PASSWD from USER");
		itemReader.setRowMapper((rs, arg) -> new UserBean(rs.getString(1), rs.getString(2)));

		return itemReader;
	}

	/**
	 * Bean for encrypting password.
	 * @return
	 */
	@Bean
	public ItemProcessor<UserBean, UserBean> itemProcessor() {
		return userBean -> new UserBean(userBean.getLogin(), PasswordUtils.encryptPassword(userBean.getPwd()));
	}

	/**
	 * Bean for writing user to BD.
	 * @return
	 */
	@Bean
	public ItemWriter<UserBean> itemWriter() {
		JdbcBatchItemWriter<UserBean> itemWriter = new JdbcBatchItemWriter<>();
		itemWriter.setDataSource(dataSource);
		itemWriter.setItemSqlParameterSourceProvider(
				userBean -> new MapSqlParameterSource().addValue("passwd", userBean.getPwd()).addValue("login", userBean.getLogin()));
		itemWriter.setSql("update USER set PASSWD = :passwd where LOGIN = :login");
		return itemWriter;
	}

	/**
	 * @param reader
	 * @param writer
	 * @param processor
	 * @return
	 * @throws Exception 
	 */
	@Bean
	public Step step1(ItemReader<UserBean> reader, ItemWriter<UserBean> writer, ItemProcessor<UserBean, UserBean> processor) throws Exception {

		ItemReadListener<UserBean> itemReadListener = new ItemReadListener<UserBean>() {

			@Override
			public void beforeRead() {

			}

			@Override
			public void afterRead(UserBean o) {
				LOGGER.info(o.toString());
			}

			@Override
			public void onReadError(Exception e) {
			}
		};

		ItemWriteListener<UserBean> itemWriteListener = new ItemWriteListener<UserBean>() {

			@Override
			public void afterWrite(List<? extends UserBean> arg0) {

			}

			@Override
			public void beforeWrite(List<? extends UserBean> users) {
				users.forEach(u -> LOGGER.info(u.toString()));
			}

			@Override
			public void onWriteError(Exception arg0, List<? extends UserBean> arg1) {

			}

		};
		return stepBuilderFactory.get("step1").<UserBean, UserBean> chunk(10).reader(reader).processor(processor).writer(writer)
				.listener(itemReadListener).listener(itemWriteListener).build();
	}

	/**
	 * 
	 * Bean defining the job.
	 * @param s1
	 * @return
	 * @throws Exception
	 */
	@Bean
	public Job encryptPasswordJob(@Qualifier("step1") Step s1) throws Exception {
		return jobs.get("encryptPasswordJob").incrementer(new RunIdIncrementer()).flow(s1).end().build();
	}
}
