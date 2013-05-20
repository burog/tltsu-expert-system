var _ver = '$Revision: 1.46 $';

function get_cookie(cc) {
	var c = document.cookie;
	var cr = cc + '=';
	var p = c.indexOf(cr);
	if(p<0) return '';
	var l = cr.length;
	p = p+l;
	var pp = c.indexOf(';',p);
	if(pp<0) return c.substring(p);
	if(pp==p) return '';
	return c.substring(p,pp);
}


function has_cookie(cc) {
	var c = get_cookie(cc);
	return c.length == 0? false: true;
}

var is_auth = has_cookie('eduuid');

var lecture_show=true;
if(lid && !is_auth) {
	document.write('<script src="/ssi/banner/lecture_show.js?lid='+lid+'&rand='+(Math.floor(Math.random() * 1000000000))+'" type="text/javascript"></script>');
}



function move_content_lection () {
	var o = document.getElementById('lecture_content');
	var oe = document.getElementById('lecture_content_empty');
	var d = document.getElementById('td-content');
	var banner = document.getElementById('lecture_banner');
	if(o && d) {
		d.innerHTML = o.innerHTML;
		o.style.display ='none';
		d.id = o.id;
		d.style.width = '90%';
		if(banner) {
			banner.style.display ='none';
		}
	}
	else if(oe && d && banner) {
		banner.style.display ='none';
		d.style.display ='none';
	}
	else if(d && banner) {
		d.innerHTML = banner.innerHTML;
		banner.style.display ='none';
		d.id = banner.id;
		d.style.width = '90%';
//		alert("d && banner");
	}
}


function on_load_lecture () {
	move_content_lection();
    if(!is_auth) {
	return;
    }
    if (!check_revision()) {
        alert("Данная страница сверстана без поддержки комментариев.\nОбратитесь к системному администратору.");
        return; 
    }
    window.focus();
    var oController = LaController.getInstance();
    oController.update();
}

function nop() {
}

function check_revision () {
    try {
        var la_indicator = $('la_indicator').id;
        var menu_lecture = $('menu_lecture').id;
        var lecture_container = $('lecture_container').id;
        var lecture_container = $('lecture_body').id;
    } catch (e) {
          return false;
    }
    return true;
}
////////////////////////////////////////////////////////////////////////////////////////////
LaViewComment = Class.create();
LaViewComment.prototype = {
    initialize: function(element) {
        this.element = element;
        this.item = this.attachItem(); 
    },
    destroy:function() {
        try {
            Element.remove(this.element);
        }
        catch (e) {} 
        this.item = null;
    },
    get_mark: function() {
        var arr = new String(this.element).split(LaItem.DELIMITER);
        return arr[0];
    },
    type: function() {
        return ('LaViewComment'); 
    },
    getItem: function() {
        return this.item; 
    },
    getCheck: function() {
        return this.item.check_name();
    }, 
    getData: function() {
        return this.item.view(); 
    },
    attachItem: function() {
        var items = LaModel.getInstance().getItems(this.get_mark());
        var element = this.element;
        var item = items.find( function(item){
            return (item.tag() == element);
            });
        return item;
    },
    update: function() {
        var oController = LaController.getInstance();
        if (oController.showComments()) {
            Element.update(this.element, this.getData());
            Element.show(this.element);
        }
        else {
            Element.hide(this.element);
        }
    }
};

/////////////////////////////////////////////////////////////////
LaAnchorFlyweight = Class.create();
LaAnchorFlyweight.DEFAULT_STYLE = $H({'cursor' : 'default'});
LaAnchorFlyweight.MOUSEOVER_STYLE = $H({'cursor' : 'pointer'});
LaAnchorFlyweight.prototype = {
    initialize: function() {
        //if (LaAnchorFlyweight.caller != LaAnchorFlyweight.getInstance) {
        //    throw new Error("There is no public constructor LaAnchorFlyweight.");
        //}
        this.dom_anchors = $A(document.getElementsByClassName(LaFactoryView.CLASS, LaFactoryView.BODY));
        this.dom_objects = this.get_objects();
    },
    get_objects: function() {
        var objects = $A();
        this.dom_anchors.each(function(elem) {
            objects.push($(LaFactoryView.mark2obj(elem.id)));
            });
        return objects;
    }, 
    update: function() {
        this.dom_objects.each(function(elem) {
            if (LaController.getInstance().showComments()) { 
                Element.setStyle(elem, LaAnchorFlyweight.MOUSEOVER_STYLE);
                elem.onclick = LaAnchorFlyweight.add_comment.bindAsEventListener(this);
            }
            else {
                Element.setStyle(elem, LaAnchorFlyweight.DEFAULT_STYLE);
                elem.onclick = null;
            }  
        });
    }
}
LaAnchorFlyweight.__instance__ = null;
LaAnchorFlyweight.getInstance = function () {
    if (this.__instance__ == null) {
        this.__instance__ = new LaAnchorFlyweight();
    }
    return this.__instance__;
}
LaAnchorFlyweight.add_comment = function (evt) {
    var elem = Event.element(evt)
    var oController = LaController.getInstance();
    if (elem.id.substring(0,3) != 'id_') {
        var lev = 10;
        while (elem.id.substring(0,3) != 'id_' && lev-- > 0) {
           elem = elem.parentNode;
        }
    }
    try {
        //LaFade.getInstance().fade(elem.id);
        if ($(oController.clicked) != null) {
            LaAnchorFlyweight.highlight(LaFactoryView.mark2obj(oController.clicked), false); 
        }
        oController.clicked =  LaFactoryView.obj2mark(elem.id);
        oController.child_win = new Popup({width:470, height: 350, url:LaController.ADD_URL, name:LaController.ADD_WINDOW_NAME});
    } catch (e) {};
}
LaAnchorFlyweight.highlight = function (elem, on) {
    try {
        $(elem).style.background = (on)?'yellow':'white'; 
    }
    catch (e){}
}

