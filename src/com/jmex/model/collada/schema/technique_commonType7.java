/**
 * technique_commonType7.java
 *
 * This file was generated by XMLSpy 2007sp2 Enterprise Edition.
 *
 * YOU SHOULD NOT MODIFY THIS FILE, BECAUSE IT WILL BE
 * OVERWRITTEN WHEN YOU RE-RUN CODE GENERATION.
 *
 * Refer to the XMLSpy Documentation for further details.
 * http://www.altova.com/xmlspy
 */


package com.jmex.model.collada.schema;


public class technique_commonType7 extends com.jmex.xml.xml.Node {

	public technique_commonType7(technique_commonType7 node) {
		super(node);
	}

	public technique_commonType7(org.w3c.dom.Node node) {
		super(node);
	}

	public technique_commonType7(org.w3c.dom.Document doc) {
		super(doc);
	}

	public technique_commonType7(com.jmex.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
		super(doc, namespaceURI, prefix, name);
	}
	
	public void adjustPrefix() {
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "dynamic" );
				tmpNode != null;
				tmpNode = getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "dynamic", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, true);
			new dynamicType2(tmpNode).adjustPrefix();
		}
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "mass" );
				tmpNode != null;
				tmpNode = getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "mass", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, true);
			new TargetableFloat(tmpNode).adjustPrefix();
		}
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "mass_frame" );
				tmpNode != null;
				tmpNode = getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "mass_frame", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, true);
			new mass_frameType2(tmpNode).adjustPrefix();
		}
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "inertia" );
				tmpNode != null;
				tmpNode = getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "inertia", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, true);
			new TargetableFloat3(tmpNode).adjustPrefix();
		}
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_physics_material" );
				tmpNode != null;
				tmpNode = getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_physics_material", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, true);
			new InstanceWithExtra(tmpNode).adjustPrefix();
		}
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "physics_material" );
				tmpNode != null;
				tmpNode = getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "physics_material", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, true);
			new physics_materialType(tmpNode).adjustPrefix();
		}
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "shape" );
				tmpNode != null;
				tmpNode = getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "shape", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, true);
			new shapeType2(tmpNode).adjustPrefix();
		}
	}
	public void setXsiType() {
 		org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
		el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "technique_common");
	}

	public static int getdynamicMinCount() {
		return 0;
	}

	public static int getdynamicMaxCount() {
		return 1;
	}

	public int getdynamicCount() {
		return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "dynamic");
	}

	public boolean hasdynamic() {
		return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "dynamic");
	}

	public dynamicType2 newdynamic() {
		return new dynamicType2(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "dynamic"));
	}

	public dynamicType2 getdynamicAt(int index) throws Exception {
		return new dynamicType2(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "dynamic", index));
	}

	public org.w3c.dom.Node getStartingdynamicCursor() throws Exception {
		return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "dynamic" );
	}

	public org.w3c.dom.Node getAdvanceddynamicCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "dynamic", curNode );
	}

	public dynamicType2 getdynamicValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new dynamicType2(curNode);
	}

	public dynamicType2 getdynamic() throws Exception 
 {
		return getdynamicAt(0);
	}

	public void removedynamicAt(int index) {
		removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "dynamic", index);
	}

	public void removedynamic() {
		removedynamicAt(0);
	}

	public org.w3c.dom.Node adddynamic(dynamicType2 value) {
		return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "dynamic", value);
	}

	public void insertdynamicAt(dynamicType2 value, int index) {
		insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "dynamic", index, value);
	}

	public void replacedynamicAt(dynamicType2 value, int index) {
		replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "dynamic", index, value);
	}

	public static int getmassMinCount() {
		return 0;
	}

	public static int getmassMaxCount() {
		return 1;
	}

	public int getmassCount() {
		return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "mass");
	}

	public boolean hasmass() {
		return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mass");
	}

	public TargetableFloat newmass() {
		return new TargetableFloat(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "mass"));
	}

	public TargetableFloat getmassAt(int index) throws Exception {
		return new TargetableFloat(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "mass", index));
	}

	public org.w3c.dom.Node getStartingmassCursor() throws Exception {
		return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mass" );
	}

	public org.w3c.dom.Node getAdvancedmassCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "mass", curNode );
	}

	public TargetableFloat getmassValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new TargetableFloat(curNode);
	}

	public TargetableFloat getmass() throws Exception 
 {
		return getmassAt(0);
	}

	public void removemassAt(int index) {
		removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "mass", index);
	}

	public void removemass() {
		removemassAt(0);
	}

	public org.w3c.dom.Node addmass(TargetableFloat value) {
		return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "mass", value);
	}

	public void insertmassAt(TargetableFloat value, int index) {
		insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "mass", index, value);
	}

	public void replacemassAt(TargetableFloat value, int index) {
		replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "mass", index, value);
	}

	public static int getmass_frameMinCount() {
		return 0;
	}

	public static int getmass_frameMaxCount() {
		return 1;
	}

	public int getmass_frameCount() {
		return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "mass_frame");
	}

	public boolean hasmass_frame() {
		return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mass_frame");
	}

	public mass_frameType2 newmass_frame() {
		return new mass_frameType2(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "mass_frame"));
	}

	public mass_frameType2 getmass_frameAt(int index) throws Exception {
		return new mass_frameType2(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "mass_frame", index));
	}

	public org.w3c.dom.Node getStartingmass_frameCursor() throws Exception {
		return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "mass_frame" );
	}

	public org.w3c.dom.Node getAdvancedmass_frameCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "mass_frame", curNode );
	}

	public mass_frameType2 getmass_frameValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new mass_frameType2(curNode);
	}

	public mass_frameType2 getmass_frame() throws Exception 
 {
		return getmass_frameAt(0);
	}

	public void removemass_frameAt(int index) {
		removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "mass_frame", index);
	}

	public void removemass_frame() {
		removemass_frameAt(0);
	}

	public org.w3c.dom.Node addmass_frame(mass_frameType2 value) {
		return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "mass_frame", value);
	}

	public void insertmass_frameAt(mass_frameType2 value, int index) {
		insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "mass_frame", index, value);
	}

	public void replacemass_frameAt(mass_frameType2 value, int index) {
		replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "mass_frame", index, value);
	}

	public static int getinertiaMinCount() {
		return 0;
	}

	public static int getinertiaMaxCount() {
		return 1;
	}

	public int getinertiaCount() {
		return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "inertia");
	}

	public boolean hasinertia() {
		return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "inertia");
	}

	public TargetableFloat3 newinertia() {
		return new TargetableFloat3(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "inertia"));
	}

	public TargetableFloat3 getinertiaAt(int index) throws Exception {
		return new TargetableFloat3(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "inertia", index));
	}

	public org.w3c.dom.Node getStartinginertiaCursor() throws Exception {
		return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "inertia" );
	}

	public org.w3c.dom.Node getAdvancedinertiaCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "inertia", curNode );
	}

	public TargetableFloat3 getinertiaValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new TargetableFloat3(curNode);
	}

	public TargetableFloat3 getinertia() throws Exception 
 {
		return getinertiaAt(0);
	}

	public void removeinertiaAt(int index) {
		removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "inertia", index);
	}

	public void removeinertia() {
		removeinertiaAt(0);
	}

	public org.w3c.dom.Node addinertia(TargetableFloat3 value) {
		return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "inertia", value);
	}

	public void insertinertiaAt(TargetableFloat3 value, int index) {
		insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "inertia", index, value);
	}

	public void replaceinertiaAt(TargetableFloat3 value, int index) {
		replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "inertia", index, value);
	}

	public static int getinstance_physics_materialMinCount() {
		return 1;
	}

	public static int getinstance_physics_materialMaxCount() {
		return 1;
	}

	public int getinstance_physics_materialCount() {
		return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_physics_material");
	}

	public boolean hasinstance_physics_material() {
		return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_physics_material");
	}

	public InstanceWithExtra newinstance_physics_material() {
		return new InstanceWithExtra(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "instance_physics_material"));
	}

	public InstanceWithExtra getinstance_physics_materialAt(int index) throws Exception {
		return new InstanceWithExtra(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_physics_material", index));
	}

	public org.w3c.dom.Node getStartinginstance_physics_materialCursor() throws Exception {
		return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_physics_material" );
	}

	public org.w3c.dom.Node getAdvancedinstance_physics_materialCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_physics_material", curNode );
	}

	public InstanceWithExtra getinstance_physics_materialValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new InstanceWithExtra(curNode);
	}

	public InstanceWithExtra getinstance_physics_material() throws Exception 
 {
		return getinstance_physics_materialAt(0);
	}

	public void removeinstance_physics_materialAt(int index) {
		removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "instance_physics_material", index);
	}

	public void removeinstance_physics_material() {
		removeinstance_physics_materialAt(0);
	}

	public org.w3c.dom.Node addinstance_physics_material(InstanceWithExtra value) {
		return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "instance_physics_material", value);
	}

	public void insertinstance_physics_materialAt(InstanceWithExtra value, int index) {
		insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "instance_physics_material", index, value);
	}

	public void replaceinstance_physics_materialAt(InstanceWithExtra value, int index) {
		replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "instance_physics_material", index, value);
	}

	public static int getphysics_materialMinCount() {
		return 1;
	}

	public static int getphysics_materialMaxCount() {
		return 1;
	}

	public int getphysics_materialCount() {
		return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "physics_material");
	}

	public boolean hasphysics_material() {
		return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "physics_material");
	}

	public physics_materialType newphysics_material() {
		return new physics_materialType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "physics_material"));
	}

	public physics_materialType getphysics_materialAt(int index) throws Exception {
		return new physics_materialType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "physics_material", index));
	}

	public org.w3c.dom.Node getStartingphysics_materialCursor() throws Exception {
		return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "physics_material" );
	}

	public org.w3c.dom.Node getAdvancedphysics_materialCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "physics_material", curNode );
	}

	public physics_materialType getphysics_materialValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new physics_materialType(curNode);
	}

	public physics_materialType getphysics_material() throws Exception 
 {
		return getphysics_materialAt(0);
	}

	public void removephysics_materialAt(int index) {
		removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "physics_material", index);
	}

	public void removephysics_material() {
		removephysics_materialAt(0);
	}

	public org.w3c.dom.Node addphysics_material(physics_materialType value) {
		return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "physics_material", value);
	}

	public void insertphysics_materialAt(physics_materialType value, int index) {
		insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "physics_material", index, value);
	}

	public void replacephysics_materialAt(physics_materialType value, int index) {
		replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "physics_material", index, value);
	}

	public static int getshapeMinCount() {
		return 1;
	}

	public static int getshapeMaxCount() {
		return Integer.MAX_VALUE;
	}

	public int getshapeCount() {
		return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "shape");
	}

	public boolean hasshape() {
		return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "shape");
	}

	public shapeType2 newshape() {
		return new shapeType2(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "shape"));
	}

	public shapeType2 getshapeAt(int index) throws Exception {
		return new shapeType2(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "shape", index));
	}

	public org.w3c.dom.Node getStartingshapeCursor() throws Exception {
		return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "shape" );
	}

	public org.w3c.dom.Node getAdvancedshapeCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "shape", curNode );
	}

	public shapeType2 getshapeValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new shapeType2(curNode);
	}

	public shapeType2 getshape() throws Exception 
 {
		return getshapeAt(0);
	}

	public void removeshapeAt(int index) {
		removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "shape", index);
	}

	public void removeshape() {
		while (hasshape())
			removeshapeAt(0);
	}

	public org.w3c.dom.Node addshape(shapeType2 value) {
		return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "shape", value);
	}

	public void insertshapeAt(shapeType2 value, int index) {
		insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "shape", index, value);
	}

	public void replaceshapeAt(shapeType2 value, int index) {
		replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "shape", index, value);
	}

}
