package org.jeecg.modules.estar.bs.util;

import org.apache.commons.lang3.StringUtils;
import org.jeecg.common.util.SpringContextUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.util.CollectionUtils;

import org.jeecg.modules.estar.bs.annotation.DtoSkip;
import org.jeecg.modules.estar.bs.annotation.Formatter;
import org.jeecg.modules.estar.bs.annotation.FormatterType;
import org.jeecg.modules.estar.bs.cache.CacheHelper;
import org.jeecg.modules.estar.bs.constant.BsConstant;
import org.jeecg.modules.estar.bs.constant.Enabled;
import org.jeecg.modules.estar.bs.constant.EstarKeyConstant;

/**
 * 字段翻译
 *
 * @author nbacheng
 * @since 2023-3-23
 */
public abstract class EstarBeanUtils {

	 private static final Logger logger = LoggerFactory.getLogger(EstarBeanUtils.class);

	    /**
	     * 字段类型转换
	     *
	     * @param source
	     * @param target
	     * @param <T>
	     * @return
	     */
	    public static <T> T copyAndFormatter(Object source, T target) {
	        //获取目标类并翻译
	        Field[] declaredFields = target.getClass().getDeclaredFields();
	        List<Field> fields = new ArrayList<>(declaredFields.length);

	        //跳过翻译的字段
	        List<String> skipFields = new ArrayList<>();

	        //非普通类型的翻译即对象，需要翻译其对象
	        List<Field> formatterTypeFields = new ArrayList<>();

	        //过滤掉DtoSkip注解的字段
	        setFormatterFields(target.getClass(), declaredFields, fields, skipFields, formatterTypeFields);

	        //entity翻译成DTO,跳过忽略DtoSkip的字段和类型翻译FormatterType的字段
	        BeanUtils.copyProperties(source, target, skipFields.toArray(new String[0]));

	        //遍历字段，找出 Formatter注解注释的字段,并翻译
	        formatterHandler(source, target, fields);

	        //翻译非基础类型即：对象或者集合
	        for (Field field : formatterTypeFields) {
	            formatSubFields(source, (T) target, field);
	        }

	        return target;
	    }

	    /**
	     * 复制集合
	     *
	     * @param sourceList
	     * @param targetClass
	     * @param <T>
	     * @return
	     */
	    public static <T> List<T> copyList(List<? extends Object> sourceList, Class<T> targetClass) {
	        if (CollectionUtils.isEmpty(sourceList)) {
	            return new ArrayList<>();
	        }
	        Field[] declaredFields = targetClass.getDeclaredFields();
	        List<Field> fields = new ArrayList<>(declaredFields.length);
	        //跳过翻译的字段
	        List<String> skipFields = new ArrayList<>();
	        //非普通类型的翻译即对象，需要翻译其对象
	        List<Field> formatterTypeFields = new ArrayList<>();
	        //过滤掉DtoSkip注解的字段
	        setFormatterFields(targetClass, declaredFields, fields, skipFields, formatterTypeFields);
	        List<T> ret = new ArrayList<>();
	        try {
	            for (Object source : sourceList) {
	                T target = targetClass.newInstance();
	                //entity翻译成DTO,跳过忽略DtoSkip的字段和类型翻译FormatterType的字段
	                BeanUtils.copyProperties(source, target, skipFields.toArray(new String[0]));
	                ret.add(target);
	            }
	            //遍历字段，找出 Formatter注解注释的字段,并翻译
	            Locale locale = LocaleContextHolder.getLocale();
	            //语言
	            String language = locale.getLanguage();
	            Map<String, Object> params = UserContentHolder.getContext().getParams();
	            // 需要处理的字段
	            List<Field> formatterFields = fields.stream().parallel()
	                    .filter(field -> field.isAnnotationPresent(Formatter.class)).collect(Collectors.toList());

	            // 数据准备
	            Map<String, Set<String>> hashKeys = new HashMap<>();
	            for (int i = 0; i < sourceList.size(); i++) {
	                Object s1 = sourceList.get(i);
	                T t1 = ret.get(i);
	                formatterFields.forEach(field -> {
	                    extractDefinition(s1, t1, language, params, hashKeys, field);
	                });
	            }
	            // 批量获取cache数据到本地
	            Map<String, Map<String, String>> cacheResult = new HashMap<>();
	            if (cacheHelper == null) {
	            	cacheHelper = SpringContextUtils.getBean(CacheHelper.class);
	            }
	            for (Entry<String, Set<String>> item : hashKeys.entrySet()) {
	                fillCacheMap(cacheResult, item);
	            }
	            // 转换
	            for (int i = 0; i < sourceList.size(); i++) {
	                Object s2 = sourceList.get(i);
	                T t2 = ret.get(i);
	                formatterFields.forEach(field -> {
	                    fieldFormat(s2, t2, language, params, cacheResult, field);
	                });
	            }
	            for (int i = 0; i < sourceList.size(); i++) {
	                Object s3 = sourceList.get(i);
	                T t3 = ret.get(i);
	                //翻译非基础类型即：对象或者集合
	                for (Field field : formatterTypeFields) {
	                    formatSubFields(s3, t3, field);
	                }
	            }
	        } catch (Exception ex) {

	        }
	        return ret;
	    }

