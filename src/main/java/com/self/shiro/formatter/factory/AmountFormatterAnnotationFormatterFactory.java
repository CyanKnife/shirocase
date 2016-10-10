package main.java.com.self.shiro.formatter.factory;

import org.springframework.format.AnnotationFormatterFactory;
import org.springframework.format.Parser;
import org.springframework.format.Printer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 金额格式化注解
 *
 * @create 2016/2/15 17:40
 */
public class AmountFormatterAnnotationFormatterFactory implements AnnotationFormatterFactory<AmountProperty> {
    private static final Set<Class<?>> FIELD_TYPES;
    private static final AmountFormatter FORMATTER;

    static {
        Set<Class<?>> fieldTypes = new HashSet<>();
        fieldTypes.add(Long.class);
        FIELD_TYPES = Collections.unmodifiableSet(fieldTypes);
        FORMATTER = new AmountFormatter();
    }

    @Override
    public Set<Class<?>> getFieldTypes() {
        return FIELD_TYPES;
    }

    @Override
    public Printer<?> getPrinter(AmountProperty annotation, Class<?> fieldType) {
        return FORMATTER;
    }

    @Override
    public Parser<?> getParser(AmountProperty annotation, Class<?> fieldType) {
        return FORMATTER;
    }

}
