package homework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AtmTest {

    Atm atm;

    @BeforeEach
    void createATM() {
        atm = new Atm();
    }

    @Test
    void testFree() {
        //when
        Integer amountByStart = atm.getATMAmount();
        //then
        assertThat(amountByStart).isEqualTo(0);
    }

    @Test
    void pushTest() {
        assertThat(atm.getATMAmount()).isEqualTo(0);

        List<MoneyBundle> pushBundleList = new ArrayList<>();
        pushBundleList.add(new MoneyBundle(1, Money.MONEY_1000));
        pushBundleList.add(new MoneyBundle(1, Money.MONEY_100));

        atm.push(pushBundleList);
        assertThat(atm.getATMAmount()).isEqualTo(1100);

        List<MoneyBundle> pushBundleList2 = new ArrayList<>();
        pushBundleList2.add(new MoneyBundle(1, Money.MONEY_2000));
        pushBundleList2.add(new MoneyBundle(2, Money.MONEY_500));

        atm.push(pushBundleList2);
        assertThat(atm.getATMAmount()).isEqualTo(4100);

    }

    @Test
    void popTest() throws Exception {

        //TODO
        // подскажите, а в тестах нормально переиспользовать другие тесты?
        // pushTest();
        // с одной стороны, удобно
        // с другой - поменял что-то в pushTest() и всё сломалось в другом месте

        List<MoneyBundle> pushBundleList = new ArrayList<>();
        pushBundleList.add(new MoneyBundle(1, Money.MONEY_1000));
        pushBundleList.add(new MoneyBundle(1, Money.MONEY_100));

        atm.push(pushBundleList);

        atm.pop(100);
        assertThat(atm.getATMAmount()).isEqualTo(1000);

    }

    @Test
    void pop2Test() throws Exception {

        // а в тестах нормально переиспользовать другие тесты?
        // pushTest();
        // с одной стороны, удобно
        // с другой - поменял что-то в pushTest() и всё сломалось в другом месте

        List<MoneyBundle> pushBundleList = new ArrayList<>();
        pushBundleList.add(new MoneyBundle(4, Money.MONEY_1000));
        pushBundleList.add(new MoneyBundle(4, Money.MONEY_500));

        atm.push(pushBundleList);

        atm.pop(5000);
        assertThat(atm.getATMAmount()).isEqualTo(1000);

    }

    @Test
    void popNoMoney() {

        List<MoneyBundle> pushBundleList = new ArrayList<>();
        pushBundleList.add(new MoneyBundle(1, Money.MONEY_1000));
        pushBundleList.add(new MoneyBundle(1, Money.MONEY_100));
        atm.push(pushBundleList);

        Exception exception = assertThrows(Exception.class, () -> atm.pop(200));

        assertThat(exception.getMessage()).isEqualTo("Requested amount cannot be paid");
        assertThat(atm.getATMAmount()).isEqualTo(1100);

    }

}