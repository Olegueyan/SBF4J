package net.olegueyan.sbf4j.logger;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class Sbf4jFormatter extends Formatter
{
    private static final DateFormat df = new SimpleDateFormat("dd/MM/yyyy - hh:mm:ss");

    public String format(LogRecord record)
    {
        StringBuilder builder = new StringBuilder(1000);
        builder.append("[").append(df.format(new Date(record.getMillis()))).append("]");
        builder.append(" { ").append(record.getSourceClassName()).append(" } ");
        builder.append(record.getLevel()).append(" : ");
        builder.append(formatMessage(record));
        builder.append("\n");
        return builder.toString();
    }

    public String getHead(Handler h)
    {
        return super.getHead(h);
    }

    public String getTail(Handler h)
    {
        return super.getTail(h);
    }
}