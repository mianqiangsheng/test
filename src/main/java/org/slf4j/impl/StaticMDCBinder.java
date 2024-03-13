package org.slf4j.impl;

import ThreadLocal.TtlMDCAdapter;
import org.slf4j.spi.MDCAdapter;

/**
 * @author ：li zhen
 * @description:
 * @date ：2024/3/13 11:08
 */
public class StaticMDCBinder {
    /**
     * The unique instance of this class.
     */
    public static final StaticMDCBinder SINGLETON = new StaticMDCBinder();

    private StaticMDCBinder() {
    }

    /**
     * Currently this method always returns an instance of
     * {@link StaticMDCBinder}.
     */
    public MDCAdapter getMDCA() {
        return TtlMDCAdapter.getInstance();
    }

    public String getMDCAdapterClassStr() {
        return TtlMDCAdapter.getInstance().getClass().getName();
    }
}
