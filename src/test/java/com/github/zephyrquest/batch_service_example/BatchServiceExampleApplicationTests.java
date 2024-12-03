package com.github.zephyrquest.batch_service_example;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;

@SpringBootTest
@ActiveProfiles("test")
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class})
// This is to avoid clashing of several JobRepository instances using the same data source for several test classes
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBatchTest
class BatchServiceExampleApplicationTests {

	@Autowired
	private JobLauncherTestUtils jobLauncherTestUtils;
	@Autowired
	private JobRepositoryTestUtils jobRepositoryTestUtils;
	@Autowired
	private Job importUserJob;


	@BeforeEach
	void setUp() {
		jobLauncherTestUtils.setJob(importUserJob);
	}

	@AfterEach
	void tearDown() {
		jobRepositoryTestUtils.removeJobExecutions();
	}

	@Test
	void importUserJob_WhenJobEnds_ThenStatusCompleted() throws Exception {
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		Assertions.assertEquals(ExitStatus.COMPLETED, jobExecution.getExitStatus());
	}
}
