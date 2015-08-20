package com.illucit.faces.component.imageeditor;

import javax.faces.component.UIComponent;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.UploadedFile;

/**
 * Event to indicate that the image was stored by {@link ImageEditor} action.
 * 
 * @author Christian Simon
 *
 */
public class ImageEditedEvent extends FileUploadEvent {

	private static final long serialVersionUID = -2624800646668578507L;

	/**
	 * Create event.
	 * 
	 * @param component
	 *            component that fired the event
	 * @param file
	 *            uploaded file data
	 */
	public ImageEditedEvent(UIComponent component, UploadedFile file) {
		super(component, file);
	}

}
