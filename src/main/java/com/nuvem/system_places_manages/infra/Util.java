package com.nuvem.system_places_manages.infra;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.util.HashSet;
import java.util.Set;

public class Util {

    public static  String[] getNullPropertyNames(Object source) {

        final BeanWrapper src = new BeanWrapperImpl(source);
        java.beans.PropertyDescriptor[] atributes = src.getPropertyDescriptors();

        Set<String> emptyNames = new HashSet<>();
        for (java.beans.PropertyDescriptor atribute : atributes) {
            Object srcValue = src.getPropertyValue(atribute.getName());
            if (srcValue == null) emptyNames.add(atribute.getName());
        }
        String[] result = new String[emptyNames.size()];
        return emptyNames.toArray(result);
    }
}
