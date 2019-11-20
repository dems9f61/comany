package com.takeaway.employeeservice.employee.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/**
 * User: StMinko Date: 20.03.2019 Time: 00:05
 *
 * <p>
 */
public class JsonDateDeSerializer extends JsonDeserializer<ZonedDateTime>
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================
  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================

  @Override
  public ZonedDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException
  {
    DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(UsableDateFormat.DEFAULT.getDateFormat());
    LocalDate localDate = LocalDate.parse(jsonParser.getValueAsString(), dateFormatter);
    return localDate.atStartOfDay(ZoneOffset.UTC);
  }

  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}

// class Test
// {
//  public static void main(String[] args)
//  {
//    java.time.format.DateTimeFormatter dateFormatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
//    java.time.LocalDate localDate = java.time.LocalDate.parse("2011-12-03", dateFormatter);
//    ZonedDateTime zonedDateTime = localDate.atStartOfDay(ZoneOffset.UTC);
//    System.out.println("zonedDateTime = " + zonedDateTime);
//
//    System.out.println(zonedDateTime.toLocalDate()
//                                    .toString());
//  }
// }
