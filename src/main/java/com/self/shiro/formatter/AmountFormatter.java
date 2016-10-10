package main.java.com.self.shiro.formatter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.format.Formatter;

import java.util.Locale;

/**
 * 金额*100 格式化
 *
 * @create 2016/2/15 17:16
 */
public class AmountFormatter implements Formatter<Long> {
    /**
     * @param text
     * @param locale
     * @return
     * @throws if 金额超过8位数 and 金额格式不合法，转换失败
     */
    @Override
    public Long parse(String text, Locale locale) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        /*金额判断是否大于8位*/
        if (text.length() > 8) {
            throw new IllegalNumberException();
        }
        double amount = 0;
        try {
            amount = Double.parseDouble(text) * 100;
        } catch (NumberFormatException e) {
            throw new IllegalNumberException();
        }
        return (long) amount;
    }

    @Override
    public String print(Long object, Locale locale) {
        if (object == null) {
            return null;
        }
//        return Double.toString(object / 100D);
        return String.valueOf(object);
    }

}