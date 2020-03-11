/* 
 * IlluFaces ImageEditor Widget
 */
PrimeFaces.widget.ImageEditor = PrimeFaces.widget.BaseWidget.extend({

	init : function(cfg, refreshOnly) {
		this._super(cfg);

		this.jqCanvas = $(this.jqId + '_canvas');
		this.clearButton = $(this.jqId + '_clear-button');
		this.saveButton = $(this.jqId + '_save-button');
		this.downloadButton = $(this.jqId + '_download-button');
		this.rotateCWButton = $(this.jqId + '_rotate-cw-button');
		this.rotateCCWButton = $(this.jqId + '_rotate-ccw-button');
		this.undoButton = $(this.jqId + '_undo-button');
		this.colorChooser = this.jq.find('input[type=color]');
		
		this.form = this.jq.closest('form');

		if (refreshOnly !== true) {
			this.initState();
		} else {
			this.refreshState();
		}
		this.initializeCanvas();
		this.bindEvents();
		this.skinButtons();
	},
	
	refresh : function(cfg) {
		this.init(cfg, true);
	},
	
	getShapeSelectionWidget: function() {
		return PrimeFaces.widgets[this.widgetVar + 'DrawSelection'];
	},
	
	initState : function() {
		this.state = {
			// Visible shapes
			shapes: [],
			
			// Settings for new shapes
			shapeType: 'rect',
			strokeSize: 5,
			color: this.colorChooser.val(),
			
			// Drawing
			down: false,
			shape: null,
			startX: 0,
			startY: 0
		};
		
		if (this.cfg.initialShape) {
			this.state.shapeType = this.cfg.initialShape;
		}
	},
	
	refreshState : function() {
		this.state.shapes = [];
		
		// Mark currently selected shape in refreshed UI
		this.selectShape(this.state.shapeType);
		
		// Restore current color
		this.colorChooser.val(this.state.color);
	},

	initializeCanvas : function() {
		this.canvas = new fabric.Canvas(this.jqCanvas[0], {
			selection : false
		});
		var $this = this;
		fabric.Image.fromURL(this.cfg.imageSource, function(img) {
			var size = img.getOriginalSize();
			img.selectable = false;
			$this.canvas.setWidth(size.width);
			$this.canvas.setHeight(size.height);
			$this.canvas.setBackgroundImage(img);
			$this.backgroundImage = img;
			$this.canvas.renderAll();
		});
	},
	
	bindEvents : function() {
		var $this = this;
		this.clearButton.on('click.imageed', function() {
			$this.clear();
			return false;
		});
		this.downloadButton.on('click.imageed', function(event) {
			$this.download(event);
		});
		this.saveButton.on('click.imageed', function() {
			$this.save();
			return false;
		});
		this.rotateCWButton.on('click.imageed', function() {
			$this.rotateClockwise();
			return false;
		});
		this.rotateCCWButton.on('click.imageed', function() {
			$this.rotateCounterClockwise();
			return false;
		});
		this.undoButton.on('click.imageed', function() {
			$this.undo();
			return false;
		});
		this.canvas.on('mouse:down', function(event) {
			$this.onMouseDown(event);
		});
		this.canvas.on('mouse:move', function(event) {
			$this.onMouseMove(event);
		});
		this.canvas.on('mouse:up', function(event) {
			$this.onMouseUp(event);
		});
		this.colorChooser.on('change', function(event) {
			$this.state.color = $this.colorChooser.val();
		});
	},
	
	skinButtons : function() {
		var rectButton = this.jq.find('.ui-button input[value=rect]').closest('div.ui-button');
		rectButton.removeClass('ui-button-text-only').addClass('ui-button-icon-only');
		rectButton.append('<span class="ui-button-icon-left ui-icon ui-icon-imageeditor ui-c ui-icon-rect"></span>');
		
		var ellipseButton = this.jq.find('.ui-button input[value=ellipse]').closest('div.ui-button');
		ellipseButton.removeClass('ui-button-text-only').addClass('ui-button-icon-only');
		ellipseButton.append('<span class="ui-button-icon-left ui-icon ui-icon-imageeditor ui-c ui-icon-ellipse"></span>');
		
		var lineButton = this.jq.find('.ui-button input[value=line]').closest('div.ui-button');
		lineButton.removeClass('ui-button-text-only').addClass('ui-button-icon-only');
		lineButton.append('<span class="ui-button-icon-left ui-icon ui-icon-imageeditor ui-c ui-icon-line"></span>');
	},
	
	onSelectedShapeChanged : function() {
		var selectedShape = this.getShapeSelectionWidget().inputs.filter(':checked').val();
		this._selectShape(selectedShape);
	},

	selectShape : function(shape) {
		var button = this.getShapeSelectionWidget().inputs.filter('input[value=' + shape + ']').closest('div.ui-button');
		if (button.length) {
			this.getShapeSelectionWidget().select(button);
		}
	},
	
	_selectShape : function(shape) {
		switch (shape) {
		case 'rect':
		case 'line':
		case 'ellipse':
			this.state.shapeType = shape;
			break;

		default:
			this._selectShape('rect'); // Default for unknown shapes
			break;
		}
	},

	clear : function() {
		this.canvas.clear();
		this.state.shapes = [];
	},
	
	onMouseDown : function(event) {
		var color = this.state.color;
		var shapeType = this.state.shapeType;
		var strokeSize = this.state.strokeSize;
		var pointer = this.canvas.getPointer(event.e);
		
		this.state.down = true;
		this.state.startX = pointer.x;
		this.state.startY = pointer.y;
		
		switch (shapeType) {
		case 'line':
			var points = [pointer.x, pointer.y, pointer.x, pointer.y];
			this.state.shape = new fabric.Line(points, {
				strokeWidth : this.state.strokeSize,
				fill : color,
				stroke : color,
				selectable : false,
				originX : 'center',
				originY : 'center'
			});
			break;
			
		case 'rect':
			this.state.shape = new fabric.Rect({
				left : pointer.x,
				top : pointer.y,
				width : 0,
				height : 0,
				strokeWidth : this.state.strokeSize,
				stroke : color,
				selectable : false,
				fill : 'transparent',
				hasBorders : true
			});
			break;
			
		case 'ellipse':
			this.state.shape = new fabric.Ellipse({
				left : pointer.x,
				top : pointer.y,
				rx : 0,
				ry : 0,
				strokeWidth : this.state.strokeSize,
				stroke : color,
				selectable : false,
				fill : 'transparent',
				hasBorders : true
			});
			break;
		}
		
		if (!this.state.shape) {
			return;
		}
		
		this.canvas.add(this.state.shape);
		this.state.shapes.push(this.state.shape);
	},
	
	onMouseMove : function(event) {
		var shape = this.state.shape;
		var shapeType = this.state.shapeType;
		var pointer = this.canvas.getPointer(event.e);
		var left, top;
		
		if (!this.state.down || !shape) {
			return;
		}
		
		switch (shapeType) {
		case 'line':
			shape.set({
				x2 : pointer.x,
				y2 : pointer.y
			});
			break;
			
		case 'rect':
			var width = Math.abs(pointer.x - this.state.startX);
			var height = Math.abs(pointer.y - this.state.startY);
			
			left = Math.min(pointer.x, this.state.startX);
			top = Math.min(pointer.y, this.state.startY);
			
			shape.set({
				left   : left,
				top    : top,
				width  : width,
				height : height
			});
			break;
			
		case 'ellipse':
			var rx = Math.abs(pointer.x - this.state.startX) / 2;
			var ry = Math.abs(pointer.y - this.state.startY) / 2;
			
			left = Math.min(pointer.x, this.state.startX);
			top = Math.min(pointer.y, this.state.startY);
			
			shape.set({
				left : left,
				top  : top,
				rx   : rx,
				ry   : ry
			});
			break;
		}

		this.canvas.renderAll();
	},
	
	onMouseUp : function(event) {
		this.state.down = false;
		this.state.shape = null;
	},
	
	rotateClockwise : function() {
		this._rotateCanvas(90);
		$.each(this.state.shapes, function (index, shape) {
			var height = shape.canvas.getWidth(); // Canvas is already rotated
			switch(shape.type) {
			case 'ellipse':
				shape.set({
					left : height - shape.getTop() - (2 * shape.getRy()),
					top  : shape.getLeft(),
					rx   : shape.getRy(),
					ry   : shape.getRx()
				});
				break;
				
			case 'rect':
				shape.set({
					left   : height - shape.getTop() - shape.getHeight() - shape.getStrokeWidth(),
					top    : shape.getLeft(),
					width  : shape.getHeight(),
					height : shape.getWidth()
				});
				break;
				
			case 'line':
				shape.set({
					x1 : height - shape.y1,
					y1 : shape.x1,
					x2 : height - shape.y2,
					y2 : shape.x2
				});
				break;
			}
		});
		this.canvas.renderAll();
	},
	
	rotateCounterClockwise : function() {
		this._rotateCanvas(-90);
		$.each(this.state.shapes, function (index, shape) {
			var width = shape.canvas.getHeight(); // Canvas is already rotated
			switch(shape.type) {
			case 'ellipse':
				shape.set({
					left : shape.getTop(),
					top  : width - shape.getLeft() - (2 * shape.getRx()),
					rx   : shape.getRy(),
					ry   : shape.getRx()
				});
				break;
				
			case 'rect':
				shape.set({
					left   : shape.getTop(),
					top    : width - shape.getLeft() - shape.getWidth() - shape.getStrokeWidth(),
					width  : shape.getHeight(),
					height : shape.getWidth()
				});
				break;
				
			case 'line':
				shape.set({
					x1 : shape.y1,
					y1 : width - shape.x1,
					x2 : shape.y2,
					y2 : width - shape.x2
				});
				break;
			}
		});
		this.canvas.renderAll();
	},
	
	undo : function() {
		var shape = this.state.shapes.pop();
		if (shape) {
			this.canvas.remove(shape);
		}
	},
	
	_rotateCanvas : function(angle) {
		var height = this.canvas.height;
		var width = this.canvas.width;
		this.backgroundImage.setAngle(this.backgroundImage.angle + angle);
		this.canvas.setHeight(width);
		this.canvas.setWidth(height);
		this.canvas.centerObject(this.backgroundImage);
	},
	
	download : function(event) {
		var data = this.getDataUrl();
		window.open(data);
	},
	
	save : function() {

		var options = {
			source : this.id,
			process : this.id
		};
		
		var $this = this;
		
		options.onerror = function() {
			if ($this.cfg.onerror) {
				$this.cfg.onerror.call(this);
			} else {
				alert('Error');
			}
		};
		
		options.onsuccess = function() {
			if ($this.cfg.onsuccess) {
				$this.cfg.onsuccess.call(this);
			}
		};
		
		options.oncomplete = function() {
			// Re-Enable Save Button
			$this.saveButton
				.removeClass('ui-state-disabled')
				.removeAttr('disabled');
			var label = $this.saveButton.find('span.ui-button-text');
			label.text(label.data('originalLabel'));
		};
		
		options.onstart = function() {
			// Disable Save Button
			$this.saveButton
				.removeClass('ui-state-hover ui-state-focus ui-state-active')
				.addClass('ui-state-disabled')
				.attr('disabled', 'disabled');
			var label = $this.saveButton.find('span.ui-button-text');
			label.data('originalLabel', label.text());
			label.text(label.text() + " ...");
		};

		options.params = [ {
			name : this.id + '_save',
			value : this.getDataUrl()
		} ];

		PrimeFaces.ajax.AjaxRequest(options);
	},
	
	getDataUrl : function() {
		var options = {
			format: 'jpeg',
			quality: 1
		};
		return this.canvas.toDataURL(options);
	}

});