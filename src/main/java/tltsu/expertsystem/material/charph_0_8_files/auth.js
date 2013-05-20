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

var cookie_roles = get_cookie('roles');

var is_auth = has_cookie('eduuid');
var is_inspector = cookie_roles.indexOf('inspector')>=0?true:false;
var is_lector    = cookie_roles.indexOf('lector')>=0?true:false;
var is_gins = cookie_roles.indexOf('gins')>=0?true:false;


function show_if_(is_vis,o) {
	var e = document.getElementById(o);
	if(!e) return;
//	e.style.display=is_vis? 'block' : 'none';
	e.style.display=is_vis? '' : 'none';
}


function display_mode_if(is,element_for_vis) {
	var is_vis = true;
	if(element_for_auth && !is_auth) is_vis = false;
	if(!element_for_auth && is_auth) is_vis = false;
//alert('is_vis = '+is_vis);
	return is_vis? 'block' : 'none';
}

function display_block_auth(element_for_auth) {
	var is_vis = true;
	if(element_for_auth && !is_auth) is_vis = false;
	if(!element_for_auth && is_auth) is_vis = false;
//alert('is_vis = '+is_vis);
	return is_vis? 'block' : 'none';
}

function show_block_auth(o,element_for_auth) {
	o.style.display = display_block_auth(element_for_auth);
}

function show_block_by_id_auth(o,element_for_auth) {
	var e = document.getElementById(o);
//alert(o+' | '+e);
	if(!e) return;
	show_block_auth(e,element_for_auth)
}

function show_if_auth(o) {
	show_if_(is_auth,o);
}

function show_if_not_auth(o) {
	show_if_(!is_auth,o);
}

function show_if_inspector(o) {
	show_if_(is_inspector,o);
}

function show_if_lector(o) {
	show_if_(is_lector,o);
}

function show_if_gins(o) {
	show_if_(is_gins,o);
}


function write_login() {
//	alert('zzz');
	document.write(get_cookie('edulogin'));
}

function set_login_at_form() {
	var login = get_cookie('edulogin');
	var form = document.getElementById('login_form');
	if(!form) return;
	form.login.value=login;
}

function open_auth_window(o,w,opt) {
	var url = o.href;
	if(is_auth) {
		window.open(url,w,opt);
		return false;
	}
	var u = "window.open('"+url+"','"+w+"','"+opt+"');";
	o.href = "http://www.intuit.ru/user/login/?on_exit_js="+encodeURIComponent(u);
	return true;
}

function set_subscribe_program(id)
{
	var c = get_cookie('program');
	var p = c.indexOf('-'+id+'-');
	if(p<0) return '';
	var v1 = document.getElementById('program_'+id+'_subscribe');
	var v2 = document.getElementById('program_'+id+'_edit');
	if(!(v1 && v2)) return;
	v1.style.display = 'none';
	v2.style.display = 'block';
}

function set_subscribe_course(cid)
{
	var c = get_cookie('course');
	var p = c.indexOf('-'+cid+'-');
	if(p<0) return '';
	var v1 = document.getElementById('course_subscribe');
	var v2 = document.getElementById('course_edit');
	if(!(v1 && v2)) return;
	v1.style.display = 'none';
	v2.style.display = 'block';
}

function set_subscribe_program_intuit(cid)
{
	var c = get_cookie('course');
	var p = c.indexOf('-'+cid+'-');
	if(p<0) return '';
	var v1 = document.getElementById('program_intuit_subscribe');
	var v2 = document.getElementById('program_intuit_edit');
	if(!(v1 && v2)) return;
	v1.style.display = 'none';
	v2.style.display = 'block';
}

