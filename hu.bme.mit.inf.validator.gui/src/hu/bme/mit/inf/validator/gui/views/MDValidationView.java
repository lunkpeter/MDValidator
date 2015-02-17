package hu.bme.mit.inf.validator.gui.views;

import hu.bme.mit.inf.mdvalidator.core.MDQuerySpecificationRegistry;
import hu.bme.mit.inf.mdvalidator.core.Record;

import java.util.List;
import java.util.Set;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.UpdateValueStrategy;
import org.eclipse.core.databinding.beans.BeansObservables;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.incquery.runtime.api.IPatternMatch;
import org.eclipse.incquery.runtime.api.IQuerySpecification;
import org.eclipse.incquery.runtime.api.IncQueryMatcher;
import org.eclipse.incquery.runtime.matchers.psystem.annotations.PAnnotation;
import org.eclipse.jface.databinding.swt.SWTObservables;
import org.eclipse.jface.databinding.viewers.ViewersObservables;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.Form;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.part.ViewPart;
/**
 * 
 * View that offers a user interface for managing the MD Query specification registry
 * @author Lunk Péter
 *
 */
public class MDValidationView extends ViewPart {
	@SuppressWarnings("unused")
	private DataBindingContext m_bindingContext;

	public MDValidationView() {
	}

	FormToolkit toolkit;
	Form form;

	private Text filename;
	private Text annot;
	private Text name;
	private Text message;
	private Text severity;
	

	private SpecificationManager manager = new SpecificationManager();

	private IObservableValue selection;

	private TreeViewer viewer;

	private Composite detailsComposite;

	private SpecificationItem selected = null;


	@Override
	public void createPartControl(Composite parent) {

		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createForm(parent);
		toolkit.decorateFormHeading(form);

		form.setText("MagicDraw Pattern Repository Manager");
		Composite body = form.getBody();
		body.setLayout(new ColumnLayout());

		// PAtterns section
		Section patternsSection = toolkit.createSection(body,
				Section.TITLE_BAR | Section.EXPANDED);
		patternsSection.setText("Query Specifications");

		Tree tree = toolkit.createTree(patternsSection, SWT.FULL_SELECTION);

		patternsSection.setClient(tree);

		viewer = new TreeViewer(tree);
		viewer.setContentProvider(new ITreeContentProvider() {

			private SpecificationManager manager;

			@Override
			public void inputChanged(Viewer viewer, Object oldInput,
					Object newInput) {
				if (newInput instanceof SpecificationManager) {
					manager = (SpecificationManager) newInput;
				} else {
					manager = null;
				}
			}

			@Override
			public void dispose() {
			}

			@Override
			public boolean hasChildren(Object element) {
				return false;
			}

			@Override
			public Object getParent(Object element) {
				return null;
			}

			@Override
			public Object[] getElements(Object inputElement) {

				return manager.getAllSpecificationItems().toArray();
			}

			@Override
			public Object[] getChildren(Object parentElement) {
				return null;
			}
		});

		viewer.setLabelProvider(new SpecificationLabelProvider());
		// Expand the tree
		viewer.setAutoExpandLevel(2);
		// provide the input to the ContentProvider

		viewer.addDoubleClickListener(new IDoubleClickListener() {

			@Override
			public void doubleClick(DoubleClickEvent event) {
				TreeViewer viewer = (TreeViewer) event.getViewer();
				IStructuredSelection thisSelection = (IStructuredSelection) event
						.getSelection();
				Object selectedNode = thisSelection.getFirstElement();
				viewer.setExpandedState(selectedNode,
						!viewer.getExpandedState(selectedNode));
			}
		});

		viewer.addSelectionChangedListener(new ISelectionChangedListener() {
			   public void selectionChanged(SelectionChangedEvent event) {
			       // if the selection is empty clear the label
			       if(event.getSelection().isEmpty()) {
			    	   selected = null;
			    	   return;
			       }
			       if(event.getSelection() instanceof IStructuredSelection) {
			           IStructuredSelection selection = (IStructuredSelection)event.getSelection();
			           Object firstElement = selection.getFirstElement();
			           if (firstElement instanceof SpecificationItem){
			        	   selected = ((SpecificationItem)firstElement);
			           }			      
			       }
			   }
			});

		// details section
		Section detailsSection = toolkit.createSection(body,
				Section.TITLE_BAR | Section.EXPANDED);
		detailsSection.setText("Specification Details");
		detailsComposite = toolkit.createComposite(detailsSection);
		detailsSection.setClient(detailsComposite);

		detailsComposite.setLayout(new GridLayout(2, true));

		toolkit.createLabel(detailsComposite, "Fully Qualified Name");
		name = toolkit.createText(detailsComposite, "Fully Qualified Name");
		name.setEditable(false);

		toolkit.createLabel(detailsComposite, "Containing file path");
		filename = toolkit.createText(detailsComposite, "Containing file path");
		filename.setEditable(false);
		
		toolkit.createLabel(detailsComposite, "Annotations");
		annot = toolkit.createText(detailsComposite, "Annotations");
		annot.setEditable(false);
		
		toolkit.createLabel(detailsComposite, "Constraint Message");
		message = toolkit.createText(detailsComposite, "Constraint Message");
		message.setEditable(false);
		
		toolkit.createLabel(detailsComposite, "Severity");
		severity = toolkit.createText(detailsComposite, "Severity");
		severity.setEditable(false);
		// Layouting
		{
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.widthHint = 250;
			name.setLayoutData(gd);
		}
		{
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.widthHint = 250;
			filename.setLayoutData(gd);
		}
		{
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.widthHint = 250;

			annot.setLayoutData(gd);
		}
		
		{
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.widthHint = 250;

			message.setLayoutData(gd);
		}
		
		{
			GridData gd = new GridData(GridData.FILL_HORIZONTAL);
			gd.widthHint = 250;

			severity.setLayoutData(gd);
		}
		m_bindingContext = initDataBindings();
		// Loading content
		refreshTree();

		


	}

