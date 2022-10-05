package homework;

import java.util.*;

public class Atm implements AtmInterface {

    private final AtmBoxes atmBoxes;

    public Atm(Map<Money, Box> boxMap) {
        this.atmBoxes = new AtmBoxes(boxMap);
    }

    @Override
    public int getATMAmount() {
        return atmBoxes.getATMAmount();
    }

    @Override
    public List<MoneyBundle> push(List<MoneyBundle> moneyBundleList) {
        return atmBoxes.push(moneyBundleList);
    }

    @Override
    public List<MoneyBundle> pop(int amount) throws Exception {
        return atmBoxes.pop(amount);
    }
}
