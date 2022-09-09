package homework;

import homework.annotations.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.*;

public class DynamicProxy {

    private DynamicProxy() {
    }

    static TestInterface createMyClass() {
        InvocationHandler handler = new DemoInvocationHandler(new TestClassImpl());
        return (TestInterface) Proxy.newProxyInstance(DynamicProxy.class.getClassLoader(),
                new Class<?>[]{TestInterface.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {

        private final TestClassImpl myClass;

        private final Set<MyMethodSignature> methodsToLogSet;

        DemoInvocationHandler(TestClassImpl myClass) {
            this.myClass = myClass;
            methodsToLogSet = new HashSet<>();
            for (Method m: myClass.getClass().getDeclaredMethods()) {
                if(m.isAnnotationPresent(Log.class)) {
                    methodsToLogSet.add(new MyMethodSignature(m.getName(), m.getParameterTypes()));
                }
            }
        }

        public TestClassImpl getMyClass() {
            return myClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            MyMethodSignature invokedMethod = new MyMethodSignature(method.getName(), method.getParameterTypes());

            if(methodsToLogSet.contains(invokedMethod)) {
                System.out.println("executed method: " + method.getName() + ", param: " + Arrays.toString(args));
            }

            return method.invoke(myClass, args);

        }

        @Override
        public String toString() {
            return "DemoInvocationHandler{" +
                    "myClass=" + myClass +
                    '}';
        }
    }
}
