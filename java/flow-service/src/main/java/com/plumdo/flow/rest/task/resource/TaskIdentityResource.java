package com.plumdo.flow.rest.task.resource;

import java.util.List;

import org.flowable.engine.common.api.FlowableIllegalArgumentException;
import org.flowable.engine.common.api.FlowableObjectNotFoundException;
import org.flowable.identitylink.api.IdentityLink;
import org.flowable.identitylink.service.IdentityLinkType;
import org.flowable.task.api.Task;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.flow.rest.task.TaskIdentityRequest;
import com.plumdo.flow.rest.task.TaskIdentityResponse;

@RestController
public class TaskIdentityResource extends BaseTaskResource{

	@RequestMapping(value="/task/{taskId}/identity", method = RequestMethod.GET, produces="application/json", name="任务候选人查询")
	public List<TaskIdentityResponse> getIdentityLinks(@PathVariable("taskId") String taskId) {
		Task task = getTaskFromRequest(taskId);
	    List<IdentityLink> identityLinks = taskService.getIdentityLinksForTask(task.getId());
	    return restResponseFactory.createTaskIdentityResponseList(identityLinks);
	}
	
	@RequestMapping(value="/task/{taskId}/identity", method = RequestMethod.POST, name="任务候选人创建")
	@ResponseStatus(value = HttpStatus.OK)
	public void createIdentityLink(@PathVariable("taskId") String taskId, @RequestBody TaskIdentityRequest taskIdentityRequest) {
		Task task = getTaskFromRequest(taskId);
	    
		validateIdentityLinkArguments(taskIdentityRequest.getIdentityId(), taskIdentityRequest.getType());
	    
	    if (TaskIdentityRequest.AUTHORIZE_GROUP.equals(taskIdentityRequest.getType())) {
	    	taskService.addGroupIdentityLink(task.getId(), taskIdentityRequest.getIdentityId(),IdentityLinkType.CANDIDATE);
	    } else if(TaskIdentityRequest.AUTHORIZE_USER.equals(taskIdentityRequest.getType())) {
	    	taskService.addUserIdentityLink(task.getId(), taskIdentityRequest.getIdentityId(),IdentityLinkType.CANDIDATE);
	    }
	}
	
	@RequestMapping(value="/task/{taskId}/identity/{type}/{identityId}", method = RequestMethod.DELETE, name="任务候选人删除")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public void deleteIdentityLink(@PathVariable("taskId") String taskId, @PathVariable("identityId") String identityId, 
		      @PathVariable("type") String type) {
		Task task = getTaskFromRequest(taskId);
	    
		validateIdentityLinkArguments(identityId, type);
		
		getIdentityLink(taskId, identityId, type);

	    if (TaskIdentityRequest.AUTHORIZE_GROUP.equals(type)) {
	    	taskService.deleteGroupIdentityLink(task.getId(), identityId,IdentityLinkType.CANDIDATE);
	    } else if(TaskIdentityRequest.AUTHORIZE_USER.equals(type)) {
	    	taskService.deleteUserIdentityLink(task.getId(), identityId,IdentityLinkType.CANDIDATE);
	    }
	}
	
	protected void validateIdentityLinkArguments(String identityId, String type) {
		if (identityId == null) {
			throw new FlowableIllegalArgumentException("IdentityId is required.");
		}
		if (type == null) {
			throw new FlowableIllegalArgumentException("Type is required.");
		}
		 
		if(!TaskIdentityRequest.AUTHORIZE_GROUP.equals(type)&&!TaskIdentityRequest.AUTHORIZE_USER.equals(type)){
			throw new FlowableIllegalArgumentException("Type is must be group or user.");
		}
	}
	 
	 protected IdentityLink getIdentityLink(String taskId,String identityId, String type) {
		 List<IdentityLink> allLinks = taskService.getIdentityLinksForTask(taskId);
		 for (IdentityLink link : allLinks) {
			 boolean rightIdentity = false;
			 if (TaskIdentityRequest.AUTHORIZE_USER.equals(type)) {
				 rightIdentity = identityId.equals(link.getUserId());
			 } else {
				 rightIdentity = identityId.equals(link.getGroupId());
			 }
		      
			 if (rightIdentity && link.getType().equals(IdentityLinkType.CANDIDATE)) {
		        return link;
			 }
		 }
		 throw new FlowableObjectNotFoundException("Could not find the requested identity link.", IdentityLink.class);
	 }
}
