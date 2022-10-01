package homework;

public class Box {

    private final Money money;
    private Integer value = 0;

    public Box(Money money) {
        this.money = money;
    }

    public void push(MoneyBundle moneyBundle) {
        if(moneyBundle.getValue() > 0) {
            this.value += moneyBundle.getValue();
        }
    }

    public MoneyBundle pop(Integer popAmount, Boolean onlyCheck) {
        int popVal = popAmount / money.getNominal();
        if(popVal > value) {
            popVal = value;
        }
        if(popVal > 0) {
            if(!onlyCheck) {
                value -= popVal;
            }
            return new MoneyBundle(popVal, money);
        }
        return null;
    }

    public Integer getAmount() {
        return value * money.getNominal();
    }

    public Integer getNominal() {
        return money.getNominal();
    }

    @Override
    public String toString() {
        return "Box{" +
                "nominal=" + money.getNominal() +
                ", value=" + value +
                ", amount=" + getAmount() +

                '}';
    }
}
