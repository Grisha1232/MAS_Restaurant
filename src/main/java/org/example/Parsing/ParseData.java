package org.example.Parsing;

import javax.xml.crypto.Data;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParseData {
    public static Date ParseData(String data) throws ParseException {
        String dateStr = "2023-01-12T08:12:36";
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return dateFormat.parse(dateStr);
    }
}
