package com.illucit.faces.component.imageeditor;

import javax.faces.application.Application;
import javax.faces.component.UIComponent;
import javax.faces.component.UISelectItem;
import javax.faces.view.facelets.*;

import org.primefaces.component.button.Button;
import org.primefaces.component.colorpicker.ColorPicker;
import org.primefaces.component.selectonebutton.SelectOneButton;
import org.primefaces.component.toolbar.Toolbar;
import org.primefaces.component.toolbar.ToolbarGroup;
import org.primefaces.facelets.MethodRule;

/**
 * Component Handler for Image Editor component.
 *
 * @author Christian Simon
 */
public class ImageEditorComponentHandler extends ComponentHandler {

    private static final MetaRule FILE_UPLOAD_LISTENER = new MethodRule("fileUploadListener", null,
            new Class[]{ImageEditedEvent.class});

    public ImageEditorComponentHandler(ComponentConfig config) {
        super(config);
    }

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

        // "left" toolbar group
        ToolbarGroup leftGroup = (ToolbarGroup) app.createComponent(ToolbarGroup.COMPONENT_TYPE);
        toolbar.getChildren().add(leftGroup);

        // Clear
        addButton(app, c, leftGroup, "clear-button", "trash", null,
                "ui-button-danger ui-image-editor-mr-lg");

        // || Space

        // Line

        SelectOneButton drawSelection = (SelectOneButton) app.createComponent(SelectOneButton.COMPONENT_TYPE);
        drawSelection.setId(baseId + "draw-selection");
        drawSelection.setWidgetVar(ed.getWidgetVar() + "DrawSelection");
        drawSelection.getChildren().add(getSelectItem("rect", "Square"));
        drawSelection.getChildren().add(getSelectItem("ellipse", "Ellipsis"));
        drawSelection.getChildren().add(getSelectItem("line", "Line"));
        drawSelection.setValue(ed.getInitialShape());
        drawSelection.setStyleClass("ui-image-editor-mr-lg");
        drawSelection.setOnchange("PrimeFaces.widgets." + ed.resolveWidgetVar() + ".onSelectedShapeChanged()");
        leftGroup.getChildren().add(drawSelection);

        // || Space

        // Color Picker
        ColorPicker colorChooser = (ColorPicker) app.createComponent(ColorPicker.COMPONENT_TYPE);
        colorChooser.setId(baseId + "color");
        colorChooser.setValue(ed.getInitialColor());
        colorChooser.setStyleClass("ui-image-editor-mr-lg");
        leftGroup.getChildren().add(colorChooser);

        // || Space

        // Rotate
        addButton(app, c, leftGroup, "rotate-ccw-button", "directions-alt", null,
                "ui-image-editor-mr-sm");
        addButton(app, c, leftGroup, "rotate-cw-button", "directions", null,
                "ui-image-editor-mr-lg");

        // || Space

        // Undo Button
        addButton(app, c, leftGroup, "undo-button", "undo", null, null);

        // "right" facet
        ToolbarGroup rightGroup = (ToolbarGroup) app.createComponent(ToolbarGroup.COMPONENT_TYPE);
        rightGroup.setAlign("right");
        toolbar.getChildren().add(rightGroup);

        // Save
        addButton(app, c, rightGroup, "save-button", "save", ed.getLabelSave(),
                "ui-button-success ui-image-editor-mr-sm");

        // Download
        addButton(app, c, rightGroup, "download-button", "download", ed.getLabelDownload(),
                "ui-button-success");

    }

    private UISelectItem getSelectItem(String value, String label) {
        UISelectItem item = new UISelectItem();
        item.setItemValue(value);
        item.setItemLabel(label);
        return item;
    }

    private void addButton(Application app, UIComponent parent, UIComponent panel, String idSuffix, String icon,
                           String value, String styleClass) {
        Button btn = (Button) app.createComponent(Button.COMPONENT_TYPE);
        btn.setId(parent.getId() + "_" + idSuffix);
        if (value != null) {
            btn.setValue(value);
        }
        if (icon != null) {
            btn.setIcon("pi pi-" + icon);
        }
        if (styleClass != null) {
            btn.setStyleClass(styleClass);
        }
        btn.setOnclick("return false");
        panel.getChildren().add(btn);
    }

}
