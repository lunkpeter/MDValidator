package hu.bme.mit.inf.mdvalidator.magicdrawmodelprovider;

import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import com.nomagic.magicdraw.annotation.Annotation;
import com.nomagic.magicdraw.annotation.AnnotationManager;
import com.nomagic.magicdraw.core.Application;
import com.nomagic.magicdraw.core.Project;
import com.nomagic.magicdraw.integrations.eclipse.rcp.EclipseUMLPlugin;
import com.nomagic.magicdraw.openapi.uml.SessionManager;
import com.nomagic.magicdraw.uml.BaseElement;
import com.nomagic.uml2.ext.jmi.helpers.StereotypesHelper;
import com.nomagic.uml2.ext.magicdraw.auxiliaryconstructs.mdmodels.Model;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.Element;
import com.nomagic.uml2.ext.magicdraw.classes.mdkernel.EnumerationLiteral;
import com.nomagic.uml2.ext.magicdraw.mdprofiles.Stereotype;

/**
 * Class repsonsible for extracting the magicdraw model to be validated, and
 * adds annotation to the faulty elements
 * 
 * @author Lunk Péter
 * 
 */
@SuppressWarnings("deprecation")
public class MagicDrawModelProvider {

	private static MagicDrawModelProvider instance = null;

	public static MagicDrawModelProvider getInstance() {
		if (instance == null) {
			instance = new MagicDrawModelProvider();
		}
		return instance;
	}
	/**
	 * returns the active MD model
	 * @return - the active MD model
	 */
	public Resource GetModel() {

		EclipseUMLPlugin plugin = EclipseUMLPlugin.getEclipseUMLPlugin();

		plugin.startMagicDraw();

		Project project = Application.getInstance().getProjectsManager()
				.getActiveProject();

		if (project == null) {
			project = Application.getInstance().getProjectsManager()
					.createProject();
		}

		// getting project model
		Model model = project.getModel();
		model = project.getModel();
		if (model != null) {
			Collection<Element> elems = model.getOwnedElement();

			Stereotype BSStereotype = StereotypesHelper.getStereotype(project,
					"auxiliaryResource");

			try {
				for (Element element : elems) {
					if (StereotypesHelper.hasStereotype(element, BSStereotype)
							&& (!element.getHumanName().contains("SysML"))) {
						model.getOwnedElement().remove(element);
					}
				}
			} catch (Exception e) {
			}

			elems = model.getOwnedElement();
			for (Element element : elems) {
				if (StereotypesHelper.hasStereotype(element, BSStereotype)
						&& (!element.getHumanName().contains("SysML"))) {
					model.getOwnedElement().remove(element);
				}
			}

		}

		if (model == null) {
			System.out.println("");
		}

		return model.eResource();
	}
	/**
	 * adds annotations to the selected elements
	 * @param elems - elements to be annotated
	 * @param text - message of the annotation
	 * @param severity - severity of the constraint
	 */
	public void addAnnotation(Collection<BaseElement> elems, String text,
			String severity) {

		SessionManager.getInstance().createSession("Add Annotations");

		try {
			GetModel();

			EnumerationLiteral severityLevel = null;
			List<EnumerationLiteral> severityLevels = Annotation
					.getSeverityLevels(Application.getInstance().getProject());
			for (EnumerationLiteral enumerationLiteral : severityLevels) {
				if (enumerationLiteral.getName().equals(severity)) {
					severityLevel = enumerationLiteral;
				}
			}

			Annotation annot = null;

			Collection<BaseElement> annotatedElements = AnnotationManager
					.getInstance().getAnnotatedElements();
			for (BaseElement baseElement : annotatedElements) {
				for (Annotation annotation : AnnotationManager.getInstance()
						.getAnnotations(baseElement)) {
					if (annotation.getText().equals(text)) {
						AnnotationManager.getInstance().remove(annotation);
					}
				}
			}
			AnnotationManager.getInstance().update();

			for (BaseElement baseElement : elems) {
				try {

					annot = new Annotation(severityLevel, text, text,
							baseElement);
					AnnotationManager.getInstance().add(annot);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
			AnnotationManager.getInstance().update();
		} catch (Exception e) {
			
		}

		SessionManager.getInstance().closeSession();

	}
	/**
	 * helper method that removes all annotation from elements
	 */
	public void removeAllAnnot() {
		Collection<BaseElement> annotatedElements = AnnotationManager
				.getInstance().getAnnotatedElements();
		for (BaseElement baseElement : annotatedElements) {
			for (Annotation annotation : AnnotationManager.getInstance()
					.getAnnotations(baseElement)) {

				AnnotationManager.getInstance().remove(annotation);

			}
		}
		AnnotationManager.getInstance().update();
	}
}
