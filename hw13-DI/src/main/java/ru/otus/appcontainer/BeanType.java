package ru.otus.appcontainer;

import java.util.List;

public class BeanType {
    List<Class> beanType;
    Object bean;

    public BeanType(List<Class> beanType, Object bean) {
        this.beanType = beanType;
        this.bean = bean;
    }

    public List<Class> getBeanType() {
        return beanType;
    }

    public void setBeanType(List<Class> beanType) {
        this.beanType = beanType;
    }

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    @Override
    public String toString() {
        return "BeanType{" +
                "beanType=" + beanType +
                ", bean=" + bean +
                '}';
    }
}
