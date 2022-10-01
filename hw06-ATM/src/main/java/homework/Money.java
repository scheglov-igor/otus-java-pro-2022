package homework;

public enum Money {
    MONEY_100(100),
    MONEY_200(200),
    MONEY_500(500),
    MONEY_1000(1000),
    MONEY_2000(2000),
    MONEY_5000(5000);

    private final Integer nominal;

    Money(Integer nominal) {
        this.nominal = nominal;
    }

    public Integer getNominal() {
        return nominal;
    }
}
