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

import java.util.Optional;
import java.util.logging.Logger;

import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;

import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.Grid;
import com.vaadin.ui.Grid.SelectionMode;
import com.vaadin.ui.components.grid.SingleSelectionModel;

public class ProcessesForm extends FormLayout {
	private static final long serialVersionUID = 1889671745530867020L;

	private final Logger logger = Logger.getLogger(ProcessesForm.class.getName());

	private Grid<ProcessDefinitionModel> processDefinitionGrid = new Grid<>();
	private Button startButton = new Button("Start");

	private Grid<ProcessInstanceModel> processInstanceGrid = new Grid<>();
	private Button updateButton = new Button("Update");

	private AdminService adminService = new AdminService();

	public ProcessesForm() {
		setSizeFull();

		processDefinitionGrid.setCaption("Process Definitions");
		processDefinitionGrid.addColumn(ProcessDefinitionModel::getName).setCaption("Name");
		processDefinitionGrid.addColumn(ProcessDefinitionModel::getKey).setCaption("Key");
		processDefinitionGrid.addColumn(ProcessDefinitionModel::getId).setCaption("ID");
		processDefinitionGrid.setSelectionMode(SelectionMode.SINGLE);
		processDefinitionGrid.setItems(adminService.fetchProcessDefinitions());
		
		startButton.addClickListener(event -> startProcess());
		 
		addComponents(processDefinitionGrid, startButton);

		processInstanceGrid.setCaption("Process Instances");
		processInstanceGrid.addColumn(ProcessInstanceModel::getId).setCaption("ID");
		processInstanceGrid.addColumn(ProcessInstanceModel::getProcessInstanceId).setCaption("Process Instance");
		processInstanceGrid.setSelectionMode(SelectionMode.SINGLE);
		
		updateButton.addClickListener(event -> updateProcessInstances());
		
		addComponents(processInstanceGrid, updateButton);

	}

	/*
	 * Starts the process from selected item of grid.
	 */
	private void startProcess() {
		SingleSelectionModel<ProcessDefinitionModel> processDefinitionSelection = (SingleSelectionModel<ProcessDefinitionModel>) processDefinitionGrid
				.getSelectionModel();
		Optional<ProcessDefinitionModel> optional = processDefinitionSelection.getSelectedItem();
		String processId = optional.get().getKey();
		adminService.startProcessInstance(processId);
	}

	/*
	 * Updates the process instances by using selected process definition of
	 * process definition grid.
	 */
	private void updateProcessInstances() {
		SingleSelectionModel<ProcessDefinitionModel> processDefinitionSelection = (SingleSelectionModel<ProcessDefinitionModel>) processDefinitionGrid
				.getSelectionModel();
		ProcessDefinitionModel processDefinitionModel = processDefinitionSelection.getSelectedItem().get();
		processInstanceGrid.setItems(adminService.fetchProcessInstances(processDefinitionModel));
	}

}
