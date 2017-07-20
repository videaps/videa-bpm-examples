/*
 Copyright (c) 2017 Videa Project Services GmbH

 Permission is hereby granted, free of charge, to any person obtaining a copy of this software 
 and associated documentation files (the "Software"), to deal in the Software without restriction, 
 including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, 
 and/or sell copies of the Software,and to permit persons to whom the Software is furnished to do so, 
 subject to the following conditions:

 The above copyright notice and this permission notice shall be included in all copies or substantial 
 portions of the Software.

 THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT 
 NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. 
 IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, 
 WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE 
 SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/
package services.videa.bpm.examples.vaadin.admin;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;

public class AdminService implements Serializable {
	private static final long serialVersionUID = -7741958290104246176L;

	private final Logger logger = Logger.getLogger(AdminService.class.getName());

	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	private RepositoryService repositoryService = processEngine.getRepositoryService();
	private RuntimeService runtimeService = processEngine.getRuntimeService();
//	private TaskService taskService = processEngine.getTaskService();

	public List<ProcessDefinitionModel> fetchProcessDefinitions() {
		List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().list();
		logger.finer("processDefinitions=" + processDefinitions);

		List<ProcessDefinitionModel> processDefinitionModels = new ArrayList<>();
		for (ProcessDefinition processDefinition : processDefinitions) {
			processDefinitionModels.add(new ProcessDefinitionModel(processDefinition.getName(),
					processDefinition.getKey(), processDefinition.getId()));
		}

		return processDefinitionModels;
	}

	public void startProcessInstance(String processId) {
		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processId);
		logger.finer("processInstance=" + processInstance);
	}

	/**
	 * 
	 * @param processDefinitionModel
	 * @return
	 */
	public List<ProcessInstanceModel> fetchProcessInstances(ProcessDefinitionModel processDefinitionModel) {
		List<ProcessInstance> processInstances = runtimeService.createProcessInstanceQuery()
				.processDefinitionId(processDefinitionModel.getId()).active().list();
		logger.finer("processInstances=" + processInstances);

		List<ProcessInstanceModel> processInstanceModels = new ArrayList<>();
		for (ProcessInstance processInstance : processInstances) {
			processInstanceModels
					.add(new ProcessInstanceModel(processInstance.getId(), processInstance.getProcessInstanceId()));
		}

		return processInstanceModels;
	}

//	public List<Task> fetchUserTasks(ProcessInstance processInstance) {
//		List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
//		logger.finer("tasks=" + tasks);
//
//		return tasks;
//	}

}