	    private static <T> void formatSubFields(Object s3, T t3, Field field) {
	        try {
	            PropertyDescriptor sd = new PropertyDescriptor(( String ) field.getName(), s3.getClass());
	            PropertyDescriptor td = new PropertyDescriptor(( String ) field.getName(), t3.getClass());
	            Object fieldSource = sd.getReadMethod().invoke(s3);
	            if (fieldSource == null) {
	                return;
	            }
	            Method writeMethod = td.getWriteMethod();
	            FormatterType ft = field.getAnnotation(FormatterType.class);
	            switch (ft.type()) {
	                case OBJECT:
	                    Object fieldTarget = field.getType().newInstance();
	                    copyAndFormatter(fieldSource, fieldTarget);
	                    writeMethod.invoke(t3, fieldTarget);
	                    break;
	                case LIST:
	                    Type genericType = field.getGenericType();
	                    ParameterizedType parameterizedType = (ParameterizedType) genericType;
	                    Class fieldTargetClass = (Class) parameterizedType.getActualTypeArguments()[0];
	                    if(!ft.target().getName().equals(Object.class.getName())){
	                        fieldTargetClass = ft.target();
	                    }
	                    List fieldTargetList = copyList((List) fieldSource, fieldTargetClass);
	                    writeMethod.invoke(t3, fieldTargetList);
	                    break;
	                default:
	            }
	        } catch (Exception e) {
	            logger.error("FormatterType处理异常", e);
	        }
	    }

	    private static <T> void setFormatterFields(Class<T> targetCls, Field[] declaredFields,
	                                               List<Field> fields, List<String> skipFields, List<Field> formatterTypeFields) {
	        for (Field field : declaredFields) {
	            if (field.isAnnotationPresent(DtoSkip.class)) {
	                skipFields.add(field.getName());
	                continue;
	            }

	            if (field.isAnnotationPresent(FormatterType.class)) {
	                formatterTypeFields.add(field);
	                continue;
	            }

	            fields.add(field);
	        }
	        //都要放过
	        skipFields.addAll(formatterTypeFields.stream().map(Field::getName).collect(Collectors.toList()));

	        Field[] superDeclaredFields = targetCls.getSuperclass().getDeclaredFields();
	        fields.addAll(Arrays.asList(superDeclaredFields));
	    }

	    private static CacheHelper cacheHelper = null;

	    /**
	     * 翻译被Formatter注解的字段
	     *
	     * @param target
	     * @param fields
	     * @param <T>
	     */
	    private static <T> void formatterHandler(Object source, T target, List<Field> fields) {
	        //遍历字段，找出 Formatter注解注释的字段,获取对应字典中的值,国际化
	        Locale locale = LocaleContextHolder.getLocale();
	        //语言
	        String language = locale.getLanguage();
	        Map<String, Object> params = UserContentHolder.getContext().getParams();
	        // 需要处理的字段
	        List<Field> formatterFields = fields.stream().parallel()
	                .filter(field -> field.isAnnotationPresent(Formatter.class)).collect(Collectors.toList());

	        // 数据准备
	        Map<String, Set<String>> hashKeys = new HashMap<>();
	        formatterFields.forEach(field -> {
	            extractDefinition(source, target, language, params, hashKeys, field);
	        });

	        Map<String, Map<String, String>> cacheResult = new HashMap<>();
	        if (cacheHelper == null) {
	        	cacheHelper = SpringContextUtils.getBean(CacheHelper.class);
	        }
	        for (Entry<String, Set<String>> item : hashKeys.entrySet()) {
	            fillCacheMap(cacheResult, item);
	        }

	        formatterFields.forEach(field -> {
	            fieldFormat(source, target, language, params, cacheResult, field);
	        });
	    }

