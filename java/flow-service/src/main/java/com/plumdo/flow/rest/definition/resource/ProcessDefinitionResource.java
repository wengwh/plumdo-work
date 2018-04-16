package com.plumdo.flow.rest.definition.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;

import org.flowable.engine.RuntimeService;
import org.flowable.engine.common.api.FlowableException;
import org.flowable.engine.common.api.FlowableIllegalArgumentException;
import org.flowable.engine.common.api.FlowableObjectNotFoundException;
import org.flowable.engine.common.api.query.QueryProperty;
import org.flowable.engine.impl.ProcessDefinitionQueryProperty;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.plumdo.common.resource.PageResponse;
import com.plumdo.common.utils.ObjectUtils;
import com.plumdo.flow.exception.FlowableForbiddenException;
import com.plumdo.flow.rest.definition.ProcessDefinitionResponse;
import com.plumdo.flow.rest.definition.ProcessDefinitionsPaginateList;

@RestController
public class ProcessDefinitionResource extends BaseProcessDefinitionResource {
	@Autowired
	private RuntimeService runtimeService;

	private static final Map<String, QueryProperty> allowedSortProperties = new HashMap<String, QueryProperty>();

	static {
		allowedSortProperties.put("id", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_ID);
		allowedSortProperties.put("key", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_KEY);
		allowedSortProperties.put("category", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_CATEGORY);
		allowedSortProperties.put("name", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_NAME);
		allowedSortProperties.put("version", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_VERSION);
		allowedSortProperties.put("tenantId", ProcessDefinitionQueryProperty.PROCESS_DEFINITION_TENANT_ID);
	}

	@GetMapping(value = "/process-definitions", name = "流程定义查询")
	public PageResponse getProcessDefinitions(@RequestParam Map<String, String> requestParams) {
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
		if (ObjectUtils.isNotEmpty(requestParams.get("id"))) {
			processDefinitionQuery.processDefinitionId(requestParams.get("id"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("category"))) {
			processDefinitionQuery.processDefinitionCategoryLike(ObjectUtils.convertToLike(requestParams.get("category")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("key"))) {
			processDefinitionQuery.processDefinitionKeyLike(ObjectUtils.convertToLike(requestParams.get("key")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("name"))) {
			processDefinitionQuery.processDefinitionNameLike(ObjectUtils.convertToLike(requestParams.get("name")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("version"))) {
			processDefinitionQuery.processDefinitionVersion(ObjectUtils.convertToInteger(requestParams.get("version")));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("suspended"))) {
			boolean suspended = ObjectUtils.convertToBoolean(requestParams.get("suspended"));
			if (suspended) {
				processDefinitionQuery.suspended();
			} else {
				processDefinitionQuery.active();
			}
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("latestVersion"))) {
			boolean latest = ObjectUtils.convertToBoolean(requestParams.get("latestVersion"));
			if (latest) {
				processDefinitionQuery.latestVersion();
			}
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("startableByUser"))) {
			processDefinitionQuery.startableByUser(requestParams.get("startableByUser"));
		}
		if (ObjectUtils.isNotEmpty(requestParams.get("tenantId"))) {
			processDefinitionQuery.processDefinitionTenantId(requestParams.get("tenantId"));
		}

		return new ProcessDefinitionsPaginateList(restResponseFactory).paginateList(getPageable(requestParams), processDefinitionQuery, allowedSortProperties);
	}

	@GetMapping(value = "/process-definitions/{processDefinitionId}", name = "根据ID获取流程定义")
	public ProcessDefinitionResponse getProcessDefinition(@PathVariable String processDefinitionId) {
		ProcessDefinition processDefinition = getProcessDefinitionFromRequest(processDefinitionId);
		return restResponseFactory.createProcessDefinitionResponse(processDefinition);
	}
	
	@PostMapping(value = "/process-definitions", name = "流程定义创建")
	@ResponseStatus(value = HttpStatus.CREATED)
	public ProcessDefinitionResponse createProcessDefinition(@RequestParam(value = "tenantId", required = false) String tenantId, HttpServletRequest request) {

		if (request instanceof MultipartHttpServletRequest == false) {
			throw new FlowableIllegalArgumentException("Multipart request is required");
		}

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		if (multipartRequest.getFileMap().size() == 0) {
			throw new FlowableIllegalArgumentException("Multipart request with file content is required");
		}

		MultipartFile file = multipartRequest.getFileMap().values().iterator().next();

		try {
			DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
			String fileName = file.getOriginalFilename();
			if (StringUtils.isEmpty(fileName) || !(fileName.endsWith(".bpmn20.xml") || fileName.endsWith(".bpmn") || fileName.toLowerCase().endsWith(".bar") || fileName.toLowerCase().endsWith(".zip"))) {
				fileName = file.getName();
			}

			if (fileName.endsWith(".bpmn20.xml") || fileName.endsWith(".bpmn")) {
				deploymentBuilder.addInputStream(fileName, file.getInputStream());
			} else if (fileName.toLowerCase().endsWith(".bar") || fileName.toLowerCase().endsWith(".zip")) {
				deploymentBuilder.addZipInputStream(new ZipInputStream(file.getInputStream()));
			} else {
				throw new FlowableIllegalArgumentException("File must be of type .bpmn20.xml, .bpmn, .bar or .zip");
			}
			deploymentBuilder.name(fileName);

			if (tenantId != null) {
				deploymentBuilder.tenantId(tenantId);
			}

			String deploymentId = deploymentBuilder.deploy().getId();
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
			return restResponseFactory.createProcessDefinitionResponse(processDefinition);
		} catch (Exception e) {
			if (e instanceof FlowableException) {
				throw (FlowableException) e;
			}
			throw new FlowableException(e.getMessage(), e);
		}
	}

	@DeleteMapping(value = "/process-definitions/{processDefinitionId}", name = "流程定义删除")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteProcessDefinition(@PathVariable String processDefinitionId, @RequestParam(value = "cascade", required = false, defaultValue = "false") Boolean cascade) {

		ProcessDefinition processDefinition = getProcessDefinitionFromRequest(processDefinitionId);

		if (processDefinition.getDeploymentId() == null) {
			throw new FlowableObjectNotFoundException("No found deployment ");
		}

		if (cascade) {
			repositoryService.deleteDeployment(processDefinition.getDeploymentId(), true);
		} else {
			long count = runtimeService.createProcessInstanceQuery().processDefinitionId(processDefinitionId).count();
			if (count != 0) {
				throw new FlowableForbiddenException("Cannot delete a process-definition that have process-instance");
			}
			repositoryService.deleteDeployment(processDefinition.getDeploymentId());
		}
	}
}
