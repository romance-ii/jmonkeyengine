/**
 * technique_commonType2.java
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


public class technique_commonType2 extends com.jmex.xml.xml.Node {

	public technique_commonType2(technique_commonType2 node) {
		super(node);
	}

	public technique_commonType2(org.w3c.dom.Node node) {
		super(node);
	}

	public technique_commonType2(org.w3c.dom.Document doc) {
		super(doc);
	}

	public technique_commonType2(com.jmex.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
		super(doc, namespaceURI, prefix, name);
	}
	
	public void adjustPrefix() {
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "orthographic" );
				tmpNode != null;
				tmpNode = getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "orthographic", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, true);
			new orthographicType(tmpNode).adjustPrefix();
		}
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "perspective" );
				tmpNode != null;
				tmpNode = getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "perspective", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, true);
			new perspectiveType(tmpNode).adjustPrefix();
		}
	}
	public void setXsiType() {
 		org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
		el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "technique_common");
	}

	public static int getorthographicMinCount() {
		return 1;
	}

	public static int getorthographicMaxCount() {
		return 1;
	}

	public int getorthographicCount() {
		return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "orthographic");
	}

	public boolean hasorthographic() {
		return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "orthographic");
	}

	public orthographicType neworthographic() {
		return new orthographicType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "orthographic"));
	}

	public orthographicType getorthographicAt(int index) throws Exception {
		return new orthographicType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "orthographic", index));
	}

	public org.w3c.dom.Node getStartingorthographicCursor() throws Exception {
		return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "orthographic" );
	}

	public org.w3c.dom.Node getAdvancedorthographicCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "orthographic", curNode );
	}

	public orthographicType getorthographicValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new orthographicType(curNode);
	}

	public orthographicType getorthographic() throws Exception 
 {
		return getorthographicAt(0);
	}

	public void removeorthographicAt(int index) {
		removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "orthographic", index);
	}

	public void removeorthographic() {
		removeorthographicAt(0);
	}

	public org.w3c.dom.Node addorthographic(orthographicType value) {
		return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "orthographic", value);
	}

	public void insertorthographicAt(orthographicType value, int index) {
		insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "orthographic", index, value);
	}

	public void replaceorthographicAt(orthographicType value, int index) {
		replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "orthographic", index, value);
	}

	public static int getperspectiveMinCount() {
		return 1;
	}

	public static int getperspectiveMaxCount() {
		return 1;
	}

	public int getperspectiveCount() {
		return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "perspective");
	}

	public boolean hasperspective() {
		return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "perspective");
	}

	public perspectiveType newperspective() {
		return new perspectiveType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "perspective"));
	}

	public perspectiveType getperspectiveAt(int index) throws Exception {
		return new perspectiveType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "perspective", index));
	}

	public org.w3c.dom.Node getStartingperspectiveCursor() throws Exception {
		return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "perspective" );
	}

	public org.w3c.dom.Node getAdvancedperspectiveCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "perspective", curNode );
	}

	public perspectiveType getperspectiveValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new perspectiveType(curNode);
	}

	public perspectiveType getperspective() throws Exception 
 {
		return getperspectiveAt(0);
	}

	public void removeperspectiveAt(int index) {
		removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "perspective", index);
	}

	public void removeperspective() {
		removeperspectiveAt(0);
	}

	public org.w3c.dom.Node addperspective(perspectiveType value) {
		return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "perspective", value);
	}

	public void insertperspectiveAt(perspectiveType value, int index) {
		insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "perspective", index, value);
	}

	public void replaceperspectiveAt(perspectiveType value, int index) {
		replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "perspective", index, value);
	}

}