/////////////////////////////////////////////////////////////////
LaViewCommentContainer = Class.create();
LaViewCommentContainer.INLINE_ELEMENT = 'div';
LaViewCommentContainer.POPUP_ELEMENT = 'span';
LaViewCommentContainer.POPUP_STYLE = $H({'display':'inline'});
LaViewCommentContainer.POPED = null;

LaViewCommentContainer.AddChild = function (item, hidden) {
    var element = $(item.mark_id);
    var la;
    la = document.createElement(LaViewCommentContainer.INLINE_ELEMENT);
    la.className = element.className+'_la';
    la.id = item.tag();
    element.appendChild(la);
    (hidden) ? Element.hide(la): Element.show(la); 
    return new LaViewComment(la.id)
}; 

LaViewCommentContainer.prototype = {
    initialize: function(element, inline) {
        this.element = element;
        this.comments = this.generateComments();
        this.hidden = !inline;
    },
    destroy:function() {
        this.comments.each(function(comment) {
            comment.destroy(); 
            comment = null;
            });
        this.comments = null;
        this.deleteIndicator();
    },
    type: function() {
        return ('LaViewCommentContainer'); 
    }, 
    generateComments: function() {
        var items = LaModel.getInstance().getItems(this.element);
        var hidden = this.hiddden;
        var comments = $A();
        items.each(function(item) {
            comments.push(LaViewCommentContainer.AddChild(item, hidden));
            });
        return comments; 
    },
    update: function() {
        if (LaController.getInstance().showComments()) {
            this.makeIndicator();
            this.positionIndicator();
            (this.hidden) ? Element.hide(this.element) : Element.show(this.element);
            $(this.indicator_id()).title = this.get_indicator_title();
            $(this.indicator_id()).className = this.get_indicator_class();
        } else {
            this.deleteIndicator();
        }

        this.comments.each(function(comment) {
             comment.update();
            });
    },
    toggle_inline: function() {
        this.hidden = !this.hidden;
        LaFactoryView.getInstance().updateViews();
    },
    indicator_id: function() {
        return this.element + '_ind'; 
    },
    makeIndicator: function() {
        if ($(this.indicator_id()) != null) { return;}
        var indicator = document.createElement(LaViewCommentContainer.POPUP_ELEMENT);
        indicator.id = this.indicator_id();
        indicator.className = this.get_indicator_class();

        indicator.title = this.get_indicator_title();
        $(LaFactoryView.mark2obj(this.element)).appendChild(indicator);
        Element.update(indicator, new String(this.comments.length));
        indicator.onclick = this.show_popup_comments.bindAsEventListener(this); 
    },
    deleteIndicator: function() {
        try {
            Element.remove($(this.indicator_id()));
        }
        catch (e) {} 
    },
    show_popup_comments: function(evt) {
        Event.stop(evt);
        this.toggle_inline();
        return;
    }, 
    positionIndicator: function() {
        var indicator = $(this.indicator_id());
        var p = $(LaFactoryView.mark2obj(this.element));

        var lecture_container_offset = Position.cumulativeOffset($('lecture_container'));
        var p_offset = Position.cumulativeOffset(p);

        indicator.style.position = 'absolute';
        indicator.style.top = p_offset[1] -10  + 'px';
//        indicator.style.left = lecture_container_offset[0] -5 + 'px';
	indicator.style.left = '7px';

    },
    get_indicator_title: function() {
        return (this.hidden)?'показать комментарии':'скрыть комментарии';
    },
    get_indicator_class: function() {
        return (!this.hidden)?'comment-indicator-inactive':'comment-indicator-active'
    }   
};


