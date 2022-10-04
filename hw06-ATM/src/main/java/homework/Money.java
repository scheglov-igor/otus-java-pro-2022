package homework;

import java.util.Map;
import java.util.TreeMap;

public enum Money {
    MONEY_100(100),
    MONEY_200(200),
    MONEY_500(500),
    MONEY_1000(1000),
    MONEY_2000(2000),
    MONEY_5000(5000);

    private final int nominal;

    Money(int nominal) {
        this.nominal = nominal;
    }

    public int getNominal() {
        return nominal;
    }

    public static Map<Money, Box> createAllMoneyBoxes() {
        Map<Money, Box> boxMap = new TreeMap<>(((o1, o2) -> (Integer.compare(o2.getNominal(), o1.getNominal()))));
        for (Money money: Money.values()) {
            boxMap.put(money, new Box(money));
        }
        return boxMap;
    }
}
