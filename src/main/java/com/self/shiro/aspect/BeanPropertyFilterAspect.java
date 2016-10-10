package main.java.com.self.shiro.aspect;


import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

@Aspect
@Component
public class BeanPropertyFilterAspect {
    /**
     *
     */
    private static final Set<Class<?>> WRAPPER_TYPES = initWrapperTypes();
    /**
     * get方法前缀
     */
    private static final String GET_STR = "get";
    /**
     * set方法前缀
     */
    private static final String SET_STR = "set";
    @Autowired
    ProjectConfig projectConfig;

    public void filter(Object result) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, NoSuchFieldException {
        if (result == null) {
            return;
        }
        if (Map.class.isAssignableFrom(result.getClass())
                || result instanceof Boolean || result instanceof Character
                || result instanceof Byte || result instanceof Short
                || result instanceof Integer || result instanceof Long
                || result instanceof Float || result instanceof Double
                || result instanceof Void || result instanceof String
                || result instanceof Date || result instanceof MultipartFile
                || result instanceof ServletRequest || result instanceof HttpSession) {
            return;
        }
        if (isCollectionOrArray(result.getClass())) {
            for (Object o : (Collection) result) {
                filter(o);
            }
        }
        /*对ModelAndView处理*/
        else if (result instanceof ModelAndView) {
            Map<String, Object> model = ((ModelAndView) result).getModel();
            if (MapUtils.isEmpty(model)) {
                return;
            }
            Set<Map.Entry<String, Object>> modelEntries = model.entrySet();
            for (Map.Entry<String, Object> modelEntry : modelEntries) {
                if (modelEntry == null || modelEntry.getValue() == null) {
                    continue;
                } else {
                    filter(modelEntry.getValue());
                }
            }
        } else {
            filterPropertyInvoke(result);
        }
    }

    private LinkedList<Field> getAllFields(Class clazz) {
        LinkedList<Field> fields = new LinkedList<>();
        if (clazz.getGenericSuperclass() != null && clazz.getGenericSuperclass() != Object.class) {
            fields.addAll(getAllFields(clazz.getSuperclass()));
        }
        fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
        return fields;
    }

    /**
     * 过滤参数字段, 自定义注解的实现
     * 非基础类型的对象调用
     *
     * @param result 对象
     * @throws NoSuchMethodException
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     */
    private void filterPropertyInvoke(Object result) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, NoSuchFieldException {
        Class clazz = result.getClass();
        for (Field field : getAllFields(clazz)) {
            boolean continueFilter = false;
            if (isCollectionOrArray(field.getType())) {
                if (this.isList(field.getType()) && field.isAnnotationPresent(ImageUrlProperty.class)) {
                    //处理List类型字段,添加注解ImageUrlProperty的逻辑
                    Object value = result.getClass().getMethod(generateMethodName(GET_STR, field)).invoke(result);
                    this.setListImageUrlProperty(field, (List<Object>) value);
                    continue;
                } else {
                    continueFilter = true;
                }
            } else {
                continueFilter = !isPrimitiveType(field.getType());
            }
            if (continueFilter) {//复杂对象,继续递归
                Object nextResult = result.getClass().getMethod(generateMethodName(GET_STR, field)).invoke(result);
                if (nextResult != null) {
                    filter(nextResult);
                }
            } else {//基础类型的处理
                this.setImageUrlProperty(field, result);//实现图片URL的自定义注解
                //实现Date2String的自定义注解
                //this.setDateFormatProperty(field, result);
                //金额除以100
                this.AmountFormatter(field, result);
                //清除空格
                this.clearStringTrim(field, result);
            }
        }
    }

