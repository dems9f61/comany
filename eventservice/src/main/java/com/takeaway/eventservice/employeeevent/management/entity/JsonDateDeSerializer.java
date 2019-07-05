package com.takeaway.eventservice.employeeevent.management.entity;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * User: StMinko
 * Date: 20.03.2019
 * Time: 00:05
 * <p/>
 */
public class JsonDateDeSerializer extends JsonDeserializer<Date>
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================
    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    @Override
    public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException
    {
        try
        {
            return new SimpleDateFormat(UsableDateFormat.DEFAULT.getDateFormat()).parse(jsonParser.getValueAsString());
        }
        catch (ParseException caught)
        {
            throw new IOException(caught.getLocalizedMessage());
        }
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
