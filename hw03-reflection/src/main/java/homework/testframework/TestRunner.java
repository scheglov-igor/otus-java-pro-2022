package homework.testframework;

import homework.annotations.After;
import homework.annotations.Before;
import homework.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class TestRunner {


    //это все получилось достаточно stateless???
    public static void main(String[] args) {
        try {
            runTest("homework.test.MyTest");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static void runTest(String className) throws ClassNotFoundException {
        Class<?> clazz = Class.forName(className);

        //собираем все методы с подходящими аннотациями
        Method[] methodsAll = clazz.getDeclaredMethods();

        List<Method> beforeMethodList = new ArrayList<>();
        List<Method> afterMethodList = new ArrayList<>();
        List<Method> testMethodList = new ArrayList<>();

        for (Method method: methodsAll) {
            if(method.isAnnotationPresent(Before.class)) {
                beforeMethodList.add(method);
            }
            if(method.isAnnotationPresent(After.class)) {
                afterMethodList.add(method);
            }
            if(method.isAnnotationPresent(Test.class)) {
                testMethodList.add(method);
            }
        }

        int success = 0;
        int wrong = 0;
        int error = 0;

        for(Method testMethod: testMethodList) {
            TestInstance ti = new TestInstance(clazz, beforeMethodList, afterMethodList, testMethod);
            ti.runTests();
            System.out.println(ti.getResultDescription());

            if(ti.getResult() == null) {
                error++;
            }
            else if (ti.getResult()) {
                success++;
            }
            else {
                wrong++;
            }


        }

        System.out.println("---");
        System.out.println("tests success: " + success);
        System.out.println("tests wrong: " + wrong);
        System.out.println("tests error: " + error);


    }
}
