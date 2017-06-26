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
package services.videa.bpm.examples.javadelegate;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.assertThat;
import static org.junit.Assert.*;

import org.camunda.bpm.engine.RepositoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.Execution;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRuleBuilder;
import org.camunda.bpm.model.bpmn.Bpmn;
import org.camunda.bpm.model.bpmn.BpmnModelInstance;
import org.camunda.bpm.model.bpmn.instance.ServiceTask;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

public class ServiceTaskJavaDelegateTest {

	@Rule
	@ClassRule
	public static ProcessEngineRule processEngine = TestCoverageProcessEngineRuleBuilder.create().build();

	private RepositoryService repositoryService = null;
	private RuntimeService runtimeService = null;
	@SuppressWarnings("unused")
	private TaskService taskService = null;

	@Before
	public void setUp() throws Exception {
		runtimeService = processEngine.getRuntimeService();
		taskService = processEngine.getTaskService();
		repositoryService = processEngine.getRepositoryService();
	}

	@Test
	public void testHappyPath() {
		VariableMap vars = Variables.createVariables().putValue("firstName", "Oli").putValue("surName", "Hock");

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Process_ServiceTaskJavaDelegate",
				vars);
		assertThat(processInstance).isEnded();
	}

	@Test
	public void testJavaDelegate() {
		BpmnModelInstance modelInstance = Bpmn.readModelFromStream(
				getClass().getClassLoader().getResourceAsStream("service-task-java-delegate.bpmn"));

		repositoryService.createDeployment().addModelInstance("service-task-java-delegate.bpmn", modelInstance)
				.deploy();

		ServiceTask serviceTask = modelInstance.getModelElementById("Task_MapVariables");
		assertEquals("Map Variables", serviceTask.getName());
	}

	@Deployment
	public void testExpressionFieldInjection() {
		VariableMap vars = Variables.createVariables().putValue("firstName", "Oliver").putValue("surName", "Hock");

		ProcessInstance pi = runtimeService.startProcessInstanceByKey("Process_ServiceTaskJavaDelegate", vars);
		Execution execution = runtimeService.createExecutionQuery().processInstanceId(pi.getId())
				.activityId("Task_MapVariables").singleResult();

		assertEquals("Oli Hock", runtimeService.getVariable(execution.getId(), "name"));
	}
}
