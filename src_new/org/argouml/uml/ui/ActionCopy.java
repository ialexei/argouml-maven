// $Id$
// Copyright (c) 1996-2004 The Regents of the University of California. All
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

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.JTextComponent;

import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.i18n.Translator;
import org.tigris.gef.base.CmdCopy;
import org.tigris.gef.base.Globals;

/** @stereotype singleton
 */
public class ActionCopy extends AbstractAction implements CaretListener {

    ////////////////////////////////////////////////////////////////
    // static variables

    private static ActionCopy _Instance = new ActionCopy();

    private static final String LOCALIZE_KEY = "action.copy";

    ////////////////////////////////////////////////////////////////
    // constructors

    private ActionCopy() {
        super(Translator.localize(LOCALIZE_KEY));
        Icon icon =
            ResourceLoaderWrapper.getResourceLoaderWrapper()
	        .lookupIconResource(
				    Translator.getImageBinding(LOCALIZE_KEY),
				    Translator.localize(LOCALIZE_KEY));
        if (icon != null) {
            putValue(Action.SMALL_ICON, icon);
	}
        putValue(
		 Action.SHORT_DESCRIPTION,
		 Translator.localize(LOCALIZE_KEY) + " ");
    }

    public static ActionCopy getInstance() {
        return _Instance;
    }

    private JTextComponent _textSource;

    /**
     * Copies some text or a fig
     */
    public void actionPerformed(ActionEvent ae) {
        if (_textSource != null) {
            _textSource.copy();
            Globals.clipBoard = null;            
        } else {
            CmdCopy cmd = new CmdCopy();
            cmd.doIt();            
        }
        if (isSystemClipBoardEmpty()
            && (Globals.clipBoard == null
		|| Globals.clipBoard.isEmpty())) {
            ActionPaste.getInstance().setEnabled(false);
        } else {
            ActionPaste.getInstance().setEnabled(true);
        }
    }

    /**
     * @see
     * javax.swing.event.CaretListener#caretUpdate(javax.swing.event.CaretEvent)
     */
    public void caretUpdate(CaretEvent e) {
        if (e.getMark() != e.getDot()) { // there is a selection        
            setEnabled(true);
            _textSource = (JTextComponent) e.getSource();
        } else {
            setEnabled(false);
            _textSource = null;
        }
    }

    private boolean isSystemClipBoardEmpty() {
        //      if there is a selection on the clipboard
        boolean hasContents = false;
        try {
            Object text =
                Toolkit.getDefaultToolkit().getSystemClipboard()
		    .getContents(null).getTransferData(DataFlavor.stringFlavor);
            return text == null;
        } catch (IOException ignorable) {
        } catch (UnsupportedFlavorException ignorable) {
        }
        return true;
    }

} /* end class ActionCopy */
