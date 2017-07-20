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
package services.videa.bpm.examples.vaadin;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.assertThat;
import static org.junit.Assert.assertEquals;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.task.Task;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.camunda.bpm.extension.process_test_coverage.junit.rules.TestCoverageProcessEngineRuleBuilder;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

@Deployment(resources = { "support-case.bpmn" })
public class SupportCaseTest {

	@Rule
	@ClassRule
	public static ProcessEngineRule processEngine = TestCoverageProcessEngineRuleBuilder.create().build();
	
	private RuntimeService runtimeService = null;
	private TaskService taskService = null;

	@Before
	public void setUp() throws Exception {
		runtimeService = processEngine.getRuntimeService();
		taskService = processEngine.getTaskService();
	}

	@Test
	public void testHappyPath() {
		VariableMap variables = Variables.createVariables();

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Process_SupportCaseCapture", variables);
		assertThat(processInstance).isActive();
		
		Task task = taskService.createTaskQuery().taskDefinitionKey("Task_CaptureSupportCase").singleResult();
		assertEquals("Task_CaptureSupportCase", task.getTaskDefinitionKey());
		assertEquals("Capture Support Case", task.getName());

		assertThat(processInstance).isNotEnded();
		
		taskService.setVariable(task.getId(), "userName", "ohock");
		taskService.setVariable(task.getId(), "category", "USER_GUIDE");
		
		assertEquals("ohock", taskService.getVariable(task.getId(), "userName"));
		assertEquals("USER_GUIDE", taskService.getVariable(task.getId(), "category"));
		
		taskService.complete(task.getId());
		assertThat(processInstance).isEnded();
	}

}
