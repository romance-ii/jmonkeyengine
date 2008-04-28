/**
 * SchemaString.java
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

import java.math.BigDecimal;
import java.math.BigInteger;

public class SchemaString implements SchemaTypeNumber, SchemaTypeCalendar {
  protected String value;
  protected boolean isempty;
  protected boolean isnull;

  // construction
  public SchemaString() {
    setEmpty();
  }

  public SchemaString(SchemaString newvalue) {
    value = newvalue.value;
    isempty = newvalue.isempty;
    isnull = newvalue.isnull;
  }

  public SchemaString(String newvalue) {
    setValue( newvalue );
  }

  public SchemaString(SchemaType newvalue) {
    assign( newvalue );
  }

  public SchemaString(SchemaTypeNumber newvalue) {
    assign( (SchemaType)newvalue );
  }

  public SchemaString(SchemaTypeCalendar newvalue) {
    assign( (SchemaType)newvalue );
  }

  // getValue, setValue
  public String getValue() {
    return value;
  }

  public void setValue(String newvalue) {
    if( newvalue == null ) {
      isempty = true;
      isnull = true;
      value = "";
      return;
    }
    value = newvalue;
    isempty = (value.length()==0);
  }

  public void parse(String newvalue) {
    if( newvalue == null )
      setNull();
    else if( newvalue.length() == 0)
      setEmpty();
    else
      setValue(newvalue);
  }

  public void assign(SchemaType newvalue) {
    if( newvalue == null || newvalue.isNull() )
      setNull();
    else if( newvalue.isEmpty() )
      setEmpty();
    else {
      value = newvalue.toString();
      isempty = (value.length()==0);
      isnull = false;
    }
  }

  public void setNull() {
    isnull = true;
    isempty = true;
    value = "";
  }

  public void setEmpty() {
    isnull = false;
    isempty = true;
    value = "";
  }

  // further
  public int hashCode() {
    return value.hashCode();
  }

  public boolean equals(Object obj) {
    if (! (obj instanceof SchemaString))
      return false;
    return value.equals( ( (SchemaString) obj).value);
  }

  public Object clone() {
    if ( isnull ) {
      SchemaString result = new SchemaString();
      result.setNull();
      return result;
    }
    else if ( isempty ) 
      return new SchemaString();
    return new SchemaString( new String( value ) );
  }

  public String toString() {
    if( isempty || isnull )
      return "";
    return value;
  }

  public int length() {
    return value.length();
  }

  public boolean booleanValue() {
    if( value==null || value.length()==0 || value.compareToIgnoreCase("false")==0 )
      return false;
    if (isValueNumeric())
      return bigDecimalValue().compareTo(BigDecimal.valueOf(0)) != 0;
    return true;
  }

  public boolean isEmpty() {
    return isempty;
  }

  public boolean isNull() {
    return isnull;
  }

  public int compareTo(Object obj) {
    return compareTo( (SchemaString) obj);
  }

  public int compareTo(SchemaString obj) {
    return value.compareTo(obj.value);
  }

  public boolean isValueNumeric() {
    try {
      BigDecimal tmp = new BigDecimal(value.trim());
    }
    catch (NumberFormatException e) {
      return false;
    }
    return true;
  }

  // ---------- interface SchemaTypeNumber ----------
  public int numericType() {
    return NUMERIC_VALUE_BIGDECIMAL;
  }

  public int intValue() {
    if( isnull || isempty || value.length() == 0)
      return 0;
    try {
      return Integer.parseInt(value);
    } catch( NumberFormatException e ) {
      throw new ValuesNotConvertableException( this, new SchemaInt( 0 ) );
    }
  }

  public long longValue() {
    if( isnull || isempty || value.length() == 0)
      return 0;
    try {
      return Long.parseLong(value);
    } catch( NumberFormatException e ) {
      throw new ValuesNotConvertableException( this, new SchemaLong( 0 ) );
    }
  }

  public BigInteger bigIntegerValue() {
    if( isnull || isempty || value.length() == 0)
      return new BigInteger( "0" );
    try {
      return new BigInteger(value.trim());
    } catch( NumberFormatException e ) {
      throw new ValuesNotConvertableException( this, new SchemaInteger( 0 ) );
    }
  }

  public float floatValue() {
    if( isnull || isempty || value.length() == 0)
      return 0;
    try {
      return Float.parseFloat(value);
    } catch( NumberFormatException e ) {
      throw new ValuesNotConvertableException( this, new SchemaFloat( 0 ) );
    }
  }

  public double doubleValue() {
    if( isnull || isempty || value.length() == 0)
      return 0.0;
    try {
      return Double.parseDouble(value);
    } catch( NumberFormatException e ) {
      throw new ValuesNotConvertableException( this, new SchemaDouble( 0 ) );
    }
  }

  public BigDecimal bigDecimalValue() {
    if( isnull || isempty || value.length() == 0)
      return new BigDecimal( 0 );
    try {
      return new BigDecimal(value.trim());
    } catch( NumberFormatException e ) {
      throw new ValuesNotConvertableException( this, new SchemaDecimal( 0 ) );
    }
  }

  // ---------- interface SchemaTypeCalendar ----------
  public int calendarType() {
    return CALENDAR_VALUE_UNDEFINED;
  }

  public SchemaDuration durationValue() {
    try {
      return new SchemaDuration(value);
    } catch( StringParseException e ) {
      throw new ValuesNotConvertableException( this, new SchemaDuration( "PT" ) );
    }
  }

  public SchemaDateTime dateTimeValue() {
    try {
      return new SchemaDateTime(value);
    } catch( StringParseException e ) {
      throw new ValuesNotConvertableException( this, new SchemaDateTime( "2003-08-06T00:00:00" ) );
    }
  }

  public SchemaDate dateValue() {
    try {
      return new SchemaDate(value);
    }
    catch (StringParseException e) {
      throw new ValuesNotConvertableException(this, new SchemaDate("2003-08-06"));
    }
  }

  public SchemaTime timeValue() {
    try {
      return new SchemaTime(value);
    }
    catch (StringParseException e) {
      throw new ValuesNotConvertableException(this, new SchemaTime("00:00:00"));
    }
  }

}
