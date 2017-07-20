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

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.camunda.bpm.engine.repository.ProcessDefinition;

import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.components.grid.SingleSelectionModel;

public class ProcessesForm extends FormLayout {
	private static final long serialVersionUID = 1889671745530867020L;

	private final Logger logger = Logger.getLogger(ProcessesForm.class.getName());

	private Grid<ProcessDefinition> processGrid = new Grid<>();
	private Button startButton = new Button("Start");

	private AdminService adminService = new AdminService();
	
	public ProcessesForm() {
		logger.finest("BEGIN");

		setSizeFull();
		
		processGrid.setCaption("Deployed Processes");
		processGrid.addColumn(ProcessDefinition::getName).setCaption("Name");
		processGrid.addColumn(ProcessDefinition::getKey).setCaption("Key");
		processGrid.addColumn(ProcessDefinition::getId).setCaption("ID");
		processGrid.setSelectionMode(SelectionMode.SINGLE);
		processGrid.setItems(fetchProcesses());

		startButton.addClickListener(event -> startProcess());

		addComponents(processGrid, startButton);

		logger.finest("END");
	}

	private Collection<ProcessDefinition> fetchProcesses() {
		List<ProcessDefinition> processDefinitions = adminService.fetchDeployedProcesses();
		return processDefinitions;
	}

	private void startProcess() {
		logger.finest("BEGIN");

		SingleSelectionModel<ProcessDefinition> singleSelectionModel = (SingleSelectionModel<ProcessDefinition>) processGrid
				.getSelectionModel();
		Optional<ProcessDefinition> optional = singleSelectionModel.getSelectedItem();
		String processId = optional.get().getKey();
		logger.finer("processId=" + processId);
		adminService.startProcessInstance(processId);
		
		logger.finest("END");
	}
}