////////////////////////////////////////////////////////////////////////////////////////////
LaMemoize = Class.create()
LaMemoize.prototype = {
    initialize: function() {
        this.exceptions = $A();
    },
    clear: function() {
        this.exceptions.clear();
    },
    fill_in: function() {
        this.clear();
        var oFactory = LaFactoryView.getInstance();
        if (LaController.getInstance().showInline()) {
            oFactory.comment_views.each(function(view) {
                if (view.hidden) {
                   LaMemoize.getInstance().add(view.element);
                } 
            });
        }
        else {
            oFactory.comment_views.each(function(view) {
                if (!view.hidden) {
                   LaMemoize.getInstance().add(view.element);
                } 
            });

        }
    },
    add: function(val) {
        this.exceptions.push(val); 
    },
    is_contained: function(val) {
        return (this.exceptions.indexOf(val) >=0)? true:false;
    }
}



function _fill_in_exceptions() {
   LaMemoize.getInstance().clear();
   return;
   var oFactory = LaFactoryView.getInstance();
   if (LaController.getInstance().showInline()) {
       oFactory.comment_views.each(function(view) {
           if (view.hidden) {
               _exceptions.push(view.element);
           } 
       });
   }
   else {
       oFactory.comment_views.each(function(view) {
           if (!view.hidden) {
               _exceptions.push(view.element);
           } 
       });
   }
}

LaMemoize.__instance__ = null;
LaMemoize.getInstance = function () {
    if (this.__instance__ == null) {
        this.__instance__ = new LaMemoize();
    }
    return this.__instance__;
}



////////////////////////////////////////////////////////////////////////////////////////////
// View Factory. A Singleton class
LaFactoryView = Class.create();
LaFactoryView.DELIMITER = '_';
LaFactoryView.MARK = 'mark';
LaFactoryView.CLASS = 'lecture_mark';
LaFactoryView.BODY = 'body_lecture';
LaFactoryView.prototype = {
    initialize: function() {
        //if (LaFactoryView.caller != LaFactoryView.getInstance) {
        //    throw new Error("There is no public constructor LaFactoryView.");
        //}
        this.comment_views = this.loadCommentViews();
        this.indicator = null; 
    },
    destroy:function() {
        this.comment_views.each(function(view) {
            view.destroy(); 
            view = null;
            });
        this.comment_views = null;
        this.deleteIndicator();
        this.__instance__ = null;
        return this.__instance__;
    },

    domMarks: function() {
        var marks = $A(document.getElementsByClassName(LaFactoryView.CLASS, LaFactoryView.BODY));
        return marks;
    },
    loadCommentViews: function() {
        var views = $A();
        var marks = this.domMarks();
        marks.each(function(mark) {
            var items = LaModel.getInstance().getItems(mark.id)
            if (items.length > 0) {
                if (LaMemoize.getInstance().is_contained(mark.id)) {
                    views.push(new LaViewCommentContainer(mark.id, !LaController.getInstance().showInline()));
                }
                else {
                    views.push(new LaViewCommentContainer(mark.id, LaController.getInstance().showInline()));
                }
            }
            });
        return views;

    },
    updateViews: function() {
        LaAnchorFlyweight.getInstance().update();
        this.comment_views.each(function(view) {
            view.update(); 
            });
        if (LaController.getInstance().showComments()) {
            this.makeIndicator();
        }
        else {
            this.deleteIndicator();
        }  

    },
    getCommentViews: function() {
        var views = $A();
        this.comment_views.each(function(view) {
            view.comments.each(function(comment) {
                views.push(comment);
            }); 
        });
        return views; 
    },
    makeIndicator: function() {
        if (this.indicator != null) { Element.show($('la_indicator')); return;}
        this.indicator = $('la_indicator');
        Element.show($('la_indicator'));
        this.markIndicator();
        this.indicator.onclick = this.toggleInline.bindAsEventListener(this); 
    },
    deleteIndicator: function() {
        Element.hide($('la_indicator'));
    },
    positionIndicator: function() {
        return;
    },
    markIndicator: function() {
        if (LaController.getInstance().showInline()) {
            this.indicator.className = 'la_indicator_collapse';
            this.indicator.title="Свернуть все";
        }
         else {
            this.indicator.className = 'la_indicator_open';
            this.indicator.title="Развернуть все";
        }
    },
    toggleInline: function() {
        var showinline =  LaController.getInstance().showInline();
        LaController.getInstance().getOptions().showinline = !showinline;
        this.markIndicator();
        this.comment_views.each(function(view) {
            view.hidden = !LaController.getInstance().getOptions().showinline; 
            });
        this.updateViews();
        LaController.getInstance().getOptions().saveOptions(true)
    }

};

LaFactoryView.mark2obj = function (mark_id) {
    var pieces = new String(mark_id).split(LaFactoryView.DELIMITER);
    return "id_"+pieces[1]; 
}

LaFactoryView.obj2mark = function (obj_id) {
    var pieces = new String(obj_id).split(LaFactoryView.DELIMITER);
    return LaFactoryView.MARK + LaFactoryView.DELIMITER + pieces[1];
}

LaFactoryView.__instance__ = null;
LaFactoryView.getInstance = function () {
    if (this.__instance__ == null) {
        this.__instance__ = new LaFactoryView();
    }
    return this.__instance__;
}


