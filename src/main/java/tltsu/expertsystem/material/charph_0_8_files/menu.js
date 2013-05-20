var menu_timeID = 0;
var menu_delay_time = 2000;
var menu_main_item;
var menu_href;
var menu_selected_topic;
var menu_selected_a;

var menu_items = new Array("courses", "education", "school", "shop", "work", "communication", "news", "news_rss", "help");

function menu_find_menu2selected(href) {

	href=href.replace(/\/w2k-bin\/poem\/cart.cgi(\?.*)?$/,'/user/cart/');


	for(var i=0; i<menu_items.length; ++i) {
		var n = menu_items[i];
		var ms = menu_go(n);
		if(ms) {
			var m = ms[1];
			var els = m.getElementsByTagName('a');
			for(var j=0; j<els.length; ++j) {
				var a = els[j];
				if(a.href == href) {
					menu_selected_topic=n;
					menu_selected_a=a;
					return [n,a];
				}
			}
		}
	}
	return;
}

function menu_clearTimeout() {
	if(!menu_timeID) return;
	clearTimeout(menu_timeID);
	menu_timeID = 0;
}

function menu_setTimeout() {
	menu_clearTimeout();
	menu_timeID = setTimeout( "menu_reset()", menu_delay_time );
}

function menu_main() {
	if(menu_main_item) return menu_main_item;
	menu_main_item = 'courses';
	var href = window.document.location.href;
	href=href.replace(/^http:\/\/www\.intuit\.ru\//,'/');
	menu_href=href;
	var aselected = menu_find_menu2selected(window.document.location.href);
	if(aselected) {
		menu_main_item=aselected[0];
		return menu_main_item;
	}
	if(href.match(/^\/(speciality|groups)\//)) {
		menu_main_item = 'education';
		return menu_main_item;
	}
	if(href.match(/^\/news\//)) {
		menu_main_item = 'news';
		return menu_main_item;
	}

	if(href.match(/^(\/help\/askform.xhtml|\/rating_students\/)/)) {
		menu_main_item = 'communication';
		return menu_main_item;
	}
	if(href.match(/^\/help\//)) {
		menu_main_item = 'help';
		return menu_main_item;
	}
	if(href.match(/^\/shop\//)) {
		menu_main_item = 'shop';
		return menu_main_item;
	}
	if(href.match(/^\/user\/(account|cart)\//)) {
		menu_main_item = 'shop';
		return menu_main_item;
	}
	if(href.match(/^\/school/)) {
		menu_main_item = 'school';
		return menu_main_item;
	}
	return menu_main_item;
}



function menu_reset() {
	menu_clearTimeout();
	menu_set(menu_main());
}

function menu_set(no) {
	for(var i=0; i<menu_items.length; ++i) {
		var n = menu_items[i];
		var ms = menu_go(n);
		if(ms) {
			var m = ms[0];
			var m2 = ms[1];
			var s = '';
			if(n == 'work') {
				s = '_new';
			}
			if(no == n) {
				m.className='active'+s;
				m2.className='menu_collections_vis'+s;
			}
			else {
				m.className=''+s;
				m2.className='menu_collections'+s;
			}
		}
	}
	var s = '';
	if(no == 'work') {
		s = '_new';
	}
	var ms = menu_go(no);
	if(ms) {
		var m = ms[0];
		var m2 = ms[1];
		m.className='active'+s;
		m2.className='menu_collections_vis'+s;
	}
	if(menu_selected_a) {
		//alert ('!!!');
		menu_selected_a.className='active'+s;
	}
}

function menu_go(no) {
	var m = document.getElementById('menu_'+no);
	if(!m) 	return;
	var sm = document.getElementById('cmenu_'+no);
	if(!sm)	sm = document.getElementById('cmenu_default');
	if(!sm)	return;
	return new Array(m,sm);
}


function menu_mov(no) {
	menu_set(no);
	menu_clearTimeout();
}

function menu_mot(no) {
	menu_setTimeout();
}

function menu2_mov() {
	menu_clearTimeout();
}


function menu2_mot() {
	menu_setTimeout();
}
