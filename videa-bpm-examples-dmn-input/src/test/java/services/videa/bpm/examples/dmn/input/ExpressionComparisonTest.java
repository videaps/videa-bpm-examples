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
package services.videa.bpm.examples.dmn.input;

import static org.junit.Assert.*;

import org.camunda.bpm.dmn.engine.DmnDecisionTableResult;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.engine.test.ProcessEngineRule;
import org.camunda.bpm.engine.variable.VariableMap;
import org.camunda.bpm.engine.variable.Variables;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

@Deployment(resources = { "expression-comparison.dmn" })
public class ExpressionComparisonTest {

	@Rule
	public ProcessEngineRule processEngine = new ProcessEngineRule();

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void rule1() {
		Input1 input1 = new Input1("");
		String valid = null;
		Input2 input2 = new Input2(null);
		
		VariableMap variables = Variables.createVariables().putValue("input1", input1)
				.putValue("valid", valid).putValue("input2", input2);

		DmnDecisionTableResult decisionResult = processEngine.getDecisionService()
				.evaluateDecisionTableByKey("Decision_ExpressionComparison", variables);

		assertEquals("Rule 1 applies", decisionResult.getSingleResult().getEntry("message"));
	}

	@Test
	public void rule2() {
		Input1 input1 = new Input1("000");
		String valid = null;
		Input2 input2 = new Input2(null);
		
		VariableMap variables = Variables.createVariables().putValue("input1", input1)
				.putValue("valid", valid).putValue("input2", input2);

		DmnDecisionTableResult decisionResult = processEngine.getDecisionService()
				.evaluateDecisionTableByKey("Decision_ExpressionComparison", variables);

		assertEquals("Rule 2 applies", decisionResult.getSingleResult().getEntry("message"));
	}

	@Test
	public void rule3() {
		Input1 input1 = new Input1(null);
		boolean valid = false;
		Input2 input2 = new Input2(null);
		
		VariableMap variables = Variables.createVariables().putValue("input1", input1)
				.putValue("valid", valid).putValue("input2", input2);

		DmnDecisionTableResult decisionResult = processEngine.getDecisionService()
				.evaluateDecisionTableByKey("Decision_ExpressionComparison", variables);

		assertEquals("Rule 3 applies", decisionResult.getSingleResult().getEntry("message"));
	}

	@Test
	public void rule4_match() {
		Input1 input1 = new Input1("typ1");
		String valid = null;
		Input2 input2 = new Input2("typ1");
		
		VariableMap variables = Variables.createVariables().putValue("input1", input1)
				.putValue("valid", valid).putValue("input2", input2);

		DmnDecisionTableResult decisionResult = processEngine.getDecisionService()
				.evaluateDecisionTableByKey("Decision_ExpressionComparison", variables);

		assertEquals("Rule 4 applies", decisionResult.getSingleResult().getEntry("message"));
	}

	@Test
	public void rule4_difference() {
		Input1 input1 = new Input1("typ1");
		String valid = null;
		Input2 input2 = new Input2("typ2");
		
		VariableMap variables = Variables.createVariables().putValue("input1", input1)
				.putValue("valid", valid).putValue("input2", input2);

		DmnDecisionTableResult decisionResult = processEngine.getDecisionService()
				.evaluateDecisionTableByKey("Decision_ExpressionComparison", variables);

		assertNotEquals("Rule 4 applies", decisionResult.getSingleEntry());
	}


	@Test
	public void rule5() {
		Input1 input1 = new Input1(null);
		String valid = null;
		Input2 input2 = new Input2("");
		
		VariableMap variables = Variables.createVariables().putValue("input1", input1)
				.putValue("valid", valid).putValue("input2", input2);

		DmnDecisionTableResult decisionResult = processEngine.getDecisionService()
				.evaluateDecisionTableByKey("Decision_ExpressionComparison", variables);

		assertEquals("Rule 5 applies", decisionResult.getSingleEntry());
	}

	@Test
	public void none() {
		Input1 input1 = new Input1(null);
		String valid = null;
		Input2 input2 = new Input2(null);
		
		VariableMap variables = Variables.createVariables().putValue("input1", input1)
				.putValue("valid", valid).putValue("input2", input2);

		DmnDecisionTableResult decisionResult = processEngine.getDecisionService()
				.evaluateDecisionTableByKey("Decision_ExpressionComparison", variables);

		// Watch out: Rule 4 applies, as input1.typ is null.
		assertEquals("Rule 4 applies", decisionResult.getSingleEntry());
	}

}
