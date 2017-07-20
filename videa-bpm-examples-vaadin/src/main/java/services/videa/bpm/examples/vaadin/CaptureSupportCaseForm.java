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

import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;

public class CaptureSupportCaseForm extends FormLayout {
	private static final long serialVersionUID = 1889671745530867020L;

	private TextField userName = new TextField();
	private ComboBox<CategoryEnum> category = new ComboBox<CategoryEnum>();

	private Button save = new Button("Save");
	private Button complete = new Button("Complete");

	public CaptureSupportCaseForm() {
		setSizeUndefined();
		
		HorizontalLayout buttons = new HorizontalLayout(save, complete);

		userName.setValue("ohock");
		category.setItems(CategoryEnum.values());
		addComponents(userName, category, save, category, buttons);
		
		save.addClickListener(event -> this.save());
		complete.addClickListener(event -> this.complete());
	}
	
	private void save() {
		System.out.println("save()");
	}
	
	private void complete() {
		System.out.println("complete()");
	}
	
}
