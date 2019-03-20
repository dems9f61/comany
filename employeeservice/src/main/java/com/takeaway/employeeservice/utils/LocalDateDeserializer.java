package com.takeaway.employeeservice.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.time.LocalDate;

/**
 * User: StMinko
 * Date: 20.03.2019
 * Time: 00:27
 * <p/>
 */
public class LocalDateDeserializer extends StdDeserializer<LocalDate>
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================
    // ============================  Constructors  ===========================

    protected LocalDateDeserializer()
    {
        super(LocalDate.class);
    }

    // ===========================  public  Methods  =========================

    @Override
    public LocalDate deserialize(JsonParser parser, DeserializationContext context) throws IOException
    {
        return LocalDate.parse(parser.readValueAs(String.class));
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
