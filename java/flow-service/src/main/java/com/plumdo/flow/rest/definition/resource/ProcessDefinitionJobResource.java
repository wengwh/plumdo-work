package com.plumdo.flow.rest.definition.resource;

import java.util.List;

import org.flowable.engine.ManagementService;
import org.flowable.job.api.Job;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProcessDefinitionJobResource extends BaseProcessDefinitionResource {

	@Autowired
	protected ManagementService managementService;

	@GetMapping(value = "/process-definitions/{processDefinitionId}/jobs", name = "流程定义激活")
	@ResponseStatus(value = HttpStatus.OK)
	public List<Job> activateProcessDefinition(@PathVariable String processDefinitionId) {
		List<Job> jobs = managementService.createTimerJobQuery().processDefinitionId(processDefinitionId).list();
		return jobs;
	}

	@DeleteMapping(value = "/process-definitions/{processDefinitionId}/jobs/{jobId}", name = "流程定义激活")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteJob(@PathVariable String processDefinitionId, @PathVariable String jobId) {
		managementService.deleteTimerJob(jobId);
	}
}
