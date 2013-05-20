function video_help_download(a) {
    window.open(a.href,'', 'toolbars=no,status=yes,location=no,resizable=yes,scrollbars=yes');
    return false;
}

function video_buy_dvd(url) {
	window.open(url,'','');
	return false;
}


function show_video(config) {

// config = http://www.flexis.ru/dev/intuit-dev/Intuit2/lection1.xml

var hasProductInstall = DetectFlashVer(6, 0, 65);
var hasRequestedVersion = DetectFlashVer(9, 0, 0);

if ( hasProductInstall && !hasRequestedVersion ) {
    var MMPlayerType = (isIE == true) ? "ActiveX" : "PlugIn";
    var MMredirectURL = window.location;
    document.title = document.title.slice(0, 47) + " - Flash Player Installation";
    var MMdoctitle = document.title;

    AC_FL_RunContent(
        "src", "/ssi/playerProductInstall",
        "FlashVars", "MMredirectURL="+MMredirectURL+'&MMplayerType='+MMPlayerType+'&MMdoctitle='+MMdoctitle+"",
        "width", "802",
        "height", "400",
        "align", "middle",
        "id", "IntuitMaxi",
        "quality", "high",
        "bgcolor", "#000000",
        "name", "IntuitMaxi",
        "allowScriptAccess","sameDomain",
        "type", "application/x-shockwave-flash",
        "pluginspage", "http://www.adobe.com/go/getflashplayer"
    );
} else if (hasRequestedVersion) {
    AC_FL_RunContent(
            "src", "/ssi/IntuitMaxi",
            "width", "802",
            "height", "400",
            "id", "IntuitMaxi",
            "allowFullScreen", "true",
            "FlashVars", "config=" + config
    );
  } else {
    var alternateContent =  'Для демонстрации лекций необходим Flash Player. '
       + '<a href=http://www.adobe.com/go/getflash/>Установить Flash</a>';
    document.write(alternateContent);
  }
}


function  _set_counter(id,cnt) {
	var o = document.getElementById(id);
	if(!o) return;
	var avalue=getElementsByClass('value',o,'div');
	if(!avalue.length) return;
	var value=avalue[0];
	value.innerHTML = cnt;
}

function  set_video_counter() {
	_set_counter('video_counter',counter_video);
	_set_counter('download_counter',counter_downlod);
	set_download();

	var o = document.getElementById('lecture_content');
	var d = document.getElementById('td-content');
	if(o && d) {
		d.innerHTML = o.innerHTML;
		o.style.display ='none';
		d.id = o.id;
		d.style.width = '90%';
	}
}

function  __set_video_counter() {
	var o = document.getElementById('video_counter');
	if(!o) return;
	var avalue=getElementsByClass('value',o,'div');
	if(!avalue.length) return;
	var value=avalue[0];
	value.innerHTML = counter_video;
}

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

function  set_download() {
	var o = document.getElementById('lecture_body');
	if(!o) return;
	var avalue=getElementsByClass('download',o,'div');
	if(!avalue.length) return;
	var d = avalue[0];
	var els = d.getElementsByTagName('a');

	var elsLen = els.length;
	var exts = ['avi','mp4','mp3','ppt'];
	for (var i = 0; i < exts.length; i++) {
		var ext = exts[i];
		if(download_rights && download_rights[ext] && !download_rights[ext]['has_rights']) {
			var h = download_rights[ext];
			var patt=new RegExp('\.'+ext+'$');
			for (var j = 0; j < elsLen; j++) {
				var a = els[j];
				var href=a.href;
				if(patt.test(href)) {
					a.style.textDecoration='line-through';
					a.title = h['text']+': '+a.title;
					a.href=h['help_url'];
				}
			}
		}
	}
}
