/**
 * gles_texture_unit.java
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

import com.jmex.xml.types.SchemaNCName;

public class gles_texture_unit extends com.jmex.xml.xml.Node {

	public gles_texture_unit(gles_texture_unit node) {
		super(node);
	}

	public gles_texture_unit(org.w3c.dom.Node node) {
		super(node);
	}

	public gles_texture_unit(org.w3c.dom.Document doc) {
		super(doc);
	}

	public gles_texture_unit(com.jmex.xml.xml.Document doc, String namespaceURI, String prefix, String name) {
		super(doc, namespaceURI, prefix, name);
	}
	
	public void adjustPrefix() {
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Attribute, null, "sid" );
				tmpNode != null;
				tmpNode = getDomNextChild( Attribute, null, "sid", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, false);
		}
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "surface" );
				tmpNode != null;
				tmpNode = getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "surface", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, true);
		}
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler_state" );
				tmpNode != null;
				tmpNode = getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler_state", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, true);
		}
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "texcoord" );
				tmpNode != null;
				tmpNode = getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "texcoord", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, true);
			new texcoordType(tmpNode).adjustPrefix();
		}
		for (	org.w3c.dom.Node tmpNode = getDomFirstChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "extra" );
				tmpNode != null;
				tmpNode = getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "extra", tmpNode )
			) {
			internalAdjustPrefix(tmpNode, true);
			new extraType(tmpNode).adjustPrefix();
		}
	}
	public void setXsiType() {
 		org.w3c.dom.Element el = (org.w3c.dom.Element) domNode;
		el.setAttributeNS("http://www.w3.org/2001/XMLSchema-instance", "xsi:type", "gles_texture_unit");
	}

	public static int getsidMinCount() {
		return 0;
	}

	public static int getsidMaxCount() {
		return 1;
	}

	public int getsidCount() {
		return getDomChildCount(Attribute, null, "sid");
	}

	public boolean hassid() {
		return hasDomChild(Attribute, null, "sid");
	}

	public SchemaNCName newsid() {
		return new SchemaNCName();
	}

	public SchemaNCName getsidAt(int index) throws Exception {
		return new SchemaNCName(getDomNodeValue(getDomChildAt(Attribute, null, "sid", index)));
	}

	public org.w3c.dom.Node getStartingsidCursor() throws Exception {
		return getDomFirstChild(Attribute, null, "sid" );
	}

	public org.w3c.dom.Node getAdvancedsidCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Attribute, null, "sid", curNode );
	}

	public SchemaNCName getsidValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new SchemaNCName(getDomNodeValue(curNode));
	}

	public SchemaNCName getsid() throws Exception 
 {
		return getsidAt(0);
	}

	public void removesidAt(int index) {
		removeDomChildAt(Attribute, null, "sid", index);
	}

	public void removesid() {
		removesidAt(0);
	}

	public org.w3c.dom.Node addsid(SchemaNCName value) {
		if( value.isNull() )
			return null;

		return  appendDomChild(Attribute, null, "sid", value.toString());
	}

	public org.w3c.dom.Node addsid(String value) throws Exception {
		return addsid(new SchemaNCName(value));
	}

	public void insertsidAt(SchemaNCName value, int index) {
		insertDomChildAt(Attribute, null, "sid", index, value.toString());
	}

	public void insertsidAt(String value, int index) throws Exception {
		insertsidAt(new SchemaNCName(value), index);
	}

	public void replacesidAt(SchemaNCName value, int index) {
		replaceDomChildAt(Attribute, null, "sid", index, value.toString());
	}

	public void replacesidAt(String value, int index) throws Exception {
		replacesidAt(new SchemaNCName(value), index);
	}

	public static int getsurfaceMinCount() {
		return 0;
	}

	public static int getsurfaceMaxCount() {
		return 1;
	}

	public int getsurfaceCount() {
		return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "surface");
	}

	public boolean hassurface() {
		return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "surface");
	}

	public SchemaNCName newsurface() {
		return new SchemaNCName();
	}

	public SchemaNCName getsurfaceAt(int index) throws Exception {
		return new SchemaNCName(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "surface", index)));
	}

	public org.w3c.dom.Node getStartingsurfaceCursor() throws Exception {
		return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "surface" );
	}

	public org.w3c.dom.Node getAdvancedsurfaceCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "surface", curNode );
	}

	public SchemaNCName getsurfaceValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new SchemaNCName(getDomNodeValue(curNode));
	}

	public SchemaNCName getsurface() throws Exception 
 {
		return getsurfaceAt(0);
	}

	public void removesurfaceAt(int index) {
		removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "surface", index);
	}

	public void removesurface() {
		removesurfaceAt(0);
	}

	public org.w3c.dom.Node addsurface(SchemaNCName value) {
		if( value.isNull() )
			return null;

		return  appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "surface", value.toString());
	}

	public org.w3c.dom.Node addsurface(String value) throws Exception {
		return addsurface(new SchemaNCName(value));
	}

	public void insertsurfaceAt(SchemaNCName value, int index) {
		insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "surface", index, value.toString());
	}

	public void insertsurfaceAt(String value, int index) throws Exception {
		insertsurfaceAt(new SchemaNCName(value), index);
	}

	public void replacesurfaceAt(SchemaNCName value, int index) {
		replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "surface", index, value.toString());
	}

	public void replacesurfaceAt(String value, int index) throws Exception {
		replacesurfaceAt(new SchemaNCName(value), index);
	}

	public static int getsampler_stateMinCount() {
		return 0;
	}

	public static int getsampler_stateMaxCount() {
		return 1;
	}

	public int getsampler_stateCount() {
		return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler_state");
	}

	public boolean hassampler_state() {
		return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler_state");
	}

	public SchemaNCName newsampler_state() {
		return new SchemaNCName();
	}

	public SchemaNCName getsampler_stateAt(int index) throws Exception {
		return new SchemaNCName(getDomNodeValue(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler_state", index)));
	}

	public org.w3c.dom.Node getStartingsampler_stateCursor() throws Exception {
		return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler_state" );
	}

	public org.w3c.dom.Node getAdvancedsampler_stateCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler_state", curNode );
	}

	public SchemaNCName getsampler_stateValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new SchemaNCName(getDomNodeValue(curNode));
	}

	public SchemaNCName getsampler_state() throws Exception 
 {
		return getsampler_stateAt(0);
	}

	public void removesampler_stateAt(int index) {
		removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler_state", index);
	}

	public void removesampler_state() {
		removesampler_stateAt(0);
	}

	public org.w3c.dom.Node addsampler_state(SchemaNCName value) {
		if( value.isNull() )
			return null;

		return  appendDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler_state", value.toString());
	}

	public org.w3c.dom.Node addsampler_state(String value) throws Exception {
		return addsampler_state(new SchemaNCName(value));
	}

	public void insertsampler_stateAt(SchemaNCName value, int index) {
		insertDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler_state", index, value.toString());
	}

	public void insertsampler_stateAt(String value, int index) throws Exception {
		insertsampler_stateAt(new SchemaNCName(value), index);
	}

	public void replacesampler_stateAt(SchemaNCName value, int index) {
		replaceDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "sampler_state", index, value.toString());
	}

	public void replacesampler_stateAt(String value, int index) throws Exception {
		replacesampler_stateAt(new SchemaNCName(value), index);
	}

	public static int gettexcoordMinCount() {
		return 0;
	}

	public static int gettexcoordMaxCount() {
		return 1;
	}

	public int gettexcoordCount() {
		return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "texcoord");
	}

	public boolean hastexcoord() {
		return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "texcoord");
	}

	public texcoordType newtexcoord() {
		return new texcoordType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "texcoord"));
	}

	public texcoordType gettexcoordAt(int index) throws Exception {
		return new texcoordType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "texcoord", index));
	}

	public org.w3c.dom.Node getStartingtexcoordCursor() throws Exception {
		return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "texcoord" );
	}

	public org.w3c.dom.Node getAdvancedtexcoordCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "texcoord", curNode );
	}

	public texcoordType gettexcoordValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new texcoordType(curNode);
	}

	public texcoordType gettexcoord() throws Exception 
 {
		return gettexcoordAt(0);
	}

	public void removetexcoordAt(int index) {
		removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "texcoord", index);
	}

	public void removetexcoord() {
		removetexcoordAt(0);
	}

	public org.w3c.dom.Node addtexcoord(texcoordType value) {
		return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "texcoord", value);
	}

	public void inserttexcoordAt(texcoordType value, int index) {
		insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "texcoord", index, value);
	}

	public void replacetexcoordAt(texcoordType value, int index) {
		replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "texcoord", index, value);
	}

	public static int getextraMinCount() {
		return 0;
	}

	public static int getextraMaxCount() {
		return Integer.MAX_VALUE;
	}

	public int getextraCount() {
		return getDomChildCount(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra");
	}

	public boolean hasextra() {
		return hasDomChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra");
	}

	public extraType newextra() {
		return new extraType(domNode.getOwnerDocument().createElementNS("http://www.collada.org/2005/11/COLLADASchema", "extra"));
	}

	public extraType getextraAt(int index) throws Exception {
		return new extraType(getDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra", index));
	}

	public org.w3c.dom.Node getStartingextraCursor() throws Exception {
		return getDomFirstChild(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra" );
	}

	public org.w3c.dom.Node getAdvancedextraCursor( org.w3c.dom.Node curNode ) throws Exception {
		return getDomNextChild( Element, "http://www.collada.org/2005/11/COLLADASchema", "extra", curNode );
	}

	public extraType getextraValueAtCursor( org.w3c.dom.Node curNode ) throws Exception {
		if( curNode == null )
			throw new com.jmex.xml.xml.XmlException("Out of range");
		else
			return new extraType(curNode);
	}

	public extraType getextra() throws Exception 
 {
		return getextraAt(0);
	}

	public void removeextraAt(int index) {
		removeDomChildAt(Element, "http://www.collada.org/2005/11/COLLADASchema", "extra", index);
	}

	public void removeextra() {
		while (hasextra())
			removeextraAt(0);
	}

	public org.w3c.dom.Node addextra(extraType value) {
		return appendDomElement("http://www.collada.org/2005/11/COLLADASchema", "extra", value);
	}

	public void insertextraAt(extraType value, int index) {
		insertDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "extra", index, value);
	}

	public void replaceextraAt(extraType value, int index) {
		replaceDomElementAt("http://www.collada.org/2005/11/COLLADASchema", "extra", index, value);
	}

}