////////////////////////////////////////////////////////

function fade(container) {
    LaFade.getInstance().fade(container);
}

LaFade = Class.create();
LaFade.RED = 255;
LaFade.GREEN = 253;
LaFade.BLUE = 55;
LaFade.HOLD = 700;
LaFade.SPEED = 200;
LaFade.STEP = 25;

LaFade.prototype = {
    initialize: function() {
        this.r = LaFade.RED
        this.g = LaFade.GREEN
        this.b = LaFade.BLUE
    },
    fade: function(container) {
	if (this.r + this.g + this.b != (255 * 3)) {
	    $(container).style.background = "rgb(" + this.r + "," + this.g + "," + this.b + ")";
	        if ((this.r == LaFade.RED) && (this.g == LaFade.GREEN) && (this.b == LaFade.BLUE)) {
                    setTimeout('fade("' + container + '")', LaFade.HOLD);
		}
		else {
                    setTimeout('fade("' + container + '")', LaFade.SPEED);
		}
		if ((this.r >= 255) || (this.r + LaFade.STEP > 255)) this.r = 255; else this.r = this.r + LaFade.STEP;
		if ((this.g >= 255) || (this.g + LaFade.STEP > 255)) this.g = 255; else this.g = this.g + LaFade.STEP;
		if ((this.b >= 255) || (this.b + LaFade.STEP > 255)) this.b  = 255; else this.b = this.b + LaFade.STEP;
	}
	else {
	    $(container).style.background = "rgb(" + this.r + "," + this.g + "," + this.b + ")";
	    this.r = LaFade.RED;
	    this.g = LaFade.GREEN;
	    this.b = LaFade.BLUE;
	}
    }
}

LaFade.__instance__ = null;
LaFade.getInstance = function () {
    if (this.__instance__ == null) {
        this.__instance__ = new LaFade();
    }
    return this.__instance__;
}





////////////////////////////////////////////////////////
////////////////////////////////////////////////////////
LaOptions = Class.create();
LaOptions.showcomments = 1;
LaOptions.showinline = 2;
LaOptions.showall = 4;
LaOptions.inlinecolor = '#ffffff';

LaOptions.prototype = {
    initialize: function() {
        //if (LaOptions.caller != LaOptions.getInstance) {
        //    throw new Error("There is no public constructor for LaOptions.");
        //}
        this.url = '/la/json_query';
        this.showcomments = false;
        this.showinline = false;
        this.showall = false;
        this.inlinecolor = LaOptions.inlinecolor;
        this.user = null;
        this.moderator = null;
    },
    destroy:function() {
        this.__instance__ = null;
        return this.__instance__;
    },
    loadOptions: function (aRequest) {
        var response = eval('(' + aRequest.responseText + ')');
        var opts = $H(response.options);
        this.user = opts['user'];
        this.moderator = opts['moderator'];
        if (opts['visibility'] != null) {
                var visi = opts['visibility']
                this.showcomments = (visi & LaOptions.showcomments )? true: false;
                this.showinline = (visi & LaOptions.showinline)? true: false;
                this.showall = (visi & LaOptions.showall)? true: false;
        }
        if (opts['inlinecolor'] != null) {
                this.inlinecolor = opts['inlinecolor'];
                LaOptions.inlinecolor = opts['inlinecolor'];
        }
        else {
                this.inlinecolor = LaOptions.inlinecolor;
        }

        var oController = LaController.getInstance();
        oController.options = true;
    },
    canModerate: function () {
        return (this.moderator)? true: false;
    },
    saveOptions: function (not_refresh_page) {
        if (this.user == null) { return;}
        var visi = 0;
        if (this.showcomments) {visi |=  LaOptions.showcomments;}
        if (this.showinline) {visi |=  LaOptions.showinline;}
        if (this.showall) {visi |=  LaOptions.showall;}
        var pars = {
                   m: 'save_options',
                   visibility: visi,
                   inlinecolor: this.inlinecolor
                   };
        if (not_refresh_page) {
           new Ajax.Request( this.url, { method: 'get', parameters: $H(pars).toQueryString(), onComplete: _saveOptionsResponceSilence }); 
        }
        else {
          new Ajax.Request( this.url, { method: 'get', parameters: $H(pars).toQueryString(), onComplete: _saveOptionsResponce });  
        } 
    }
};


LaOptions.__instance__ = null;
LaOptions.getInstance = function () {
    if (this.__instance__ == null) {
        this.__instance__ = new LaOptions();
    }
    return this.__instance__;
}

