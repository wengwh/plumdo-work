package com.plumdo.flow.rest.task.resource;

import java.util.List;

import org.flowable.engine.HistoryService;
import org.flowable.engine.TaskService;
import org.flowable.engine.common.api.FlowableObjectNotFoundException;
import org.flowable.engine.history.HistoricTaskInstance;
import org.flowable.engine.impl.identity.Authentication;
import org.flowable.engine.task.IdentityLink;
import org.flowable.engine.task.IdentityLinkType;
import org.flowable.engine.task.Task;
import org.flowable.engine.task.TaskInfo;
import org.springframework.beans.factory.annotation.Autowired;

import com.plumdo.flow.rest.RestResponseFactory;

public class BaseTaskResource {

	@Autowired
	protected RestResponseFactory restResponseFactory;

	@Autowired
	protected TaskService taskService;

	@Autowired
	protected HistoryService historyService;

	
	protected Task getTaskFromRequest(String taskId) {
		return getTaskFromRequest(taskId, true);
	}
	
	
	protected Task getTaskFromRequest(String taskId,boolean isAuthenticate) {
		Task task = taskService.createTaskQuery().taskId(taskId).singleResult();
		if (task == null) {
			throw new FlowableObjectNotFoundException("Could not find a run task with id '" + taskId + "'.",Task.class);
		}
		if(isAuthenticate){
			checkTaskAuthorize(task,false);
		}
		return task;
	}
	
	protected HistoricTaskInstance getHistoricTaskFromRequest(String taskId) {
		return getHistoricTaskFromRequest(taskId, true);
	}
	
	
	protected HistoricTaskInstance getHistoricTaskFromRequest(String taskId,boolean isAuthenticate) {
		HistoricTaskInstance task = historyService.createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
		if (task == null) {
			throw new FlowableObjectNotFoundException("Could not find a task with id '" + taskId + "'.",HistoricTaskInstance.class);
		}
		if(isAuthenticate){
			checkTaskAuthorize(task,true);
		}
		return task;
	}
	
	protected void checkTaskAuthorize(TaskInfo task,boolean isFinished){
		if(!BasicAuthenticationProvider.Admin.equals(Authentication.getAuthenticatedUserId())){
			if(task.getAssignee()!=null){
				if(!task.getAssignee().equals(Authentication.getAuthenticatedUserId())){
					throw new FlowableConflictException("task id " + task.getId() + " the assignee "+task.getAssignee()+" is not you.");
				}
			}else{
				/*if(!isFinished){
					boolean candidateIsNull = true;
					List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(task.getId());
					for(IdentityLink identityLink : identityLinks){
						if(identityLink.getType().equals(IdentityLinkType.CANDIDATE)){
							candidateIsNull = false;
							break;
						}
					}
					if(!candidateIsNull){
						if(taskService.createTaskQuery().taskId(task.getId()).taskCandidateUser(Authentication.getAuthenticatedUserId()).count()==0){
							throw new FlowableConflictException("task id " + task.getId() + " the candidate user is not include you.");
						}
					}
				}*/
			}
		}
	}
}
