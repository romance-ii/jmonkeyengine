/**
 * SchemaInteger.java
 *
 * This file was generated by XMLSpy 2007sp2 Enterprise Edition.
 *
 * YOU SHOULD NOT MODIFY THIS FILE, BECAUSE IT WILL BE
 * OVERWRITTEN WHEN YOU RE-RUN CODE GENERATION.
 *
 * Refer to the XMLSpy Documentation for further details.
 * http://www.altova.com/xmlspy
 */


package com.jmex.xml.types;

import java.math.BigInteger;
import java.math.BigDecimal;

public class SchemaInteger implements SchemaTypeNumber {
  protected BigInteger value;
  protected boolean isempty;
  protected boolean isnull;

  // construction
  public SchemaInteger() {
    setEmpty();
  }

  public SchemaInteger(SchemaInteger newvalue) {
    value = newvalue.value;
    isempty = newvalue.isempty;
    isnull = newvalue.isnull;
  }

  public SchemaInteger(BigInteger newvalue) {
    setValue( newvalue );
  }

  public SchemaInteger(long newvalue) {
    setValue( newvalue );
  }

  public SchemaInteger(String newvalue) {
    parse( newvalue );
  }

  public SchemaInteger(SchemaType newvalue) {
    assign(newvalue);
  }

  public SchemaInteger(SchemaTypeNumber newvalue) {
    assign( (SchemaType)newvalue );
  }

  // getValue, setValue
  public BigInteger getValue() {
    return value;
  }

  public void setValue(BigInteger newvalue) {
    if( newvalue == null ) {
      isempty = true;
      isnull = true;
      value = BigInteger.valueOf(0);
      return;
    }
    value = newvalue;
    isempty = false;
    isnull = false;
  }

  public void setValue(long newvalue) {
    value = BigInteger.valueOf(newvalue);
    isempty = false;
    isnull = false;
  }

  public void parse(String s) {
	  
	String newvalue = SchemaNormalizedString.normalize(SchemaNormalizedString.WHITESPACE_COLLAPSE, s);

    if( newvalue == null )
      setNull();
    else if( newvalue.length() == 0)
      setEmpty();
    else {
      try {
        value = new BigInteger(newvalue);
        isempty = false;
        isnull = false;
      } catch( NumberFormatException e ) {
        throw new StringParseException(e);
      }
    }
  }

  public void assign(SchemaType newvalue) {
    if( newvalue == null || newvalue.isNull() )
      setNull();
    else if( newvalue.isEmpty() )
      setEmpty();
    else if( newvalue instanceof SchemaTypeNumber ) {
      value = ( (SchemaTypeNumber) newvalue).bigIntegerValue();
      isempty = false;
      isnull = false;
    }
    else
      throw new TypesIncompatibleException( newvalue, this );
  }

  public void setNull() {
    isnull = true;
    isempty = true;
    value = BigInteger.valueOf(0);
  }

  public void setEmpty() {
    isnull = false;
    isempty = true;
    value = BigInteger.valueOf(0);
  }

  // further
  public int hashCode() {
    return value.hashCode();
  }

  public boolean equals(Object obj) {
    if (! (obj instanceof SchemaInteger))
      return false;
    return value.equals( ( (SchemaInteger) obj).value);
  }

  public Object clone() {
    return new SchemaInteger( this );
  }

  public String toString() {
    if( isempty || isnull )
      return "";
    return value.toString();
  }

  public int length() {
    return toString().length();
  }

  public boolean booleanValue() {
    return value.compareTo(BigInteger.valueOf(0)) != 0;
  }

  public boolean isEmpty() {
    return isempty;
  }

  public boolean isNull() {
    return isnull;
  }

  public int compareTo(Object obj) {
    return compareTo( (SchemaInteger) obj);
  }

  public int compareTo(SchemaInteger obj) {
    return value.compareTo(obj.value);
  }

  // interface SchemaTypeNumber
  public int numericType() {
    return NUMERIC_VALUE_BIGINTEGER;
  }

  public int intValue() {
    if (value.compareTo(new BigInteger("2147483647")) > 0  || value.compareTo(new BigInteger("-2147483648")) < 0)
      throw new SchemaTypeException("Overflow: value " + value + " cannot be converted to int");
    return value.intValue();
  }

  public long longValue() {
    if (value.compareTo(new BigInteger("9223372036854775807")) > 0  || value.compareTo(new BigInteger("-9223372036854775808")) < 0)
      throw new SchemaTypeException("Overflow: value " + value + " cannot be converted to long");
    return value.longValue();
  }

  public BigInteger bigIntegerValue() {
    return value;
  }

  public float floatValue() {
    float ret = value.floatValue(); 
    if (ret  == Float.NEGATIVE_INFINITY || ret == Float.POSITIVE_INFINITY )
    	throw new SchemaTypeException("Overflow: value " + value + " cannot be converted to float");
    return ret;
  }

  public double doubleValue() {
	  double ret = value.doubleValue();
	  if (ret  == Double.NEGATIVE_INFINITY || ret == Double.POSITIVE_INFINITY )
		  throw new SchemaTypeException("Overflow: value " + value + " cannot be converted to double");
    return ret;
  }

  public BigDecimal bigDecimalValue() {
    try {
      return new BigDecimal(value.toString());
    } catch( NumberFormatException e) {
      throw new ValuesNotConvertableException(this, new SchemaDecimal( 0 ) );
    }
  }
}
