package com.plumdo.flow.rest.diagram.resource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.bpmn.model.Artifact;
import org.flowable.bpmn.model.Association;
import org.flowable.bpmn.model.BpmnModel;
import org.flowable.bpmn.model.GraphicInfo;
import org.flowable.bpmn.model.Process;
import org.flowable.bpmn.model.TextAnnotation;
import org.flowable.engine.FlowableObjectNotFoundException;
import org.flowable.engine.HistoryService;
import org.flowable.engine.IdentityService;
import org.flowable.engine.RepositoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricIdentityLink;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.history.HistoricTaskInstance;
import org.flowable.engine.identity.Group;
import org.flowable.engine.identity.User;
import org.flowable.engine.impl.bpmn.behavior.BoundaryEventActivityBehavior;
import org.flowable.engine.impl.bpmn.behavior.CallActivityBehavior;
import org.flowable.engine.impl.bpmn.behavior.MultiInstanceActivityBehavior;
import org.flowable.engine.impl.bpmn.parser.BpmnParse;
import org.flowable.engine.impl.bpmn.parser.ErrorEventDefinition;
import org.flowable.engine.impl.bpmn.parser.EventSubscriptionDeclaration;
import org.flowable.engine.impl.jobexecutor.TimerDeclarationImpl;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.flowable.engine.impl.pvm.PvmTransition;
import org.flowable.engine.impl.pvm.delegate.ActivityBehavior;
import org.flowable.engine.impl.pvm.process.ActivityImpl;
import org.flowable.engine.impl.pvm.process.Lane;
import org.flowable.engine.impl.pvm.process.LaneSet;
import org.flowable.engine.impl.pvm.process.ParticipantProcess;
import org.flowable.engine.impl.pvm.process.TransitionImpl;
import org.flowable.engine.repository.ProcessDefinition;
import org.flowable.engine.repository.ProcessDefinitionQuery;
import org.flowable.engine.task.IdentityLink;
import org.flowable.engine.task.IdentityLinkType;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * 获取监控的配置信息
 * @author wengwh
 *
 */
public class BaseDiagramLayoutResource {
	protected static final Logger LOGGER = LoggerFactory.getLogger(BaseDiagramLayoutResource.class);

	@Autowired
	protected ObjectMapper objectMapper;
	
	@Autowired
	protected RepositoryService repositoryService;
  
	@Autowired
	protected HistoryService historyService;
	
	@Autowired
	protected TaskService taskService;
	
	@Autowired
	protected IdentityService identityService;
	
	@Autowired
	protected BaseDiagramHighLighted baseDiagramHighLighted;
  
	private HistoricProcessInstance getHistoricProcessInstanceById(String processInstanceId){
		HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
    	if (historicProcessInstance == null) {
    		throw new FlowableObjectNotFoundException("Could not find a process instance with id '" + processInstanceId + "'.",HistoricProcessInstance.class);
    	}
    	return historicProcessInstance;
	}
	
	private ProcessDefinition getProcessDefinitionByKey(String processDefinitionKey,String tenantId){
		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery().processDefinitionKey(processDefinitionKey);
		if(StringUtils.isNotEmpty(tenantId)){
			query.processDefinitionTenantId(tenantId);
		}
		ProcessDefinition processDefinition = query.latestVersion().singleResult();
		
		if (processDefinition == null) {
			throw new FlowableObjectNotFoundException("Could not find a process definition with key '"+ processDefinitionKey + "',tenantId "+tenantId,ProcessDefinition.class);
		}
		return processDefinition;
	}
	
	private ProcessDefinitionEntity getProcessDefinitionById(String processDefinitionId){
		ProcessDefinitionEntity processDefinition = (ProcessDefinitionEntity) repositoryService.getProcessDefinition(processDefinitionId);

    	if (processDefinition == null) {
	    	throw new FlowableObjectNotFoundException("Process definition " + processDefinitionId + " could not be found",ProcessDefinition.class);
	    }
    	
	    return processDefinition;
	}
	
