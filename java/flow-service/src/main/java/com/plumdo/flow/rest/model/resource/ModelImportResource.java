package com.plumdo.flow.rest.model.resource;

import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.lang3.StringUtils;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.editor.constants.ModelDataJsonConstants;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.ManagementService;
import org.flowable.engine.common.api.FlowableException;
import org.flowable.engine.common.api.FlowableIllegalArgumentException;
import org.flowable.engine.repository.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.plumdo.common.utils.ObjectUtils;
import com.plumdo.flow.cmd.SaveModelEditorCmd;
import com.plumdo.flow.exception.FlowableForbiddenException;

@RestController
public class ModelImportResource extends BaseModelResource {
	@Autowired
	private ManagementService managementService;

	private BpmnXMLConverter bpmnXmlConverter = new BpmnXMLConverter();
	private BpmnJsonConverter bpmnJsonConverter = new BpmnJsonConverter();

	@PostMapping(value = "/models/import", name = "流程模型导入")
	@ResponseStatus(value = HttpStatus.CREATED)
	@Transactional(propagation = Propagation.REQUIRED)
	public void importModel(HttpServletRequest request) {

		if (request instanceof MultipartHttpServletRequest == false) {
			throw new FlowableIllegalArgumentException("Multipart request is required");
		}

		MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

		if (multipartRequest.getFileMap().size() == 0) {
			throw new FlowableIllegalArgumentException("Multipart request with file content is required");
		}

		MultipartFile file = multipartRequest.getFileMap().values().iterator().next();

		String fileName = file.getOriginalFilename();
		if (fileName != null && (fileName.endsWith(".bpmn") || fileName.endsWith(".bpmn20.xml"))) {
			try {
				XMLInputFactory xif = XMLInputFactory.newInstance();
				InputStreamReader xmlIn = new InputStreamReader(file.getInputStream(), "UTF-8");
				XMLStreamReader xtr = xif.createXMLStreamReader(xmlIn);
				BpmnModel bpmnModel = bpmnXmlConverter.convertToBpmnModel(xtr);
				if (ObjectUtils.isEmpty(bpmnModel.getProcesses())) {
					throw new FlowableForbiddenException("No process found in definition " + fileName);
				}

				ObjectNode modelNode = bpmnJsonConverter.convertToJson(bpmnModel);

				org.flowable.bpmn.model.Process process = bpmnModel.getMainProcess();
				String name = process.getId();
				if (StringUtils.isNotEmpty(process.getName())) {
					name = process.getName();
				}
				String description = process.getDocumentation();

				Model modelData = repositoryService.newModel();
				modelData.setKey(process.getId());
				modelData.setName(name);

				ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
				modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, name);
				modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
				modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, description);
				modelData.setMetaInfo(modelObjectNode.toString());

				repositoryService.saveModel(modelData);

				managementService.executeCommand(new SaveModelEditorCmd(modelData.getId(), modelNode.toString()));

			} catch (Exception e) {
				logger.error("Import failed for {}", fileName, e);
				throw new FlowableException("Import failed for " + fileName + ", error message " + e.getMessage());
			}
		} else {
			throw new FlowableIllegalArgumentException("Invalid file name, only .bpmn and .bpmn20.xml files are supported not " + fileName);
		}
	}
}
