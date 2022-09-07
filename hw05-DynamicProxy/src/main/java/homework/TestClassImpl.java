package homework;

import homework.annotations.Log;

import java.util.Arrays;

public class TestClassImpl implements TestInterface{
    @Log
    public void calculation(int param) {}

//    @Log
    public void calculation(int param1, int param2) {}

    @Log
    public void calculation(String param1, int param2) {}
}