/////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////
// A Singleton class
LaModel = Class.create();
LaModel.URL = '/la/json_query';
LaModel.prototype = {
    initialize: function() {
        //if (LaModel.caller != LaModel.getInstance) {
        //    throw new Error("There is no public constructor for LaModel.");
        //}
        this.lecture_id=null;
        try {
            this.lecture_id = lid;
        } catch (e) {};
        this.items = $H();
        this.current_item = null;
    },
    destroy:function() {
        this.items = null;
        this.__instance__ = null;
        return this.__instance__;
    },

    convertItems: function(rawArr) {
        rawArr = $A(rawArr);
        var h = $H({});
        rawArr.each(function(item) {
            if (h[item.mark_id] == undefined) {
                h[item.mark_id] = $A();
            }
            h[item.mark_id].push(new LaItem(item));
            }); 
        return h; 
    },
    loadItems: function(aRequest) {
        if (!aRequest) {
            var pars = {
                       m: 'get',
                       lecture_id: this.lecture_id
                       } 
            new Ajax.Request( LaModel.URL, { method: 'get', parameters: $H(pars).toQueryString(), onComplete: _loadItemsHelper,  onFailure: _onFailure }); 
        }
        else {
            var response = eval('(' + aRequest.responseText + ')');
            this.items = this.convertItems(response.items);
            var oController = LaController.getInstance();
            oController.model = true;
            if (!oController.getOptions().options) {
                oController.getOptions().loadOptions(aRequest);
            }
            oController.update(); 
        }
    },
    addCommentCallback: function(ret) {
        ret.url = window.location.href;
        new Ajax.Request( LaModel.URL, { method: 'post', parameters: $H(ret).toQueryString(), onComplete: _ajaxResponse });
    },
    editComment: function(id) {
        this.current_item = this.getItemById(id);
        LaController.getInstance().child_win = new Popup({width:470, height: 350, url:LaController.ADD_URL, name:LaController.MODIFY_WINDOW_NAME});
    },
    modifyCommentCallback: function(ret) {
        new Ajax.Request( LaModel.URL, { method: 'post', parameters: $H(ret).toQueryString(), onComplete: _ajaxResponse });
    },
    addOpinion: function(id) {
        this.current_item = id;
        LaController.getInstance().child_win = new Popup({width:470, height: 350, url:LaController.ADD_OPINION_URL, name:LaController.ADD_OPINION_WINDOW_NAME});
    },
    addOpinionCallback: function(ret) {
        ret.url = window.location.href;
        new Ajax.Request( LaModel.URL, { method: 'post', parameters: $H(ret).toQueryString(), onComplete: _ajaxResponse });
    },
    deleteComment: function(id) {
        this.current_item = this.getItemById(id);
        LaController.getInstance().child_win = new Popup({width:470, height: 350, url:LaController.DEL_URL, name:LaController.DEL_WINDOW_NAME});
    },
    delCommentCallback: function(ret) {
        new Ajax.Request( LaModel.URL, { method: 'post', parameters: $H(ret).toQueryString(), onComplete: _ajaxResponse });
    },
    deleteSelected: function(deletereason) {
        var oController = LaController.getInstance();
        var aSelected = oController.getChecked();
        if (aSelected.length < 1) {
            alert ('Требуется отметить хотя бы 1 комментарий');
            return;
        }
        var pars = {
                   m: 'delete',
                   deletereason: deletereason,
                   id: aSelected.join(",")
        };
        new Ajax.Request( LaModel.URL, { method: 'post', parameters: $H(pars).toQueryString(), onComplete: _ajaxResponse });
    },
    approveComment: function(id) {
        var pars = {
                   m: 'approve',
                   id: id
        };
        new Ajax.Request( LaModel.URL, { method: 'post', parameters: $H(pars).toQueryString(), onComplete: _ajaxResponse });
    },

    approveSelected: function() {
        var oController = LaController.getInstance();
        var aSelected = oController.getChecked();
        if (aSelected.length < 1) {
            alert ('Требуется отметить хотя бы 1 комментарий');
            return;
        }
        var pars = {
                   m: 'approve',
                   id: aSelected.join(",")
        };
        new Ajax.Request( LaModel.URL, { method: 'post', parameters: $H(pars).toQueryString(), onComplete: _ajaxResponse });
    },
    editSelected: function() {
        var oController = LaController.getInstance();
        var aSelected = oController.getChecked();
        if (aSelected.length != 1) {
            alert ('Требуется отметить ровно 1 комментарий');
            return;
        }
        this.current_item = this.getItemById(aSelected[0]);
        LaController.getInstance().child_win = new Popup({width:470, height: 350, url:LaController.ADD_URL, name:LaController.MODIFY_WINDOW_NAME});

    }, 
    getItems: function(element) {
        if (this.items != null && this.items[element] != undefined) {
            return this.items[element];
        }
        else {
            return $A(); 
        }
    },
    getItemById: function(item_id) {
        var item = null;
        var aKeys = this.items.keys();
        var key;
        var items;
        for (var i=0; i < aKeys.length; i++) {
            key = aKeys[i];
            items = this.items[key];
            for (var j=0; j < items.length; j++) {
                if (items[j].id == item_id) {
                    item = items[j];
                    break; 
                }
            } 
        }
        return item;
    }
};
LaModel.__instance__ = null;
LaModel.getInstance = function () {
    if (this.__instance__ == null) {
        this.__instance__ = new LaModel();
    }
    return this.__instance__;
}

