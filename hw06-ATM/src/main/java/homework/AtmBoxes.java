package homework;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class AtmBoxes {
    private final Map<Money, Box> boxMap;

    public AtmBoxes(Map<Money, Box> boxMap) {
        this.boxMap = boxMap;
    }


    public int getATMAmount() {
        int amount = 0;
        for(Box box: boxMap.values()) {
            amount += box.getAmount();
        }
        return amount;
    }


    public List<MoneyBundle> push(List<MoneyBundle> moneyBundleList) {
        List<MoneyBundle> moneyBundleToReturnList = new ArrayList<>();
        for(MoneyBundle moneyBundle: moneyBundleList) {
            if (boxMap.get(moneyBundle.getMoney()) != null) {
                boxMap.get(moneyBundle.getMoney()).push(moneyBundle);
            }
            else {
                moneyBundleToReturnList.add(moneyBundle);
            }
        }
        return moneyBundleToReturnList;
    }


    public List<MoneyBundle> pop (int amount) throws Exception {
        int amountToCheck = amount;
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

    public static Map<Money, Box> createAllMoneyBoxes() {
        Map<Money, Box> boxMap = new TreeMap<>(((o1, o2) -> (Integer.compare(o2.getNominal(), o1.getNominal()))));
        for (Money money: Money.values()) {
            boxMap.put(money, new Box(money));
        }
        return boxMap;
    }

}
