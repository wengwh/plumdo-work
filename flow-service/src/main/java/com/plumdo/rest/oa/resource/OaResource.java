package com.plumdo.rest.oa.resource;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.history.HistoricProcessInstance;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.plumdo.rest.oa.OaEntity;
import com.plumdo.rest.oa.OaRepository;

@RestController
public class OaResource {

	@Autowired
	protected OaRepository oaRepository;
	@Autowired
	protected RuntimeService runtimeService;
	@Autowired
	protected HistoryService historyService;
	@Autowired
	protected TaskService taskService;

	@RequestMapping(value = "/oa", method = RequestMethod.GET, produces = "application/json")
	public List<OaEntity> getOas(@RequestParam Map<String, String> requestParams) {
		List<OaEntity> oaEntities = oaRepository.findAll();
		// 不做分页，简单处理，要一个个查询流程信息较慢，可以通过视图关联
		for (OaEntity oaEntity : oaEntities) {
			List<Task> tasks = taskService.createTaskQuery().processInstanceId(oaEntity.getProcessInstanceId()).list();
			if (tasks.size() > 0) {
				// 只获取一个做标识
				oaEntity.setTaskName(tasks.get(0).getName());
			}
		}
		return oaEntities;

	}

	@RequestMapping(value = "/oa/{oaId}", method = RequestMethod.GET, produces = "application/json")
	public OaEntity getOa(@PathVariable Long oaId) {
		OaEntity oaEntity = oaRepository.findOne(oaId);
		return oaEntity;
	}

	@RequestMapping(value = "/oa/start", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(value = HttpStatus.CREATED)
	@Transactional(propagation = Propagation.REQUIRED)
	public OaEntity startOa(@RequestBody OaEntity oaEntity) {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("OA");
		oaEntity.setProcessInstanceId(processInstance.getProcessInstanceId());
		oaEntity.setCreateTime(new Timestamp(new Date().getTime()));
		oaRepository.save(oaEntity);
		return oaEntity;
	}

	@RequestMapping(value = "/oa/{oaId}/complete", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(value = HttpStatus.CREATED)
	@Transactional(propagation = Propagation.REQUIRED)
	public OaEntity completeOa(@PathVariable Long oaId) {
		OaEntity oaEntity = oaRepository.findOne(oaId);
		List<Task> tasks = taskService.createTaskQuery().processInstanceId(oaEntity.getProcessInstanceId()).list();
		for (Task task : tasks) {
			taskService.complete(task.getId());
		}
		HistoricProcessInstance processInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(oaEntity.getProcessInstanceId()).singleResult();
		if (processInstance.getEndTime() != null) {
			oaEntity.setEndTime(new Timestamp(new Date().getTime()));
			oaRepository.save(oaEntity);
		}
		return oaEntity;
	}

	@RequestMapping(value = "/oa/{oaId}/return", method = RequestMethod.POST, produces = "application/json")
	@ResponseStatus(value = HttpStatus.CREATED)
	@Transactional(propagation = Propagation.REQUIRED)
	public OaEntity returnOa(@PathVariable Long oaId) {
		OaEntity oaEntity = oaRepository.findOne(oaId);
		runtimeService.createChangeActivityStateBuilder().processInstanceId(oaEntity.getProcessInstanceId()).cancelActivityId("jlsp")// taskBefore
				.startActivityId("zzsp").changeState();
		return oaEntity;
	}

}
