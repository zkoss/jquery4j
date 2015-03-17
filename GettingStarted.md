

# What's jQuery4j #
It is aimed to give developer the power to control jQuery widget with purely java and markup language.

# What's ZK #
[ZK](http://www.zkoss.org) is the most proven Ajax + Mobile framework available, designed to maximize an enterprise’s operational efficiency and minimize development time and cost.

ZK implemented its widget with jQuery. Therefore, makes it easy to incorporate with other jQuery plugins.
Has its own 120+ component widget ready to use.

# Demo #

```
<datepicker id="target" numberOfMonths="2,2"/>
<button label="setNumberOfMonths(1)" onClick="target.setNumberOfMonths(1)"/>
```

A [Screencast of above code](http://docs.zkoss.org/images/6/6a/Datepicker_sample_1.swf)


In above example,
we declare <tt>datepicker</tt> in [zul](http://docs.zkoss.org/wiki/ZUML), and then modify its property in java.

# How to Use #
Java API is mapping to jQuery plugin. So you can set any option a jQuery Widget has.
For example, in the following section, you can see how to set the ''changeMonth'' option of datepicker in [zul](http://docs.zkoss.org/wiki/ZUML) and in java.

ZK introduced ''model'' concept. You can save the data needed to be presented as model, and render it at client side. It abstracts data from actual user interface.

For event, it's basically handled in server side.(Though you can also handle event in client side if you like)

## By ZUML ##

In original jQuery UI, to declare a ''datepicker'' you have to write:

```
$(function() {
	$("#datepicker").datepicker();
});

<input id="datepicker" type="text">
```

In jQuery4j, the equivalent can be achieved by zul:

```
<datepicker/>
```

To set an option when initialize datepicker by zul:

```
<datepicker changeMonth="true"/>
```

To get the date choosed by datepicker by zul:

```
<datepicker id="target"/>
<button label="show" onClick="target.getDate()"/>
```

target.getDate() will get the date choosed by datepicker whose id is "target".

note: Some component has naming conflict with original ZK component, for such component, we'll add 'jq' as prefix, stands for 'jQuery'

## By Pure Java ##

A [richlet](http://docs.zkoss.org/wiki/SWING) is a small Java program that creates all necessary components in response to user's request.
You can compose the UI by purely java, without html, ZUML. The programming logic is much like swing.

For example:
The mapping richlet for

```
<datepicker changeMonth="true" />
```

is:
```
package org.zkoss.jquerydemo;

import org.zkoss.jquery4j.jqueryui.datepicker.Datepicker;
import org.zkoss.zk.ui.GenericRichlet;
import org.zkoss.zk.ui.Page;

public class DatepickerRichlet extends GenericRichlet {

	public void service(Page page) {
		Datepicker comp = new Datepicker();
		comp.setChangeMonth(true);
		comp.setPage(page);
	}
}
```

note: You have to properly setup web.xml and zk.xml to make richlet work. Please refer to [richlet](http://docs.zkoss.org/wiki/SWING) chapter of [ZK developer's guide](http://docs.zkoss.org/wiki/Developer%27s_Guide).

## Model Concept ##
```
<zscript>
<![CDATA[
	import org.zkoss.jquery4j.jqueryui.accordion.AccordionModel;    
	String[][] model = {{"t1","c1"},{"t2","c2"}};	
	AccordionModel tm = new AccordionModel(model);
	]]>
</zscript>

<accordion model="${tm}"/>
```

ZK will use the data inside model, to render html, then call ''accordion'' command of jQuery UI .

note: Currently, model implementation not yet provide much flexibility of jQuery, but it can be approved. Just give me some time to catch up.

## Event ##
Event happened in widget will be send back to server side.
For example
```
<jqbutton label="demo" onClick='alert("clicked")' />
```
When you click the button, onClick event is send back to server. And server will call alert(), a pure java function.
alert() will send a response back to client side, ask browser to show an message window.

If you want to handle the event without server, please refer to [ZK: Client Side Programming](http://docs.zkoss.org/wiki/Client_Side_Programming)

# Download #
  * [jquery4j.war](http://jquery4j.googlecode.com/files/jQuery4j-demo-0.8.zip) : You can run [Online Demo](http://www.jquery4j.org/) at your local site, simply copy it to \webapps\ directory under Tomcat.
  * [jquery4j.jar](http://jquery4j.googlecode.com/files/jQuery4j-bin-0.8.zip) : If you already have ZK environment, download this jar to your classpath, then you should be able to use these new widgets.

# See Also #
  * [ZK official site](http://www.zkoss.org)
  * [jQuery official site](http://jquery.com)
  * [jQuery UI official site](http://jqueryui.com)
  * [ZK developer guide in html](http://www.zkoss.org/doc/devguide-single/index.html)
  * [ZK developer guide in html in wiki](http://docs.zkoss.org/wiki/Developer%27s_Guide)
  * [ZK: Client Side Programming](http://docs.zkoss.org/wiki/Client_Side_Programming)
  * [ZK: How to Wrap a jQuery UI Widget](http://docs.zkoss.org/wiki/How_to_Wrap_a_jQuery_UI_Widget)