	public ObjectNode getDiagramNode(String processInstanceId, String processDefinitionId,String processDefinitionKey) {
		return getDiagramNode(processInstanceId, processDefinitionId, processDefinitionKey, null);
	}
	public ObjectNode getDiagramNode(String processInstanceId, String processDefinitionId,String processDefinitionKey,String tenantId) {
	    ObjectNode responseJSON = objectMapper.createObjectNode();
	    
	    if (processInstanceId != null) {
	    	processDefinitionId = getHistoricProcessInstanceById(processInstanceId).getProcessDefinitionId();
	    }else if (processDefinitionKey != null){
	    	processDefinitionId = getProcessDefinitionByKey(processDefinitionKey,tenantId).getId();
	    }

	    ProcessDefinitionEntity processDefinition = getProcessDefinitionById(processDefinitionId);
	   
	    tenantId = processDefinition.getTenantId();
	    
    	List<String> highLightedFlows = new ArrayList<String>();
	    Map<String, String> activityStatus = new HashMap<String, String>();
	    Map<String, ArrayNode> activityVars = Collections.<String, ArrayNode>emptyMap();
	    Map<String, ObjectNode> subProcessInstanceMap = Collections.<String, ObjectNode>emptyMap();
	   
	    if (processInstanceId != null) {
	    	activityVars = this.getActivityVars(processInstanceId);
	    	subProcessInstanceMap = this.getSubProcessInstanceMap(processInstanceId);
	    	baseDiagramHighLighted.init(processDefinition, processInstanceId, highLightedFlows, activityStatus);
	    }

    	ObjectNode processDefinitionNode = getProcessDefinitionJson(processDefinition, processInstanceId);
    	if(processDefinitionNode != null)
    		responseJSON.put("processDefinition",processDefinitionNode);
    	
    	ObjectNode participantProcessNode = getParticipantProcessJson(processDefinition);
    	if(participantProcessNode != null)
    		responseJSON.put("participantProcess", participantProcessNode);
    	
    	ArrayNode laneSetArray = getLaneSetJson(processDefinition);
    	if (laneSetArray.size() > 0)
			responseJSON.put("laneSets", laneSetArray);
       	
    	ArrayNode sequenceFlowArray = getSequenceFlowJson(processDefinition.getFlowablees(), highLightedFlows);
    	if(sequenceFlowArray.size()>0)
        	responseJSON.put("sequenceFlows", sequenceFlowArray);
    	
    	
    	BpmnModel bpmnModel = repositoryService.getBpmnModel(processDefinition.getId());
    	
    	ArrayNode activityArray = getActivityJson(processInstanceId, tenantId,processDefinition.getFlowablees(), subProcessInstanceMap, activityStatus, activityVars);
    	activityArray.addAll(getTextAnnotationJson(bpmnModel));
    	if(activityArray.size()>0)
    		responseJSON.put("flowablees", activityArray);
    	
    	ArrayNode associationArray = getAssociationJson(bpmnModel);
    	if(associationArray.size()>0)
    		responseJSON.put("associations", associationArray);

    	return responseJSON;
	}
	
	/**
	 * 获取连线的JSON信息（递归获取）
	 * @param activityImpls
	 * @param highLightedFlows
	 * @return
	 */
	private ArrayNode getSequenceFlowJson(List<ActivityImpl> activityImpls,List<String> highLightedFlows){
		ArrayNode sequenceFlowArray = objectMapper.createArrayNode();
		for (ActivityImpl activity : activityImpls) {
			if(CollectionUtils.isNotEmpty(activity.getFlowablees())){
				sequenceFlowArray.addAll(getSequenceFlowJson(activity.getFlowablees(), highLightedFlows));
			}
			sequenceFlowArray.addAll(getSequenceFlowArray(activity, highLightedFlows));
    	}
    	return sequenceFlowArray;
	}
	
