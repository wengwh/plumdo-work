package com.plumdo.flow.rest.model.resource;

import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.Process;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.repository.Model;
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
import com.plumdo.flow.constant.ErrorConstant;
import com.plumdo.flow.constant.TableConstant;
import com.plumdo.flow.rest.model.ModelResponse;

/**
 * 流程模型导入接口
 *
 * @author wengwenhui
 * @date 2018年4月11日
 */
@RestController
public class ModelImportResource extends BaseModelResource {
    private BpmnXMLConverter bpmnXmlConverter = new BpmnXMLConverter();
    private BpmnJsonConverter bpmnJsonConverter = new BpmnJsonConverter();

    @PostMapping(value = "/models/import", name = "流程模型导入")
    @ResponseStatus(value = HttpStatus.CREATED)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public ModelResponse importModel(HttpServletRequest request) {
        if (!(request instanceof MultipartHttpServletRequest)) {
            exceptionFactory.throwIllegalArgument(ErrorConstant.REQUEST_NOT_MULTIPART);
        }

        MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;

        if (multipartRequest.getFileMap().size() == 0) {
            exceptionFactory.throwIllegalArgument(ErrorConstant.MULTIPART_CONTENT_EMPTY);
        }

        MultipartFile file = multipartRequest.getFileMap().values().iterator().next();

        String fileName = file.getOriginalFilename();
        if (fileName == null || (!fileName.endsWith(".bpmn") && !fileName.endsWith(".bpmn20.xml"))) {
            exceptionFactory.throwIllegalArgument(ErrorConstant.FILE_NOT_BPMN, fileName);
        }
        try {
            XMLInputFactory xif = XMLInputFactory.newInstance();
            InputStreamReader xmlIn = new InputStreamReader(file.getInputStream(), "UTF-8");
            XMLStreamReader xtr = xif.createXMLStreamReader(xmlIn);
            BpmnModel bpmnModel = bpmnXmlConverter.convertToBpmnModel(xtr);
            if (ObjectUtils.isEmpty(bpmnModel.getProcesses())) {
                exceptionFactory.throwObjectNotFound(ErrorConstant.MODEL_NOT_FOUND_PROCESS, fileName);
            }
            Process process = bpmnModel.getMainProcess();
            Model modelData = repositoryService.newModel();
            modelData.setKey(process.getId());
            Model lastModel = repositoryService.createModelQuery().modelKey(modelData.getKey()).latestVersion().singleResult();
            if (lastModel == null) {
                modelData.setVersion(TableConstant.MODEL_VESION_START);
            } else {
                modelData.setVersion(lastModel.getVersion() + 1);
            }
            if (ObjectUtils.isNotEmpty(process.getName())) {
                modelData.setName(process.getName());
            } else {
                modelData.setName(process.getId());
            }
            ObjectNode metaInfo = new ObjectMapper().createObjectNode();
            metaInfo.put("name", modelData.getName());
            metaInfo.put("description", process.getDocumentation());
            modelData.setMetaInfo(metaInfo.toString());
            repositoryService.saveModel(modelData);
            managementService.executeCommand(new SaveModelEditorCmd(modelData.getId(), bpmnJsonConverter.convertToJson(bpmnModel).toString()));
            return restResponseFactory.createModelResponse(modelData);
        } catch (Exception e) {
            logger.error("导入流程文件异常", e);
            exceptionFactory.throwDefinedException(ErrorConstant.MODEL_IMPORT_FILE_ERROR, fileName, e.getMessage());
        }
        return null;
    }
}