//////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////
LaItem = Class.create();
LaItem.DELIMITER = '__';
LaItem.CHECK = '___';
LaItem.prototype = {
    initialize: function(o) {
        this.id = o.id;
        this.lecture_id = o.lecture_id;
        this.mark_id = o.mark_id;
        this.user = o.user;
        this.body = o.body;
        this.state = o.state; 
        this.privat = (o.privat == 1)?true:false;
        this.last_modified = o.last_modified; 
        this.can_add = (o.can_add == 1)?true:false;
        this.can_add_opinion = (o.can_add_opinion == 1)?true:false;
        this.can_edit = (o.can_edit == 1)?true:false;
        this.can_delete = (o.can_delete == 1)?true:false;
        this.can_approve = (o.can_approve == 1)?true:false;
    },
    tag: function() {
        return this.mark_id + LaItem.DELIMITER + this.id;
    },
    view: function() {
        return new String (this.title_title() + this.body);
    }, 

    title_title: function() {
        return new String ('<div class="lecture_mark_title">' +
                           '<div style="float:right">' +
                           this.title_actions() +
                           this.check_text() +
                           '</div>' +

                           '<div>' +
                           this.title_comment() +
                           this.title_last_modified() + 
                           this.title_user() +
                           '</div></div>'
                          );
    },
    check_text: function() {
        if (LaController.getInstance().getOptions().canModerate()) {
            return '<input type="checkbox" id="' + this.check_name() + '"/>';
        }
        return '';
    },
    check_name: function() {
        return this.mark_id + LaItem.CHECK + this.id;
    },
    title_comment: function() {
        if (this.privat) {
            return '<img src="/la/images/lock_co.gif" title="Частный"/>';
        }
        else {
            if (this.state == 'draft') {
                return '<img src="/la/images/comment_draft.gif" title="Ожидает модерации" />&nbsp;';
            }
            else {
                return '<img src="/la/images/comment.gif" title="Опубликован" />&nbsp;';
            }
        }
    },
    title_actions: function() {
        if (this.can_add_opinion || this.can_edit || this.can_delete || this.can_approve) {
            var tag = '<span  class="la_action">';
            if (this.can_add_opinion) {
                tag += '<img  onclick="_addOpinion(' + this.id + ')" src="/la/images/reputation.gif" title="Добавить отзыв" />'; 
            }
            if (this.can_approve) {
                tag += '<img  onclick="_approveComment(' + this.id + ')" src="/la/images/approve.gif" title="Одобрить" />'; 

            }
            if (this.can_edit) {
                tag += '<img  onclick="_editComment(' + this.id + ')" src="/la/images/edit.gif" title="Редактировать" />'; 
            }
            if (this.can_delete) {
                tag += '<img  onclick="_deleteComment(' + this.id + ')" src="/la/images/cancel.gif" title="Удалить" />'; 
            } 
            tag += '</span>';
            return tag;
        }
        else {
            return '';
        }

    },
    title_last_modified: function() {
        return '<span class="lecture_mark_title_date">' + '#' + this.id + '&nbsp;' + this.last_modified + '</span>&nbsp;';
    },
    title_user: function() {
        return '<span class="lecture_mark_title_user">' + this.user + '</span>&nbsp;';
    }
}

////////////////////////////////////////////////////////
////////////////////////////////////////////////////////
LaController = Class.create();
LaController.CONFIGURE_URL = '/la/configure_comments.html';
LaController.CONFIGURE_WINDOW_NAME = 'la_configure';
LaController.MODERATE_URL = '/la/moderate_comments.html';
LaController.MODERATE_WINDOW_NAME = 'la_moderate';
LaController.ADD_URL = '/la/add_comments.html';
LaController.ADD_WINDOW_NAME = 'la_add';
LaController.MODIFY_WINDOW_NAME = 'la_modify';
LaController.DEL_URL = '/la/delete_comments.html';
LaController.DEL_WINDOW_NAME = 'la_del';
LaController.MANAGE_REPUTATION_WINDOW_NAME = 'la_manage_reputation';
LaController.MANAGE_REPUTATION_URL = '/la/manage_reputation.html';
LaController.ADD_OPINION_WINDOW_NAME = 'la_opinion';
LaController.ADD_OPINION_URL = '/la/add_opinion.html';
LaController.HELP_WINDOW_NAME = 'la_help';
LaController.HELP_URL = '/la/help.html';
LaController.POPUP_WINDOW_NAME = 'la_popup';
LaController.POPUP_URL = '/la/popup_comments.html';



