package homework.test;

import homework.annotations.After;
import homework.annotations.Before;
import homework.annotations.Test;

public class MyTest {

    Integer i;

    @Before
    void beforeTest1() {
        i = 0;
    }

    @Before
    void beforeTest2() {
        i = 0;
    }


    @Test
    Boolean testSuccess() {
        i *= 5;
        return i.equals(0);
    }

    @Test
    Boolean testWrong() {
        i = 0;
        i = i + 1;
        return i.equals(1000);
    }

    @Test
    Boolean testError() {

        i = i / i;

        return i.equals(0);
    }

    @Test
    Boolean testSuccess2() {
        i += 1;
        i *= 5;
        return i.equals(5);
    }

    @After
    public void afterTest() {
        i = null;
    }




}
