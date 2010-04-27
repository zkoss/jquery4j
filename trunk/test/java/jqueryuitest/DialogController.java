package jqueryuitest;

import org.zkoss.jquery4j.jqueryui.dialog.Dialog;
import org.zkoss.jquery4j.jqueryui.progressbar.Progressbar;
import org.zkoss.jquery4j.jqueryui.progressbar.events.ChangedEvent;
import org.zkoss.zk.ui.util.GenericForwardComposer;

public class DialogController extends GenericForwardComposer {
	
	
	Dialog comp1;
	
	public void onProgressbarChange$comp1(ChangedEvent e) {
		System.out.println("Dialog value changed : "+ e.getValue());
	}
	
}
