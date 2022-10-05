package homework;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Main {

    public static void main(String[] args) {
        AtmInterface atm = new Atm(AtmBoxes.createAllMoneyBoxes());

        List<MoneyBundle> pushBundleList = new ArrayList<>();
        pushBundleList.add(new MoneyBundle(1, Money.MONEY_1000));
        pushBundleList.add(new MoneyBundle(1, Money.MONEY_100));

        atm.push(pushBundleList);

        try {
            List<MoneyBundle> popBundleList = atm.pop(100);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
