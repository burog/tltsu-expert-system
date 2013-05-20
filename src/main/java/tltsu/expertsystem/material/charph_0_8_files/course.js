function course_show_info(on) 
{
	var cs = document.getElementById('course_stat');
	var m_on = document.getElementById('lecture_show_info_on');
	var m_off = document.getElementById('lecture_show_info_off');
	if(on) {
		cs.style.display='block';
		m_on.style.display='none';
		m_off.style.display='block';
	}
	else {
		cs.style.display='none';
		m_on.style.display='block';
		m_off.style.display='none';
	}
}

function popup_diploma (o,width,height)
{
    var url=o.href;
    if (width  != null && width  != '')    width =  'width='+width +',';
    if (height != null && height != '')   height = 'height='+height+',';

    window.open(url,'', width+height+'toolbars=no,status=yes,location=no,resizable=yes,scrollbars=yes,menubar=yes');
    return false;
}

function getElementsByClass(searchClass,node,tag) {
	var classElements = new Array();
	if ( node == null )
		node = document;
	if ( tag == null )
		tag = '*';
	var els = node.getElementsByTagName(tag);
	var elsLen = els.length;
	for (i = 0, j = 0; i < elsLen; i++) {
		if ( els[i].className ==  searchClass) {
			classElements[j] = els[i];
			j++;
		}
	}
	return classElements;
}


function toggle_write_to_us()
{
	return;
	var cl = 'lecture_write_to_us';
	var es = getElementsByClass(cl);
	var elsLen = es.length;
	for (i = 0, j = 0; i < elsLen; i++) {
		var o = es[i];
//alert(i+'('+o+')'+' ['+o.style.paddingLeft+']');
		if(o.style.paddingLeft < 20)
		{
			o.style.paddingLeft = '30 px';
		}
		else {
			o.style.paddingLeft = '4 px';
		}
	}
}

function set_annotation_video()
{
	return;
	var la = document.getElementById('lecture_annotation');
	var lac = document.getElementById('lecture_annotation_content');
	var n = getElementsByClass('lecture_video_annotation');
	if( !la || !lac || !n ) return;
	n = n[0];
//	var x = document.createTextNode('zzzzzzzzzzzzz');
//	alert(n);
//	n.appendChild(x);
//	n.style.display = 'none';
//	n.appendChild(lac.cloneNode());
	n.appendChild(lac);
	la.style.display = 'none';
}

var is_set_video_reclama = false;

function set_video_reclama()
{	
	if(is_set_video_reclama) return;
	var la = document.getElementById('video_reclama');
	var n = getElementsByClass('lecture_video_reclama');
	n = n[0];
	if(!n) return;
	var x = document.createTextNode('zzzzzzzzzzzzz');
//	n.appendChild(x);
	n.appendChild(la);
//return;
//	n.style.width = '4cm';
	is_set_video_reclama = true;
}


function lecture_video_new_window(o,content_id)
{
	var url = o.href;
	var width  = 490;
	var height = 420;
	if (width  != null && width  != '')    width =  'width='+width +',';
	if (height != null && height != '')   height = 'height='+height+',';

	window.open(url,'', width+height+'toolbars=no,status=no,location=no,resizable=yes,scrollbars=no,menubar=no');
	expand_video(content_id,false);
	return false;
}

function  expand_video(id,expand)
{
	var v = document.getElementById(id);
	var i = document.getElementById(id+'_off');
//	alert(id+' '+expand+' | '+v+' '+i);
	if(!expand) {
		var t = v;
		v = i;
		i = t;
	}
	v.style.display='block';
	i.style.display='none';
}

function  video_popup_fitWindow(id)
{
    // запрещаем изменение размеров для окон, которые не были открыты из скрипта
//    if ( window.opener == null ) return;

    var el = getElement(id);
    if (el)
    {
        // временно отключаем событие, дабы не вызывать его своими же манипуляциями с окном
        el.onresize = "return true";
        // сначала раскрываем окно на весь экран, чтобы работчая облась заняла максимум свободного места
//        window.resizeTo(window.screen.availWidth, window.screen.availHeight);


	var dw = 40;
	var dh = 10;

        var newHeight = el.offsetHeight + dh;

        if (newHeight > window.screen.availHeight)
            newHeight = window.screen.availHeight;

        var newWidth = el.offsetWidth + dw;
        if (newWidth > window.screen.availWidth)
            newWidth = window.screen.availWidth;

        window.resizeTo(newWidth, newHeight);
    }
	
}

function  on_load_lecture()
{
}


function  set_video_counter() {
	var o = document.getElementById('video_counter');
	if(!o) return;
	var avalue=getElementsByClass('value',o,'div');
	if(!avalue.length) return;
	var value=avalue[0];
	value.innerHTML = counter_video;
}

function  set_course_video_sd_stat(s) {
	for (i = 0; i < s.length; i++) {
		var r = s[i];
		var num = r[0];
		var vs = r[2];
		var vd = r[3];
		var ovs = document.getElementById('vs'+num);
		if(ovs) {
			ovs.innerHTML=vs;
		}
		var ovd = document.getElementById('vd'+num);
		if(ovd) {
			ovd.innerHTML=vd;
		}
	}	
}


function  set_display(s,di) {
	var d = document.getElementById(s);
	if(!d) return;
	d.style.display=di;
}


function  set_className(s,di) {
	var d = document.getElementById(s);
	if(!d) return;
	d.className=di;
}


function  sign_up_set_subscribe(id) {
	if(!course_info['is_auth']) {
		return;
	}
	var s = course_info['spec'][id];
	if(!s) return;
	if(s['passed']) {
		set_className('prog_title_'+id,"vars_of_edu_selected");
		set_className('prog_price_'+id,"spec_price_selected");
		set_display('submit_sign_up_'+id,"none");
		set_display('submit_sign_up_'+id+"_passed","block");
	} else if(s['recorded']) {
		set_className('prog_title_'+id,"vars_of_edu_selected");
		set_className('prog_price_'+id,"spec_price_selected");
		set_display('submit_sign_up_'+id,"none");
		set_display('submit_sign_up_'+id+"_edit","block");
	}
	else if(course_info['specall']['passed']) {
		var d = document.getElementById('submit_'+id);
/*		alert('xxx '+id+' = ['+d+']'); */
		if(d) d.value='юЇюЁьшЄ№';
	}
	
}

