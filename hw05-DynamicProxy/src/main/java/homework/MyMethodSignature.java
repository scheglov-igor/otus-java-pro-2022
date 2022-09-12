package homework;

import java.util.Arrays;

public class MyMethodSignature {
    String name;
    Object[] params;

    public MyMethodSignature(String name, Object[] params) {
        this.name = name;
        this.params = params;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MyMethodSignature that = (MyMethodSignature) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return Arrays.equals(params, that.params);
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + Arrays.hashCode(params);
        return result;
    }

    @Override
    public String toString() {
        return "MyMethodSignature{" +
                "name='" + name + '\'' +
                ", params=" + Arrays.toString(params) +
                '}';
    }
}