LaController.prototype = {
    initialize: function() {
        //if (LaController.caller != LaController.getInstance) {
        //    throw new Error("There is no public constructor for LaController.");
        //}
        this.model = false;
        this.options = false;
        this.comments_nav = $('comments_nav');
        this.comments_toggle = $('comments_toggle');
        this.comments_configure = $('comments_configure');
        this.comments_moderate = $('comments_moderate');
        this.comments_help = $('comments_help');
        this.clicked = null;
        this.child_win = null;
    },
    loading_banner: function (flag) {
        if (flag) {
            var banner = document.createElement('span');
            banner.id = 'loading';
            this.comments_nav.appendChild(banner);
            Element.update(banner,"Загрузка комментариев...");
            Element.show(banner);
        }
        else {
            Element.remove($('loading'));
            Element.show(this.comments_toggle); 
        }
    },
    update: function() {
        if (!this.model) {
            this.loading_banner(true);
            LaModel.getInstance().loadItems();
        }
        else {
            this.loading_banner(false);
            if (this.getUser() == null) {
                this.comments_toggle.href="/user/login/";
            } 
            this.showMenu();
            this.updateViews();
        }
    },
    updateViews: function() {
        LaFactoryView.getInstance().updateViews();
        this.moveToBlock();
    },
    moveToBlock: function() {
        var url = new String(window.location);
        if(url.indexOf('#') > 0) {
            var uri_parts = url.split('#');
            try {
                Element.scrollTo(uri_parts[1]);
                LaFade.getInstance().fade(uri_parts[1]);
            }
            catch (e) {}  
        }
    },
    toggleTurnOn: function() {
        var oOptions = this.getOptions();
        oOptions.showcomments =  !oOptions.showcomments;
        this.showMenu();
        this.updateViews(); 
        this.getOptions().saveOptions(true)
    },
    configureComments: function() {
        var oOptions = this.getOptions();
        oOptions.showall =  !oOptions.showall;
        this.getOptions().saveOptions()
    },
    showMenu: function() {
        this.showTurnOn();
        this.showConfigure();
        this.showModeration();
        this.showLectureContainer();

    },
    _showHowToAdd: function() {
        if (this.showComments()) {
            if ($('comments_add_text') != null) { return;}
            var add_text = document.createElement('div');
            add_text.id = 'comments_add_text'
            $('comments_nav').appendChild(add_text);
            Element.update(add_text, 'Для добавления комментариев к объекту нажмите на нем левую кнопку мыши.');
        }
        else {
            try {
                Element.remove($('comments_add_text'));
            }
            catch (e) {} 
        }
    }, 
    showTurnOn: function() {
        if (this.showComments()) {
            Element.update(this.comments_toggle,"Выключить комментарии");
            this.comments_toggle.title="Выключить показ комментариев";
        }
        else {
            Element.update(this.comments_toggle,"Включить комментарии");
            this.comments_toggle.title="Включить показ комментариев";
        }
        Element.show(this.comments_help);
        this.comments_help.title="Как работать с комментариями";
        this._showHowToAdd(); 
    }, 
    showConfigure: function() {
        var oOptions = this.getOptions();
        if (oOptions.user != null && oOptions.showcomments) {
            if (!oOptions.showall) {
                Element.update(this.comments_configure,
                 '||&nbsp;<a class="comments_nav" href="javascript:_configureComments()">Показывать все</a>');
                this.comments_configure.title="Показывать все комментарии";
            }
            else {
                Element.update(this.comments_configure,
                 '||&nbsp;<a class="comments_nav" href="javascript:_configureComments()">Показывать свои</a>');
                this.comments_configure.title="Показывать только свои комментарии";
            }
            Element.show(this.comments_configure);
        }
        else {
            Element.hide(this.comments_configure);
        } 
    },
    showModeration: function() {
        var oOptions = this.getOptions();
        if (oOptions.user != null && oOptions.canModerate() && oOptions.showcomments) {
            Element.show(this.comments_moderate);
            this.comments_moderate.title="Модерация комментариев";
        }
        else {
            Element.hide(this.comments_moderate);
        }
    },
    showLectureContainer: function() {
        if (!this.showComments()) {
            $('lecture_container').className = 'la_lecture_container';
            $('lecture_body').className = 'la_lecture_body';
        }
        else {
            $('lecture_container').className = 'la_lecture_container_popup';
            $('lecture_body').className = 'la_lecture_body_popup';
        }
    },
    configureCallback: function(win, opts) {
        var oOptions = this.getOptions();
        this.child_win = win;
        oOptions.showall = opts['showall'];
        oOptions.inlinecolor = opts['inlinecolor'];
        oOptions.saveOptions();
    },
    getOptions: function() {
        return LaOptions.getInstance();
    },
    getUser: function() {
        return this.getOptions().user;
    },
    showComments: function() {
        return this.getOptions().showcomments;
    },
    showInline: function() {
        return this.getOptions().showinline;
    },
    showAll: function() {
        return this.getOptions().showall;
    },
    inlineColor: function() {
        return this.getOptions().inlinecolor;
    },
    selectAll: function() {
        var views = LaFactoryView.getInstance().getCommentViews();
        views.each (function(view) {
            var check = view.getCheck();
            $(check).checked = true; 
            });
    },
    deSelectAll: function() {
        var views = LaFactoryView.getInstance().getCommentViews();
        views.each (function(view) {
            var check = view.getCheck();
            $(check).checked = false; 
            });
    },
    invert: function() {
        var views = LaFactoryView.getInstance().getCommentViews();
        views.each (function(view) {
            var check = view.getCheck();
            $(check).checked = !$(check).checked;
            });
    },
    getChecked: function() {
        var aChecked = $A()
        var views = LaFactoryView.getInstance().getCommentViews();
        views.each (function(view) {
            var item = view.getItem();
            var check = view.getCheck();
            if ($(check).checked) {
                aChecked.push(item.id);
            }
            }); 
        return aChecked;
    }
};

