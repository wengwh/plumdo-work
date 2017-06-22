package com.plumdo.flow.rest.task.resource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.flowable.engine.impl.identity.Authentication;
import org.flowable.engine.task.DelegationState;
import org.flowable.engine.task.Task;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.flow.rest.task.MultiKey;
import com.plumdo.flow.rest.task.TaskActor;
import com.plumdo.flow.rest.task.TaskCompleteRequest;
import com.plumdo.flow.rest.task.TaskCompleteResponse;
import com.plumdo.flow.rest.task.TaskDueDate;


@RestController
public class TaskCompleteResource extends BaseTaskResource {

	@RequestMapping(value="/task/{taskId}/complete", method = RequestMethod.PUT, name="任务完成")
	@ResponseStatus(value = HttpStatus.OK)
	@Transactional(propagation = Propagation.REQUIRED)
	public List<TaskCompleteResponse> completeTask(@PathVariable("taskId") String taskId,@RequestBody(required=false) TaskCompleteRequest taskCompleteRequest) {
		
		List<TaskCompleteResponse> responses = new ArrayList<TaskCompleteResponse>();
		
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
   		if(task.getDelegationState() != null && task.getDelegationState().equals(DelegationState.PENDING)){
   			//协办的情况，把运行的任务表开始时间改成当前时间
   			taskExtService.setStartTime(taskId);
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
   	   	 	//设置任务超时时间
   	 		if(taskCompleteRequest != null && taskCompleteRequest.getNextDueDates() != null){
   	 			this.setDueDate(nextTask, taskCompleteRequest);
   	 		}
   	   	 	//设置任务候选人或组
   	 		if(taskCompleteRequest != null && taskCompleteRequest.getNextActors() != null){
   	 			this.addCandidate(nextTask, taskCompleteRequest);
   	 		}
   	 		//设置了新的属性要重新获取
   	 		TaskExt taskExt = taskExtService.getTaskExtById(nextTask.getId());
   	 		responses.add(restResponseFactory.createTaskCompleteResponse(taskExt,taskService.getIdentityLinksForTask(taskExt.getId())));
   	 	}
   	 	
		return responses;
	}
	
	private void setDueDate(Task task,TaskCompleteRequest taskCompleteRequest){
		for(TaskDueDate nextDueDate : taskCompleteRequest.getNextDueDates()){
			if(StringUtils.isEmpty(nextDueDate.getTaskDefinitionKey())
					||nextDueDate.getTaskDefinitionKey().equals(task.getTaskDefinitionKey())){
				if(StringUtils.isEmpty(nextDueDate.getMultiKey())){
					taskService.setDueDate(task.getId(), nextDueDate.getDate());
				}else{
					for (MultiKey multiKey : taskCompleteRequest.getMultiKeys()) {
						String multiVar = String.valueOf(taskService.getVariable(task.getId(), multiKey.getKey()));
						if(multiVar.equals(nextDueDate.getMultiKey())){
							taskService.setDueDate(task.getId(), nextDueDate.getDate());
							break;
						}
					}
				}
			}
		}
	}
	
	private void addCandidate(Task task,TaskCompleteRequest taskCompleteRequest){
		for(TaskActor nextActor : taskCompleteRequest.getNextActors()){
			if(StringUtils.isEmpty(nextActor.getTaskDefinitionKey())
					||nextActor.getTaskDefinitionKey().equals(task.getTaskDefinitionKey())){
				if(StringUtils.isEmpty(nextActor.getMultiKey())){
					if(nextActor.getType().equals(TaskActor.TYPE_GROUP)){
						taskService.addCandidateGroup(task.getId(), nextActor.getId());
					}else if(nextActor.getType().equals(TaskActor.TYPE_USER)){
						taskService.addCandidateUser(task.getId(), nextActor.getId());
					}	
				}else{
					for (MultiKey multiKey : taskCompleteRequest.getMultiKeys()) {
						String multiVar = String.valueOf(taskService.getVariable(task.getId(), multiKey.getKey()));
						if(multiVar.equals(nextActor.getMultiKey())){
							if(nextActor.getType().equals(TaskActor.TYPE_GROUP)){
								taskService.addCandidateGroup(task.getId(), nextActor.getId());
							}else if(nextActor.getType().equals(TaskActor.TYPE_USER)){
								taskService.addCandidateUser(task.getId(), nextActor.getId());
							}
							break;
						}
					}
				}
			}
		}
	}
}
