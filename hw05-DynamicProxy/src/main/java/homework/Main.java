package homework;

import homework.annotations.Log;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        TestInterface testClass = DynamicProxy.createMyClass();
        testClass.calculation(55);
        testClass.calculation(1, 2);
        testClass.calculation("qqqq", 123);
    }

    static void printMethods(Object obj) {
        Arrays.stream(obj.getClass().getDeclaredMethods())
                .forEach(method -> System.out.println(method.toString() + " has A annotation: " + method.isAnnotationPresent(Log.class)));
    }
}