	private ArrayNode getSequenceFlowArray(ActivityImpl activity,List<String> highLightedFlows){
		ArrayNode sequenceFlowArray = objectMapper.createArrayNode();
		for (PvmTransition sequenceFlow : activity.getOutgoingTransitions()) {
    		String flowName = (String) sequenceFlow.getProperty("name");
    		boolean isHighLighted = (highLightedFlows.contains(sequenceFlow.getId()));
    		boolean isConditional = sequenceFlow.getProperty(BpmnParse.PROPERTYNAME_CONDITION) != null && 
    			!((String) activity.getProperty("type")).toLowerCase().contains("gateway");
    		boolean isDefault = sequenceFlow.getId().equals(activity.getProperty("default"))
    			&& ((String) activity.getProperty("type")).toLowerCase().contains("gateway");

    		List<Integer> waypoints = ((TransitionImpl) sequenceFlow).getWaypoints();
    		ArrayNode xPointArray = objectMapper.createArrayNode();
    		ArrayNode yPointArray = objectMapper.createArrayNode();
    		for (int i = 0; i < waypoints.size(); i += 2) {
    			xPointArray.add(waypoints.get(i));
    			yPointArray.add(waypoints.get(i + 1));
    		}

    		ObjectNode flowJSON = objectMapper.createObjectNode();
    		flowJSON.put("id", sequenceFlow.getId());
    		flowJSON.put("name", flowName);
    		flowJSON.put("flow", "(" + sequenceFlow.getSource().getId() + ")--"
					    		  	 + sequenceFlow.getId() + "-->("
					    		  	 + sequenceFlow.getDestination().getId() + ")");

    		if (isConditional)
    			flowJSON.put("isConditional", isConditional);
    		if (isDefault)
    			flowJSON.put("isDefault", isDefault);
    		if (isHighLighted)
    			flowJSON.put("isHighLighted", isHighLighted);
      
    		flowJSON.put("xPointArray", xPointArray);
    		flowJSON.put("yPointArray", yPointArray);

    		sequenceFlowArray.add(flowJSON);
    	}
		
		return sequenceFlowArray;
	}
	
	/**
	 * 获取节点的JSON信息（递归）
	 * @param processInstanceId
	 * @param activityImpls
	 * @param subProcessInstanceMap
	 * @param activityStatus
	 * @param activityVars
	 * @return
	 */
	private ArrayNode getActivityJson(String processInstanceId,String tenantId, List<ActivityImpl> activityImpls, 
			Map<String, ObjectNode> subProcessInstanceMap,Map<String, String> activityStatus,Map<String, ArrayNode> activityVars) {
		ArrayNode activityArray = objectMapper.createArrayNode();
		for (ActivityImpl activity : activityImpls) {
			if(CollectionUtils.isNotEmpty(activity.getFlowablees())){
				activityArray.addAll(getActivityJson(processInstanceId,tenantId, activity.getFlowablees(), subProcessInstanceMap,activityStatus,activityVars));
			}
			activityArray.add(getActivityNode(processInstanceId, tenantId,activity, subProcessInstanceMap, activityStatus, activityVars));
    	}
		return activityArray;
	}
	 
	
	private ObjectNode getActivityNode(String processInstanceId,String tenantId, ActivityImpl activity, 
			Map<String, ObjectNode> subProcessInstanceMap,Map<String, String> activityStatus,Map<String, ArrayNode> activityVars) {
		
		ObjectNode activityJSON = getActivityNode(processInstanceId, activity);
	   	
		if(activityVars.containsKey(activity.getId())){
    		activityJSON.put("vars",activityVars.get(activity.getId()));
    	}
		
		ObjectNode propertiesJSON = getActivityPropertyJson(activity);
		
		ArrayNode processInstanceArray = getCallActivityJson(activity, subProcessInstanceMap, processInstanceId,tenantId);
    	
		if (processInstanceArray.size() > 0) {
			propertiesJSON.put("processDefinitons", processInstanceArray);
		}
    	
    	if(activityStatus.containsKey(activity.getId())){
    		propertiesJSON.put("status",activityStatus.get(activity.getId()) );
    	}
    	
    	activityJSON.put("properties", propertiesJSON);

    	return activityJSON;
	}
	
