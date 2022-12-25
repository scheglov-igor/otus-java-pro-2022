package ru.otus.appcontainer;

import ru.otus.appcontainer.api.AppComponent;
import ru.otus.appcontainer.api.AppComponentsContainer;
import ru.otus.appcontainer.api.AppComponentsContainerConfig;
import ru.otus.config.AppConfig;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

public class AppComponentsContainerImpl implements AppComponentsContainer {

    private final List<BeanType> appComponents = new ArrayList<>();
    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);

        try {
            Object instance = configClass.getConstructor().newInstance();

            Method[] methods = configClass.getDeclaredMethods();

            List<Method> orderedComponentMethods = Arrays.stream(methods)
                    .filter(method -> method.isAnnotationPresent(AppComponent.class))
                    .sorted((o1, o2) -> (o1.getAnnotation(AppComponent.class).order() - o2.getAnnotation(AppComponent.class).order()))
                    .toList();

            for (Method method: orderedComponentMethods) {

                AppComponent appComponent = method.getAnnotation(AppComponent.class);
                String name = appComponent.name();

                Parameter[] parameters = method.getParameters();

                Object beanInstance = null;

                if(parameters.length == 0) {
                    beanInstance = method.invoke(instance);
                }
                else {
                    Object[] paramObjects = new Object[parameters.length];
                    for (int i = 0; i < parameters.length; i++) {
                        paramObjects[i] = getAppComponent(parameters[i].getType());
                    }
                    beanInstance = method.invoke(instance, paramObjects);
                }

                Object repeatBean = appComponentsByName.put(name, beanInstance);
                if(repeatBean != null) {
                    throw new RuntimeException(String.format("Repeated bean name %s", name));
                }

                List<Class> beanTypes = new ArrayList<>();
                beanTypes.add(method.getReturnType());
                beanTypes.add(beanInstance.getClass());
                BeanType bt = new BeanType(beanTypes, beanInstance);

                appComponents.add(bt);
            }
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private void checkConfigClass(Class<?> configClass) {
        if (!configClass.isAnnotationPresent(AppComponentsContainerConfig.class)) {
            throw new IllegalArgumentException(String.format("Given class is not config %s", configClass.getName()));
        }
    }

    @Override
    public <C> C getAppComponent(Class<C> componentClass) {
        C returnBean = null;

        for (BeanType beanType: appComponents) {
            for(Class type: beanType.getBeanType()) {
               if (componentClass == type) {
                    if (returnBean == null) {
                        returnBean = (C) beanType.getBean();
                    } else {
                        throw new RuntimeException(String.format("There are many beans of type %s", componentClass.getName()));
                    }
                }
            }
        }
        if (returnBean == null) {
            throw new RuntimeException(String.format("There are no beans of type %s", componentClass.getName()));
        }
        return returnBean;

    }

    @Override
    public <C> C getAppComponent(String componentName) {
        Object returnObject = appComponentsByName.get(componentName);

        if (returnObject == null) {
            throw new RuntimeException(String.format("There are no beans of name %s", componentName));
        }
        return (C) returnObject;
    }
}