	    private static <T> void fieldFormat(Object source, T target, String language, Map<String, Object> params,
	                                        Map<String, Map<String, String>> cacheResult, Field field) {
	        try {
	            Formatter annotation = field.getAnnotation(Formatter.class);
	            String key = getCacheKey(annotation, language, params, source);
	            PropertyDescriptor descriptor = new PropertyDescriptor(( String )field.getName(), target.getClass());
	            Method readMethod = descriptor.getReadMethod();
	            //读取属性值
	            String val = readMethod.invoke(target) + "";
	            val = cacheResult.get(key).get(val/*.toLowerCase()*/);
	            if (StringUtils.isNotBlank(val)) {
	                PropertyDescriptor t = new PropertyDescriptor(( String )field.getName(), target.getClass());
	                // 替换
	                if (StringUtils.isBlank(annotation.targetField())) {
	                    Method writeMethod = t.getWriteMethod();
	                    writeMethod.invoke(target, val);
	                } else {
	                    t = new PropertyDescriptor(( String ) annotation.targetField(), target.getClass());
	                    if (t != null) {
	                        t.getWriteMethod().invoke(target, val);
	                    }
	                }
	            }
	        } catch (Exception ignore) {

	        }
	    }

	    private static void fillCacheMap(Map<String, Map<String, String>> cacheResult, Entry<String, Set<String>> item) {
	        List<String> keys = item.getValue().stream().distinct().collect(Collectors.toList());
	        List<String> values = cacheHelper.hashMultiGet(item.getKey(), keys);
	        List<String> lowcase = new ArrayList<>();
	        Map<String, String> kv = new HashMap<>();
	        if (!CollectionUtils.isEmpty(values)) {
	            for (int i = 0; i < values.size(); i++) {
	                // key小写
	                if (Objects.isNull(values.get(i))) {
	                    lowcase.add(keys.get(i)/*.toLowerCase()*/);
	                } else {
	                    kv.put(keys.get(i)/*.toLowerCase()*/, values.get(i));
	                }
	            }
	            // key小写后再查一次
	            /*if (!CollectionUtils.isEmpty(lowcase)) {
	                List<String> list = lowcase.stream().distinct().collect(Collectors.toList());
	                values = cacheHelper.hashMultiGet(item.getKey(), list);
	                if (!CollectionUtils.isEmpty(values)) {
	                    for (int i = 0; i < values.size(); i++) {
	                        if (Objects.isNull(values.get(i))) {

	                        } else {
	                            kv.put(list.get(i), values.get(i));
	                        }
	                    }
	                }
	            }*/
	        }
	        cacheResult.put(item.getKey(), kv);
	    }

	    private static <T> void extractDefinition(Object source, T target, String language, Map<String, Object> params,
	                                              Map<String, Set<String>> hashKeys, Field field) {
	        try {
	            //判断是否有注解Formatter
	            PropertyDescriptor descriptor = new PropertyDescriptor(( String )field.getName(), target.getClass());
	            Method readMethod = descriptor.getReadMethod();
	            //读取属性值
	            Object result = readMethod.invoke(target);
	            if (result instanceof Boolean) {
	                result = (Boolean) result ? Enabled.YES.getValue() : Enabled.NO.getValue();
	            }
	            //非空判断
	            if (result != null) {
	                Formatter annotation = field.getAnnotation(Formatter.class);
	                String key = getCacheKey(annotation, language, params, source);
	                //只需第一次从Redis获取并缓存
	                String hashKey = result.toString();
	                hashKeys.putIfAbsent(key, new HashSet<>());
	                hashKeys.get(key).add(hashKey);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	    }

	    private static String getCacheKey(Formatter annotation, String language, Map params, Object source) {
	        if (StringUtils.isBlank(annotation.key())) {
	            String dictCode = annotation.dictCode();
	            return EstarKeyConstant.DICT_PREFIX + language + BsConstant.REDIS_SPLIT + dictCode;
	        } else {
	            //支持动态key，比如key=DICT:${DEMO}，注解Formatter的replace=["demo"]
	            return formatKey(annotation.key(), annotation.replace(), params, source);
	        }
	    }

	    /**
	     * 替换占位符key
	     *
	     * @param key
	     * @param replaceArray 替换
	     * @param source
	     * @return
	     */
	    public static String formatKey(String key, String[] replaceArray, Map<String, Object> params, Object source) {
	        if (key.contains(BsConstant.URL_PATTERN_MARK)) {
	            Map<String, Object> keyPatternMap = new HashMap<>(2);
	            for (String fieldName : replaceArray) {
	                try {
	                    Object value = params.get(fieldName);
	                    if (null == value || "".equals(value)) {
	                        Field declaredField = source.getClass().getDeclaredField(fieldName);
	                        declaredField.setAccessible(true);
	                        value = declaredField.get(source);
	                    }
	                    keyPatternMap.put(fieldName, value);
	                } catch (Exception e) {
	                    continue;
	                }
	            }

	            key = EstarUtils.replaceFormatString(key, keyPatternMap);
	            if (key.contains(BsConstant.URL_PATTERN_MARK)) {
	                return null;
	            }
	        }

	        return key;
	    }
	}