	private ObjectNode getActivityNode(String processInstanceId, ActivityImpl activity) {
		ObjectNode activityJSON = objectMapper.createObjectNode();
		String multiInstance = (String) activity.getProperty("multiInstance");
		if (multiInstance != null) {
			if (!"sequential".equals(multiInstance)) {
				multiInstance = "parallel";
			}
    		activityJSON.put("multiInstance", multiInstance);
	    }
		
		ActivityBehavior activityBehavior = getActivityBehavior(activity);

		Boolean collapsed = (activityBehavior instanceof CallActivityBehavior);
    	Boolean expanded = (Boolean) activity.getProperty(BpmnParse.PROPERTYNAME_ISEXPANDED);
    	if (expanded != null) {
    		collapsed = !expanded;
    	}
    	if (collapsed)
    		activityJSON.put("collapsed", collapsed);
    	
    	
    	if (activityBehavior instanceof BoundaryEventActivityBehavior) {
    		Boolean isInterrupting = ((BoundaryEventActivityBehavior) activityBehavior).isInterrupting();
        	if (isInterrupting != null)
        		activityJSON.put("isInterrupting", isInterrupting);
    	}

    	ArrayNode nestedActivityArray = objectMapper.createArrayNode();
    	for (ActivityImpl nestedActivity : activity.getFlowablees()) {
    		nestedActivityArray.add(nestedActivity.getId());
    	}
    	if (nestedActivityArray.size() > 0)
    		activityJSON.put("nestedFlowablees", nestedActivityArray);
    	
    	activityJSON.put("activityId", activity.getId());
    	activityJSON.put("x", activity.getX());
    	activityJSON.put("y", activity.getY());
    	activityJSON.put("width", activity.getWidth());
    	activityJSON.put("height", activity.getHeight());
    	return activityJSON;
	}

