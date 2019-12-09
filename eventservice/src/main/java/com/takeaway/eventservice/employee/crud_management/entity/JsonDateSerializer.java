package com.takeaway.eventservice.employee.crud_management.entity;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.ZonedDateTime;

/**
 * User: StMinko Date: 20.03.2019 Time: 00:15
 *
 * <p>
 */
public class JsonDateSerializer extends JsonSerializer<ZonedDateTime>
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================
    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    @Override
    public void serialize(ZonedDateTime value, JsonGenerator jsonGenerator, SerializerProvider provider) throws IOException
    {
        jsonGenerator.writeString(value.toLocalDate()
                                       .toString());
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
