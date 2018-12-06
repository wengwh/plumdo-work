package com.plumdo.flow.rest.model.resource;

import java.io.ByteArrayInputStream;

import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.repository.Model;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.plumdo.flow.constant.ErrorConstant;

/**
 * 获取模型XML
 *
 * @author wengwenhui
 * @date 2018年4月11日
 */
@RestController
public class ModelXmlResource extends BaseModelResource {

    @GetMapping(value = "/models/{modelId}/xml", name = "获取模型XML")
    public ResponseEntity<byte[]> getModelXml(@PathVariable String modelId) {
        Model model = getModelFromRequest(modelId);
        try {
            BpmnJsonConverter jsonConverter = new BpmnJsonConverter();
            JsonNode editorNode = new ObjectMapper().readTree(repositoryService.getModelEditorSource(model.getId()));
            BpmnModel bpmnModel = jsonConverter.convertToBpmnModel(editorNode);
            BpmnXMLConverter xmlConverter = new BpmnXMLConverter();
            byte[] bpmnBytes = xmlConverter.convertToXML(bpmnModel);
            ByteArrayInputStream in = new ByteArrayInputStream(bpmnBytes);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentType(MediaType.TEXT_XML);
            return new ResponseEntity<>(IOUtils.toByteArray(in), responseHeaders, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("获取模型XML信息异常", e);
            exceptionFactory.throwDefinedException(ErrorConstant.MODEL_XML_READ_ERROR, e.getMessage());
        }
        return null;
    }
}