	/**
	 * 获取外嵌子流程的配置信息
	 * @param activity
	 * @param subProcessInstanceMap
	 * @param processInstanceId
	 * @return
	 */
	private ArrayNode getCallActivityJson(ActivityImpl activity,Map<String, ObjectNode> subProcessInstanceMap,String processInstanceId,String tenantId){
		ArrayNode processInstanceArray = objectMapper.createArrayNode();
		if ("callActivity".equals(activity.getProperty("type"))) {
    		CallActivityBehavior callActivityBehavior = null;
    		
    		if (getActivityBehavior(activity) instanceof CallActivityBehavior) {
    			callActivityBehavior = (CallActivityBehavior) getActivityBehavior(activity);
    		}

    		if (callActivityBehavior != null) {
    			// get processDefinitonId from execution or get last processDefinitonId
    			if (processInstanceId != null) {
    				List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery()
    				.processInstanceId(processInstanceId).activityId(activity.getId()).orderByHistoricActivityInstanceStartTime().desc().list();
    				if (historicActivityInstances.size() > 0) {
    					for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
    						ObjectNode processInstanceJSON = subProcessInstanceMap.get(historicActivityInstance.getCalledProcessInstanceId());
    						processInstanceArray.add(processInstanceJSON);
    					}
    				}
    			}
    			// If active flowablees nas no instance of this callActivity then add
    			if (processInstanceArray.size() == 0 && StringUtils.isNotEmpty(callActivityBehavior.getProcessDefinitonKey())) {
    				ProcessDefinition lastProcessDefinition = getProcessDefinitionByKey(callActivityBehavior.getProcessDefinitonKey(),tenantId);
    				if (lastProcessDefinition != null) {
    					ObjectNode processInstanceJSON = objectMapper.createObjectNode();
    					processInstanceJSON.put("processDefinitionId", lastProcessDefinition.getId());
    					processInstanceJSON.put("processDefinitionKey", lastProcessDefinition.getKey());
    					processInstanceJSON.put("processDefinitionName", lastProcessDefinition.getName());
    					processInstanceArray.add(processInstanceJSON);
    				}
    			}
    		}
   	 	}
		return processInstanceArray;
	}
	
	private ActivityBehavior getActivityBehavior(ActivityImpl activity){
		ActivityBehavior activityBehavior = null;
		//如果是多实例的情况获取
		if (activity.getActivityBehavior() instanceof MultiInstanceActivityBehavior){
			activityBehavior = ((MultiInstanceActivityBehavior) activity.getActivityBehavior()).getInnerActivityBehavior();
		}else{
			activityBehavior = activity.getActivityBehavior();
		}
		return activityBehavior;
	}
	
	/**
	 * 获取节点属性信息
	 * @param activity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private ObjectNode getActivityPropertyJson(ActivityImpl activity){
		ObjectNode propertiesJSON = objectMapper.createObjectNode();
		Map<String, Object> properties = activity.getProperties();
    	for (String key : properties.keySet()) {
    		Object prop = properties.get(key);
    		if (prop instanceof String)
    			propertiesJSON.put(key, (String) properties.get(key));
    		else if (prop instanceof Integer)
    			propertiesJSON.put(key, (Integer) properties.get(key));
    		else if (prop instanceof Boolean)
    			propertiesJSON.put(key, (Boolean) properties.get(key));
    		else if ("initial".equals(key)) {
    			ActivityImpl act = (ActivityImpl) properties.get(key);
    			propertiesJSON.put(key, act.getId());
    		} else if ("timerDeclarations".equals(key)) {
    			ArrayList<TimerDeclarationImpl> timerDeclarations = (ArrayList<TimerDeclarationImpl>) properties.get(key);
    			ArrayNode timerDeclarationArray = objectMapper.createArrayNode();

    			if (timerDeclarations != null)
    				for (TimerDeclarationImpl timerDeclaration : timerDeclarations) {
    					ObjectNode timerDeclarationJSON = objectMapper.createObjectNode();

    					timerDeclarationJSON.put("isExclusive", timerDeclaration.isExclusive());
    					if (timerDeclaration.getRepeat() != null)
    						timerDeclarationJSON.put("repeat", timerDeclaration.getRepeat());
            
    					timerDeclarationJSON.put("retries", String.valueOf(timerDeclaration.getRetries()));
    					timerDeclarationJSON.put("type", timerDeclaration.getJobHandlerType());
    					timerDeclarationJSON.put("configuration", timerDeclaration.getJobHandlerConfiguration());
    					//timerDeclarationJSON.put("expression", timerDeclaration.getDescription());
    					timerDeclarationArray.add(timerDeclarationJSON);
    				}
    			if (timerDeclarationArray.size() > 0)
    				propertiesJSON.put(key, timerDeclarationArray);
			} else if ("eventDefinitions".equals(key)) {
				ArrayList<EventSubscriptionDeclaration> eventDefinitions = (ArrayList<EventSubscriptionDeclaration>) properties.get(key);
				ArrayNode eventDefinitionsArray = objectMapper.createArrayNode();

				if (eventDefinitions != null) {
					for (EventSubscriptionDeclaration eventDefinition : eventDefinitions) {
						ObjectNode eventDefinitionJSON = objectMapper.createObjectNode();
						
						if (eventDefinition.getActivityId() != null)
							eventDefinitionJSON.put("activityId",eventDefinition.getActivityId());
            
						eventDefinitionJSON.put("eventName", eventDefinition.getEventName());
						eventDefinitionJSON.put("eventType", eventDefinition.getEventType());
						eventDefinitionJSON.put("isAsync", eventDefinition.isAsync());
						eventDefinitionJSON.put("isStartEvent", eventDefinition.isStartEvent());
						eventDefinitionsArray.add(eventDefinitionJSON);
					}	
				}

				if (eventDefinitionsArray.size() > 0)
					propertiesJSON.put(key, eventDefinitionsArray);
			} else if ("errorEventDefinitions".equals(key)) {
				ArrayList<ErrorEventDefinition> errorEventDefinitions = (ArrayList<ErrorEventDefinition>) properties.get(key);
				ArrayNode errorEventDefinitionsArray = objectMapper.createArrayNode();

				if (errorEventDefinitions != null) {
					for (ErrorEventDefinition errorEventDefinition : errorEventDefinitions) {
						ObjectNode errorEventDefinitionJSON = objectMapper.createObjectNode();

						if (errorEventDefinition.getErrorCode() != null)
							errorEventDefinitionJSON.put("errorCode", errorEventDefinition.getErrorCode());
						else
							errorEventDefinitionJSON.putNull("errorCode");
						
						errorEventDefinitionJSON.put("handlerActivityId",errorEventDefinition.getHandlerActivityId());
						errorEventDefinitionsArray.add(errorEventDefinitionJSON);
					}
				}
				if (errorEventDefinitionsArray.size() > 0)
					propertiesJSON.put(key, errorEventDefinitionsArray);
			}
    	}
    	return propertiesJSON;
	}
	
	
	/**
	 * 获取池道JSON信息
	 * @param processDefinition
	 * @return
	 */
  	private ArrayNode getLaneSetJson(ProcessDefinitionEntity processDefinition) {
  		ArrayNode laneSetArray = objectMapper.createArrayNode();
  		if (CollectionUtils.isNotEmpty(processDefinition.getLaneSets())) {
  			for (LaneSet laneSet : processDefinition.getLaneSets()) {
  				ArrayNode laneArray = objectMapper.createArrayNode();
  				if (laneSet.getLanes() != null && !laneSet.getLanes().isEmpty()) {
  					for (Lane lane : laneSet.getLanes()) {
  						ObjectNode laneJSON = objectMapper.createObjectNode();
  						laneJSON.put("id", lane.getId());
  						if (StringUtils.isNotEmpty(lane.getName())) {
  							laneJSON.put("name", lane.getName());
  						} else {
  							laneJSON.put("name", "");
  						}
  						laneJSON.put("x", lane.getX());
  						laneJSON.put("y", lane.getY());
  						laneJSON.put("width", lane.getWidth());
  						laneJSON.put("height", lane.getHeight());

  						List<String> flowNodeIds = lane.getFlowNodeIds();
  						ArrayNode flowNodeIdsArray = objectMapper.createArrayNode();
  						for (String flowNodeId : flowNodeIds) {
  							flowNodeIdsArray.add(flowNodeId);
  						}
  						laneJSON.put("flowNodeIds", flowNodeIdsArray);

  						laneArray.add(laneJSON);
  					}
  				}
  				ObjectNode laneSetJSON = objectMapper.createObjectNode();
  				laneSetJSON.put("id", laneSet.getId());
  				if (StringUtils.isNotEmpty(laneSet.getName())) {
  					laneSetJSON.put("name", laneSet.getName());
  				} else {
  					laneSetJSON.put("name", "");
  				}
  				laneSetJSON.put("lanes", laneArray);

  				laneSetArray.add(laneSetJSON);
  			}
  			
  		}
  		return laneSetArray;
	}
  	/**
  	 * 获取参与流程JSON信息
  	 * @param processDefinition
  	 * @return
  	 */
  	private ObjectNode getParticipantProcessJson(ProcessDefinitionEntity processDefinition) {
  		ObjectNode participantProcessJSON = null;
  	    if (processDefinition.getParticipantProcess() != null) {
  	    	ParticipantProcess pProc = processDefinition.getParticipantProcess();

  	    	participantProcessJSON = objectMapper.createObjectNode();
  	    	participantProcessJSON.put("id", pProc.getId());
  	    	if (StringUtils.isNotEmpty(pProc.getName())) {
  	    		participantProcessJSON.put("name", pProc.getName());
  	    	} else {
  	    		participantProcessJSON.put("name", "");
  	    	}
  	    	participantProcessJSON.put("x", pProc.getX());
  	    	participantProcessJSON.put("y", pProc.getY());
  	      	participantProcessJSON.put("width", pProc.getWidth());
  	      	participantProcessJSON.put("height", pProc.getHeight());
  	      	
  	    }
  	    return participantProcessJSON;
  	}
  	
  	/**
  	 * 获取文本展示的JSON信息
  	 * @param bpmnModel
  	 * @return
  	 */
  	private ArrayNode getTextAnnotationJson(BpmnModel bpmnModel){
  		ArrayNode activityArray = objectMapper.createArrayNode();
  		for (Process process : bpmnModel.getProcesses()) {
			for (Artifact artifact : process.getArtifacts()) {
				if(artifact instanceof TextAnnotation){
					ObjectNode activityJSON = objectMapper.createObjectNode();
					GraphicInfo graphicInfo = bpmnModel.getGraphicInfo(artifact.getId());
					TextAnnotation textAnnotation = (TextAnnotation) artifact;
					activityJSON.put("activityId", artifact.getId());
					activityJSON.put("x", graphicInfo.getX());
					activityJSON.put("y", graphicInfo.getY());
					activityJSON.put("width", graphicInfo.getWidth());
					activityJSON.put("height", graphicInfo.getHeight());
					ObjectNode propertiesJSON = objectMapper.createObjectNode();
					propertiesJSON.put("type", "textAnnotation");
					propertiesJSON.put("text", textAnnotation.getText());
					activityJSON.put("properties", propertiesJSON);
					activityArray.add(activityJSON);
				}
			}
		}
  		return activityArray;
  	}
  	
  	/**
  	 * 获取Association的JSON信息
  	 * @param bpmnModel
  	 * @return
  	 */
  	private ArrayNode getAssociationJson(BpmnModel bpmnModel){
  		ArrayNode associationArray = objectMapper.createArrayNode();
  		for (Process process : bpmnModel.getProcesses()) {
			for (Artifact artifact : process.getArtifacts()) {
				if(artifact instanceof Association){
					ObjectNode activityJSON = objectMapper.createObjectNode();
					Association association = (Association) artifact;
					ArrayNode xPointArray = objectMapper.createArrayNode();
					ArrayNode yPointArray = objectMapper.createArrayNode();
					for(GraphicInfo graphicInfo: bpmnModel.getFlowLocationGraphicInfo(association.getId())){
						xPointArray.add(graphicInfo.getX());
						yPointArray.add(graphicInfo.getY());
					}
					activityJSON.put("id", association.getId());
					String flow="("+association.getSourceRef()+")--"+association.getId()+"-->("+association.getTargetRef()+")";
					activityJSON.put("flow", flow);
					activityJSON.put("xPointArray", xPointArray);
					activityJSON.put("yPointArray", yPointArray);
					associationArray.add(activityJSON);
				}
			}
		}
  		return associationArray;
  	}
  	
  	/**
  	 * 流程定义JSON信息
  	 * @param processDefinition
  	 * @param processInstanceId
  	 * @return
  	 */
  	private ObjectNode getProcessDefinitionJson(ProcessDefinition processDefinition,String processInstanceId) {
  		ObjectNode pdrJSON = objectMapper.createObjectNode();
	    if(StringUtils.isNotEmpty(processInstanceId)){
	        pdrJSON.put("id", processInstanceId);
	    }else{
	        pdrJSON.put("id", processDefinition.getId());
	    }
	    pdrJSON.put("processDefinitionId", processDefinition.getId());
	    pdrJSON.put("name", processDefinition.getName());
	    pdrJSON.put("key", processDefinition.getKey());
	    pdrJSON.put("version", processDefinition.getVersion());
	    pdrJSON.put("deploymentId", processDefinition.getDeploymentId());
	    pdrJSON.put("isGraphicNotationDefined",  processDefinition.hasGraphicalNotation());
	    return pdrJSON;
  	}
  
  	/**
  	 * 获取外嵌子流程的Map
  	 * @param processInstanceId
  	 * @return
  	 */
  	private Map<String, ObjectNode> getSubProcessInstanceMap(String processInstanceId){
  		Map<String, ObjectNode> subProcessInstanceMap = new HashMap<String, ObjectNode>();
  		List<HistoricProcessInstance> subProcessInstances = historyService.createHistoricProcessInstanceQuery().superProcessInstanceId(processInstanceId).list();
    	for (HistoricProcessInstance subProcessInstance : subProcessInstances) {
	        String subDefId = subProcessInstance.getProcessDefinitionId();
	
	        ProcessDefinition subDef = repositoryService.getProcessDefinition(subDefId);
	        
	        ObjectNode processInstanceJSON = objectMapper.createObjectNode();
	        processInstanceJSON.put("processInstanceId", subProcessInstance.getId());
	        processInstanceJSON.put("processDefinitionId", subProcessInstance.getProcessDefinitionId());
	        processInstanceJSON.put("processDefinitionKey", subDef.getKey());
	        processInstanceJSON.put("processDefinitionName", subDef.getName());
	
	        subProcessInstanceMap.put(subProcessInstance.getId(), processInstanceJSON);
    	}
    	return subProcessInstanceMap;
  	}
  
  	/**
  	 * 获取节点的变量信息
  	 * @param processInstanceId
  	 * @return
  	 */
  	private Map<String, ArrayNode> getActivityVars(String processInstanceId){
  		Map<String, ArrayNode> flowableesVars = new HashMap<String, ArrayNode>();
	 
  		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  		List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery().processInstanceId(processInstanceId).orderByTaskCreateTime().desc().list();
	  
  		for(HistoricTaskInstance historicTaskInstance :historicTaskInstances){
  			ObjectNode varJson = objectMapper.createObjectNode();
  			if("return".equals(historicTaskInstance.getDeleteReason())){
  				varJson.put("环节状态","已回退");
  			}else if(StringUtils.isNotEmpty(historicTaskInstance.getDeleteReason())){
  				varJson.put("环节状态","已结束");
  			}else{
  				varJson.put("环节状态","待处理");
  			}
		  	varJson.put("开始时间", historicTaskInstance.getStartTime()!=null?simpleDateFormat.format(historicTaskInstance.getStartTime()):"");
		  	varJson.put("结束时间", historicTaskInstance.getEndTime()!=null?simpleDateFormat.format(historicTaskInstance.getEndTime()):"");
		  	varJson.put("环节历时", historicTaskInstance.getDurationInMillis()!=null?this.formatDuration(historicTaskInstance.getDurationInMillis()):"");
		  	if(historicTaskInstance.getAssignee() != null){
	  		 	varJson.put("受理人", getDisplayName(null, historicTaskInstance.getAssignee()));
		  	}else{
	  			varJson.put("受理人", getCandicateInfo(historicTaskInstance));
		  	}
			  	
		  	if(!flowableesVars.containsKey(historicTaskInstance.getTaskDefinitionKey())){
			  	ArrayNode arrayNode = objectMapper.createArrayNode();
			  	flowableesVars.put(historicTaskInstance.getTaskDefinitionKey(), arrayNode);
		  	}
		  	flowableesVars.get(historicTaskInstance.getTaskDefinitionKey()).add(varJson);
	  	}
	  
	  	return flowableesVars;
  	}
  	
  	/**
  	 * 获取候选信息
  	 * @param taskId
  	 * @return
  	 */
  	private String getCandicateInfo(HistoricTaskInstance historicTaskInstance){
  		String result = "";
  		try{
	  		StringBuffer sbCandicateGroup = new StringBuffer();
	  		StringBuffer sbCandicateUser = new StringBuffer();
	  		if(historicTaskInstance.getEndTime() == null){
	  			List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(historicTaskInstance.getId());
		  		for(IdentityLink identityLink: identityLinks){
		  			if(identityLink.getType().equals(IdentityLinkType.CANDIDATE)){
		  				if(identityLink.getGroupId()!=null){
		  					sbCandicateGroup.append(getDisplayName(identityLink.getGroupId(), null)).append(",");
		  				}else if(identityLink.getUserId() != null){
		  					sbCandicateUser.append(getDisplayName(null,identityLink.getUserId())).append(",");
		  				}
		  			}
		  		}
	  		}else{
	  			List<HistoricIdentityLink> historicIdentityLinks = historyService.getHistoricIdentityLinksForTask(historicTaskInstance.getId());
	  			for(HistoricIdentityLink identityLink: historicIdentityLinks){
		  			if(identityLink.getType().equals(IdentityLinkType.CANDIDATE)){
		  				if(identityLink.getGroupId()!=null){
		  					sbCandicateGroup.append(getDisplayName(identityLink.getGroupId(), null)).append(",");
		  				}else if(identityLink.getUserId() != null){
		  					sbCandicateUser.append(getDisplayName(null,identityLink.getUserId())).append(",");
		  				}
		  			}
		  		}
	  		}
	  		if(sbCandicateGroup.length()>0){
	  			result += "候选范围:" + sbCandicateGroup;
	  		}
	  		if(sbCandicateUser.length()>0){
	  			result += "候选人:" + sbCandicateUser.substring(0, sbCandicateUser.length()-1);
	  		}
  		}catch (Exception e) {
  			LOGGER.error("get candicateInfo have exception",e);
		}
  		return result;
  	}
  	
  	private String getDisplayName(String groupId,String userId){
  		if(groupId!=null){
			Group group = identityService.createGroupQuery().groupId(groupId).singleResult();
			if(group!=null){
				return group.getName();
			}else{
				return groupId;
			}
		}else{
			User user = identityService.createUserQuery().userId(userId).singleResult();
			if(user!=null){
				return user.getFirstName();
			}else{
				return userId;
			}
		}
  	}
  	
  	
  	private String formatDuration(long durationInMillis){
  		durationInMillis = durationInMillis/1000;
  		int day = (int) (durationInMillis/(24*60*60));
  		int hour = (int) (durationInMillis%(24*60*60)/(60*60));
  		int min = (int)(durationInMillis%(60*60)/(60));
  		return day+"天"+hour+"小时"+min+"分钟";
  	}
}
