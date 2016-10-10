package main.java.com.self.shiro.resolver;

import com.alibaba.fastjson.JSON;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * @date 2016-01-14 10:18
 * @Description API异常处理
 */
public class WebHandlerExceptionResolver implements HandlerExceptionResolver {

    private static final Logger logger = LoggerFactory.getLogger(WebHandlerExceptionResolver.class);

    private String contentType;

    @Autowired
    private MessageSource messageSource;

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    private void printJson(HttpServletResponse response, Object value) {
        response.setContentType(contentType);
        try {
            PrintWriter writer = response.getWriter();
            String text = JSON.toJSONString(value);
            logger.info(text);
            writer.print(text);
            writer.flush();
        } catch (IOException e) {

        }
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
                                         Exception ex) {


        return null;
    }
}