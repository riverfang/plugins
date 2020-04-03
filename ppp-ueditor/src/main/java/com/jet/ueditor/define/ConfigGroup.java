package com.jet.ueditor.define;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 配置分组
 *
 * @author fangjiang
 * @date 2019年09月19日 16:52
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD})
public @interface ConfigGroup {
    ActionTypeEnum[] value();
    String prefix();
}
