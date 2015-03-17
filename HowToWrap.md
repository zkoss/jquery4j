

# Introduction #

ZK5 has adapted famous jQuery as underlying JavaScript library.
jQuery UI is also famous and promising.
jQuery UI's widgets can be easily wrapped to zk widget now, and enjoy the power of ZK.

# Benefit #

Any pure client widget can now easily connect to server.
ZK provides many server side ability.
Such as DataModel, Databindng, Event Handling.

You can use jQuery UI widget in more simplified syntax in zul, expression language is available too.
In extreme cases, you can even use only java without any markup language or JavaScript, just like ZK always do.
You can also modify widget option at run time with java.

And the best of all, you can modify the content of underlying data model with java.
Then the widget content will be updated accordingly.

Through databinding, your interaction with client UI widget can automatically sync with java bean in server.

# How to wrap #

Since it is only a wrapping, it's far more easy than implementing a whole new actual ZK component.

## setup files ##

  * lang-addon.xml : Define component-name, component-class, widget-class, and load needed CSS file
  * zk.wpd : Define widget package and load needed JavaScript file.
  * zk.xml : Setting to make debug JavaScript possible. Includes debug-js, org.zkoss.web.classWebResource.cache

Please download source code at [jQuery4j](http://code.google.com/p/jquery4j/). It's concrete to work.

## JavaScript part ##

In most common cases, you have to implement 4 methods:

### [$define](http://www.zkoss.org/javadoc/latest/jsdoc/_global_/zk.html) ###
It's the catcher to set initial options from renderProperties() in server. Also It's the catcher of setter function from server to modify widget option after it is initialized.

### redraw ###
Provide html content as jQuery UI expected. The rest job will be delegated to jQuery UI in `bind_`.

### `bind_` ###
Client event handler may send Event back to server. You may search "fire" in `*.js` for example code. Get options sended from renderProperties() in java. And call jQuery UI api to init it.
note:If an field of option is undefined, jQuery UI will use its default value

### `unbind_` ###
Release jQuery UI widget, and zk widget.

## Java part ##

Jobs includes updating data to client, and handle client event.

### getter, setter of fields ###
smartUpdate() inside setter will effect at client side #define Setter may do some check to see if the input value is acceptable. Some datatype conversion may need extra work. For example "2,2" inside `<datepicker numberOfMonths="2,2"/>` is String, and need to be converted int[.md](.md).

### renderProperties ###
It's used to initialize widget properties. Properties rendered will be catched by $define in client side, value would be available at client side redraw, `bind_`

### Event Handling ###
You may use ZK defined event, or define your own event. To make your own event work, you have to do following things:

In Server side
  * addClientEvent(): Adds an event that the client might send to the server
  * service(): Processes an AU request. Event reported from client is handled here first. Update field in server side, postEvent() for further event handling.
  * Register event handler at controller for test: Event without event handler may be not send to save network bandwidth.

In Client side
  * all fire("eventname",DataObject):It will send event to notify server
note:eventName please follow onXXX naming convention.

## Code example ##

Because the field related code is much the same, you may define some template for different data type.
It's more easy to code and less error.

For example, related code for a boolean option field disabled

In java:
```
private boolean _disabled = false;

public boolean getDisabled() {
	return _disabled;
}

public void setDisabled(boolean disabled) {
	if(_disabled != disabled){
		_disabled = disabled;
		smartUpdate("disabled", _disabled);					
	}
}

//inside renderProperties
if(_disabled)
	render(renderer, "disabled", _disabled);
	(*if false, try renderer.render("disabled", _disabled))
```

In JavaScript
```
//inside $define
disabled :  function(){
	$(this.$n('cnt')).datepicker('option','disabled',this._disabled);
},

//options for init inside bind_
,disabled: this._disabled	
```

### Execution sequence for both	server and client ###

For example `<datepicker disabled="true"/>` sets the option disabled in zul, the execution sequence would be like:

  1. setDisabled:Modify `_disabled` variable in server.
  1. renderProperties: Render the `_disabled` property to client side
  1. $define:catch renderProperties(), and set client side variables of widget `_disabled`
  1. redraw:`_disabled` is available now. note:If it's `_model`, can be used to change html output here. Please refer to source code of accordion, search "model")
  1. `bind_`:Use client side variable `_disabled` to initialize jQuery UI widget.

## Test ##

To test ZK component functionality , you may test several use case:

```
<datepicker id="target" numberOfMonths="2,2"/>
```

It can test if rendering is OK, and if String conversion to field is OK.

```
<button label="setNumberOfMonths(1)" onClick="target.setNumberOfMonths(1)"/>
```

It can test if setter works fine.
note:Some jQuery UI option not work as we think, you may test in pure html first

```
Datepicker target;	
public void onSelection$target(Event e) {
	System.out.println("Datepicker selection happen : "+ e);
}
```

To see if event is send accordingly.



# Demo Model Usage #
```
<zscript>
<![CDATA[
	import org.zkoss.jquery4j.jqueryui.accordion.AccordionModel;    
	String[][] model = {{"t1","c1"},{"t2","c2"}};
	String[][] model2 = {{"tc1","cc1"},{"tc2","cc2"},{"tc3","cc3"}};
	AccordionModel tm = new AccordionModel(model);
	AccordionModel tm2 = new AccordionModel(model2);
	]]>
</zscript>

<accordion id="target" model="${tm}"/>
<button label="setModel(date)">
	<attribute name="onClick"><![CDATA[		
		target.setModel(tm2);
		]]>
	</attribute>    
</button>	
```

A [demo](http://docs.zkoss.org/images/6/66/Accordion_sample_1.swf)

In above example, you can see the content of accordion is changed as we set different model to it.

# Differences between wrapped widget and ZK widget #

  * Childable: Currently wrapped jQuery UI widget must be leaf node of component tree. We're trying to found a way to implement such childable feature.

  * styling: Since the html structure of widget is managed by jQuery UI, its styling does not follow ZK's rule. You can use [jQuery UI Themeroller](http://jqueryui.com/themeroller/) to customize

# Summary #
Although jQuery UI has only 8 widgets now, but it has planned to provide more in [roadmap](http://wiki.jqueryui.com/). In [jQuery4j](http://code.google.com/p/jquery4j/), we already wrapped all 8 widgets.

There is still jobs to perfect this project. Including databinding, and childable component.

It's fun to have many other little widgets to enrich ZK. It's easier to wrap jQuery UI widget than jQuery widget than JavaScript widget. Since the former followes more coding discipline, less "surprise" to overcome.
Sometimes, widget from different source may conflict. For example, a fancy calculator jQuery Widget conflict
with jQuery UI library, because jQuery UI library rewrites some jQuery function show().

It also provide another way to implement your own ZK component.
You may implement jQuery UI widget first, and then wrap it to ZK component.

# See Also #
  * [jQuery4j](http://code.google.com/p/jquery4j/) in google code.
  * [ZK5: Component Development Guide](http://docs.zkoss.org/wiki/ZK5:_Component_Development_Guide)
  * [jQuery UI](http://jqueryui.com/home)
  * [Roadmap of jQuery UI](http://wiki.jqueryui.com/)
  * [ZK5: Component Development Guide](http://docs.zkoss.org/wiki/ZK5:_Component_Development_Guide)
# Download #
[jQuery4j](http://code.google.com/p/jquery4j/) : Link /src,/src/archive as java src folder in Eclipse