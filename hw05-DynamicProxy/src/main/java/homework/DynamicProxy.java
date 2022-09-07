package homework;

import homework.annotations.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

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

        DemoInvocationHandler(TestClassImpl myClass) {
            this.myClass = myClass;
        }

        public TestClassImpl getMyClass() {
            return myClass;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

            Method implMethod = myClass.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());
            if (implMethod.isAnnotationPresent(Log.class)) {
                System.out.println("executed method: " + method.getName() + ", param: " + Arrays.toString(args));
            }
            
            /*
            if (Proxy.isProxyClass(proxy.getClass())) {
                InvocationHandler handler = Proxy.getInvocationHandler(proxy);
                if (handler instanceof DemoInvocationHandler) {
                    DemoInvocationHandler demoInvocationHandler = (DemoInvocationHandler) handler;
                    TestClassImpl tci = demoInvocationHandler.getMyClass();
                    Method implMethod = tci.getClass().getDeclaredMethod(method.getName(), method.getParameterTypes());

                    if (implMethod.isAnnotationPresent(Log.class)) {
                        System.out.println("executed method: " + method.getName() + ", param: " + Arrays.toString(args));
                    }
                }
            }
            */

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
