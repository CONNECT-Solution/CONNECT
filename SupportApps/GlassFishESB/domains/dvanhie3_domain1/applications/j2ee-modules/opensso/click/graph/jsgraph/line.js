/* This notice must remain at all times.

graph.js
Copyright (c) Balamurugan S, 2005. sbalamurugan @ hotmail.com
Development support by Jexp, Inc http://www.jexp.com 

This package is free software. It is distributed under GPL - legalese removed, it means that you can use this for any purpose, but cannot charge for this software. Any enhancements you make to this piece of code, should be made available free to the general public! 

Latest version can be downloaded from http://www.sbmkpm.com

This library is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  

line.js provides a simple mechanism to draw line graphs. It  uses 
wz_jsgraphics.js  which is copyright of its author. 

Usage:
var g = new line_graph();
g.add("title1",value);
g.add("title2",value2);

g.render("canvas_name", "graph title");

//where canvas_name is a div defined INSIDE body tag 
<body>
<div id="canvas_name" style="overflow: auto; position:relative;height:300px;width:400px;"></div>


*/

hD="0123456789ABCDEF";

function d2h(d) 
{
 var h = hD.substr(d&15,1);
 while(d>15) {d>>=4;h=hD.substr(d&15,1)+h;}
 return h;
}

function h2d(h) 
{
 return parseInt(h,16);
}

function line_graph()
{
 this.ct = 0;
 
 this.data      = new Array();
 this.x_name    = new Array();
 this.max       = -64000; //MAX INT

 this.c_array = new Array();
 this.c_array[0] = new Array(255, 192, 95);
 this.c_array[1] = new Array(80, 127, 175);
 this.c_array[2] = new Array(159, 87, 112);
 this.c_array[3] = new Array(111, 120, 96);
 this.c_array[4] = new Array(224, 119, 96);
 this.c_array[5] = new Array(80, 159, 144);
 this.c_array[6] = new Array(207, 88, 95);
 this.c_array[7] = new Array(64, 135, 96);
 this.c_array[8] = new Array(239, 160, 95);
 this.c_array[9] = new Array(144, 151, 80);
 this.c_array[10] = new Array(255, 136, 80);

 this.getColor = function()
  {
   if(this.ct >= (this.c_array.length-1))
      this.ct = 0;
   else
      this.ct++;

   return "#" + d2h(this.c_array[this.ct][0]) + d2h(this.c_array[this.ct][1]) + d2h(this.c_array[this.ct][2]);
  }


 this.add = function(x_name, value)
  {
   this.x_name.push(x_name);  
   this.data.push(parseInt(value,10));

   if(value > this.max)
      this.max = parseInt(value,10);
  }

 this.render = function(canvas, title)
  {
   var jg = new jsGraphics(canvas);

   var h  = 250;
   var sx = 40;
   var dw = 15;
   var shadow = 0;
   var fnt    = 12;

   var rtmax = sx + 10 + (dw+Math.round((dw/2))+shadow)*(this.data.length);

   // Draw markers
   var i;
   jg.setColor("black");
   for(i = 1 ; i <= 5 ; i++)
     {
      jg.drawLine(0,Math.round((h/5*i)),rtmax+20,Math.round((h/5*i)));
      var ff = Math.round(this.max - (this.max / 5 * i));
      jg.drawString(ff+"",4,Math.round((h/5*i)-2));
     }

   // Draw the bar graph
   var color = this.getColor();
   var oldx, oldy;
   jg.setStroke(1);

   for(i = 0; i < this.data.length; i++)
      {
       var ht1 = Math.round(this.data[i]*h/this.max);

       if(i >= 1)
         {
          jg.setColor(color);
          jg.drawLine(oldx, h-oldy, sx, h-ht1);
	 }

       jg.setColor("red");
       jg.fillEllipse(sx-2, h-ht1-2, 5, 5);

       jg.setColor("black");
       jg.drawString(this.x_name[i], sx, h);

       oldx = sx;
       oldy = ht1;

       sx = sx+dw+Math.round(dw/2)+shadow;
      }

   jg.setFont("Verdana", fnt,  Font.BOLD);
   jg.drawStringRect(title, 0, h+fnt+4, rtmax+20, "right");
   jg.paint();
  }

}
