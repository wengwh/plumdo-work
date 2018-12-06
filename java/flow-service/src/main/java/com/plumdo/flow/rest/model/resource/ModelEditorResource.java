package com.plumdo.flow.rest.model.resource;

import org.flowable.engine.repository.Model;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.plumdo.common.constant.CoreConstant;
import com.plumdo.common.utils.ObjectUtils;
import com.plumdo.flow.cmd.SaveModelEditorCmd;
import com.plumdo.flow.cmd.UpdateModelKeyCmd;
import com.plumdo.flow.constant.ErrorConstant;
import com.plumdo.flow.constant.TableConstant;
import com.plumdo.flow.rest.model.ModelEditorJsonRequest;

/**
 * 模型设计器接口
 *
 * @author wengwenhui
 * @date 2018年4月11日
 */
@RestController
public class ModelEditorResource extends BaseModelResource {

    @GetMapping(value = "/models/{modelId}/editor", name = "设计器获取模型信息")
    public ObjectNode getEditorJson(@PathVariable String modelId) {
        ObjectNode modelNode = null;
        Model model = getModelFromRequest(modelId);
        try {
            if (ObjectUtils.isNotEmpty(model.getMetaInfo())) {
                modelNode = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
            } else {
                modelNode = objectMapper.createObjectNode();
                modelNode.put("name", model.getName());
            }
            modelNode.put("key", model.getKey());
            modelNode.put("category", model.getCategory());
            modelNode.put("tenantId", model.getTenantId());
            modelNode.put("modelId", model.getId());
            byte[] editors = repositoryService.getModelEditorSource(model.getId());
            ObjectNode editorJsonNode = (ObjectNode) objectMapper.readTree(new String(editors, CoreConstant.DEFAULT_CHARSET));
            editorJsonNode.put("modelType", "model");
            modelNode.set("model", editorJsonNode);
        } catch (Exception e) {
            logger.error("获取模型设计信息异常", e);
            exceptionFactory.throwDefinedException(ErrorConstant.MODEL_GET_EDITOR_ERROR, e.getMessage());
        }
        return modelNode;
    }

    @PostMapping(value = "/models/{modelId}/editor", name = "模型设计器保存模型")
    @ResponseStatus(value = HttpStatus.OK)
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public void saveModelEditor(@PathVariable String modelId, @RequestBody ModelEditorJsonRequest values) {
        Model model = getModel(modelId, values.isNewVersion());
        if (!model.getKey().equals(values.getKey())) {
            checkModelKeyExists(values.getKey());
            managementService.executeCommand(new UpdateModelKeyCmd(modelId, values.getKey()));
        }
        try {
            ObjectNode modelJson = (ObjectNode) objectMapper.readTree(model.getMetaInfo());
            modelJson.put("name", values.getName());
            modelJson.put("description", values.getDescription());
            model.setMetaInfo(modelJson.toString());
            model.setName(values.getName());
            model.setKey(values.getKey());
            repositoryService.saveModel(model);
            managementService.executeCommand(new SaveModelEditorCmd(model.getId(), values.getJsonXml()));
        } catch (Exception e) {
            logger.error("保存模型设计信息异常", e);
            exceptionFactory.throwDefinedException(ErrorConstant.MODEL_GET_EDITOR_ERROR, e.getMessage());
        }
    }

    private Model getModel(String modelId, boolean isNewVersion) {
        Model model = getModelFromRequest(modelId);
        if (isNewVersion) {
            Model newModel = repositoryService.newModel();
            Model lastModel = repositoryService.createModelQuery().modelKey(model.getKey()).latestVersion().singleResult();
            if (lastModel == null) {
                newModel.setVersion(TableConstant.MODEL_VESION_START);
            } else {
                newModel.setVersion(lastModel.getVersion() + 1);
            }
            newModel.setKey(model.getKey());
            newModel.setMetaInfo(model.getMetaInfo());
            newModel.setCategory(model.getCategory());
            newModel.setTenantId(model.getTenantId());
            return newModel;
        } else {
            return model;
        }
    }
}