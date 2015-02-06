package org.sweet.revelation.revelation.core.convert.impl;

import java.math.BigDecimal;

public class StringToBigDecimalConverter extends SafeStringConverter<BigDecimal> {

    public String getUsage() {
        return "DECIMAL";
    }

    @Override
    protected BigDecimal doConvert(String s) {
        return new BigDecimal(s);
    }
}
