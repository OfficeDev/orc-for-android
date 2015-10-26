package com.microsoft.services.orc.serialization.impl;

import org.joda.time.Period;

import java.text.ParseException;

public class DurationSerializer {
    /**
     * Deserializes Duration
     * @param strVal the str val
     * @return the period
     * @throws ParseException the parse exception
     */
    public static Period deserialize(String strVal) throws ParseException {
        return new Period(strVal);
    }

    /**
     * Serialize string.
     *
     * @param src the src
     * @return the string
     */
    public static String serialize(Period src) {

        return src.toString();
    }
}