    /**
     * 清除空格
     * @param field
     * @param result
     */
    private void clearStringTrim(Field field, Object result)
    {
        if (field.isAnnotationPresent(StringTrimProperty.class)) {
            try {
                StringTrimProperty imageUrlProperty = field.getAnnotation(StringTrimProperty.class);
                String getMethodName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                String value = (String) result.getClass().getMethod(getMethodName).invoke(result);
                if (!StringUtils.isEmpty(value)) {
                    if(imageUrlProperty.stringTrimEnum() == StringTrimEnum.CLEAR_PREFIX_TRIM)
                    {
                        int len = value.length();
                        int st = 0;
                        char[] val = value.toCharArray();
                        while ((st < len) && (val[st] == '\n' || val[st] == '\r' || val[st] <= ' ')) {
                            st++;
                        }
                        value = (st > 0)  ? value.substring(st, len) : value;
                    }else if(imageUrlProperty.stringTrimEnum() == StringTrimEnum.CLEAR_SUFFIX_TRIM)
                    {
                        int len = value.length();
                        int st = 0;
                        char[] val = value.toCharArray();
                        while ((st < len) && (val[len - 1] == '\n' || val[len - 1] == '\r') || val[len - 1] <= ' ') {
                            len--;
                        }
                        value = (st > 0)  ? value.substring(st, len) : value;

                    }else{
                        int len = value.length();
                        int st = 0;
                        char[] val = value.toCharArray();    /* avoid getfield opcode */
                        while ((st < len) && (val[st] == '\n' || val[st] == '\r' || val[st] <= ' ')) {
                            st++;
                        }
                        while ((st < len) && (val[len - 1] == '\n' || val[len - 1] == '\r') || val[len - 1] <= ' ') {
                            len--;
                        }
                        value = ((st > 0) || (len < value.length())) ? value.substring(st, len) : value;
                    }
                    String setMethodName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                    result.getClass().getMethod(setMethodName, field.getType()).invoke(result, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理ImageUrlProperty注解的实现(result和field成对出现)
     *
     * @param field  反射待处理的字段
     * @param result 待反射的字段值
     */
    private void setImageUrlProperty(Field field, Object result) {
        if (field.isAnnotationPresent(ImageUrlProperty.class)) {
            try {
                ImageUrlProperty imageUrlProperty = field.getAnnotation(ImageUrlProperty.class);
                String getMethodName = "get" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                Object value = result.getClass().getMethod(getMethodName).invoke(result);
                if (value != null) {
                    String setMethodName = "set" + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                    result.getClass().getMethod(setMethodName, field.getType()).invoke(result, projectConfig.getResourceImageDomain() + imageUrlProperty.path() + value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理ImageUrlProperty注解的实现(result和field成对出现)
     * {@code Object} 类型必须有重写了toString() 方法的类型，
     * 建议使用{@code List<String>}等基本数据类型
     *
     * @param field
     * @param resultList
     */
    private void setListImageUrlProperty(Field field, List<Object> resultList) {
        if (field == null || CollectionUtils.isEmpty(resultList)) {
            return;
        }
        try {
            ImageUrlProperty imageUrlProperty = field.getAnnotation(ImageUrlProperty.class);
            StringBuilder sb;
            for (int i = 0; i < resultList.size(); i++) {
                sb = new StringBuilder();
                sb.append(projectConfig.getResourceImageDomain());
                sb.append(imageUrlProperty.path());
                sb.append(resultList.get(i));
                resultList.set(i, sb.toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 订单预算和金额进行转换 除以100
     *
     * @param field
     * @param obj
     * @throws IllegalNumberException if {@code amount.length > 8} or parse Long failed
     */
    private void AmountFormatter(Field field, Object obj) {
        if (field.isAnnotationPresent(AmountProperty.class)) {
            try {
                AmountProperty amountProperty = field.getAnnotation(AmountProperty.class);
                AmountFormatConstant amountFormatConstant = amountProperty.amountFormatType();
                String getMethodName = this.generateMethodName("get", field);
                String setMethodName = this.generateMethodName("set", field);
                Long amount;
                if (amountFormatConstant.equals(AmountFormatConstant.AMOUNT_TRANSFER_DIVIDE)) {
                    if (obj != null) {
                        Object sourceObject = obj.getClass().getMethod(getMethodName).invoke(obj);
                        if (sourceObject != null) {
                            String source = sourceObject.toString();
                            if (!StringUtils.isEmpty(source)) {
                                amount = Long.parseLong(source) / 100;
                                obj.getClass().getMethod(setMethodName, field.getType()).invoke(obj, amount);
                            }
                        }
                    }
                }
                if (amountFormatConstant.equals(AmountFormatConstant.AMOUNT_TRANSFER_MULTIPLY)) {
                    if (obj != null) {
                        Object sourceObject = obj.getClass().getMethod(getMethodName).invoke(obj);
                        if (sourceObject != null) {
                            String source = sourceObject.toString();
                            if (!StringUtils.isEmpty(source)) {
                                if (StringUtils.length(source) > 8) {
                                    throw new IllegalNumberException();
                                }
                                try {
                                    amount = Long.parseLong(source) * 100;
                                } catch (Exception e) {
                                    throw new IllegalNumberException();
                                }
                                obj.getClass().getMethod(setMethodName, field.getType()).invoke(obj, amount);
                            }
                        }
                    }
                }
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 处理DateFormatProperty注解的实现（result和field成对出现）
     *
     * @param field  反射待处理的成员变量
     * @param result 待处理对象
     */
    private void setDateFormatProperty(Field field, Object result) {
        if (field.isAnnotationPresent(DateFormatProperty.class)) {
            try {
                //获得注解
                DateFormatProperty dateFormatProperty = field.getAnnotation(DateFormatProperty.class);
                //获得source field
                Field source = result.getClass().getDeclaredField(dateFormatProperty.source());
                if (source == null) {
                    return;
                }
                //获得Date数据
                String getSourceMethodName = this.generateMethodName("get", source);
                Date sourceDate = (Date) result.getClass().getMethod(getSourceMethodName).invoke(result);
                //为目标field 赋值
                if (sourceDate != null) {
                    String format = dateFormatProperty.format();
                    String date = DateUtil.toDateString(sourceDate, format);
                    if (date != null) {
                        String setTargetMethodName = this.generateMethodName("set", source);
                        result.getClass().getMethod(setTargetMethodName, field.getType()).invoke(result, date);
                    }
                    //判断是否需将source field 置为null
                    boolean flag = dateFormatProperty.sourceEnable();
                    if (!flag) {
                        source.setAccessible(true);
                        source.set(result, null);
                    }
                }
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }

        }
    }


    /**
     * 判断是否为集合或数组
     *
     * @param type 类型
     * @return boolean
     */
    public boolean isCollectionOrArray(Class<?> type) {
        //if (type.isArray()) return true;
        return Collection.class.isAssignableFrom(type);
    }

    /**
     * 判断是否为list
     *
     * @param type 类型
     * @return boolean
     */
    public boolean isList(Class<?> type) {
        return List.class.isAssignableFrom(type);
    }

    /**
     * 判断是否为基本类型
     *
     * @param clazz 类型
     * @return boolean
     */
    public static boolean isPrimitiveType(Class<?> clazz) {
        return WRAPPER_TYPES.contains(clazz);
    }

    /**
     * 初始化基本类型
     *
     * @return Set<Class<?>>
     */
    private static Set<Class<?>> initWrapperTypes() {
        Set<Class<?>> ret = new HashSet<Class<?>>();
        ret.add(Boolean.class);
        ret.add(Character.class);
        ret.add(Byte.class);
        ret.add(Short.class);
        ret.add(Integer.class);
        ret.add(Long.class);
        ret.add(Float.class);
        ret.add(Double.class);
        ret.add(Void.class);
        ret.add(boolean.class);
        ret.add(char.class);
        ret.add(byte.class);
        ret.add(short.class);
        ret.add(int.class);
        ret.add(long.class);
        ret.add(float.class);
        ret.add(double.class);
        ret.add(void.class);
        ret.add(String.class);
        ret.add(Date.class);
        return ret;
    }

    /**
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("@within(org.springframework.web.bind.annotation.RequestMapping) && @annotation(org.springframework.web.bind.annotation.ResponseBody)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] args = joinPoint.getArgs();
        if (!ArrayUtils.isEmpty(args)) {
            for (int i = 0; i < args.length; i++) {
                if (!(args[i] instanceof BindingResult)) {
                    filter(args[i]);
                }
            }
        }
        Object result = joinPoint.proceed(joinPoint.getArgs());
        filter(result);
        return result;
    }

    /**
     * 生成field getSolutionAttachmentCategoryList/set方法名称
     *
     * @param prefix getSolutionAttachmentCategoryList or set
     * @param field  field
     * @return String
     */
    private String generateMethodName(String prefix, Field field) {
        return prefix + field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
    }

}