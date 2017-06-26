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
package services.videa.bpm.dmn.implementations;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.value.StringValue;

public class JavaClassDelegate implements JavaDelegate {

	public void execute(DelegateExecution execution) throws Exception {
		System.out.println(breadCrump(new Exception()));
		
		String inParam1 = (String) execution.getVariable("inParam1");
		StringValue inParam2 = execution.getVariableTyped("inParam2");
		
		System.out.println("inParam1="+inParam1);
		System.out.println("inParam2="+inParam2.getValue());
		
		String concat = inParam1 + " " + inParam2.getValue();
		execution.setVariable("outParam", concat);
		execution.setVariable("partialSentence", "is a name.");
	}

	private String breadCrump(Exception exception) {
		StackTraceElement elem = exception.getStackTrace()[0];
		String method = elem.getClassName()+"."+elem.getMethodName();
		return method;
	}

}
