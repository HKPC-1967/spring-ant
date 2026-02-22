package org.hkpc.dtd.common.enums;

/**
 * Used to log the same flag for the same business, which makes log searching easier for specific business operations <br>
 * You can choose not to use it.
 * 中文：用于同一业务使用同一flag进行业务日志前缀记录，方便针对特定业务操作进行日志搜索<br>
 * 你也可以选择不用他。
 */
public enum ServiceLogFlagEnum {
    login, base_demo;

    @Override
    public String toString() {
//      "lf_"(log flag) is a customized special prefix for searching in log
        return "lf_" + this.name();
    }
}
