package com.plumdo.flow.rest.definition.resource;

import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;

import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.plumdo.common.utils.ObjectUtils;
import com.plumdo.flow.constant.ErrorConstant;
import com.plumdo.flow.rest.definition.ProcessDefinitionResponse;

/**
 * 流程定义导入接口
 *
 * @author wengwenhui
 * @date 2018年4月17日
 */
@RestController
public class ProcessDefinitionImportResource extends BaseProcessDefinitionResource {

	@PostMapping(value = "/process-definitions/import", name = "流程定义导入")
	@ResponseStatus(value = HttpStatus.CREATED)
	public ProcessDefinitionResponse createProcessDefinition(@RequestParam(value = "tenantId", required = false) String tenantId, HttpServletRequest request) {
		if (request instanceof MultipartHttpServletRequest == false) {
			exceptionFactory.throwIllegalArgument(ErrorConstant.REQUEST_NOT_MULTIPART);
		}

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		if (multipartRequest.getFileMap().size() == 0) {
			exceptionFactory.throwIllegalArgument(ErrorConstant.MULTIPART_CONTENT_EMPTY);
		}

		MultipartFile file = multipartRequest.getFileMap().values().iterator().next();
		String fileName = file.getOriginalFilename();
		if (ObjectUtils.isEmpty(fileName) || !(fileName.endsWith(".bpmn20.xml") || fileName.endsWith(".bpmn") || fileName.toLowerCase().endsWith(".bar") || fileName.toLowerCase().endsWith(".zip"))) {
			exceptionFactory.throwIllegalArgument(ErrorConstant.FILE_NOT_BPMN, fileName);
		}
		try {
			DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();

			if (fileName.endsWith(".bpmn20.xml") || fileName.endsWith(".bpmn")) {
				deploymentBuilder.addInputStream(fileName, file.getInputStream());
			} else if (fileName.toLowerCase().endsWith(".bar") || fileName.toLowerCase().endsWith(".zip")) {
				deploymentBuilder.addZipInputStream(new ZipInputStream(file.getInputStream()));
			}

			deploymentBuilder.name(fileName);

			if (tenantId != null) {
				deploymentBuilder.tenantId(tenantId);
			}

			String deploymentId = deploymentBuilder.deploy().getId();
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().deploymentId(deploymentId).singleResult();
			return restResponseFactory.createProcessDefinitionResponse(processDefinition);
		} catch (Exception e) {
			logger.error("导入流程文件异常", e);
			exceptionFactory.throwDefinedException(ErrorConstant.DEFINITION_IMPORT_FILE_ERROR, fileName, e.getMessage());
			return null;
		}
	}

}
