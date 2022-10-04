package homework;

public class MoneyBundle {
    private final int value;
    private final Money money;

    public MoneyBundle(int value, Money money) {
        this.value = value;
        this.money = money;
    }

    public int getValue() {
        return value;
    }

    public Money getMoney() {
        return money;
    }

    public int getAmonunt() { return value * money.getNominal(); }

    @Override
    public String toString() {
        return "MoneyBundle{" +
                "value=" + value +
                ", money=" + money +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MoneyBundle that = (MoneyBundle) o;

        if (value != that.value) return false;
        return money == that.money;
    }
}
