package homework.testframework;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class TestInstance {
    private Class clazz;
    private List<Method> beforeMethods;
    private List<Method> afterMethods;
    private Method testMethod;

    private Boolean result;
    private String resultDescription;

    public TestInstance(Class clazz, List<Method> beforeMethods, List<Method> afterMethods, Method testMethod) {
        this.clazz = clazz;
        this.beforeMethods = beforeMethods;
        this.afterMethods = afterMethods;
        this.testMethod = testMethod;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public List<Method> getBeforeMethods() {
        return beforeMethods;
    }

    public void setBeforeMethods(List<Method> beforeMethods) {
        this.beforeMethods = beforeMethods;
    }

    public List<Method> getAfterMethods() {
        return afterMethods;
    }

    public void setAfterMethods(List<Method> afterMethods) {
        this.afterMethods = afterMethods;
    }

    public Method getTestMethod() {
        return testMethod;
    }

    public void setTestMethod(Method testMethod) {
        this.testMethod = testMethod;
    }

    public String getResultDescription() {
        return resultDescription;
    }

    public void setResultDescription(String resultDescription) {
        this.resultDescription = resultDescription;
    }

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
        this.result = result;
    }

    public void runTests() {

        resultDescription = this.getTestMethod().getName() + " \t=>\t ";

        Object instance = null;

        try {
            instance = ReflectionHelper.instantiate(clazz);

            for(Method m: this.getBeforeMethods()) {
                m.setAccessible(true);
                m.invoke(instance);
            }

            this.getTestMethod().setAccessible(true);
            result = (Boolean)this.getTestMethod().invoke(instance);

            resultDescription += result;

        //мне тут хотелось разных catch повставлять, но поведение везде одинаковое...
        } catch (Exception e) {
            resultDescription += e.getCause();
        }

        finally {
            for(Method m: this.getAfterMethods()) {
                m.setAccessible(true);
                try {
                    m.invoke(instance);
                } catch (Exception e) {
                    resultDescription += e.getCause();
                }
            }
        }
    }

    @Override
    public String toString() {
        return "TestInstance{" +
                "clazz=" + clazz +
                ", beforeMethods=" + beforeMethods +
                ", afterMethods=" + afterMethods +
                ", testMethod=" + testMethod +
                ", resultString='" + resultDescription + '\'' +
                '}';
    }
}
