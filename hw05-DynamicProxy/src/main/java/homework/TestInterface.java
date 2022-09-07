package homework;

import homework.annotations.Log;

public interface TestInterface {
    public void calculation(int param);

    @Log
    public void calculation(int param1, int param2);

    @Log
    public void calculation(String param1, int param2);
}
