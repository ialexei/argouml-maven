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

package org.argouml.ui;

import java.beans.PropertyVetoException;
import java.util.Collection;
import java.util.Iterator;
import java.util.Vector;

import org.argouml.cognitive.ItemUID;
import org.argouml.kernel.ProjectManager;
import org.argouml.util.ChangeRegistry;
import org.argouml.model.ModelFacade;

import org.tigris.gef.base.Diagram;
import org.tigris.gef.base.Editor;
import org.tigris.gef.presentation.Fig;

public class ArgoDiagram extends Diagram {

    ItemUID _id;

    // hack to use vetocheck in constructing names
    protected static ArgoDiagram TheInstance = new ArgoDiagram(); 
  
    ////////////////////////////////////////////////////////////////
    // constructors

    public ArgoDiagram() { 
        super();
        // really dirty hack to remove unwanted listeners
        getLayer().getGraphModel().removeGraphEventListener(getLayer());
    }

    public ArgoDiagram(String diagramName ) {
  	// next line patch to issue 596 (hopefully)
  	super(diagramName);
	try { setName(diagramName); }
	catch (PropertyVetoException pve) { }
    }

    ////////////////////////////////////////////////////////////////
    // accessors

    public void setName(String n) throws PropertyVetoException {
	super.setName(n);
    }

    public void setItemUID(ItemUID id) {
	_id = id;
    }

    public ItemUID getItemUID() {
	return _id;
    }

    ////////////////////////////////////////////////////////////////
    // event management

    public void addChangeRegistryAsListener( ChangeRegistry change ) {
	getGraphModel().addGraphEventListener( change );
    }

    public void removeChangeRegistryAsListener( ChangeRegistry change ) {
	getGraphModel().removeGraphEventListener( change );
    }

    static final long serialVersionUID = -401219134410459387L;

    /**
     * TODO: The reference to the method
     * org.argouml.uml.ui.VetoablePropertyChange#getVetoMessage(String)
     * was here but the class does exist anymore. Where is it?
     *
     * @param propertyName is the name of the property
     * @return a message or null if not applicable.
     */
    public String getVetoMessage(String propertyName) {
    	if (propertyName.equals("name")) {
	    return "Name of diagram may not exist already";
    	}
        return null;
    }

  

    /**
     * Finds the presentation (the Fig) for some object. If the object
     * is a modelelement that is contained in some other modelelement
     * that has its own fig, that fig is returned. It extends
     * presentationFor that only gets the fig belonging to the node
     * obj.<p>
     *
     * @author jaap.branderhorst@xs4all.nl
     * @return the Fig for the object
     * @param obj is th object
     */
    public Fig getContainingFig(Object obj) {
        Fig fig = super.presentationFor(obj);
        if (fig == null && ModelFacade.isAModelElement(obj)) {
	    // maybe we have a modelelement that is part of some other
            // fig
            if (ModelFacade.isAOperation(obj)
		|| ModelFacade.isAAttribute(obj)) {

                // get all the classes from the diagram
                Iterator it = getNodes().iterator();
                while (it.hasNext()) {
                    Object o = it.next();
                    if (ModelFacade.isAClassifier(o)) {
                        if (ModelFacade.getFeatures(o).contains(obj))
			    return presentationFor(o);
                    }
                }
            }
        }
        return fig;
    }

    /**
     * @see org.tigris.gef.base.Diagram#initialize(Object)
     */
    public void initialize(Object owner) {
	super.initialize(owner);
	ProjectManager.getManager().getCurrentProject().setActiveDiagram(this);
    }
    
    public void damage() {
        if (getLayer() != null && getLayer().getEditors(null) != null) {
            Iterator it = getLayer().getEditors(null).iterator();
            while (it.hasNext()) {
                ((Editor) it.next()).damageAll();
            }
        }
    }

    /**
     * @see Diagram#getEdges(Collection)
     */
    public Collection getEdges(Collection c) {
        if (getGraphModel() != null) {
            return getGraphModel().getEdges();
        }
        return super.getEdges(null);
    }

    /**
     * @deprecated 0.15.3 in favour of getNodes(Collection)
     */
    public Vector getNodes() {
        if (getGraphModel() != null) {
            return getGraphModel().getNodes();
        }
        return new Vector (getNodes(null));
    }
    
    /**
     * @see Diagram#getNodes(Collection)
     */
    public Collection getNodes(Collection c) {
        if (getGraphModel() != null) {
            return getGraphModel().getNodes();
        }
        return super.getNodes(c);
    }
    
    public String toString() {
        return "Diagram: " + _name;
    }
    

} /* end class ArgoDiagram */
