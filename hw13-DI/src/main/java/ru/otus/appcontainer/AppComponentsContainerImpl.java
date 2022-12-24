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

    private final List<Object> appComponents = new ArrayList<>();

    private final Map<String, Object> appComponentsByName = new HashMap<>();

    public AppComponentsContainerImpl(Class<?> initialConfigClass) {
        processConfig(initialConfigClass);
    }

    private void processConfig(Class<?> configClass) {
        checkConfigClass(configClass);
        // You code here...

        try {
            Constructor constructor = configClass.getConstructor();
            AppConfig instance = (AppConfig) constructor.newInstance();

            System.out.println("instance = " + instance);

            AppComponentsContainerConfig accc = configClass.getAnnotation(AppComponentsContainerConfig.class);
            System.out.println("accc = " + accc);
            System.out.println("accc.order() = " + accc.order());

            Method[] methods = configClass.getDeclaredMethods();
            System.out.println("methods = " + methods);

            //
            List<Method> orderedComponentMethods = new ArrayList<>();

            for (Method m: methods) {
                if(m.isAnnotationPresent(AppComponent.class)) {
                    System.out.println("m??? = " + m);
                    orderedComponentMethods.add(m);
                }
            }

            orderedComponentMethods.sort((o1, o2) ->
                    (o1.getAnnotation(AppComponent.class).order()
                            - o2.getAnnotation(AppComponent.class).order()));

            for (Method m: orderedComponentMethods) {
                System.out.println("m = " + m);

                AppComponent ac = m.getAnnotation(AppComponent.class);
                System.out.println("ac = " + ac);
                int order = ac.order();
                String name = ac.name();
                System.out.println(order + " -> " + name);

                Parameter[] parameters = m.getParameters();
                System.out.println("parameters.length = " + parameters.length);

                Class retClass = m.getReturnType();
                System.out.println("retClass = " + retClass);

                if(parameters.length == 0) {
                    Object o = m.invoke(instance);

                    System.out.println("o = " + o);
                    System.out.println("o.getClass() = " + o.getClass());
                    
                    appComponentsByName.put(name, o);
                    appComponents.add(o);

                }
                else {
                    Object[] paramObjects = new Object[parameters.length];

                    for (int i = 0; i < parameters.length; i++) {
                        Parameter param = parameters[i];
                        System.out.println("param = " + param);
                        System.out.println("param.getName() = " + param.getName());
                        System.out.println("param.getType() = " + param.getType());

                        Object oooo = getAppComponent(param.getType());
                        System.out.println("oooo = " + oooo);
                        paramObjects[i] = oooo;
                    }
                    Object o = m.invoke(instance, paramObjects);
                    appComponentsByName.put(name, o);
                    appComponents.add(o);

                }



            }

//        System.out.println("m = " + m);
//
//




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
        System.out.println("---------------");
        System.out.println("componentClass = " + componentClass);
        System.out.println("appComponents = " + appComponents);
        C returnObject = null;

     //   System.out.println("returnObject.getClass() = " + returnObject.getClass());
        System.out.println("---------------");
        for (Object appComponent: appComponents) {
            System.out.println("appComponent.getClass() = " + appComponent.getClass());

            if(componentClass == appComponent.getClass()) {
                if(returnObject == null) {
                    returnObject = (C) appComponent;
                }
                else {
                    throw new IllegalArgumentException(String.format("There are many beans of type %s", componentClass.getName()));
                }
            }
            else {
                Class[] interfaces = appComponent.getClass().getInterfaces();
                for (Class interf : interfaces) {
                    System.out.println("interf = " + interf);
                    System.out.println("componentClass == interf = " + (componentClass == interf));

                    if (componentClass == interf) {
                        if (returnObject == null) {
                            returnObject = (C) appComponent;
                        } else {
                            throw new IllegalArgumentException(String.format("There are many beans of type %s", componentClass.getName()));
                        }
                    }
                }
            }

//            System.out.println("(appComponent.getClass() == componentClass = " + (appComponent.getClass() == componentClass));
//            System.out.println("appComponent.getClass().isInstance(componentClass) = " + appComponent.getClass().isInstance(componentClass));
//            System.out.println("!!! " + (componentClass.getClass().isInstance(appComponent)));


        }
        if (returnObject == null) {
            throw new IllegalArgumentException(String.format("There are no beans of type %s", componentClass.getName()));
        }
        return returnObject;

    }

    @Override
    public <C> C getAppComponent(String componentName) {
        Object returnObject = appComponentsByName.get(componentName);

        if (returnObject == null) {
            throw new IllegalArgumentException(String.format("There are no beans of name %s", componentName));
        }
        return (C) returnObject;
    }
}
