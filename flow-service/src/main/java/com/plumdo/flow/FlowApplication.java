package com.plumdo.flow;

import java.io.InputStream;
import java.io.InputStreamReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.apache.commons.io.IOUtils;
import org.flowable.bpmn.converter.BpmnXMLConverter;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.editor.constants.ModelDataJsonConstants;
import org.flowable.editor.language.json.converter.BpmnJsonConverter;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.common.api.FlowableException;
import org.flowable.engine.repository.Model;
import org.flowable.engine.repository.ProcessDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

@SpringBootApplication
public class FlowApplication {

	@Autowired
	protected RepositoryService repositoryService;
	
	public static void main(String[] args) {
		SpringApplication.run(FlowApplication.class, args);
	}

	// 初始化模拟数据,把定义转换为模型提供修改
	@Bean
	public CommandLineRunner init() {
		return new CommandLineRunner() {
			public void run(String... strings) throws Exception {
				if(repositoryService.createModelQuery().modelKey("OA").count()>0){
					return;
				}
				ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionKey("OA").singleResult();
				if(processDefinition==null){
					return;
				}
				
				try {
					InputStream bpmnStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getResourceName());
					XMLInputFactory xif = XMLInputFactory.newInstance();
					InputStreamReader in = new InputStreamReader(bpmnStream, "UTF-8");
					XMLStreamReader xtr = xif.createXMLStreamReader(in);
					BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);

					BpmnJsonConverter converter = new BpmnJsonConverter();
					ObjectNode modelNode = converter.convertToJson(bpmnModel);
					Model modelData = repositoryService.newModel();
					modelData.setKey(processDefinition.getKey());
					modelData.setName(processDefinition.getName());
					modelData.setCategory(processDefinition.getCategory());

					ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
					modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME,processDefinition.getName());
					modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
					modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION,processDefinition.getDescription());
					modelData.setMetaInfo(modelObjectNode.toString());

					repositoryService.saveModel(modelData);

					repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));
					repositoryService.addModelEditorSourceExtra(modelData.getId(),IOUtils.toByteArray(repositoryService.getProcessDiagram(processDefinition.getId())));
				} catch (Exception e) {
					throw new FlowableException("Error converting process-definition to model", e);
				}
			}
		};
	}
}