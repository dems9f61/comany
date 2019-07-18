package com.takeaway.eventservice.employee.crud_management.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import static com.takeaway.eventservice.employee.crud_management.entity.UsableDateFormat.Constants.DEFAULT_DATE_FORMAT;

/**
 * User: StMinko
 * Date: 23.03.2019
 * Time: 21:11
 * <p/>
 */
@Getter
@ToString
@RequiredArgsConstructor
public enum UsableDateFormat
{
    DEFAULT(DEFAULT_DATE_FORMAT);

    private final String dateFormat;

    public static class Constants
    {
        static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";

        private Constants()
        {
            throw new AssertionError("Don't meant to be initiated!");
        }
    }
}
