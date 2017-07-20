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

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.ProcessEngines;
import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;

public class AdminService {
	private final Logger logger = Logger.getLogger(AdminService.class.getName());
	
	private ProcessEngine processEngine = ProcessEngines.getDefaultProcessEngine();
	private RepositoryService repositoryService = processEngine.getRepositoryService();
	private RuntimeService runtimeService = processEngine.getRuntimeService();
	
	public List<ProcessDefinition> fetchDeployedProcesses() {
		logger.finest("BEGIN");
		
		List<ProcessDefinition> processDefinitions = repositoryService.createProcessDefinitionQuery().list();
		
		if(logger.isLoggable(Level.FINER)) {
			for(ProcessDefinition processDefinition : processDefinitions) {
				logger.finer("processDefinition=" + processDefinition);
				logger.finer("name="+processDefinition.getName());
				logger.finer("key=" + processDefinition.getKey());
				logger.finer("id=" + processDefinition.getId());
			}
		}

		logger.finest("END");
		return processDefinitions;
	}
	
	public void startProcessInstance(String processId) {
		logger.finest("BEGIN");

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processId);
		logger.finer("processInstance="+processInstance);

		logger.finest("END");
	}
}
