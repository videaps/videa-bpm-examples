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
package services.videa.bpm.examples.usertask;

import static org.camunda.bpm.engine.test.assertions.ProcessEngineAssertions.*;
import static org.junit.Assert.*;

import org.camunda.bpm.engine.FormService;
import org.camunda.bpm.engine.HistoryService;
import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.TaskService;
import org.camunda.bpm.engine.history.HistoricVariableInstance;
import org.camunda.bpm.engine.history.HistoricVariableInstanceQuery;
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

@Deployment(resources = { "usertask-junit-assert.bpmn" })
public class UserTaskJunitAssert {

	@Rule
	@ClassRule
	public static ProcessEngineRule processEngine = TestCoverageProcessEngineRuleBuilder.create().build();

	private RuntimeService runtimeService = null;
	private TaskService taskService = null;
	private FormService formService;
	private HistoryService historyService;

	@Before
	public void setUp() throws Exception {
		runtimeService = processEngine.getRuntimeService();
		taskService = processEngine.getTaskService();
		formService = processEngine.getFormService();
		historyService = processEngine.getHistoryService();
	}

	@Test
	public void happyPath() {
		Person input = new Person("Oliver", "Hock", null);
		VariableMap vars = Variables.createVariables().putValue("person", input);

		ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("Process_UserTaskJunitAssert", vars);
		Task task = taskService.createTaskQuery().processInstanceId(processInstance.getId()).singleResult();	

		Person taskPerson = (Person) taskService.getVariable(task.getId(), "person");
		assertEquals(input.toString(), taskPerson.toString());

		VariableMap formVars = formService.getTaskFormVariables(task.getId());
		Person formPerson = formVars.getValue("person", Person.class);
		formPerson.setEmail("oliver.hock@videa.services");
		formVars.putValue("person", formPerson);
		formService.submitTaskForm(task.getId(), formVars);
		
		assertThat(processInstance).isEnded();

		HistoricVariableInstanceQuery variable = historyService.createHistoricVariableInstanceQuery().variableName("person");
		HistoricVariableInstance output = variable.singleResult();
		
		Person person = new Person("Oliver", "Hock", "oliver.hock@videa.services");
		assertEquals(person.getEmail(), ((Person)output.getValue()).getEmail());

	}

}
