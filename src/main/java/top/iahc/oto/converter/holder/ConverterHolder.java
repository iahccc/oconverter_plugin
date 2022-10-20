package top.iahc.oto.converter.holder;

import top.iahc.oto.converter.Converter;
import top.iahc.oto.converter.DefaultConverter;
import top.iahc.oto.converter.ListConverter;

import java.util.HashMap;
import java.util.Map;

public class ConverterHolder {
    private static final String QUALIFIED_NAME_LIST = "java.util.List";
    private static final Converter CONVERTER_DEFAULT;
    private static final Map<String, Converter> CONVERTER_MAP;

    static {
        CONVERTER_MAP = new HashMap<>();
        CONVERTER_MAP.put(QUALIFIED_NAME_LIST, new ListConverter());
        CONVERTER_DEFAULT = new DefaultConverter();
    }

    public static Converter route(String classQualifiedName) {
        Converter converter = CONVERTER_MAP.get(classQualifiedName);
        return converter == null ? CONVERTER_DEFAULT : converter;
    }
}
