package homework;

import java.util.*;

public class Atm {
    private final Map<Money, Box> boxMap;


    public Atm() {

        boxMap = new TreeMap<>(((o1, o2) -> (o2.getNominal() - o1.getNominal())));
        for (Money money: Money.values()) {
            boxMap.put(money, new Box(money));
        }
    }


    public Integer getATMAmount() {
        Integer amount = 0;
        for(Box box: boxMap.values()) {
            amount += box.getAmount();
        }
        return amount;
    }

    public void push(List<MoneyBundle> moneyBundleList) {
        for(MoneyBundle moneyBundle: moneyBundleList) {
            boxMap.get(moneyBundle.getMoney()).push(moneyBundle);
        }
    }

    public List<MoneyBundle> pop (Integer amount) throws Exception {
        Integer amountToCheck = amount;
        for(Box box: boxMap.values()) {
            if(amountToCheck > 0) {
                MoneyBundle moneyBundleFromBox = box.pop(amount, true);
                if (moneyBundleFromBox != null) {
                    amountToCheck -= moneyBundleFromBox.getAmonunt();
                }
            }
        }

        if(amountToCheck > 0) {
            throw new Exception("Requested amount cannot be paid");
        }

        List<MoneyBundle> moneyBundleList = new ArrayList<>();
        amountToCheck = amount;
        for(Box box: boxMap.values()) {
            if(amountToCheck > 0) {
                MoneyBundle moneyBundleFromBox = box.pop(amount, false);
                if(moneyBundleFromBox != null) {
                    amount -= moneyBundleFromBox.getAmonunt();
                    moneyBundleList.add(moneyBundleFromBox);
                }
            }
        }

        return moneyBundleList;
    }
}
