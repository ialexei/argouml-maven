package org.argouml.ui;

import java.util.Hashtable;
import java.util.Properties;

import javax.swing.Action;
import javax.swing.ImageIcon;

import org.argouml.i18n.Translator;
import org.tigris.gef.base.ModeBroom;
import org.tigris.gef.base.ModeSelect;

/**
 * Extends GEF CmdSetMode to add additional metadata such as tooltips.
 * 
 * @author Jeremy Jones
**/
public class CmdSetMode extends org.tigris.gef.base.CmdSetMode {

    private static final String ACTION_PREFIX_KEY = "action.new";

    public CmdSetMode(Properties args) {
        super(args);
    }

    public CmdSetMode(Class modeClass) {
        super(modeClass);
    }

    public CmdSetMode(Class modeClass, String name) {
        super(modeClass, name);
        putToolTip(name);
    }

    public CmdSetMode(Class modeClass, String name, String tooltip) {
        super(modeClass, name);
        putToolTip(tooltip);
    }

    public CmdSetMode(Class modeClass, boolean sticky) {
        super(modeClass, sticky);
    }

    public CmdSetMode(Class modeClass, Hashtable modeArgs) {
        super(modeClass, modeArgs);
    }
    
    public CmdSetMode(Class modeClass, Hashtable modeArgs, String name) {
    	super(modeClass, name);
    	_modeArgs = modeArgs;
    }

    public CmdSetMode(Class modeClass, String arg, Object value) {
        super(modeClass, arg, value);
    }

    public CmdSetMode(Class modeClass, String arg, Object value, String name) {
        super(modeClass, arg, value, name);
        putToolTip(name);
    }

    public CmdSetMode(
        Class modeClass,
        String arg,
        Object value,
        String name,
        ImageIcon icon) {
        super(modeClass, arg, value, name, icon);
        putToolTip(name);
    }

    /**
     * Adds tooltip text to the Action.
     */
    private void putToolTip(String name) {
        Class desiredModeClass = (Class) getArg("desiredModeClass");
        if (ModeSelect.class.isAssignableFrom(desiredModeClass)
            || ModeBroom.class.isAssignableFrom(desiredModeClass)) {
            putValue(Action.SHORT_DESCRIPTION, Translator.localize(name));
        }
        else {
            putValue(Action.SHORT_DESCRIPTION, 
	    	Translator.localize(ACTION_PREFIX_KEY) + " " 
	    	+ Translator.localize(name));
        }
    }
}
