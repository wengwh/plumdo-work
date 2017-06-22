package com.plumdo.flow.rest.diagram.resource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.flowable.engine.HistoryService;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.history.HistoricActivityInstance;
import org.flowable.engine.history.HistoricTaskInstance;
import org.flowable.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 设置流程监控的节点状态和高亮的线
 * @author wengwh
 *
 */

@Component
public class BaseDiagramHighLighted {

	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private HistoryService historyService;

	// 0:未到环节;1:待处理;2:已结束;3:已回退;
	private String STATUS_NONE = "0";
	private String STATUS_ACTIVE = "1";
	private String STATUS_FINISH = "2";
	private String STATUS_RETURN = "3";

	public void init(ProcessDefinitionEntity processDefinition,
			String processInstanceId, List<String> highLightedFlows,
			Map<String, String> flowableesStatus) {

		Map<String, String> hisActInstMap = this.getHisActInstMap(processInstanceId);
		
		PvmActivity activityImpl = this.getStartEvent(processDefinition.getFlowablees());
		
		if (hisActInstMap != null && activityImpl != null) {
			getHighLighted(activityImpl.getOutgoingTransitions(),hisActInstMap, flowableesStatus, highLightedFlows, false,false,new ArrayList<String>());
		}
		
	}

	@SuppressWarnings("rawtypes")
	private PvmActivity getStartEvent(List flowablees) {
		for (int i = 0; i < flowablees.size(); i++) {
			PvmActivity activity = (PvmActivity) flowablees.get(i);
			if(isStartEvent(activity)){
				return activity;
			}
		}
		return null;
	}

	private boolean isStartEvent(PvmActivity activity){
		String actType = (String) activity.getProperty("type");
		if (actType != null
				&& actType.toLowerCase().startsWith("start") && actType.toLowerCase().endsWith("event")) {
			return true;
		}
		return false;
	}
	
	/**
	 * 获取历史节点Map(节点ID，节点状态)，按后执行先存
	 * @param processInstanceId
	 * @return
	 */
	private Map<String, String> getHisActInstMap(String processInstanceId) {
		Map<String, String> hisActInstMap = new LinkedHashMap<String, String>();

		List<String> highLightedFlowablees = null;
		try {
			highLightedFlowablees = runtimeService.getActiveActivityIds(processInstanceId);
		} catch (FlowableObjectNotFoundException e) {
			highLightedFlowablees = Collections.<String> emptyList();
		}

		List<HistoricTaskInstance> historicTaskInstances = historyService.createHistoricTaskInstanceQuery()
			.processInstanceId(processInstanceId).taskDeleteReason("return").list();

		List<String> returnTaskIds = new ArrayList<String>();
		for (HistoricTaskInstance historicTaskInstance : historicTaskInstances) {
			returnTaskIds.add(historicTaskInstance.getId());
		}

		List<HistoricActivityInstance> hisActInstList = historyService.createHistoricActivityInstanceQuery()
			.processInstanceId(processInstanceId).orderByHistoricActivityInstanceStartTime().desc().orderByActivityId().desc().list();

		// 按ID从大到小排序，数据库id是char类型，排序只能用代码
		Collections.sort(hisActInstList,new Comparator<HistoricActivityInstance>() {
					public int compare(HistoricActivityInstance b1,HistoricActivityInstance b2) {
						if (Long.parseLong(b1.getId()) < Long.parseLong(b2.getId())) {
							return 1;
						} else {
							return -1;
						}
					}
				});
 
		for (HistoricActivityInstance hisActInst : hisActInstList) {
			if (!hisActInstMap.containsKey(hisActInst.getActivityId())) {
				if (highLightedFlowablees.contains(hisActInst.getActivityId())) {
					hisActInstMap.put(hisActInst.getActivityId(), STATUS_ACTIVE);
				} else if (returnTaskIds.contains(hisActInst.getTaskId())) {
					hisActInstMap.put(hisActInst.getActivityId(), STATUS_RETURN);
				} else{
					hisActInstMap.put(hisActInst.getActivityId(), STATUS_FINISH);
				}
			}
		}
		return hisActInstMap;
	}
	
	
	/**
	 * 
	 * 对线集合进行排序，按执行时间升序
	 * @param pvmTransitions
	 * @param hisActInstMap
	 * @return
	 */
	private List<PvmTransition> sortTransition(List<PvmTransition> pvmTransitions,LinkedMap hisActInstMap){
		List<PvmTransition> newPvmTransitions = new ArrayList<PvmTransition>();
		for (PvmTransition transition : pvmTransitions) {
			PvmActivity desActivity = transition.getDestination();
			int insertIndex = newPvmTransitions.size();
			if(hisActInstMap.containsKey(desActivity.getId())){
				for(int i=0;i<newPvmTransitions.size();i++){
					PvmTransition newPvmTransition = newPvmTransitions.get(i); 
					if(hisActInstMap.indexOf(newPvmTransition.getDestination().getId())<hisActInstMap.indexOf(desActivity.getId())){
						insertIndex = i;
						break;
					}
				}
			}
			newPvmTransitions.add(insertIndex,transition);
		}
		return newPvmTransitions;
	}
	
