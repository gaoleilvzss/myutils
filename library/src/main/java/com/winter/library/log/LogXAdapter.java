package com.winter.library.log;

/**
 * 日志配置类 可扩展
 */
public class LogXAdapter {
    private String globalTag = "winter"; //全局的tag
    private boolean isShowSwitch = false; //是否打印log开关
    private boolean showCurrentThreadName = false; //是否显示当前线程的name


    public LogXAdapter(Builder builder) {
        this.globalTag = builder.tag;
        this.isShowSwitch = builder.showLogSwitch;
        this.showCurrentThreadName = builder.showCurrentThreadName;
    }

    public boolean isShowCurrentThreadName() {
        return showCurrentThreadName;
    }

    public String getGlobalTag() {
        return globalTag;
    }

    public boolean isShowSwitch() {
        return isShowSwitch;
    }

    public static class Builder {
        private String tag;
        private boolean showLogSwitch;
        private boolean showCurrentThreadName;

        public Builder setTag(String tag) {
            this.tag = tag;
            return this;
        }

        public Builder setShowCurrentThread(boolean showCurrentThread) {
            this.showCurrentThreadName = showCurrentThread;
            return this;
        }

        public Builder setShowLogSwitch(boolean isShowLog) {
            this.showLogSwitch = isShowLog;
            return this;
        }

        public LogXAdapter build() {
            return new LogXAdapter(this);
        }
    }
}
