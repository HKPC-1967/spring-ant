package org.hkpc.dtd.common.utils;

import com.alibaba.fastjson2.JSONObject;

public class ObjectUtil {

    /**
     * Clones an object to a specified target class type.
     *
     * @param dto the object to be cloned
     * @param targetClass the class type to clone the object into
     * @param <T> the type of the target class
     * @return the cloned object of the specified target class type
     */
    public static <T> T clone2TargetClass(Object dto, Class<T> targetClass){
        T targetBbject = JSONObject.parseObject(JSONObject.toJSONString(dto), targetClass);
        return targetBbject;
    }

    /**
     * Modifies a list of objects by setting specified fields to null and returns the modified list.
     *
     * @param list the list of objects to be modified
     * @param fields the set of field names to be set to null
     * @return the modified list with specified fields set to null
     */
//    public static List<Object> toJsonWithFilter(List<Object> list, Set<String> fields) {
//        for (int i = 0; i < list.size(); i++) {
//            Object item = list.get(i);
//            JSONObject jsonObject = JSONObject.parseObject(JSONObject.toJSONString(item));
//            for (String field : fields) {
//                jsonObject.put(field, null);
//            }
//            list.set(i, jsonObject);
//        }
//        return list;
//    }
}
