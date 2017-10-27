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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.plumdo.flow.exception.FlowableForbiddenException;
import com.plumdo.flow.rest.DataResponse;
import com.plumdo.flow.rest.definition.ProcessDefinitionResponse;
import com.plumdo.flow.rest.definition.ProcessDefinitionsPaginateList;

@RestController
public class ProcessDefinitionResource extends BaseProcessDefinitionResource{

	@Autowired
	protected RuntimeService runtimeService;
	
	private static final Map<String, QueryProperty> properties = new HashMap<String, QueryProperty>();
  
	static {
		properties.put("id",ProcessDefinitionQueryProperty.PROCESS_DEFINITION_ID);
		properties.put("key",ProcessDefinitionQueryProperty.PROCESS_DEFINITION_KEY);
		properties.put("category",ProcessDefinitionQueryProperty.PROCESS_DEFINITION_CATEGORY);
		properties.put("name",ProcessDefinitionQueryProperty.PROCESS_DEFINITION_NAME);
		properties.put("version",ProcessDefinitionQueryProperty.PROCESS_DEFINITION_VERSION);
		properties.put("tenantId",ProcessDefinitionQueryProperty.PROCESS_DEFINITION_TENANT_ID);
	}
	
	@RequestMapping(value = "/process-definition", method = RequestMethod.GET, produces = "application/json", name="流程定义查询")
	public DataResponse getProcessDefinitions(@RequestParam Map<String, String> allRequestParams) {
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();

		if (allRequestParams.containsKey("id")) {
			processDefinitionQuery.processDefinitionId(allRequestParams.get("id"));
		}
		
		if (allRequestParams.containsKey("category")) {
			processDefinitionQuery.processDefinitionCategoryLike(allRequestParams.get("category"));
		}
		if (allRequestParams.containsKey("key")) {
			processDefinitionQuery.processDefinitionKeyLike(allRequestParams.get("key"));
		}
		if (allRequestParams.containsKey("name")) {
			processDefinitionQuery.processDefinitionNameLike(allRequestParams.get("name"));
		}
		if (allRequestParams.containsKey("version")) {
			processDefinitionQuery.processDefinitionVersion(Integer.valueOf(allRequestParams.get("version")));
		}
		if (allRequestParams.containsKey("suspended")) {
			Boolean suspended = Boolean.valueOf(allRequestParams.get("suspended"));
			if (suspended != null) {
				if (suspended) {
					processDefinitionQuery.suspended();
				} else {
					processDefinitionQuery.active();
				}
			}
		}
		if (allRequestParams.containsKey("latestVersion")) {
			Boolean latest = Boolean.valueOf(allRequestParams.get("latestVersion"));
			if (latest != null && latest) {
				processDefinitionQuery.latestVersion();
			}
		}
		if (allRequestParams.containsKey("startableByUser")) {
			processDefinitionQuery.startableByUser(allRequestParams.get("startableByUser"));
		}
		if (allRequestParams.containsKey("tenantId")) {
			processDefinitionQuery.processDefinitionTenantIdLike(allRequestParams.get("tenantId"));
		}

		return new ProcessDefinitionsPaginateList(restResponseFactory).paginateList(allRequestParams, processDefinitionQuery, "name",properties);
	}
  
	@RequestMapping(value = "/process-definition", method = RequestMethod.POST, produces = "application/json", name="流程定义创建")
	@ResponseStatus(value = HttpStatus.CREATED)
	public ProcessDefinitionResponse createProcessDefinition(@RequestParam(value = "tenantId", required = false) String tenantId,HttpServletRequest request) {

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
			if (StringUtils.isEmpty(fileName)
					|| !(fileName.endsWith(".bpmn20.xml")
							|| fileName.endsWith(".bpmn")
							|| fileName.toLowerCase().endsWith(".bar")
							|| fileName.toLowerCase().endsWith(".zip"))) {
				fileName = file.getName();
			}
			
			if (fileName.endsWith(".bpmn20.xml") || fileName.endsWith(".bpmn")) {
				deploymentBuilder.addInputStream(fileName, file.getInputStream());
			} else if (fileName.toLowerCase().endsWith(".bar")|| fileName.toLowerCase().endsWith(".zip")) {
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
	
	@RequestMapping(value="/process-definition/{processDefinitionId}", method = RequestMethod.DELETE, produces = "application/json", name="流程定义删除")
  	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteProcessDefinition(@PathVariable String processDefinitionId, 
			@RequestParam(value="cascade", required=false, defaultValue="false") Boolean cascade) {
  		
  		ProcessDefinition processDefinition = getProcessDefinitionFromRequest(processDefinitionId);
	  	
  		if(processDefinition.getDeploymentId()==null){
		  	throw new FlowableObjectNotFoundException("No found deployment ");
	  	}
  		
	  	if (cascade) {
		  	repositoryService.deleteDeployment(processDefinition.getDeploymentId(), true);
	  	}else {
	  		long count = runtimeService.createProcessInstanceQuery().processDefinitionId(processDefinitionId).count();
	  		if(count!=0){
	  			throw new FlowableForbiddenException("Cannot delete a process-definition that have process-instance");
	  		}
	  		repositoryService.deleteDeployment(processDefinition.getDeploymentId());
	  	}
  	}
}
