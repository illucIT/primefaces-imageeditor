package com.illucit.faces.component.imageeditor;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UIPanel;
import javax.faces.component.UISelectItem;
import javax.faces.view.facelets.ComponentConfig;
import javax.faces.view.facelets.ComponentHandler;
import javax.faces.view.facelets.FaceletContext;
import javax.faces.view.facelets.MetaRule;
import javax.faces.view.facelets.MetaRuleset;

import org.primefaces.component.button.Button;
import org.primefaces.component.selectonebutton.SelectOneButton;
import org.primefaces.component.toolbar.Toolbar;
import org.primefaces.facelets.MethodRule;

import com.sun.faces.facelets.compiler.UILiteralText;

/**
 * Component Handler for Image Editor component.
 * 
 * @author Christian Simon
 *
 */
public class ImageEditorComponentHandler extends ComponentHandler {

	private final static String IMPLICIT_PANEL = "com.sun.faces.facelets.IMPLICIT_PANEL";

	private static final MetaRule FILE_UPLOAD_LISTENER = new MethodRule("fileUploadListener", null,
			new Class[] { ImageEditedEvent.class });

	public ImageEditorComponentHandler(ComponentConfig config) {
		super(config);
	}

	@SuppressWarnings("rawtypes")
	@Override
	protected MetaRuleset createMetaRuleset(Class type) {
		MetaRuleset metaRuleset = super.createMetaRuleset(type);
		metaRuleset.addRule(FILE_UPLOAD_LISTENER);
		return metaRuleset;
	}

	@Override
	public void onComponentCreated(FaceletContext ctx, UIComponent c, UIComponent parent) {

		ImageEditor ed = (ImageEditor) c;

		Application app = ctx.getFacesContext().getApplication();

		String baseId = c.getId() + "_";

		Toolbar toolbar = (Toolbar) app.createComponent(Toolbar.COMPONENT_TYPE);
		toolbar.setId(baseId + "image-editor-toolbar");
		c.getChildren().add(toolbar);

		// "left" facet
		String leftFacetName = "left";
		UIComponent leftPanelGroup = app.createComponent(UIPanel.COMPONENT_TYPE);
		toolbar.getFacets().put(leftFacetName, leftPanelGroup);
		leftPanelGroup.getAttributes().put(IMPLICIT_PANEL, true);

		// Clear
		addButton(app, c, leftPanelGroup, "clear-button", "ui-icon-trash", null);

		// ||
		leftPanelGroup.getChildren().add(createSeparator());

		// Line

		SelectOneButton drawSelection = (SelectOneButton) app.createComponent(SelectOneButton.COMPONENT_TYPE);
		drawSelection.setId(baseId + "draw-selection");
		drawSelection.setWidgetVar(ed.getWidgetVar() + "DrawSelection");
		drawSelection.getChildren().add(getSelectItem("rect", "Square"));
		drawSelection.getChildren().add(getSelectItem("ellipse", "Ellipsis"));
		drawSelection.getChildren().add(getSelectItem("line", "Line"));
		drawSelection.setValue(ed.getInitialShape());
		drawSelection.setOnchange("PrimeFaces.widgets." + ed.resolveWidgetVar() + ".onSelectedShapeChanged()");
		leftPanelGroup.getChildren().add(drawSelection);

		// ||
		leftPanelGroup.getChildren().add(createSeparator());

		// Color Picker
		UILiteralText colorChooser = new UILiteralText("<input type=\"color\" value=\"#" + ed.getInitialColor()
				+ "\" class=\"ui-button ui-widget ui-state-default ui-corner-all ui-button-icon-only"
				+ " ui-color-chooser\" />");
		leftPanelGroup.getChildren().add(colorChooser);

		// ||
		leftPanelGroup.getChildren().add(createSeparator());

		// Rotate
		addButton(app, c, leftPanelGroup, "rotate-ccw-button", "ui-icon-arrowreturnthick-1-w", null);
		addButton(app, c, leftPanelGroup, "rotate-cw-button", "ui-icon-arrowreturnthick-1-w ui-icon-mirror-horizontal",
				null);

		// ||
		leftPanelGroup.getChildren().add(createSeparator());

		// Undo Button
		addButton(app, c, leftPanelGroup, "undo-button", "ui-icon-arrowrefresh-1-s ui-icon-mirror-horizontal", null);

		// "right" facet
		String rightFacetName = "right";
		UIComponent rightPanelGroup = app.createComponent(UIPanel.COMPONENT_TYPE);
		toolbar.getFacets().put(rightFacetName, rightPanelGroup);
		rightPanelGroup.getAttributes().put(IMPLICIT_PANEL, true);

		// Save
		addButton(app, c, rightPanelGroup, "save-button", "ui-icon-disk", ed.getLabelSave());

		// Download
		addButton(app, c, rightPanelGroup, "download-button", "ui-icon-arrowthick-1-s", ed.getLabelDownload());

	}

	private UISelectItem getSelectItem(String value, String label) {
		UISelectItem item = new UISelectItem();
		item.setItemValue(value);
		item.setItemLabel(label);
		return item;
	}

	private void addButton(Application app, UIComponent parent, UIComponent panel, String idSuffix, String icon,
			String value) {
		Button btn = (Button) app.createComponent(Button.COMPONENT_TYPE);
		btn.setId(parent.getId() + "_" + idSuffix);
		if (value != null) {
			btn.setValue(value);
		}
		if (icon != null) {
			btn.setIcon(icon);
		}
		btn.setOnclick("return false");
		panel.getChildren().add(btn);
	}

	private UIComponent createSeparator() {
		return new UILiteralText("<span class=\"ui-separator\"><span class=\"ui-icon ui-icon-grip-dotted-vertical\">"
				+ "</span></span>");
	}

}
