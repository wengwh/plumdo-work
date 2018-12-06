package com.plumdo.flow.cmd;

import java.io.ByteArrayInputStream;
import java.io.Serializable;

import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.common.api.FlowableException;
import org.flowable.engine.common.api.FlowableObjectNotFoundException;
import org.flowable.engine.common.impl.interceptor.Command;
import org.flowable.engine.common.impl.interceptor.CommandContext;
import org.flowable.engine.impl.util.CommandContextUtil;
import org.flowable.engine.repository.Deployment;
import org.flowable.engine.repository.DeploymentBuilder;
import org.flowable.engine.repository.Model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 部署模型
 *
 * @author wengwh
 * @date 2018/12/6
 */
public class DeployModelCmd implements Command<Deployment>, Serializable {

    private static final long serialVersionUID = 1L;
    private String modelId;

    public DeployModelCmd(String modelId) {
        this.modelId = modelId;
    }

    @Override
    public Deployment execute(CommandContext commandContext) {
        Deployment deployment;

        RepositoryService repositoryService = CommandContextUtil.getProcessEngineConfiguration(commandContext).getRepositoryService();
        Model model = repositoryService.getModel(modelId);
        if (model == null) {
            throw new FlowableObjectNotFoundException("Could not find a model with id '" + modelId + "'.", Model.class);
        }

        byte[] editorSource = CommandContextUtil.getProcessEngineConfiguration(commandContext).getModelEntityManager().findEditorSourceByModelId(modelId);
        if (editorSource == null) {
            throw new FlowableObjectNotFoundException("Model with id '" + modelId + "' does not have source available.", String.class);
        }

        try {
            DeploymentBuilder deploymentBuilder = repositoryService.createDeployment();
            ObjectNode modelNode = (ObjectNode) new ObjectMapper().readTree(editorSource);
            BpmnModel bpmnModel = new BpmnJsonConverter().convertToBpmnModel(modelNode);
            byte[] bpmnBytes = new BpmnXMLConverter().convertToXML(bpmnModel);
            String fileName = model.getId() + ".bpmn20.xml";
            ByteArrayInputStream bis = new ByteArrayInputStream(bpmnBytes);
            deploymentBuilder.addInputStream(fileName, bis);
            deploymentBuilder.name(fileName);
            // modelId设置为部署的分类字段作为后续关联的需要
            deploymentBuilder.category(model.getId());

            if (model.getTenantId() != null) {
                deploymentBuilder.tenantId(model.getTenantId());
            }
            deployment = deploymentBuilder.deploy();
            model.setDeploymentId(deployment.getId());
        } catch (Exception e) {
            if (e instanceof FlowableException) {
                throw (FlowableException) e;
            }
            throw new FlowableException(e.getMessage(), e);
        }

        return deployment;
    }

}