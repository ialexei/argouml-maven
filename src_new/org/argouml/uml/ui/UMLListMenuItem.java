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

package org.argouml.uml.ui;
import javax.swing.*;

import org.apache.log4j.Logger;

import java.lang.reflect.*;
import java.awt.event.*;

/**
 *   This class extends JMenuItem to invoke a method upon selection.
 *   The method must have the form of "void method(int index);".
 *   @author Curt Arnold
 *
 * @deprecated as of ArgoUml 0.13.5 (10-may-2003),
 *             replaced by nothing?,
 *             this class is part of the 'old'(pre 0.13.*) 
 *             implementation of proppanels
 *             that used reflection a lot.
 */
public class UMLListMenuItem extends JMenuItem implements ActionListener {
    private static final Logger LOG = Logger.getLogger(UMLListMenuItem.class);

    private Object actionObj;
    private int index;
    private Method action;
    static final Class[] ARGCLASS = {
	int.class 
    };
    
    /**
     *   Creates a new menu item.
     *   @param caption Caption for menu item.
     *   @param theActionObj object on which method will be invoked.
     *   @param theAction name of method.
     *   @param theIndex integer value passed to method, 
     *                   typically position in list.
     */
    public UMLListMenuItem(String caption, Object theActionObj, 
            String theAction, int theIndex) {
        super(caption);
        actionObj = theActionObj;
        index = theIndex;

        //
        //  it would be a little more efficient to resolve the
        //     action only when the popup was invoked, however
        //     this will identify bad "actions" more readily
        try {
            action = actionObj.getClass().getMethod(theAction, ARGCLASS);
        }
        catch (Exception e) {
            LOG.error("Exception in " + action + " popup.", e);
            setEnabled(false);
        }
        
        addActionListener(this);
    }

    /**
     *   This method is invoked when the menu item is selected.
     *   @param event the event that invoked the menu
     */
    public void actionPerformed(final java.awt.event.ActionEvent event) {
        try {
	    Object[] argValue = {
		new Integer(index)
	    };
            action.invoke(actionObj, argValue);
        }
        catch (InvocationTargetException ex) {
            LOG.error(ex.getTargetException().toString() 
                + " is InvocationTargetException in " 
                + "UMLListMenuItem.actionPerformed()", ex);
        }
        catch (Exception e) {
            LOG.error(e.toString() 
                + " in UMLListMenuItem.actionPerformed()", e);
        }
    }
}
