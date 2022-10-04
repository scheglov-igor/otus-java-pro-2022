package homework;

public class MoneyBundle {
    private final Integer value;
    private final Money money;

    public MoneyBundle(Integer value, Money money) {
        this.value = value;
        this.money = money;
    }

    public Integer getValue() {
        return value;
    }

    public Money getMoney() {
        return money;
    }

    public Integer getAmonunt() { return value * money.getNominal(); }

    @Override
    public String toString() {
        return "MoneyBundle{" +
                "value=" + value +
                ", money=" + money +
                '}';
    }
}
