package spring.bacth.encrypt.password;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import spring.bacth.encrypt.password.config.TestConfig;

/**
 * @author JCZOBEIDE
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = { TestConfig.class })
public class ApplicationTest {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;

	@Test
	public void launchJob() throws Exception {

		// testing a job
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());

	}
	
	@Test
	public void launchStep() throws Exception {

		// Testing a individual step
		JobExecution jobExecutionStep = jobLauncherTestUtils.launchStep("step1");
		assertEquals(BatchStatus.COMPLETED, jobExecutionStep.getStatus());

	}
}
