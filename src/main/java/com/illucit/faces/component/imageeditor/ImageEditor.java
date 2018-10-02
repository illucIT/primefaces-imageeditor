package com.illucit.faces.component.imageeditor;

import javax.el.MethodExpression;
import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.component.UINamingContainer;
import javax.faces.component.UIPanel;
import javax.faces.context.FacesContext;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.FacesEvent;

import org.primefaces.component.api.Widget;

/**
 * Image Editor component.
 * 
 * @author Christian Simon
 * 
 */
//@formatter:off
@ResourceDependencies({
	@ResourceDependency(library="primefaces", name="jquery/jquery.js"),
	@ResourceDependency(library="primefaces", name="components.js"),
	@ResourceDependency(library="webjars", name="fabric.js/1.4.12/fabric.js"),
	@ResourceDependency(library="illufaces", name="imageeditor.js"),
	@ResourceDependency(library="illufaces", name="imageeditor.css")
})
//@formatter:on
public class ImageEditor extends UIPanel implements Widget {

	public static final String COMPONENT_TYPE = "com.illucit.faces.component.ImageEditor";
	public static final String COMPONENT_FAMILY = "com.illucit.faces.component";
	private static final String DEFAULT_RENDERER = "com.illucit.faces.component.ImageEditorRenderer";

	protected enum PropertyKeys {
		style, styleClass, value, widgetVar, initialColor, initialShape, fileUploadListener, disabled, labelSave, labelDownload, onsuccess, onerror
	}

	public ImageEditor() {
		setRendererType(DEFAULT_RENDERER);
	}

	public String getFamily() {
		return COMPONENT_FAMILY;
	}

	public String getStyle() {
		return (String) getStateHelper().eval(PropertyKeys.style, null);
	}

	public void setStyle(String _style) {
		getStateHelper().put(PropertyKeys.style, _style);
	}

	public String getStyleClass() {
		return (String) getStateHelper().eval(PropertyKeys.styleClass, null);
	}

	public void setStyleClass(String _styleClass) {
		getStateHelper().put(PropertyKeys.styleClass, _styleClass);
	}

	public Object getValue() {
		return getStateHelper().eval(PropertyKeys.value);
	}

	public void setValue(Object value) {
		getStateHelper().put(PropertyKeys.value, value);
	}

	public String getWidgetVar() {
		return (String) getStateHelper().eval(PropertyKeys.widgetVar, null);
	}

	public void setWidgetVar(String _widgetVar) {
		getStateHelper().put(PropertyKeys.widgetVar, _widgetVar);
	}

	public String getInitialColor() {
		return (String) getStateHelper().eval(PropertyKeys.initialColor, "000000");
	}

	public void setInitialColor(String _initialColor) {
		getStateHelper().put(PropertyKeys.initialColor, _initialColor);
	}

	public String getInitialShape() {
		return (String) getStateHelper().eval(PropertyKeys.initialShape, "rect");
	}

	public void setInitialShape(String _initialShape) {
		getStateHelper().put(PropertyKeys.initialShape, _initialShape);
	}

	public javax.el.MethodExpression getFileUploadListener() {
		return (MethodExpression) getStateHelper().eval(PropertyKeys.fileUploadListener, null);
	}

	public void setFileUploadListener(MethodExpression _fileUploadListener) {
		getStateHelper().put(PropertyKeys.fileUploadListener, _fileUploadListener);
	}

	public boolean isDisabled() {
		return (Boolean) getStateHelper().eval(PropertyKeys.disabled, false);
	}

	public void setDisabled(boolean _disabled) {
		getStateHelper().put(PropertyKeys.disabled, _disabled);
	}

	public String getLabelSave() {
		return (String) getStateHelper().eval(PropertyKeys.labelSave, "Save");
	}

	public void setLabelSave(String _labelSave) {
		getStateHelper().put(PropertyKeys.labelSave, _labelSave);
	}

	public String getLabelDownload() {
		return (String) getStateHelper().eval(PropertyKeys.labelDownload, "Download");
	}

	public void setLabelDownload(String _labelDownload) {
		getStateHelper().put(PropertyKeys.labelDownload, _labelDownload);
	}

	public String getOnsuccess() {
		return (String) getStateHelper().eval(PropertyKeys.onsuccess, null);
	}

	public void setOnsuccess(String _onsuccess) {
		getStateHelper().put(PropertyKeys.onsuccess, _onsuccess);
	}
	
	public String getOnerror() {
		return (String) getStateHelper().eval(PropertyKeys.onerror, null);
	}

	public void setOnerror(String _onerror) {
		getStateHelper().put(PropertyKeys.onerror, _onerror);
	}

	/*
	 * Utility functions
	 */

	public String resolveStyleClass() {
		String styleClass = "ui-panel ui-widget ui-widget-content ui-corner-all ui-image-editor";
		String userClass = getStyleClass();
		if (userClass != null && !userClass.isEmpty()) {
			styleClass += " " + userClass;
		}
		return styleClass;
	}

	/*
	 * Events
	 */

	@Override
	public void broadcast(FacesEvent event) throws AbortProcessingException {
		super.broadcast(event);

		FacesContext facesContext = getFacesContext();
		MethodExpression me = getFileUploadListener();

		if (me != null && event instanceof ImageEditedEvent) {
			me.invoke(facesContext.getELContext(), new Object[] { event });
		}
	}

	/*
	 * Widget
	 */

	@Override
	public String resolveWidgetVar() {
		FacesContext context = getFacesContext();
		String userWidgetVar = (String) getAttributes().get("widgetVar");

		if (userWidgetVar != null)
			return userWidgetVar;
		else
			return "widget_" + getClientId(context).replaceAll("-|" + UINamingContainer.getSeparatorChar(context), "_");
	}

}
