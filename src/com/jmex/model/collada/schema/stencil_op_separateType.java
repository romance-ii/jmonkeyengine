/**
 * stencil_op_separateType.java
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


public class stencil_op_separateType extends com.jmex.xml.xml.Node {

	public stencil_op_separateType(stencil_op_separateType node) {
		super(node);
	}

	public stencil_op_separateType(org.w3c.dom.Node node) {
		super(node);
	}

	public stencil_op_separateType(org.w3c.dom.Document doc) {
		super(doc);
	}

	public stencil_op_separateType(com.jmex.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
		super(doc, namespaceURI, prefix, name);
	}
	
	public void adjustPrefix() {
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "face" );
				tmpNode != null;
				tmpNode = getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "face", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, true);
			new faceType4(tmpNode).adjustPrefix();
		}
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "fail" );
				tmpNode != null;
				tmpNode = getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "fail", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, true);
			new failType2(tmpNode).adjustPrefix();
		}
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "zfail" );
				tmpNode != null;
				tmpNode = getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "zfail", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, true);
			new zfailType2(tmpNode).adjustPrefix();
		}
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "zpass" );
				tmpNode != null;
				tmpNode = getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "zpass", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, true);
			new zpassType2(tmpNode).adjustPrefix();
		}
	}
	public void setXsiType() {
 		org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
		el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "stencil_op_separate");
	}

	public static int getfaceMinCount() {
		return 1;
	}

	public static int getfaceMaxCount() {
		return 1;
	}

	public int getfaceCount() {
		return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "face");
	}

	public boolean hasface() {
		return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "face");
	}

	public faceType4 newface() {
		return new faceType4(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "face"));
	}

	public faceType4 getfaceAt(int index) throws Exception {
		return new faceType4(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "face", index));
	}

	public org.w3c.dom.Node getStartingfaceCursor() throws Exception {
		return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "face" );
	}

	public org.w3c.dom.Node getAdvancedfaceCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "face", curNode );
	}

	public faceType4 getfaceValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new faceType4(curNode);
	}

	public faceType4 getface() throws Exception 
 {
		return getfaceAt(0);
	}

	public void removefaceAt(int index) {
		removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "face", index);
	}

	public void removeface() {
		removefaceAt(0);
	}

	public org.w3c.dom.Node addface(faceType4 value) {
		return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "face", value);
	}

	public void insertfaceAt(faceType4 value, int index) {
		insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "face", index, value);
	}

	public void replacefaceAt(faceType4 value, int index) {
		replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "face", index, value);
	}

	public static int getfailMinCount() {
		return 1;
	}

	public static int getfailMaxCount() {
		return 1;
	}

	public int getfailCount() {
		return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "fail");
	}

	public boolean hasfail() {
		return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "fail");
	}

	public failType2 newfail() {
		return new failType2(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "fail"));
	}

	public failType2 getfailAt(int index) throws Exception {
		return new failType2(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "fail", index));
	}

	public org.w3c.dom.Node getStartingfailCursor() throws Exception {
		return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "fail" );
	}

	public org.w3c.dom.Node getAdvancedfailCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "fail", curNode );
	}

	public failType2 getfailValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new failType2(curNode);
	}

	public failType2 getfail() throws Exception 
 {
		return getfailAt(0);
	}

	public void removefailAt(int index) {
		removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "fail", index);
	}

	public void removefail() {
		removefailAt(0);
	}

	public org.w3c.dom.Node addfail(failType2 value) {
		return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "fail", value);
	}

	public void insertfailAt(failType2 value, int index) {
		insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "fail", index, value);
	}

	public void replacefailAt(failType2 value, int index) {
		replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "fail", index, value);
	}

	public static int getzfailMinCount() {
		return 1;
	}

	public static int getzfailMaxCount() {
		return 1;
	}

	public int getzfailCount() {
		return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "zfail");
	}

	public boolean haszfail() {
		return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "zfail");
	}

	public zfailType2 newzfail() {
		return new zfailType2(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "zfail"));
	}

	public zfailType2 getzfailAt(int index) throws Exception {
		return new zfailType2(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "zfail", index));
	}

	public org.w3c.dom.Node getStartingzfailCursor() throws Exception {
		return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "zfail" );
	}

	public org.w3c.dom.Node getAdvancedzfailCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "zfail", curNode );
	}

	public zfailType2 getzfailValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new zfailType2(curNode);
	}

	public zfailType2 getzfail() throws Exception 
 {
		return getzfailAt(0);
	}

	public void removezfailAt(int index) {
		removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "zfail", index);
	}

	public void removezfail() {
		removezfailAt(0);
	}

	public org.w3c.dom.Node addzfail(zfailType2 value) {
		return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "zfail", value);
	}

	public void insertzfailAt(zfailType2 value, int index) {
		insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "zfail", index, value);
	}

	public void replacezfailAt(zfailType2 value, int index) {
		replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "zfail", index, value);
	}

	public static int getzpassMinCount() {
		return 1;
	}

	public static int getzpassMaxCount() {
		return 1;
	}

	public int getzpassCount() {
		return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "zpass");
	}

	public boolean haszpass() {
		return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "zpass");
	}

	public zpassType2 newzpass() {
		return new zpassType2(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "zpass"));
	}

	public zpassType2 getzpassAt(int index) throws Exception {
		return new zpassType2(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "zpass", index));
	}

	public org.w3c.dom.Node getStartingzpassCursor() throws Exception {
		return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "zpass" );
	}

	public org.w3c.dom.Node getAdvancedzpassCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "zpass", curNode );
	}

	public zpassType2 getzpassValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new zpassType2(curNode);
	}

	public zpassType2 getzpass() throws Exception 
 {
		return getzpassAt(0);
	}

	public void removezpassAt(int index) {
		removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "zpass", index);
	}

	public void removezpass() {
		removezpassAt(0);
	}

	public org.w3c.dom.Node addzpass(zpassType2 value) {
		return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "zpass", value);
	}

	public void insertzpassAt(zpassType2 value, int index) {
		insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "zpass", index, value);
	}

	public void replacezpassAt(zpassType2 value, int index) {
		replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "zpass", index, value);
	}

}
