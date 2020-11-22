package com.winter.library.log;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;


/**
 * 自定义日记管理类
 */
public class LogX {
    private static final char TOP_LEFT_CORNER = '╔';
    private static final char BOTTOM_LEFT_CORNER = '╚';
    private static final char MIDDLE_CORNER = '╟';
    private static final char HORIZONTAL_DOUBLE_LINE = '║';
    private static final String DOUBLE_DIVIDER = "════════════════════════════════════════════";
    private static final String SINGLE_DIVIDER = "────────────────────────────────────────────";
    private static final String TOP_BORDER = TOP_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String BOTTOM_BORDER = BOTTOM_LEFT_CORNER + DOUBLE_DIVIDER + DOUBLE_DIVIDER;
    private static final String MIDDLE_BORDER = MIDDLE_CORNER + SINGLE_DIVIDER + SINGLE_DIVIDER;
    private static final char I = 'I', W = 'W', D = 'D', E = 'E', V = 'V', A = 'A', M = 'M';
    static String LINE_SEPARATOR = System.getProperty("line.separator");
    static int JSON_INDENT = 4;

    private static LogXAdapter mitLogAdapter;


    public static void addLogAdapter(LogXAdapter adapter) {
        mitLogAdapter = adapter;
    }


    public static void i(String... msg) {
        if (mitLogAdapter != null && mitLogAdapter.isShowSwitch()) {
            printLog(I, msg);
        } else {
            throw new RuntimeException("logAdapter is null");
        }

    }


    public static void d(String... msg) {
        if (mitLogAdapter != null && mitLogAdapter.isShowSwitch()) {
            printLog(D, msg);
        } else {
            throw new RuntimeException("logAdapter is null");
        }

    }

    public static void w(String... msg) {
        if (mitLogAdapter != null && mitLogAdapter.isShowSwitch()) {
            printLog(W, msg);
        } else {
            throw new RuntimeException("logAdapter is null");
        }

    }

    public static void e(String... msg) {
        if (mitLogAdapter != null && mitLogAdapter.isShowSwitch()) {
            printLog(E, msg);
        } else {
            throw new RuntimeException("logAdapter is null");
        }

    }

    public static void v(String... msg) {
        if (mitLogAdapter != null && mitLogAdapter.isShowSwitch()) {
            printLog(V, msg);
        } else {
            throw new RuntimeException("logAdapter is null");
        }

    }


    private static void printHunk(char type, String str) {
        switch (type) {
            case I:
                Log.i(mitLogAdapter.getGlobalTag(), str);
                break;
            case D:
                Log.d(mitLogAdapter.getGlobalTag(), str);
                break;
            case E:
                Log.e(mitLogAdapter.getGlobalTag(), str);
                break;
            case V:
                Log.v(mitLogAdapter.getGlobalTag(), str);
                break;
            case A:
                Log.wtf(mitLogAdapter.getGlobalTag(), str);
                break;
            case W:
                Log.w(mitLogAdapter.getGlobalTag(), str);
                break;
        }
    }

    private static void printHead(char type) {
        printHunk(type, TOP_BORDER);
        if (mitLogAdapter.isShowCurrentThreadName()) {
            printHunk(type, HORIZONTAL_DOUBLE_LINE + "   Thread:");
            printHunk(type, HORIZONTAL_DOUBLE_LINE + "   " + Thread.currentThread().getName());
            printHunk(type, MIDDLE_BORDER);
        }

    }

    /**
     * 打印消息
     *
     * @param type
     * @param msg
     */
    private static void printMsg(char type, String... msg) {
        printHunk(type, HORIZONTAL_DOUBLE_LINE + "   msg:");
        for (String str : msg) {
            printHunk(type, HORIZONTAL_DOUBLE_LINE + "   " + str);
        }
        printHunk(type, BOTTOM_BORDER);
    }

    private static void printLocation(char type, String... msg) {
        StackTraceElement[] stack = Thread.currentThread().getStackTrace();
        int i = 0;
        for (StackTraceElement e : stack) {
            String name = e.getClassName();
            if (!name.equals(LogX.class.getName())) {
                i++;
            } else {
                break;
            }
        }
        i += 3;
        String className = stack[i].getFileName();
        String methodName = stack[i].getMethodName();
        int lineNumber = stack[i].getLineNumber();
        StringBuilder sb = new StringBuilder();
        printHunk(type, HORIZONTAL_DOUBLE_LINE + "   Location:");
        sb.append(HORIZONTAL_DOUBLE_LINE)
                .append("   (").append(className).append(":").append(lineNumber).append(")# ").append(methodName);
        printHunk(type, sb.toString());
        printHunk(type, msg == null || msg.length == 0 ? BOTTOM_BORDER : MIDDLE_BORDER);
    }

    private static void printLog(char type, String... msg) {
        printHead(type);
        printLocation(type, msg);
        if (msg == null || msg.length == 0) {
            return;
        }
        printMsg(type, msg);
    }
//==========================================================以上为常规打印===============================================================
//==========================================================以下为扩展打印===============================================================

    /**
     * 打印MAp
     */
    public static void m(Map map) {
        Set set = map.entrySet();
        if (set.size() < 1) {
            printLog(D, "[]");
            return;
        }

        int i = 0;
        String[] s = new String[set.size()];
        for (Object aSet : set) {
            Map.Entry entry = (Map.Entry) aSet;
            s[i] = entry.getKey() + " = " + entry.getValue() + ",\n";
            i++;
        }
        printLog(V, s);
    }

    /**
     * 打印JSON
     *
     * @param jsonStr
     */
    public static void j(String jsonStr) {
        if (mitLogAdapter.isShowSwitch()) {
            String message;
            try {
                if (jsonStr.startsWith("{")) {
                    JSONObject jsonObject = new JSONObject(jsonStr);
                    message = jsonObject.toString(JSON_INDENT);
                } else if (jsonStr.startsWith("[")) {
                    JSONArray jsonArray = new JSONArray(jsonStr);
                    message = jsonArray.toString(JSON_INDENT);
                } else {
                    message = jsonStr;
                }
            } catch (JSONException e) {
                message = jsonStr;
            }

            message = LINE_SEPARATOR + message;
            String[] lines = message.split(LINE_SEPARATOR);
            printLog(D, lines);
        }
    }


}
