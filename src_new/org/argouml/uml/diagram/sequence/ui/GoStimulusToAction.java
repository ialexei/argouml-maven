
// $Id$
// Copyright (c) 1996-99 The Regents of the University of California. All
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

// $Id$

package org.argouml.uml.diagram.sequence.ui;

import java.util.Collection;
import java.util.Vector;

import org.argouml.application.api.Argo;
import org.argouml.model.ModelFacade;
import org.argouml.ui.AbstractGoRule;

import ru.novosoft.uml.behavior.common_behavior.MStimulus;

public class GoStimulusToAction extends AbstractGoRule {

    public Collection getChildren(Object parent) {
        if (!(org.argouml.model.ModelFacade.isAStimulus(parent)))
            return null;
        MStimulus ms = (MStimulus) parent;
        Object action = ModelFacade.getDispatchAction(ms);
        Vector vector = new Vector();
        vector.addElement(action);
        return vector;

    }

    /**
     * @see org.argouml.ui.AbstractGoRule#getRuleName()
     */
    public String getRuleName() {
        return Argo.localize("Tree", "misc.stimulus.action");
    }
}