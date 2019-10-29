package com.takeaway.employeeservice.employee.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: StMinko Date: 20.03.2019 Time: 00:15
 *
 * <p>
 */
public class JsonDateSerializer extends JsonSerializer<Date>
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================
  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================

  @Override
  public void serialize(Date value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException
  {
    String string = new SimpleDateFormat(UsableDateFormat.DEFAULT.getDateFormat()).format(value);
    jsonGenerator.writeString(string);
  }

  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
