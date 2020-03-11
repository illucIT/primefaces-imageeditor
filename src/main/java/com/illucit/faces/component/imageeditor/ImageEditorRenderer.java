package com.illucit.faces.component.imageeditor;

import static org.primefaces.application.resource.DynamicContentType.STREAMED_CONTENT;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;

import org.primefaces.renderkit.CoreRenderer;
import org.primefaces.util.DynamicContentSrcBuilder;
import org.primefaces.util.WidgetBuilder;

/**
 * Renderer for {@link ImageEditor}.
 * 
 * @author Christian Simon
 *
 */
public class ImageEditorRenderer extends CoreRenderer {

	@Override
	public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
		ImageEditor editor = (ImageEditor) component;

		encodeMarkup(context, editor);
		encodeScript(context, editor);
	}

	@Override
	public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
		// Do nothing
	}

	@Override
	public boolean getRendersChildren() {
		return true;
	}

	protected void encodeMarkup(FacesContext context, ImageEditor editor) throws IOException {
		ResponseWriter writer = context.getResponseWriter();
		String clientId = editor.getClientId(context);

		writer.startElement("div", editor);
		writer.writeAttribute("id", clientId, "id");
		writer.writeAttribute("name", clientId, "name");
		writer.writeAttribute("class", editor.resolveStyleClass(), "styleClass");
		if (editor.getStyle() != null) {
			writer.writeAttribute("style", editor.getStyle(), "style");
		}

		renderChildren(context, editor);

		writer.startElement("div", null);
		writer.writeAttribute("class", "ui-image-editor-canvas-container", null);

		writer.startElement("canvas", null);
		writer.writeAttribute("id", clientId + "_canvas", null);
		writer.writeAttribute("class", "imageEditor", null);
		writer.endElement("canvas");

		writer.endElement("div");

		writer.endElement("div");
	}

	protected void encodeScript(FacesContext context, ImageEditor editor) throws IOException {
		String clientId = editor.getClientId(context);
		WidgetBuilder wb = getWidgetBuilder(context);
		wb.init("ImageEditor", editor.resolveWidgetVar(), clientId);
		wb.attr("imageSource", getImageSrc(context, editor));
		wb.attr("initialShape", editor.getInitialShape());
		wb.callback("onsuccess", "function()", editor.getOnsuccess());
		wb.callback("onerror", "function()", editor.getOnerror());
		wb.finish();
	}

	protected String getImageSrc(FacesContext context, ImageEditor editor) {
		return DynamicContentSrcBuilder.build(context, editor.getValue(), editor, false, STREAMED_CONTENT, true);
	}

	@Override
	public void decode(FacesContext context, UIComponent component) {
		ImageEditor imageEditor = (ImageEditor) component;
		if (!imageEditor.isDisabled()) {
			String dataUrl = context.getExternalContext().getRequestParameterMap()
					.get(imageEditor.getClientId(context) + "_save");
			if (dataUrl != null) {
				String encodingPrefix = "base64,";
				int contentStartIndex = dataUrl.indexOf(encodingPrefix) + encodingPrefix.length();
				byte[] imageData = Base64.getDecoder().decode(dataUrl.substring(contentStartIndex));
				imageEditor.setTransient(true);
				imageEditor.queueEvent(new ImageEditedEvent(imageEditor, new InMemoryUploadedFile(imageData,
						"image.jpg", "image/jpg")));
			}
		}
	}

}
