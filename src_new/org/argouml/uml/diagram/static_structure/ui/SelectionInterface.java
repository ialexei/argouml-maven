// $Id$
// Copyright (c) 1996-2002 The Regents of the University of California. All
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

// File: SelectionInterface.java
// Classes: SelectionInterface
// Original Author: jrobbins@ics.uci.edu
// $Id$

package org.argouml.uml.diagram.static_structure.ui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;

import javax.swing.Icon;

import org.apache.log4j.Logger;
import org.argouml.application.helpers.ResourceLoaderWrapper;
import org.argouml.model.ModelFacade;
import org.argouml.model.uml.UmlFactory;
import org.argouml.uml.diagram.ui.ModeCreateEdgeAndNode;
import org.argouml.uml.diagram.ui.SelectionWButtons;
import org.tigris.gef.base.Editor;
import org.tigris.gef.base.Globals;
import org.tigris.gef.base.ModeManager;
import org.tigris.gef.base.ModeModify;
import org.tigris.gef.base.SelectionManager;
import org.tigris.gef.graph.MutableGraphModel;
import org.tigris.gef.presentation.Fig;
import org.tigris.gef.presentation.FigNode;
import org.tigris.gef.presentation.Handle;

import ru.novosoft.uml.foundation.core.MClass;
import ru.novosoft.uml.foundation.core.MClassImpl;
import ru.novosoft.uml.foundation.core.MInterface;

public class SelectionInterface extends SelectionWButtons {
    protected static Logger cat = 
        Logger.getLogger(SelectionInterface.class);
    ////////////////////////////////////////////////////////////////
    // constants
    public static Icon realiz =
	ResourceLoaderWrapper.getResourceLoaderWrapper().lookupIconResource("Realization");


    ////////////////////////////////////////////////////////////////
    // constructors

    /** Construct a new SelectionInterface for the given Fig */
    public SelectionInterface(Fig f) { super(f); }

    /** Return a handle ID for the handle under the mouse, or -1 if
     *  none. TODO: in the future, return a Handle instance or
     *  null. <p>
     *  <pre>
     *   0-------1-------2
     *   |               |
     *   3               4
     *   |               |
     *   5-------6-------7
     * </pre>
     */
    public void hitHandle(Rectangle r, Handle h) {
	super.hitHandle(r, h);
	if (h.index != -1) return;
	if (!_paintButtons) return;
	Editor ce = Globals.curEditor();
	SelectionManager sm = ce.getSelectionManager();
	if (sm.size() != 1) return;
	ModeManager mm = ce.getModeManager();
	if (mm.includes(ModeModify.class) && _pressedButton == -1) return;
	int cx = _content.getX();
	int cy = _content.getY();
	int cw = _content.getWidth();
	int ch = _content.getHeight();
	int iw = realiz.getIconWidth();
	int ih = realiz.getIconHeight();
	if (hitBelow(cx + cw / 2, cy + ch, iw, ih, r)) {
	    h.index = 11;
	    h.instructions = "Add a realization";
	}
	else {
	    h.index = -1;
	    h.instructions = "Move object(s)";
	}
    }


    /** Paint the handles at the four corners and midway along each edge
     * of the bounding box.  */
    public void paintButtons(Graphics g) {
	int cx = _content.getX();
	int cy = _content.getY();
	int cw = _content.getWidth();
	int ch = _content.getHeight();
	paintButtonBelow(realiz, g, cx + cw / 2, cy + ch, 11);
    }


    public void dragHandle(int mX, int mY, int anX, int anY, Handle hand) {
	if (hand.index < 10) {
	    _paintButtons = false;
	    super.dragHandle(mX, mY, anX, anY, hand);
	    return;
	}
	int cx = _content.getX(), cy = _content.getY();
	int cw = _content.getWidth(), ch = _content.getHeight();
	int newX = cx, newY = cy, newW = cw, newH = ch;
	Dimension minSize = _content.getMinimumSize();
	int minWidth = minSize.width, minHeight = minSize.height;
	Class edgeClass = null;
	Class nodeClass = MClassImpl.class;
	int bx = mX, by = mY;
	boolean reverse = false;
	switch (hand.index) {
	case 11: //add realization
	    edgeClass = (Class) ModelFacade.ABSTRACTION;
	    reverse = true;
	    by = cy + ch;
	    bx = cx + cw / 2;
	    break;
	default:
	    cat.warn("invalid handle number");
	    break;
	}
	if (edgeClass != null && nodeClass != null) {
	    Editor ce = Globals.curEditor();
	    ModeCreateEdgeAndNode m = new
		ModeCreateEdgeAndNode(ce, edgeClass, nodeClass, false);
	    m.setup((FigNode) _content, _content.getOwner(), bx, by, reverse);
	    ce.mode(m);
	}

    }


  
	
    public Object addRealization(MutableGraphModel mgm, MInterface cls,
				 MClass newCls) {
	return mgm.connect(newCls, cls, (Class) ModelFacade.ABSTRACTION);
    }
	
    /**
     * @see
     * org.argouml.uml.diagram.ui.SelectionWButtons#createEdgeUnder(org.tigris.gef.graph.MutableGraphModel,
     * java.lang.Object)
     */
    protected Object createEdgeUnder(MutableGraphModel gm, Object newNode) {
        return gm.connect(newNode, _content.getOwner(), (Class) ModelFacade.ABSTRACTION);
    }

    /**
     * @see org.argouml.uml.diagram.ui.SelectionWButtons#getNewNode(int)
     */
    protected Object getNewNode(int buttonCode) {
        return UmlFactory.getFactory().getCore().createClass();
    }

} /* end class SelectionInterface */

