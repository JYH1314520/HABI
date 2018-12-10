package com.habi.boot.system.base;

import com.habi.boot.system.base.annotation.Children;
import tk.mybatis.mapper.entity.EntityField;
import tk.mybatis.mapper.mapperhelper.FieldHelper;

import javax.persistence.Id;
import java.lang.annotation.Annotation;
import java.util.*;

public final class EntityClassInfo {
    private static final Comparator<EntityField> FIELD_COMPARATOR = Comparator.comparing(EntityField::getName);
    private static final Class<? extends Annotation>[] CONCERNED_ANNOTATION = new Class[]{Id.class, Children.class};
    private static  Map<Class<?>, EntityField[]>[] CLASS_ANNO_MAPPING  ;
    private static  Map<Class<?>, Map<String, EntityField>> CLASS_FIELDS_MAPPING ;
    private static  Map<String, String> CAMEL_UL_MAP ;
    private static  Map<String, String> UL_CAMEL_MAP ;

    private static EntityField[] getFields0(Class<?> clazz, int idx) {
        EntityField[] fields = (EntityField[])CLASS_ANNO_MAPPING[idx].get(clazz);
        if (fields == null) {
            analysis(clazz);
            fields = (EntityField[])CLASS_ANNO_MAPPING[idx].get(clazz);
        }

        return fields;
    }

    public static EntityField[] getIdFields(Class<?> clazz) {
        return getFields0(clazz, 0);
    }

    private static void analysis(Class<?> clazz) {
        List<EntityField> fields = FieldHelper.getAll(clazz);
        fields.sort(FIELD_COMPARATOR);
        List<EntityField>[] lists = new List[CONCERNED_ANNOTATION.length];

        for(int i = 0; i < CONCERNED_ANNOTATION.length; ++i) {
            lists[i] = new ArrayList();
        }

        Map<String, EntityField> fieldMap = new HashMap();
        Iterator var4 = fields.iterator();

        while(var4.hasNext()) {
            EntityField f = (EntityField)var4.next();

            for(int i = 0; i < CONCERNED_ANNOTATION.length; ++i) {
                if (f.getAnnotation(CONCERNED_ANNOTATION[i]) != null) {
                    lists[i].add(f);
                }
            }

            fieldMap.put(f.getName(), f);
        }

        CLASS_FIELDS_MAPPING.put(clazz, fieldMap);

        for(int i = 0; i < CLASS_ANNO_MAPPING.length; ++i) {
            EntityField[] fs = (EntityField[])lists[i].toArray(new EntityField[lists[i].size()]);
            CLASS_ANNO_MAPPING[i].put(clazz, fs);
        }

    }
    public static EntityField[] getFieldsOfAnnotation(Class<?> clazz, Class<? extends Annotation> annoType) {
        int idx = Arrays.asList(CONCERNED_ANNOTATION).indexOf(annoType);
        return idx != -1 ? getFields0(clazz, idx) : new EntityField[0];
    }

    public static EntityField[] getChildrenFields(Class<?> clazz) {
        return getFields0(clazz, 1);
    }

}
