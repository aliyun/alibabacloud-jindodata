package com.aliyun.emr.table.accesslog;


import java.util.ArrayList;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

public class AccessLogParser extends UDF {

    public static final int OSS_LOG_COLUMN_LENGTH = 27;

    public ArrayList<Text> evaluate(String line) {
        boolean hasDot = false;
        // todo more than OSS_LOG_COLUMN_LENGTH
        ArrayList<Text> textArray = new ArrayList<Text>(OSS_LOG_COLUMN_LENGTH);
        int length = 0;
        int start = -1;
        byte[] bytes = line.getBytes();
        for (int i = 1; i < line.length(); i++) {
            char c = line.charAt(i);
            boolean isDot = c == '"' || c == '[' || c == ']';
            if (isDot) {
                hasDot = !hasDot;
                continue;
            }
            length++;
            if (start == -1) {
                start = i;
            }
            if (c == ' ' && !hasDot) {
                Text text = new Text();
                text.set(bytes, start, length - 1);
                start = -1;
                length = 0;
                textArray.add(text);
            }
        }
        if (start != -1) {
            char c = line.charAt(line.length() - 1);
            boolean isDot = c == '"' || c == '[' || c == ']';
            Text text = new Text();
            text.set(bytes, start, isDot ? line.length() - start - 1 : line.length() - start);
            textArray.add(text);
        }
        return textArray;
    }

}
