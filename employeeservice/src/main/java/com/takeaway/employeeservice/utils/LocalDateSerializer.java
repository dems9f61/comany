package com.takeaway.employeeservice.utils;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * User: StMinko
 * Date: 20.03.2019
 * Time: 00:24
 * <p/>
 */
public class LocalDateSerializer extends StdSerializer<LocalDate>
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================
    // ============================  Constructors  ===========================

    public LocalDateSerializer()
    {
        super(LocalDate.class);
    }

    // ===========================  public  Methods  =========================

    @Override
    public void serialize(LocalDate value, JsonGenerator generator, SerializerProvider provider) throws IOException
    {
        generator.writeString(value.format(DateTimeFormatter.ISO_LOCAL_DATE));
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
