package homework;

import java.util.List;

public interface AtmInterface {
    int getATMAmount();

    List<MoneyBundle> push(List<MoneyBundle> moneyBundleList);

    List<MoneyBundle> pop(int amount) throws Exception;
}
