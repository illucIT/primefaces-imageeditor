ImageEditor Component for PrimeFaces
====================================

About
-----

This package provides a JSF (Java Server Faces) component *ImageEditor* as addition to the
commonly used [PrimeFaces](http://www.primefaces.org) widget library.

**Author**: Christian Simon  
**Copyright**: illucIT Software GmbH  
**Website**: [illucit.com](http://www.illucit.com)  
**License**: Apache License 2.0 (see LICENSE file)  
**Current Version**: 1.0.1

Compatibility:
--------------

*ImageEditor* is written for and tested with **PrimeFaces 5.2** and **JSF 2.2**.
Due to changes in the PrimeFaces API for streamed data, the library is not compatible with earlies PrimeFaces versions without modifications.

Setup
-----

The *ImageEditor*  component can either be downloaded directly on Github or included via Maven.

If you want to use Maven to add the library to your web project, you first need to add the public illucIT Maven Repository, as the library is not published on Maven Central, yet.

	<repositories>
		<repository>
			<id>illucit</id>
			<name>illucIT Maven Repository</name>
			<url>http://repository.illucit.com</url>
			<releases>
				<enabled>true</enabled>
			</releases>
		</repository>
	</repositories>

Then just add the Maven artifact to your dependencies:

	<dependencies>
		<dependency>
			<groupId>com.illucit</groupId>
			<artifactId>primefaces-imageeditor</artifactId>
			<version>${version.imageeditor}</version>
		</dependency>
	<dependencies>

Usage in JSF
------------

The library provides a taglib including the `imageEditor` component.
In order to use the component, first declare a namespace for the taglib in your JSF source file (where your also would include the namespace for PrimeFaces):

	<html xmlns="http://www.w3.org/1999/xhtml" lang="en"
		...
		xmlns:p="http://primefaces.org/ui"
		xmlns:ie="http://www.illucit.com/jsf/imageeditor">

Then you can use the `imageEditor` tag in your facelet file.

	<ie:imageEditor id="exampleImageEditor" widgetVar="imageEd"
		value="#{imageEditorBean.image}" initialColor="ffff00"
		initialShape="ellipse"
		fileUploadListener="#{imageEditorBean.onImageSaved}"
		labelSave="Save" labelDownload="Download"
		onsuccess="alert('Successfully saved!');">
		<f:param name="fileName" value="#{imageEditorBean.fileName}" />
	</ie:imageEditor>

The following parameters are required for the `imageEditor` component to work correctly:
* `value`: Expression of a method returing a `StreamedContent` object containing the image data.
Every parameter child element of type `<f:param />` will be attached to the image request.  
Note: As the image is requested by a normal GET request, no view scope can be used for this as the view id will not be transmitted.
* `fileUploadListener`: Bean method accepting an `ImageEditedEvent` object.
The method is called when the save button is pressed. The given object contains the binary image data.


Disclaimer:
-----------

ImageEditor is free software and comes with NO WARRANTY!
