//Вешаем обработчик по завершению загрузки страницы
function setGlobalOnLoad(f) 
{
  var root = window.addEventListener || window.attachEvent ? window : document.addEventListener ? document : null
  if (root)
  {
    if(root.addEventListener) 
      root.addEventListener("load", f, false)
    else if(root.attachEvent) root.attachEvent("onload", f)
  } 
  else
  {
    if(typeof window.onload == 'function') 
    {
      var existing = window.onload
      window.onload = function() 
        {
          existing()
          f()
        }
    } 
    else 
    {
      window.onload = f();
    }
  }
}

//Вызывается при окончании загрузки страницы
function onLoad()
{
	//Захватываем сообщения
	Tooltip.init(time2show, time2autoclose, offX, offY);
	Selectword.run(ContentLinkCfg);
}

function trackMouse(e)
{
	Tooltip.trackMouse(e);
}

//Обработчик событий
var dw_event = 
{
	//Добавить событие
	add: function(obj, etype, fp, cap) 
	{
		cap = cap || false;
		if (obj.addEventListener) 
			obj.addEventListener(etype, fp, cap);
		else if (obj.attachEvent) 
			obj.attachEvent("on" + etype, fp);
	}, 

	//Удалить событие
	remove: function(obj, etype, fp, cap) 
	{
		cap = cap || false;
		if (obj.removeEventListener) 
			obj.removeEventListener(etype, fp, cap);
		else if (obj.detachEvent) 
			obj.detachEvent("on" + etype, fp);
	}, 

	//Возвращает параметры события
	DOMit: function(e) 
	{ 
		e = e? e: window.event;
		e.tgt = e.srcElement? e.srcElement: e.target;
    
		if (!e.preventDefault) 
			e.preventDefault = function () { return false; }
		if (!e.stopPropagation) 
			e.stopPropagation = function () { if (window.event) window.event.cancelBubble = true; }
        
		return e;
	}
}

//Работаем с позицией окна
var viewport = 
{
	//Ширина окна
	getWinWidth: function () 
	{
		this.width = 0;
		if (window.innerWidth) 
			this.width = window.innerWidth - 18;
		else if (document.documentElement && document.documentElement.clientWidth) 
			this.width = document.documentElement.clientWidth;
		else if (document.body && document.body.clientWidth) 
			this.width = document.body.clientWidth;
	},
		  
	//Высота окна
	getWinHeight: function () 
	{
		this.height = 0;
		if (window.innerHeight) 
			this.height = window.innerHeight - 18;
		else if (document.documentElement && document.documentElement.clientHeight) 
			this.height = document.documentElement.clientHeight;
		else if (document.body && document.body.clientHeight) 
			this.height = document.body.clientHeight;
	},
  
	//Скроллинг по x
	getScrollX: function () 
	{
	    	this.scrollX = 0;
	    	if (typeof window.pageXOffset == "number") 
			this.scrollX = window.pageXOffset;
		else if (document.documentElement && document.documentElement.scrollLeft)
			this.scrollX = document.documentElement.scrollLeft;
		else if (document.body && document.body.scrollLeft) 
			this.scrollX = document.body.scrollLeft; 
		else if (window.scrollX) 
			this.scrollX = window.scrollX;
	},
  
	//Скроллинг по y
	getScrollY: function () 
	{
		this.scrollY = 0;    
		if (typeof window.pageYOffset == "number") 
			this.scrollY = window.pageYOffset;
		else if (document.documentElement && document.documentElement.scrollTop)
			this.scrollY = document.documentElement.scrollTop;
		else if (document.body && document.body.scrollTop) 
			this.scrollY = document.body.scrollTop; 
		else if (window.scrollY) 
			this.scrollY = window.scrollY;
	},
  
	//Получаем все параметры
	getAll: function () 
	{
		this.getWinWidth(); 
		this.getWinHeight();
		this.getScrollX();  
		this.getScrollY();
	}
}

function CreateCompany(max_word, banners)
{
  var company = new Object();
  company.max_word = max_word;
  company.banners = new Array();
  for(var i=0; i<banners.length; i++)
  {
    banner = new Object();
    banner.code = banners[i][0];
    banner.tpl_id = banners[i][1];
    banner.tpl_word_id = banners[i][2];
    banner.max_word = banners[i][3];
    company.banners[company.banners.length] = banner;
  }
  
  return company;
}

setGlobalOnLoad(onLoad);