LaController.__instance__ = null;
LaController.getInstance = function () {
    if (this.__instance__ == null) {
        this.__instance__ = new LaController();
    }
    return this.__instance__;
}




Popup = Class.create();
Popup.prototype = 
{
  initialize: function(options)
  {
    this.options = {
      url: '#',
      name: 'popup',
      width: 350,
      height: 400
    }
    Object.extend(this.options, options || {});
    this.win = window.open(this.options.url, this.options.name,
                          'width='+this.options.width+',height='+this.options.height+',status=1,resizable=1,scrollbars=1' );
    //alert (ret);
  }
}

//////////////////////////////////////////////////////
function _loadOptionsHelper(aRequest) {
    var oOptions = LaOptions.getInstance();
    oOptions.loadOptions(aRequest);
    return; 
}

function _loadItemsHelper(aRequest) {
    var oModel = LaModel.getInstance();
    oModel.loadItems(aRequest);
    return; 
}


function _toggleComments(e) {
    LaController.getInstance().toggleTurnOn();
}

function _configureComments(e) {
    LaController.getInstance().configureComments();
    //var pPopup = new Popup({url:LaController.CONFIGURE_URL, name:LaController.CONFIGURE_WINDOW_NAME});
}

function _moderateComments(e) {
    var pPopup = new Popup({width:350, height: 440, url:LaController.MODERATE_URL, name:LaController.MODERATE_WINDOW_NAME});
}

function _helpComments(e) {
    var pPopup = new Popup({width:500, height: 440, url:LaController.HELP_URL, name:LaController.HELP_WINDOW_NAME});
}

function _ajaxResponse (aRequest) {
    var resp = eval('('+ aRequest.responseText +')');
    if (resp.response == 'FAIL') {
        alert(resp.error);
    }
    else {
        alert (resp.info);
        refreshPage();
    } 
    //LaController.getInstance().child_win.win.close();
}


function _approveCommentAJAX(aRequest) {
    var resp = eval('('+ aRequest.responseText +')');
    if (resp.response == 'FAIL') {
        alert('Ошибка модификации комментария:' + resp.error);
    }
    else {
        alert ('Комментарий одобрен');
        refreshPage();
    } 
}


function _saveOptionsResponce(aRequest) {
    var resp = eval('('+ aRequest.responseText +')');
    if (resp.response == 'FAIL') {
        alert('Ошибка созранения настроек:' + resp.error);
    }
    else { 
        refreshPage();
    }
}

function _saveOptionsResponceSilence(aRequest) {
    var resp = eval('('+ aRequest.responseText +')');
    if (resp.response == 'FAIL') {
        alert('Ошибка созранения настроек:' + resp.error);
    }
    else { 
        ;
    }
}


function _addOpinionAJAX(aRequest) {
    var resp = eval('('+ aRequest.responseText +')');
    if (resp.response == 'FAIL') {
        alert('Ошибка добавления отзыва:' + resp.error);
    }
    else {
        alert ('Отзыв добавлен');
    } 
}

function _editComment(e) {
    LaModel.getInstance().editComment(e);
}

function _deleteComment(e) {
    LaModel.getInstance().deleteComment(e);
}

function _approveComment(e) {
    LaModel.getInstance().approveComment(e);
}

function _addOpinion(e) {
    LaModel.getInstance().addOpinion(e);
}


function refreshPage() {
    //window.location.reload();
    //return;
    LaMemoize.getInstance().fill_in();
    var oOptions = LaOptions.getInstance();
    var oController = LaController.getInstance();
    var oModel = LaModel.getInstance();
    var oFactory = LaFactoryView.getInstance();
    oOptions = oOptions.destroy();
    LaOptions.__instance__ = null;
    oController.options = false;
    oModel = oModel.destroy();
    LaModel.__instance__ = null;
    oController.model = false;
    oController.clicked = null;
    oFactory = oFactory.destroy();
    LaFactoryView.__instance__ = null;
    oController.update();
}


function _onFailure(aRequest, obj) {
    alert (aRequest + obj)
}

function _onException(aRequest, ex) {
    alert (aRequest + ex)
}
