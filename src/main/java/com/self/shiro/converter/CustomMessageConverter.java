package main.java.com.self.shiro.converter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * Descriptions :
 * Create : 2016/2/3 11:56
 * Update : 2016/2/3 11:56
 */
public class CustomMessageConverter extends FastJsonHttpMessageConverter {

    private static final Logger logger = LoggerFactory.getLogger(CustomMessageConverter.class);

    private Charset charset = UTF8;

    CustomMessageConverter() {
        super();
    }

    @Override
    protected void writeInternal(Object obj, HttpOutputMessage outputMessage) throws IOException,
            HttpMessageNotWritableException {
        OutputStream out = outputMessage.getBody();
        String text = JSON.toJSONString(obj, getFeatures());
        logger.info(text);
        byte[] bytes = text.getBytes(charset);
        out.write(bytes);
    }


}
