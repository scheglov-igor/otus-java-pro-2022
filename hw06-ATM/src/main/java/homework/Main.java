package homework;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Atm atm = new Atm();

        List<MoneyBundle> pushBundleList = new ArrayList<>();
        pushBundleList.add(new MoneyBundle(1, Money.MONEY_1000));
        pushBundleList.add(new MoneyBundle(1, Money.MONEY_100));

        atm.push(pushBundleList);

        try {
            List<MoneyBundle> popBundleList = atm.pop(100);
            System.out.println("popBundleList = " + popBundleList);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("atm.getATMAmount() " + atm.getATMAmount());

    }


}
