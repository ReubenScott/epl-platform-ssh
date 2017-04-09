/*package com.soak.util;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.jbpm.api.Execution;
import org.jbpm.api.ExecutionService;
import org.jbpm.api.ProcessDefinition;
import org.jbpm.api.ProcessEngine;
import org.jbpm.api.ProcessInstance;
import org.jbpm.api.RepositoryService;
import org.jbpm.api.TaskQuery;
import org.jbpm.api.TaskService;
import org.jbpm.api.task.Task;
import org.jbpm.pvm.internal.env.EnvironmentFactory;
import org.jbpm.pvm.internal.env.EnvironmentImpl;
import org.jbpm.pvm.internal.model.Activity;
import org.jbpm.pvm.internal.model.ActivityImpl;
import org.jbpm.pvm.internal.model.ExecutionImpl;
import org.jbpm.pvm.internal.model.ProcessDefinitionImpl;
import org.jbpm.pvm.internal.model.Transition;
import org.jbpm.pvm.internal.model.TransitionImpl;
import org.jbpm.pvm.internal.task.TaskImpl;

import com.jinf.flow.domain.Flow;
import com.jinf.flow.domain.Node;
import com.jinf.framework.action.BaseAction;

@SuppressWarnings("unchecked")
public class JbpmUtil {
	
//	private static Configuration configuration = new Configuration();
	private ProcessEngine processEngine = (ProcessEngine) (BaseAction.applicationContext.getBean("processEngine"));
	private RepositoryService repositoryService = processEngine.getRepositoryService();
	private ExecutionService executionService = processEngine.getExecutionService();
	private TaskService taskService = processEngine.getTaskService();
//	private static HistoryService historyService = processEngine.getHistoryService(); 
//	private ManagementService managementService = processEngine.getManagementService();

	
	public String deploy(Long flowId, Document jbpmDoc) throws Exception {
		jbpmDoc.setXMLEncoding("GBK");
		return repositoryService.createDeployment().addResourceFromString(flowId+".jpdl.xml", jbpmDoc.asXML()).deploy();
	}

	public ProcessInstance startFlow(Flow flow) {
//		EnvironmentImpl e = EnvironmentImpl.getCurrent();
		return executionService.startProcessInstanceByKey(String.valueOf(flow.getId()));
	}
	
	public ProcessInstance startFlow(Flow flow, Map params) {
//		EnvironmentImpl e = EnvironmentImpl.getCurrent();
		return executionService.startProcessInstanceByKey(String.valueOf(flow.getId()), params);
	}
	
	public void complete(String taskId) {
		taskService.completeTask(taskId);
	}
	
	public void endFlow(String instanceId) {
		executionService.endProcessInstance(instanceId, Execution.STATE_ENDED);
	}
	
	public void setVariables(String instanceId, Map<String, Object> variables) {
		executionService.setVariables(instanceId, variables);
	}
	
	public void setVariable(String instanceId, String key, Object value) {
		executionService.setVariable(instanceId, key, value);
	}
	
	public Object getVariable(String instanceId, String key) {
		return executionService.getVariable(instanceId, key);
	}
	
	public void clearVariable(String instanceId, String key) {
		executionService.setVariable(instanceId, key, "");
	}
	
	*//**
	 * 格式 name=value,name=value
	 * @param varString
	 * @throws Exception
	 *//*
	public void setVariables(String instanceId, String varString) {
		
		System.out.println(">>>>>>>>>>>>>>> varString:"+varString);
		
		String[] s = varString.split(",");
		for(String s2:s) {
			String s3 = s2.trim();
			if(s3.length()>2) {
				String[] s4 = s3.split("=");
				if(s4.length==2) {
					System.out.println(s4[0]+","+s4[1]);
					setVariable(instanceId, s4[0], s4[1]);
				}
			}
		}
	}
	
	public TaskImpl findTask(String instanceId, String taskName) {
		TaskQuery taskQuery = taskService.createTaskQuery();
		List<Task> taskList = taskQuery.processInstanceId(instanceId).list();
		
		for(Task task : taskList) {
			if(task.getName().equals(taskName))
				return (TaskImpl)task;
		}
		return null;
	}
	
	public TaskImpl findTaskById(String instanceId, String taskId) {
		TaskQuery taskQuery = taskService.createTaskQuery();
		List<Task> taskList = taskQuery.processInstanceId(instanceId).list();
		
		for(Task task : taskList) {
			if(task.getId().equals(taskId))
				return (TaskImpl)task;
		}
		return null;
	}
	
	public List<Task> findTasks(String instanceId) {
		TaskQuery taskQuery = taskService.createTaskQuery();
		List<Task> taskList = taskQuery.processInstanceId(instanceId).list();
		return taskList;
	}
	
	public void signal(String executionId) {
		executionService.signalExecutionById(executionId);
	}
	
	
	
	private ActivityImpl getTaskActivity(ActivityImpl current) {
		List<Transition> transitions = current.getIncomingTransitions();
		TransitionImpl transition = (TransitionImpl)transitions.get(0);
		ActivityImpl preActivity = transition.getSource();
		if(preActivity.getType()=="task") {
			return preActivity;
		}
		else {
			return getTaskActivity(preActivity);
		}

	}
	
	*//**
	 * 取出未分配处理人或允许选择处理人的节点
	 * @param currentNode
	 * @param flowXml
	 * @param resultList
	 * @throws Exception
	 *//*
	public void getNextNodes(String currentNode, String flowXml, List<Node> resultList) {
		
		try {
			SAXReader saxReader = new SAXReader();
			Document document =saxReader.read(new ByteArrayInputStream(flowXml.getBytes("UTF-8")));
			Element root = document.getRootElement();
			
			//如果没有传入当前节点，则认为是开始节点，取得开始节点以下的节点
			Element currentElement;
			if(currentNode==null) {
				currentElement = root.element("start");
			} else {
				currentElement = getElementById(root, currentNode);
			}
			if(currentElement!=null) {
				List<Element> transitions = currentElement.elements("transition");
				for(Element transition:transitions) {
					Element toElement = getElementById(root, transition.attributeValue("to"));
					if(toElement!=null&&(toElement.getName().equals("task")||toElement.getName().equals("state"))) {
						Node node = new Node();
						node.setName(toElement.attributeValue("alias"));
						node.setNodeXmlId(toElement.attributeValue("id"));
						resultList.add(node);
					}
					else if(toElement!=null) {
						getNextNodes(toElement.attributeValue("id"), flowXml, resultList);
					}
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public Node getNextNode(String currentNode, String flowXml) {
		try {
			SAXReader saxReader = new SAXReader();
			Document document =saxReader.read(new ByteArrayInputStream(flowXml.getBytes("UTF-8")));
			Element root = document.getRootElement();
			
			//如果没有传入当前节点，则认为是开始节点，取得开始节点以下的节点
			Element currentElement;
			if(currentNode==null) {
				currentElement = root.element("start");
			} else {
				currentElement = getElementById(root, currentNode);
			}
			if(currentElement!=null) {
				List<Element> transitions = currentElement.elements("transition");
				
				for(Element transition:transitions) {
					Element toElement = getElementById(root, transition.attributeValue("to"));
					Node node = new Node();
					node.setName(toElement.attributeValue("alias"));
					node.setNodeXmlId(toElement.attributeValue("id"));
					return node;
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public void getNotAllowChooserNextNodes(String currentNode, String flowXml, List<Node> resultList) throws Exception {
		SAXReader saxReader = new SAXReader();
		Document document =saxReader.read(new ByteArrayInputStream(flowXml.getBytes("UTF-8")));
		Element root = document.getRootElement();
		
		//如果没有传入当前节点，则认为是开始节点，取得开始节点以下的节点
		Element currentElement;
		if(currentNode==null) {
			currentElement = root.element("start");
		} else {
			currentElement = getElementById(root, currentNode);
		}
		if(currentElement!=null) {
			List<Element> transitions = currentElement.elements("transition");
			for(Element transition:transitions) {
				Element toElement = getElementById(root, transition.attributeValue("to"));
				if(toElement!=null&&toElement.getName().equals("task")) {
					Node node = new Node();
					node.setName(toElement.attributeValue("alias"));
					node.setNodeXmlId(toElement.attributeValue("id"));
					resultList.add(node);
				}
				else if(toElement!=null) {
					getNotAllowChooserNextNodes(toElement.attributeValue("id"), flowXml, resultList);
				}
			}
		}
	}

	public void createTask(String deployId, String instanceId, String taskName) throws Exception {
		
//		DbSessionImpl dbSession = (DbSessionImpl) EnvironmentImpl.getFromCurrent(DbSession.class);
//		Session session = dbSession.getSession();
		EnvironmentImpl env = ((EnvironmentFactory)processEngine).openEnvironment();
		Session session = ((SessionFactory)BaseAction.applicationContext.getBean("sessionFactory")).getCurrentSession();
//		Transaction tx = session.beginTransaction();
		try {
			
			TaskQuery taskQuery = taskService.createTaskQuery();
			List<Task> taskList = taskQuery.processInstanceId(instanceId).list();
			
			List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployId).list();
			
			ActivityImpl targetActivity = null;
			for(ProcessDefinition pd : list) {
				ProcessDefinitionImpl pdi = (ProcessDefinitionImpl)pd;
				targetActivity = pdi.getActivity(taskName);
				System.out.println(targetActivity.getName());
			}
			
			//检索出本节点以下所有任务，仅仅撤销本节点下去的任务
			List<Transition> transitions = new ArrayList<Transition>();
			getAllOutgoing(targetActivity, transitions);
			
			List<Task> needHandleTask = new ArrayList<Task>();
			for(Transition transition:transitions) {
				for(Task task:taskList) {
					if(	transition.getDestination().getName().equals(task.getName()) ) {
						needHandleTask.add(task);
					}
				}
			}

			for(int i=0; i<needHandleTask.size(); i++) {
				TaskImpl taskImpl = (TaskImpl)taskList.get(i);
				ExecutionImpl execution = (ExecutionImpl)taskImpl.getExecution();
				ExecutionImpl parent = execution.getParent();
				if(parent!=null) {
					ActivityImpl activity = execution.getActivity();
					ActivityImpl preActivity = getTaskActivity(activity);
					if(parent.getState()!=Execution.STATE_ACTIVE_ROOT) {
						parent.setState(Execution.STATE_ACTIVE_ROOT);
						parent.setActivity(preActivity);
						
						parent.execute(preActivity);
						
						SQLQuery query = session.createSQLQuery("delete from jbpm4_hist_actinst where execution_='"+execution.getParent().getId()+"'");
						query.executeUpdate();
					}
					SQLQuery query = session.createSQLQuery("delete from jbpm4_hist_actinst where execution_='"+execution.getId()+"'");
					query.executeUpdate();
					execution.getParent().removeExecution(execution);
					session.delete(execution);
				}
				else {
//					ActivityImpl activity = execution.getActivity();
//					ActivityImpl preActivity = getTaskActivity(activity);
//					execution.setActivity(preActivity);
					execution.execute(targetActivity);
//					execution.getParent().removeExecution(execution);
//					session.delete(execution);
					SQLQuery query = session.createSQLQuery("delete from jbpm4_hist_actinst where execution_='"+execution.getId()+"'");
					query.executeUpdate();
				}

				session.delete(taskImpl);
				SQLQuery query = session.createSQLQuery("delete from jbpm4_hist_task where dbid_='"+taskImpl.getDbid()+"'");
				query.executeUpdate();
			}
//			tx.commit();

		}
		catch(Exception e) {
//			tx.rollback();
			e.printStackTrace();
			throw e;
		}
		finally {
			env.close();
		}
	}
	
	public void getAllOutgoing(Activity activity, List<Transition> transitions) {
		List<Transition> childs = activity.getOutgoingTransitions();
		
		for(Transition transition:childs) {
			
			boolean exist = false;
			for(Transition temp:transitions) {
				if(transition.getDestination().equals(temp.getDestination())) {
					exist = true;
					break;
				}
			}
			if(!exist) {
				transitions.add(transition);
				getAllOutgoing(transition.getDestination(), transitions);
			}
		}
	}
	
	public Element getElementById(Element root, String id) {
		List<Element> children = root.elements();
		for(Element child:children) {
			String idXml = child.attributeValue("id");
			if(idXml!=null&&idXml.equals(id))
				return child;
		}
		return null;
	}
	
	*//**
	 * 任务自动完成，直到指定任务再停下
	 * @param instanceId
	 * @param taskName
	 * @return
	 *//*
//	public String autoComplete(String instanceId, String taskName) {
//		TaskQuery taskQuery = taskService.createTaskQuery();
//		List<Task> taskList = taskQuery.processInstanceId(instanceId).list();
//
//		for(int i=0; i<taskList.size(); i++) {
//			Task task = taskList.get(i);
//			if(!task.getName().equals(taskName)) {
//				taskService.completeTask(task.getId());
//				jumpTask(instanceId, taskName);
//			}
//			else
//				return task.getId();
//
//		}
//		
//		return null;
//	}
	
	*//**
	 * 所有指定任务之前的任务全部不做，直接跳到指定任务（仅支持跳转到无分支任务）
	 * @param instanceId
	 * @param taskName
	 * @return
	 *//*
	public String jumpTask(String deployId, String instanceId, String taskName) {

		TaskQuery taskQuery = taskService.createTaskQuery();
		List<Task> taskList = taskQuery.processInstanceId(instanceId).list();

		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().deploymentId(deployId).list();
		
		ActivityImpl temp = null;
		for(ProcessDefinition pd : list) {
			ProcessDefinitionImpl pdi = (ProcessDefinitionImpl)pd;
			temp = pdi.getActivity(taskName);
			System.out.println(temp.getName());
		}
		
		ExecutionImpl execution = null;
		for(int i=1; i<taskList.size(); i++) {
			Task task = taskList.get(i);
			executionService.deleteProcessInstance(task.getExecutionId());
		}
		TaskImpl task = (TaskImpl)taskList.get(0);
		execution = (ExecutionImpl)executionService.findExecutionById(task.getExecutionId());
		execution.setActivity(temp);
		execution.setName(taskName);
		EnvironmentImpl env = ((EnvironmentFactory)processEngine).openEnvironment();
		execution.execute(temp);
		env.close();
//		EnvironmentImpl env = ((EnvironmentFactory)processEngine).openEnvironment();
//		EnvironmentImpl env = EnvironmentImpl.getCurrent();
//		Session dbSession = EnvironmentImpl.getFromCurrent(Session.class, true);
//		Session session = dbSession.getSession();
		Session session = ((SessionFactory)BaseAction.applicationContext.getBean("sessionFactory")).getCurrentSession();
		Transaction tx = session.beginTransaction();
		
		session.delete(task);
		session.update(execution);
		tx.commit();
		session.flush();
//		dbSession.close();
//		env.close();
		return null;
	}
	
	public static JbpmUtil getInstance() {
		JbpmUtil ju = new JbpmUtil();
		SessionFactory sf = (SessionFactory)BaseAction.applicationContext.getBean("sessionFactory");
		Session ses = sf.getCurrentSession();
	
		//ju.processEngine.close();
//		ProcessEngineImpl e;
		ju.processEngine.setHibernateSession(ses);
		return ju;
	}
	
	public static void main(String args[]) throws Throwable {
		
//		TaskServiceImpl s;
		
		
		
//		Flow flow = new Flow();
//		flow.setId(2109l);
//		ProcessInstance pi = JbpmUtil.startFlow(flow);
//		JbpmUtil.setVariable(pi.getId(), "htFlag", "NO");
//		JbpmUtil.setVariable(pi.getId(), "hqFlag", "NO");
//		System.out.println(pi.getId());
//
//		String taskId = JbpmUtil.jumpTask(pi.getId(), "2982762446");
		JbpmUtil.getInstance().jumpTask("3720031", "2109.3790001",  "71045489632");

//		JbpmUtil.setVariable("2109.3000001.3000006", "htFlag", "NO");
//		JbpmUtil.setVariable("2109.3000001.3000006", "hqFlag", "NO");
//		JbpmUtil.complete("3620002");
//		JbpmUtil.jumpTask("3720031", "2109.3730001", "2942255835");
		
	}
}
*/