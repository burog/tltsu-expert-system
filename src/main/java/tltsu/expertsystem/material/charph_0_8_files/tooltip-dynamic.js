var Selectword = 
{
  cfg: null,
  symbol: "qwertyuiopasdfghjklzxcvbnmйцукенгшщзхъэждлорпавыфячсмитьбю-_&",
  skip: { 'SCRIPT': 1,'NOSCRIPT': 1,'A': 1,'H1': 1,'H2': 1,'H3': 1,'H4': 1,'H5': 1,'H6': 1,'BIG': 1,'TH': 1,'FIELDSET': 1,'TEXTAREA': 1,'SELECT': 1,'LEGEND': 1,'ACRONYM': 1,'ADDRESS': 1,'LABEL': 1, 'STYLE': 1, 'IFRAME': 1, 'NOINDEX': 1},
  ie6: false,
  w_bounds: '(^|$|[^0-9a-zA-Zа-яА-Я_])',
  
  run: function(config) {
    this.cfg = config;
  
        var n = document.getElementsByTagName("body")[0];
        
        this.prepareRegexp();
        this.scanBlocks(n);
    },
    
  prepareRegexp: function() {
    var i;
    
    this.cfg.regexp = new Array();
    this.cfg.inlined = new Array();
    
    for (i = 0; i < this.cfg.words.length; i++) {
      this.cfg.regexp[i] = new RegExp(this.w_bounds + '(' + this.cfg.words[i] + ')' + this.w_bounds, 'ig');
      this.cfg.inlined[i] = false;
    }
  },
    
  scanBlocks: function(n) {
    if (n.nodeType == 3) {
      if (n.data.replace(/\s+/g, "")) {
        this.scanWords(n);
		
      }
    } else {
      for (var i = n.firstChild; i != null; i = i.nextSibling) {
        if (this.skip[i.nodeName.toUpperCase()] == 1) {
          continue;
        }
        this.scanBlocks(i);
      }
    }
  },
    
  scanWords: function(n) {
    var i, r, nn;
    for (i = 0; i < this.cfg.regexp.length; i++) {
      if (this.cfg.inlined[i]) {
        continue;
      }
      r = this.cfg.regexp[i].exec(n.data);
      if (r != null) {
        nn = this.inlineWord(n, i, r.index + r[1].length, r[2].length);
        if (nn) {
          //Бэктрэдинг на 2 шага назад
          if(nn.previousSibling && nn.previousSibling.previousSibling) {
            
			this.scanWords(nn.previousSibling.previousSibling);
          }
          n = nn;
        }
      }
    }
  },
    
  inlineWord: function(n, i, b, l) {
    var pn = n.parentNode;
    if (pn) {
      var wn = this.makeHTML(n.data.substr(b, l), i);
      var bn = b > 0 ? document.createTextNode(n.data.substr(0, b)) : document.createTextNode("");
      var an = (b + l) < n.data.length ? document.createTextNode(n.data.substr(b + l, n.data.length - (b + l))) : document.createTextNode("");
      pn.replaceChild(an, n);
      pn.insertBefore(wn, an);
      pn.insertBefore(bn, wn);
      this.cfg.inlined[i] = true;
      return an;
    }
    
    return null;
  },
  
	selectCompany: function(index)
  {
    var rnd = Math.floor( Math.random( ) * (this.cfg.n - this.cfg.m + 1) ) + this.cfg.m;
    var weight = 0, c_id = 0;
    for(var i=0; i<this.cfg.wcb[index].length; i++)
    {
      if(rnd > weight && rnd < weight + this.cfg.wcb[index][i][1])
      {
        if(this.cfg.companys[this.cfg.wcb[index][i][0]].word_count == undefined)
          this.cfg.companys[this.cfg.wcb[index][i][0]].word_count = 0;
        c_id = i;
        break;
      }
      weight += this.cfg.wcb[index][i][1];
    }
    
    return c_id;
  },
  
  selectBanner: function(index, c_id)
  {
    var rnd = Math.floor( Math.random( ) * (this.cfg.n - this.cfg.m + 1) ) + this.cfg.m;
    var weight = 0, b_id = 0;
    for(var i=0; i<this.cfg.wcb[index][c_id][2].length; i++)
    {
      if(rnd > weight && rnd < (weight + this.cfg.wcb[index][c_id][2][i][1]))
      {
        if(this.cfg.companys[this.cfg.wcb[index][c_id][0]].banners[b_id].word_count == undefined)
          this.cfg.companys[this.cfg.wcb[index][c_id][0]].banners[b_id].word_count = 0;
        b_id = this.cfg.wcb[index][c_id][2][i][0];
        break;
      }
      weight += this.cfg.wcb[index][c_id][2][i][1];
    }
    
    return b_id;
  },
  
  rebuildWeight: function(index, c_id)
  {
    var weight = this.cfg.wcb[index][c_id][1];
    weight /= (this.cfg.wcb[index].length-1);
    this.cfg.wcb[index].splice(c_id, 1);
    var max_weight = 100;
    for(var i=0; i<this.cfg.wcb[index].length; i++)
    {
      if(i == this.cfg.wcb[index].length - 1)
        this.cfg.wcb[index][i][1] += max_weight;
      else
        this.cfg.wcb[index][i][1] += weight;
      max_weight -= this.cfg.wcb[index][i][1];
    }
  },
  
  rebuildBannerWeight: function(index, c_id, b_id)
  {
    var weight = this.cfg.wcb[index][c_id][2][b_id];
    weight /= (this.cfg.wcb[index][c_id][2].length-1);
    this.cfg.wcb[index][c_id][2].splice(b_id, 1);
    var max_weight = 100;
    for(var i=0; i<this.cfg.wcb[index][c_id][2].length; i++)
    {
      if(i == this.cfg.wcb[index][c_id][2].length - 1)
        this.cfg.wcb[index][c_id][2][i][1] += max_weight;
      else
        this.cfg.wcb[index][c_id][2][i][1] += weight;
      max_weight -= this.cfg.wcb[index][c_id][2][i][1];
    }
  },
  
  wordSelect: function(n, word, j)
  {
    var p = n.parentNode;
    if(p) {
      var wn = this.makeHTML(word[2], j);
      var bn = word[0] != 0 ? document.createTextNode(n.data.substr(0, word[0])) : document.createTextNode("");
      var ln = word[0] + word[1] != n.data.length ? document.createTextNode(n.data.substring(word[0] + word[1], n.data.length)) : document.createTextNode("");
      p.replaceChild(ln, n);
      p.insertBefore(wn, ln);
      p.insertBefore(bn, wn);
      return ln;
    }
    
    return null;
  },
	
	makeHTML: function(str, index)
	{
    //Select company
    var rnd = Math.floor( Math.random( ) * (this.cfg.n - this.cfg.m + 1) ) + this.cfg.m;
    var c_id = 0, cc_id = 0, b_id = null;
    var s = document.createElement("SPAN");
   
    while(b_id == null)
    {
      while(this.cfg.wcb[index].length > 0)
      {
        c_id = this.selectCompany(index);
        cc_id = this.cfg.wcb[index][c_id][0];
        if(this.cfg.companys[cc_id].word_count >= this.cfg.companys[cc_id].max_word)
          this.rebuildWeight(index, c_id);
        else
          break;
      }
      if(this.cfg.wcb[index].length == 0) {
        s.appendChild(document.createTextNode(str));
        return s;
      }
      
      while(this.cfg.wcb[index][c_id][2].length > 0)
      {
        b_id = this.selectBanner(index, c_id);
        if(this.cfg.companys[cc_id].banners[b_id].word_count >= this.cfg.companys[cc_id].banners[b_id].max_word)
          this.rebuildBannerWeight(index, c_id, b_id);
        else
          break;
      }
      if(this.cfg.wcb[index][c_id][2].length == 0) 
      {
        this.cfg.wcb[index].splice(c_id, 1);
        b_id = null;
      }
    }

    this.cfg.companys[cc_id].word_count++;
    this.cfg.companys[cc_id].banners[b_id].word_count++;
    //Build template for word
    var template = this.cfg.wtemplates[this.cfg.companys[cc_id].banners[b_id].tpl_word_id];
    var pos = template.indexOf("[[<word>]]");
    
    var s = document.createElement("SPAN");
    if(-1 != pos) {
      s.innerHTML = '<span onmouseover="doTooltip(event, this, ' + this.cfg.wcb[index][c_id][0] + ', ' + b_id + ')" onmouseout="hideTip(this)">' + template.substring(0, pos) + str + template.substr(pos + 10, template.length - 10 - pos) + '</span>';
    } else {
      s.appendChild(document.createTextNode(str));
    }
    
		return s;
	}
}

