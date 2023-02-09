package eu.tsystems.mms.tic.testframework.mobile.guielement;

import org.openqa.selenium.By;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created on 2023-02-09
 *
 * @author mgn
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface AndroidLocator {

    public String xpath() default "";
}
