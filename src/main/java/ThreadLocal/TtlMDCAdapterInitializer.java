package ThreadLocal;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 无需启动加载获得MDCAdapter，由StaticMDCBinder提供给MDC加载创建
 * @author ：li zhen
 * @description:
 * @date ：2024/3/13 10:39
 */
@Deprecated
public class TtlMDCAdapterInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        // 加载自定义的MDCAdapter
        TtlMDCAdapter.getInstance();
    }
}
