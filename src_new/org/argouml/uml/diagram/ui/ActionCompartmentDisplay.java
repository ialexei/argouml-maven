// $Id$
// Copyright (c) 1996-2001 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

// File: ActionCompartmentDisplay.java
// Classes: ActionCompartmentDisplay
// Original Author: your email address here
// $Id$

// 8 Apr 2002: Jeremy Bennett (mail@jeremybennett.com). Extended to support
// compartments for extension points on use cases.


package org.argouml.uml.diagram.ui;

import org.argouml.uml.diagram.static_structure.ui.*;
import org.argouml.uml.diagram.use_case.ui.*;
import org.argouml.uml.ui.UMLAction;
import org.tigris.gef.base.*;
import org.tigris.gef.presentation.*;
import java.awt.event.*;
import java.util.*;


/**
 * <p>A class to implement the actions involved in hiding and showing
 *   compartments on interfaces, classes and use cases.</p>
 *
 * <p>This implementation extended to handle compartments for extension points
 *   on use cases.</p>
 *
 * <p>The class declares a number of static instances, each with an
 *   actionPerformed method that performs the required action.</p>
 */

public class ActionCompartmentDisplay extends UMLAction {


    ///////////////////////////////////////////////////////////////////////////
    //
    // Instance variables
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>A flag to indicate whether the action should show or hide the
     *   relevant compartment.</p>
     */
    private boolean display = false;


    /**
     * <p>A string indicating the action desired.</p>
     */
    private String compartment = "";


    ///////////////////////////////////////////////////////////////////////////
    //
    // Class variables
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>Static instance to show the attribute compartment of a class.</p>
     */
    public static UMLAction ShowAttrCompartment =
        new ActionCompartmentDisplay(true, "action.show-attribute-compartment");


    /**
     * <p>Static instance to hide the attribute compartment of a class.</p>
     */

    public static UMLAction HideAttrCompartment =
        new ActionCompartmentDisplay(false, 
                "action.hide-attribute-compartment");


    /**
     * <p>Static instance to show the operation compartment of a class.</p>
     */

    public static UMLAction ShowOperCompartment =
        new ActionCompartmentDisplay(true, "action.show-operation-compartment");


    /**
     * <p>Static instance to hide the operation compartment of a class.</p>
     */

    public static UMLAction HideOperCompartment =
        new ActionCompartmentDisplay(false, 
				     "action.hide-operation-compartment");


    /**
     * <p>Static instance to show the extension point compartment of a use
     *   case.</p>
     */

    public static UMLAction ShowExtPointCompartment =
        new ActionCompartmentDisplay(true,
                                     "action.show-extension-point-compartment");


    /**
     * <p>Static instance to hide the extension point compartment of a use
     *   case.</p>
     */

    public static UMLAction HideExtPointCompartment =
        new ActionCompartmentDisplay(false,
                                     "action.hide-extension-point-compartment");


    /**
     * <p>Static instance to show both compartments of a class.</p>
     */

    public static UMLAction ShowAllCompartments =
        new ActionCompartmentDisplay(true, "action.show-all-compartments");


    /**
     * <p>Static instance to hide both compartments of a class.</p>
     */

    public static UMLAction HideAllCompartments =
        new ActionCompartmentDisplay(false, "action.hide-all-compartments");


    ///////////////////////////////////////////////////////////////////////////
    //
    // constructors
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>Constructor for a new instance. Can only be called by this class or
     * its children, since used to create static instances only.</p>
     *
     * @param d  <code>true</code> if the compartment is to be shown,
     *           <code>false</code> if it is to be hidden.
     *
     * @param c  The text to be displayed for this action.
     */

    protected ActionCompartmentDisplay(boolean d, String c) {

        // Invoke the parent constructor

	super(c, NO_ICON);

        // Save copies of the parameters

	display = d;
	compartment = c;
    }


    ///////////////////////////////////////////////////////////////////////////
    //
    // main methods
    //
    ///////////////////////////////////////////////////////////////////////////

    /**
     * <p>Action method invoked when an event triggers this action.</p>
     *
     * <p>The {@link #compartment} instance variable defines the action to
     *   take, and the {@link #display} instance variable whether it should
     *   set visibility or note.</p>
     *
     * <p><em>Note</em>. The {@link #display} instance variable is really
     *   redundant. Its value is implied by the operation.</p>
     *
     * @param ae  The event that triggered us.
     */

    public void actionPerformed(ActionEvent ae) {

        // Only do anything if we have a single item selected (surely this
        // should work for multiple selections as well?).

	Vector sels = Globals.curEditor().getSelectionManager().selections();

	if ( sels.size() == 1 ) {
	    Selection sel = (Selection) sels.firstElement();
	    Fig       f   = sel.getContent();

            // Perform the action

	    if (compartment.equals("action.show-attribute-compartment")) {
		((FigClass) f).setAttributeVisible(display);
            }
	    else if (compartment.equals("action.hide-attribute-compartment")) {
		((FigClass) f).setAttributeVisible(display);
            }
	    else if (compartment.equals("action.show-operation-compartment")
		  || compartment.equals("action.hide-operation-compartment")) {
		if (f instanceof FigClass)
			((FigClass) f).setOperationVisible(display);
		if (f instanceof FigInterface)
			((FigInterface) f).setOperationVisible(display);
            }
	    else if (compartment.equals(
                "action.show-extension-point-compartment")) {
		((FigUseCase) f).setExtensionPointVisible(display);
            }
	    else if (compartment.equals(
                "action.hide-extension-point-compartment")) {
		((FigUseCase) f).setExtensionPointVisible(display);
            }
	    else if (compartment.equals("action.show-all-compartments")) {
		((FigClass) f).setAttributeVisible(display);
		((FigClass) f).setOperationVisible(display);
	    }
	    else {
		((FigClass) f).setAttributeVisible(display);
		((FigClass) f).setOperationVisible(display);
	    }
	}
    }


    /**
     * <p>Indicate whether this action should be enabled.</p>
     *
     * <p>Always returns <code>true</code> in this implementation.</p>
     *
     * @return  <code>true</code> if the action should be enabled,
     *          <code>false</code> otherwise. Always returns <code>true</code>
     *          in this implementation.
     */

    public boolean shouldBeEnabled() {
	return true;
    }

} /* end class ActionCompartmentDisplay */