	/**
	 * 设置高亮和节点状态
	 * @param pvmTransitions
	 * @param hisActInstMap
	 * @param actStatusMap
	 * @param highLightedFlows
	 * @param isActive 
	 * @param isInclude  是否是包含网关未运行分支
	 * @param includeActId 包含网关中未运行分支的节点id
	 */
	private void getHighLighted(List<PvmTransition> pvmTransitions,
			Map<String, String> hisActInstMap,
			Map<String, String> actStatusMap, List<String> highLightedFlows,
			boolean isActive, boolean isInclude,List<String> includeActId) {
		
		LinkedMap linkedMap = new LinkedMap(hisActInstMap);

		pvmTransitions = sortTransition(pvmTransitions, linkedMap);
		
		for (PvmTransition transition : pvmTransitions) {
			boolean subActive = isActive;
			boolean subInclude = isInclude;
			PvmActivity desActivity = transition.getDestination();
			PvmActivity sourActivity = transition.getSource();
			String oldStatus = actStatusMap.get(desActivity.getId());
			String newStatus = null;
			//历史节点存在，同时非并发节点不能出现二次遍历，有可能非并发节点有多条线进入，在前面已经优先遍历实际走的那条
			if (hisActInstMap.containsKey(desActivity.getId())&&(StringUtils.isEmpty(oldStatus)||isParallel(desActivity))) {
				//如果前节点状态是运行
				if (isActive) {
					newStatus = STATUS_RETURN;
				} else {
					// 判断并行网关，多个分支运行结束，其他分支回退到聚合点，在并行开始不包括上次运行完成的分支的情况，通过比较顺序判断是否是回退的节点
					if (linkedMap.containsKey(sourActivity.getId())&&linkedMap.indexOf(sourActivity.getId()) < linkedMap.indexOf(desActivity.getId())) {
						newStatus = STATUS_RETURN;
					} else {
						newStatus = hisActInstMap.get(desActivity.getId());
					}
				}
			} else {
				newStatus = STATUS_NONE;
			}
			
			//开始节点加入状态，开始节点是方法的入口，没有设置状态
			if(isStartEvent(sourActivity)){
				if(STATUS_RETURN.equals(newStatus)){
					actStatusMap.put(sourActivity.getId(), STATUS_RETURN);
				}else{
					actStatusMap.put(sourActivity.getId(), STATUS_FINISH);
				}
			}
			
			if(!actStatusMap.containsKey(desActivity.getId())){
				actStatusMap.put(desActivity.getId(), newStatus);
			}
			
			
			//添加高亮线，源节点是完成状态目标节点是完成或者运行状态
			if(isHighLight(actStatusMap.get(sourActivity.getId()), newStatus)){
				highLightedFlows.add(transition.getId());
			}
			
			
			subActive = isActive(newStatus);
			
			if(isParallel(sourActivity) && isInclude(actStatusMap.get(sourActivity.getId()), newStatus)){
				subInclude = true;
			}
			
			if(subInclude){
				if(isParallel(sourActivity)){
					includeActId.add(sourActivity.getId());
				}
				includeActId.add(desActivity.getId());
			}
			
			//内嵌子流程遍历
			if ("subProcess".equals(desActivity.getProperty("type"))) {
				this.getHighLighted(this.getStartEvent(desActivity.getFlowablees()).getOutgoingTransitions(),
						hisActInstMap, actStatusMap, highLightedFlows,subActive, false,includeActId);
			}
			
			//判断是否是并发节点，如果是并发节点要等待前面的节点全部遍历完判断状态
			if(isParallel(desActivity)){
				List<String> statusList = getParallelList(desActivity, actStatusMap, includeActId);
				
				if(statusList!=null){
					if(hisActInstMap.containsKey(desActivity.getId())){
						if(statusList.contains(STATUS_FINISH)){
							if(statusList.contains(STATUS_NONE)||statusList.contains(STATUS_ACTIVE)||statusList.contains(STATUS_RETURN)){
								actStatusMap.put(desActivity.getId(), STATUS_ACTIVE);
							}else{
								actStatusMap.put(desActivity.getId(), STATUS_FINISH);
							}
						}else{
							actStatusMap.put(desActivity.getId(), STATUS_RETURN);
						}
					}else{
						actStatusMap.put(desActivity.getId(), STATUS_NONE);
					}
					
					subActive = isActive(actStatusMap.get(desActivity.getId()));
					
					//多级网关嵌套，判断网关前面的节点是否有存在运行的分支
					if(statusList.isEmpty()){
						subInclude = true;
					}else{
						subInclude = false;
					}
					
					this.getHighLighted(desActivity.getOutgoingTransitions(), hisActInstMap, actStatusMap, highLightedFlows, subActive,subInclude,includeActId);
				}
				
			}else if (StringUtils.isEmpty(oldStatus)) {
				//如果非并发节点，判断是否遍历过
				this.getHighLighted(desActivity.getOutgoingTransitions(), hisActInstMap, actStatusMap, highLightedFlows, subActive,subInclude,includeActId);
			}
			
		}
	}
	
	
	private List<String> getParallelList(PvmActivity desActivity,Map<String, String> actStatusMap,List<String> includeActId){
		List<String> statusList = new ArrayList<String>();
		for (PvmTransition pvmTransition : desActivity.getIncomingTransitions()) {
			if(!actStatusMap.containsKey(pvmTransition.getSource().getId())){
				return null;
			}else if(!includeActId.contains(pvmTransition.getSource().getId())){
				statusList.add(actStatusMap.get(pvmTransition.getSource().getId()));
			}
		}
		return statusList;
	}
	
	private boolean isHighLight(String sourStatus,String desStatus){
		//添加高亮线，源节点是完成状态目标节点是完成或者运行状态
		if (STATUS_FINISH.equals(sourStatus)
				&& (STATUS_FINISH.equals(desStatus) || STATUS_ACTIVE.equals(desStatus))) {
			return true;
		}else{
			return false;
		}
		
	}
	
	private boolean isInclude(String sourStatus,String desStatus){
		if(STATUS_FINISH.equals(sourStatus)
				&& (STATUS_NONE.equals(desStatus)||STATUS_RETURN.equals(desStatus))){
			return true;
		}else{
			return false;
		}
	}
	
	private boolean isActive(String status){
		//如果是运行状态，其后面的节点有存在全部为回退状态
		if (STATUS_ACTIVE.equals(status) || STATUS_RETURN.equals(status)) {
			return true;
		}else{
			return false;
		}
	}
	
	private boolean isParallel(PvmActivity pvmActivity){
		if("inclusiveGateway".equals(pvmActivity.getProperty("type"))
				||"parallelGateway".equals(pvmActivity.getProperty("type"))){
			return true;
		}else{
			return false;
		}
	}
}
