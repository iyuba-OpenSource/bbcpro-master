package com.ai.bbcpro.util;

import java.text.DecimalFormat;

public class NumberUtil {

    public static String getFormatDouble(Double data) {
        return new DecimalFormat("0.00").format(data);
    }
}
