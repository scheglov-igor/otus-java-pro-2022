package ru.otus.appcontainer;

import java.util.List;

public class BeanType {
    private final List<Class> beanType;
    private final Object bean;

    public BeanType(List<Class> beanType, Object bean) {
        this.beanType = beanType;
        this.bean = bean;
    }

    public List<Class> getBeanType() {
        return beanType;
    }

    public Object getBean() {
        return bean;
    }

    @Override
    public String toString() {
        return "BeanType{" +
                "beanType=" + beanType +
                ", bean=" + bean +
                '}';
    }
}
