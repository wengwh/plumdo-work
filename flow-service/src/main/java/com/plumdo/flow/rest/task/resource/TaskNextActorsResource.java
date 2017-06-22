package com.plumdo.flow.rest.task.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.engine.FlowableIllegalArgumentException;
import org.flowable.engine.impl.identity.Authentication;
import org.flowable.engine.task.DelegationState;
import org.flowable.engine.task.IdentityLink;
import org.flowable.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.flow.rest.task.MultiKey;
import com.plumdo.flow.rest.task.TaskCompleteRequest;
import com.plumdo.flow.rest.task.TaskNextActorResponse;

import cn.starnet.flowable.engine.exception.FlowableRollBackException;
import cn.starnet.flowable.rest.variable.RestVariable;

@RestController
public class TaskNextActorsResource extends BaseTaskResource {
	
	@Autowired
	private PreCompleteTask preCompleteTask;
	
	@RequestMapping(value="/task/{taskId}/nextActors", method = RequestMethod.POST, produces="application/json", name="下一步处理人查询")
	public List<TaskNextActorResponse> nextActors(@PathVariable("taskId") String taskId,@RequestBody(required=false) TaskCompleteRequest taskCompleteRequest) {
		List<TaskNextActorResponse> nextActors = new ArrayList<TaskNextActorResponse>();
		try{
			preCompleteTask.getNextActors(taskId, taskCompleteRequest,nextActors);
		}catch (FlowableRollBackException e) {
		}
		return nextActors;
	}
	
}

//预处理任务，完成任务，获取下一步处理人信息，回滚
@Component
class PreCompleteTask extends BaseTaskResource{
	
	@Transactional(propagation = Propagation.REQUIRED)
	public void getNextActors(String taskId,TaskCompleteRequest taskCompleteRequest,List<TaskNextActorResponse> nextActors) {
	   
		Task task = getTaskFromRequest(taskId);
		
		if(task.getAssignee() == null){
			taskService.setAssignee(taskId, Authentication.getAuthenticatedUserId());
		}
		//设置任务的完成人变量
		taskExtService.saveTaskAssigneeVar(taskId);
		
   		Map<String, Object> completeVariables = new HashMap<String, Object>();
   		//设置流程变量
   		if(taskCompleteRequest != null && taskCompleteRequest.getVariables() != null){
   			for (RestVariable variable : taskCompleteRequest.getVariables()) {
   	 			if (variable.getName() == null) {
   	 				throw new FlowableIllegalArgumentException("Variable name is required.");
   	 			}
   	 			completeVariables.put(variable.getName(), restResponseFactory.getVariableValue(variable));
   	 		}
   		}
   		//设置多实例变量
   		if(taskCompleteRequest != null && taskCompleteRequest.getMultiKeys() != null){
   			for (MultiKey multiKey : taskCompleteRequest.getMultiKeys()) {
	 			if (multiKey.getName() == null) {
	 				throw new FlowableIllegalArgumentException("multiKey name is required.");
	 			}
	 			completeVariables.put(multiKey.getName(), multiKey.getValue());
	 		}
   		}
   		//判断是否是协办完成还是正常流转
   		if(task.getDelegationState()!=null && task.getDelegationState().equals(DelegationState.PENDING)){
   			if(completeVariables.isEmpty()){
   				taskService.resolveTask(taskId);
   			}else{
   				taskService.resolveTask(taskId, completeVariables);
   			}
 		}else{
 			if(completeVariables.isEmpty()){
 				taskService.complete(taskId);
 			}else{
 				taskService.complete(taskId, completeVariables);
 			}
 		}
   		
		List<Task> nextTasks = taskExtService.getNextTasks(taskId);
   	 	
		for(Task nextTask : nextTasks){
			List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(nextTask.getId());
			nextActors.add(restResponseFactory.createTaskNextActorResponse(nextTask, identityLinks));
		}
		
		throw new FlowableRollBackException("query next actor rollback data");
	}

	
	
}