	@Override
	public void setFocus() {
		form.setFocus();

	}

	@Override
	public void dispose() {
		toolkit.dispose();
	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		selection = ViewersObservables.observeSingleSelection(viewer);

		IObservableValue filenameObservable = BeansObservables
				.observeDetailValue(selection, "filename", String.class);

		IObservableValue annotObservable = BeansObservables.observeDetailValue(
				selection, "annot", String.class);

		IObservableValue nameObservable = BeansObservables.observeDetailValue(
				selection, "name", String.class);
		
		IObservableValue messageObservable = BeansObservables.observeDetailValue(
				selection, "message", String.class);
		
		IObservableValue severityObservable = BeansObservables.observeDetailValue(
				selection, "severity", String.class);

		DataBindingContext dbc = new DataBindingContext();

		UpdateValueStrategy updateStrategy = new UpdateValueStrategy(false,
				UpdateValueStrategy.POLICY_UPDATE);

		dbc.bindValue(SWTObservables.observeText(annot, SWT.Modify),
				annotObservable, updateStrategy, updateStrategy);

		dbc.bindValue(SWTObservables.observeText(filename, SWT.Modify),
				filenameObservable);

		dbc.bindValue(SWTObservables.observeText(name, SWT.Modify),
				nameObservable);
		
		dbc.bindValue(SWTObservables.observeText(message, SWT.Modify),
				messageObservable);
		
		dbc.bindValue(SWTObservables.observeText(severity, SWT.Modify),
				severityObservable);

		new Label(detailsComposite, SWT.NONE);

		//
		return bindingContext;
	}

	public void refreshTree() {
		manager.clrAllSpecificationItems();

		Set<Record> specs = MDQuerySpecificationRegistry
				.getContributedQuerySpecifications();
		
		for (Record record : specs) {
			IQuerySpecification<? extends IncQueryMatcher<? extends IPatternMatch>> spec = record
					.getSpec();
			List<PAnnotation> allAnnotations = spec.getAllAnnotations();
			
			String annot = "";
			for (PAnnotation pAnnotation : allAnnotations) {
				annot += "@" + pAnnotation.getName() + " ";
			}
			
			String message = "";
			String severity = "";

			message = (String) record.getSpec().getAllAnnotations().get(0).getAllValues("message").get(0);
			severity = (String) record.getSpec().getAllAnnotations().get(0).getAllValues("severity").get(0);
			
			SpecificationItem item = new SpecificationItem(
					spec.getFullyQualifiedName(), record.getFile(), annot, message, severity);
			manager.addSpecificationItem(item);
		}

		viewer.refresh();
		viewer.setInput(manager);
		if (!manager.getAllSpecificationItems().isEmpty()) {
			viewer.setSelection(new StructuredSelection(manager
					.getAllSpecificationItems().get(0)));
		}
	}
	public SpecificationItem getSelected(){
		return selected;
	}
}
