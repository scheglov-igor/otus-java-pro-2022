package homework;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AtmTest {

    AtmInterface atm;

    @BeforeEach
    void createATM() {
        atm = new Atm(AtmBoxes.createAllMoneyBoxes());
    }

    @Test
    void testFree() {
        int amountByStart = atm.getATMAmount();
        assertThat(amountByStart).isEqualTo(0);
    }

    @Test
    void pushTest() {
        assertThat(atm.getATMAmount()).isEqualTo(0);

        List<MoneyBundle> pushBundleList = new ArrayList<>();
        pushBundleList.add(new MoneyBundle(1, Money.MONEY_1000));
        pushBundleList.add(new MoneyBundle(1, Money.MONEY_100));

        List<MoneyBundle> returnMoneyList = atm.push(pushBundleList);
        assertThat(atm.getATMAmount()).isEqualTo(1100);
        assertThat(returnMoneyList.size()).isEqualTo(0);

        List<MoneyBundle> pushBundleList2 = new ArrayList<>();
        pushBundleList2.add(new MoneyBundle(1, Money.MONEY_2000));
        pushBundleList2.add(new MoneyBundle(2, Money.MONEY_500));

        returnMoneyList = atm.push(pushBundleList2);
        assertThat(atm.getATMAmount()).isEqualTo(4100);
        assertThat(returnMoneyList.size()).isEqualTo(0);

    }

    @Test
    void popTest() throws Exception {

        List<MoneyBundle> pushBundleList = new ArrayList<>();
        pushBundleList.add(new MoneyBundle(1, Money.MONEY_1000));
        pushBundleList.add(new MoneyBundle(1, Money.MONEY_100));

        atm.push(pushBundleList);

        atm.pop(100);
        assertThat(atm.getATMAmount()).isEqualTo(1000);

    }

    @Test
    void pop2Test() throws Exception {

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

    @Test
    void pushWrongMoney() {

        Map<Money, Box> boxMap1000 = new TreeMap<>(((o1, o2) -> (Integer.compare(o2.getNominal(), o1.getNominal()))));
        boxMap1000.put(Money.MONEY_1000, new Box(Money.MONEY_1000));

        AtmInterface atm1000 = new Atm(boxMap1000);

        List<MoneyBundle> pushBundleList = new ArrayList<>();
        pushBundleList.add(new MoneyBundle(1, Money.MONEY_1000));
        pushBundleList.add(new MoneyBundle(1, Money.MONEY_100));
        List<MoneyBundle> returnBundleList = atm1000.push(pushBundleList);

        assertThat(atm1000.getATMAmount()).isEqualTo(1000);
        assertThat(returnBundleList.get(0)).isEqualTo(new MoneyBundle(1, Money.MONEY_100));

    }
}