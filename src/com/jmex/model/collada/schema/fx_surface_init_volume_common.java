/**
 * fx_surface_init_volume_common.java
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


public class fx_surface_init_volume_common extends com.jmex.xml.xml.Node {

	public fx_surface_init_volume_common(fx_surface_init_volume_common node) {
		super(node);
	}

	public fx_surface_init_volume_common(org.w3c.dom.Node node) {
		super(node);
	}

	public fx_surface_init_volume_common(org.w3c.dom.Document doc) {
		super(doc);
	}

	public fx_surface_init_volume_common(com.jmex.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
		super(doc, namespaceURI, prefix, name);
	}
	
	public void adjustPrefix() {
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "all" );
				tmpNode != null;
				tmpNode = getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "all", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, true);
			new allType2(tmpNode).adjustPrefix();
		}
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "primary" );
				tmpNode != null;
				tmpNode = getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "primary", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, true);
			new primaryType2(tmpNode).adjustPrefix();
		}
	}
	public void setXsiType() {
 		org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
		el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "fx_surface_init_volume_common");
	}

	public static int getallMinCount() {
		return 1;
	}

	public static int getallMaxCount() {
		return 1;
	}

	public int getallCount() {
		return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "all");
	}

	public boolean hasall() {
		return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "all");
	}

	public allType2 newall() {
		return new allType2(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "all"));
	}

	public allType2 getallAt(int index) throws Exception {
		return new allType2(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "all", index));
	}

	public org.w3c.dom.Node getStartingallCursor() throws Exception {
		return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "all" );
	}

	public org.w3c.dom.Node getAdvancedallCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "all", curNode );
	}

	public allType2 getallValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new allType2(curNode);
	}

	public allType2 getall() throws Exception 
 {
		return getallAt(0);
	}

	public void removeallAt(int index) {
		removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "all", index);
	}

	public void removeall() {
		removeallAt(0);
	}

	public org.w3c.dom.Node addall(allType2 value) {
		return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "all", value);
	}

	public void insertallAt(allType2 value, int index) {
		insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "all", index, value);
	}

	public void replaceallAt(allType2 value, int index) {
		replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "all", index, value);
	}

	public static int getprimaryMinCount() {
		return 1;
	}

	public static int getprimaryMaxCount() {
		return 1;
	}

	public int getprimaryCount() {
		return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "primary");
	}

	public boolean hasprimary() {
		return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "primary");
	}

	public primaryType2 newprimary() {
		return new primaryType2(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "primary"));
	}

	public primaryType2 getprimaryAt(int index) throws Exception {
		return new primaryType2(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "primary", index));
	}

	public org.w3c.dom.Node getStartingprimaryCursor() throws Exception {
		return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "primary" );
	}

	public org.w3c.dom.Node getAdvancedprimaryCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "primary", curNode );
	}

	public primaryType2 getprimaryValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new primaryType2(curNode);
	}

	public primaryType2 getprimary() throws Exception 
 {
		return getprimaryAt(0);
	}

	public void removeprimaryAt(int index) {
		removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "primary", index);
	}

	public void removeprimary() {
		removeprimaryAt(0);
	}

	public org.w3c.dom.Node addprimary(primaryType2 value) {
		return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "primary", value);
	}

	public void insertprimaryAt(primaryType2 value, int index) {
		insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "primary", index, value);
	}

	public void replaceprimaryAt(primaryType2 value, int index) {
		replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "primary", index, value);
	}

}