// Безопасные обработчики сообщений от элементов управления
// Показывает всплывающее окно
function doTooltip(e, who, c_id, b_id) 
{
	if ( typeof Tooltip == "undefined" || !Tooltip.ready ) return;

  if(who)
  {
    Tooltip.oldStyle = who.outerHTML;
		who.className = "tooltip2";
  }
    
  var msg = '';
	var template = ContentLinkCfg.templates[ContentLinkCfg.companys[c_id].banners[b_id].tpl_id];
	var pos = template.indexOf("[[<content>]]");
	if(-1 != pos)
		msg = template.substring(0, pos) + ContentLinkCfg.companys[c_id].banners[b_id].code + template.substr(pos + 13, template.length - 13 - pos);
	Tooltip.reset();
	Tooltip.show(e, msg);
}

//Скрывает всплывающее окно
function hideTip(who) 
{
	if ( typeof Tooltip == "undefined" || !Tooltip.ready ) return;
		Tooltip.hide();

	if(who)
  {
    who.outerHTML = Tooltip.oldStyle;
  }
}

//Скрывает несмотря ни на что
function mustDie()
{
	Tooltip.reset();
	Tooltip.toggleVis(Tooltip.tipID,"hidden", true);
}

var Tooltip = 
{
	captureEvent: false,
	offX: 8,
	offY: 12,
	tipID: "tipDiv",
	showDelay: 100,
	hideDelay: 1000,
  oldStyle: '',
	    
	ready:false, timer:null, tip:null,
	
	//Работа с самим окошком
	dragLeft: 50, dragTop: 5, dragWidth: 100, dragHeight: 20, 
	move: false, oldX: 0, oldY: 0,
	canHide: false, visible: false, img_count: 0,
	  
	//Инициализируем окно
	init: function(time2show, time2autoclose, ofX, ofY) 
	{
		if ( document.createElement && document.body && !document.getElementById(this.tipID)) 
		{
			var el = document.createElement("DIV");
			el.id = this.tipID; 
			el.className = "tipDiv";

			document.body.appendChild(el);
			this.ready = true;
			
			if (!this.captureEvent) // привязываем окно к мыши
			{	 
				dw_event.add( el, "mouseover", this.overMouse, true );
				dw_event.add( el, "mouseout", this.outMouse, true );
			}

			this.showDelay = time2show;
			this.hideDelay = time2autoclose;
			this.offX = ofX;
			this.offY = ofY;
		}
	},

	//Сбрасывает настройки для tooltipa
	reset: function()
	{
		if(!this.ready) return;
    
		this.canHide = false;
    this.img_count = 0;
	},
  
  autoclose: function()
  {
    if(this.tip == null) return;
    this.nodeTree(this.tip);
  },
  
  nodeTree: function(el)
  {
    if(el.tagName == 'IMG') 
    {
      if(navigator.appName == 'Microsoft Internet Explorer')
        eval('el.onload = function () {Tooltip.img_count--;}');
      else
        el.onload = Tooltip.img_count--;
      //prop(el);
      this.img_count++;
    }
    for(var i=0;i<el.childNodes.length;i++)
    {
      this.nodeTree(el.childNodes[i]);
    }
  },
  
	//Отображаем окно
	show: function(e, msg) 
	{
		if (this.timer) 
		{ 
			clearTimeout(this.timer);	
			this.timer = 0; 
		}
		
		this.tip = document.getElementById( this.tipID );
		this.writeTip("");  // сначала очищаем строку
		this.writeTip(msg);
		viewport.getAll();
		this.positionTip(e);
    this.autoclose();
		//Задаем ппериод обновления окна
		this.timer = setTimeout("Tooltip.toggleVis('" + this.tipID + "', 'visible', false)", this.showDelay);
	},
	    
	//Записываем строку
	writeTip: function(msg) 
	{
		if ( this.tip && typeof this.tip.innerHTML != "undefined" ) 
		this.tip.innerHTML = msg;
	},
	    
	//Вычисляем позицию окна
	positionTip: function(e) 
	{
		if( this.tip && this.tip.style ) 
		{
			var x = e.pageX? e.pageX: e.clientX + viewport.scrollX;
			var y = e.pageY? e.pageY: e.clientY + viewport.scrollY;
			    
			//Смещение по x
			if ( x + this.tip.offsetWidth + this.offX > viewport.width + viewport.scrollX ) 
			{
				x = x - this.tip.offsetWidth - this.offX;
				if ( x < 0 ) 
					x = 0;
			} 
			else 
				x = x + this.offX;
			        
			//Смещение по y
			if ( y + this.tip.offsetHeight + this.offY > viewport.height + viewport.scrollY ) 
			{
				y = y - this.tip.offsetHeight - this.offY;
				if ( y < viewport.scrollY ) 
					y = viewport.height + viewport.scrollY - this.tip.offsetHeight;
			} 
			else 
				y = y + this.offY;
			            
			//Заносим результаты в стиль элемента
			this.tip.style.left = x + "px"; 
			this.tip.style.top = y + "px";
		}
	},
	    
	//Скрывает окно
	hide: function()
	{
		if (this.timer) 
		{ 
			clearTimeout(this.timer);	
			this.timer = 0; 
		}
		
		this.timer = setTimeout("Tooltip.toggleVis('" + this.tipID + "', 'hidden', false)", this.hideDelay);
		this.tip = null; 
	},
	
	//Обновляет окно
	toggleVis: function(id, vis, always) 
	{
    if(!always)
    {
      if(this.img_count <= 0) 
        Tooltip.canHide = true;
      if(!Tooltip.canHide & vis == "hidden")
        return;
    }
		if(vis == "visible")
			Tooltip.visible = true;
		else
			Tooltip.visible = false;
		var el = document.getElementById(id);
		if (el) 
			el.style.visibility = vis;
	},
	
	//Определяем координаты элемента
	getBounds: function(element)
	{
		var left = element.offsetLeft;
		var top = element.offsetTop;
		for (var parent = element.offsetParent; parent; parent = parent.offsetParent)
		{
			left += parent.offsetLeft - parent.scrollLeft;
			top += parent.offsetTop - parent.scrollTop
		}
		return {left: left, top: top, width: element.offsetWidth, height: element.offsetHeight};
	},
	    
	//Обработчик мыши
	trackMouse: function(e) 
	{		
		e = dw_event.DOMit(e);
		if(!Tooltip.visible) return;
		var el = document.getElementById(Tooltip.tipID)
		if(!el) return;
		var size = Tooltip.getBounds(el);
		if(!size) return;
	},

	//Навели курсор	
	overMouse: function(e)
	{
		e = dw_event.DOMit(e);
		if(Tooltip.timer)
		{
			clearTimeout(Tooltip.timer);
			Tooltip.timer = 0;
		}
	},
	
	//Убрали курсор
	outMouse: function(e)
	{
		e = dw_event.DOMit(e);
		if(Tooltip.timer)
		{
			clearTimeout(Tooltip.timer);
			Tooltip.timer = 0;
		}

		//Проверка на уход в какой либо внутренний элемент
		var el = document.getElementById(Tooltip.tipID);
		var size = Tooltip.getBounds(el);
		if(!size) return;
		Tooltip.dragLeft += 2;
		if(((size.left + 4 < e.x) & (size.left + size. width - 5 > e.x))
			& ((size.top + 4 < e.y) & (size.top + size.height - 5 > e.y)))
			return;	
		
		Tooltip.timer = setTimeout("Tooltip.toggleVis('" + Tooltip.tipID + "', 'hidden', false)", Tooltip.hideDelay);
	}
}
