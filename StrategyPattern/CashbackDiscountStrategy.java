
@Component
public class CashbackDiscountStrategy implements DiscountStrategy {

    private static final double CASHBACK_PERCENTAGE = 5.0;

    @Override
    public double applyDiscount(double orderAmount) {
        double cashbackAmount = (orderAmount * CASHBACK_PERCENTAGE) / 100;
        double discountedAmount = orderAmount - cashbackAmount;
        System.out.println("[CashbackDiscountStrategy] " + CASHBACK_PERCENTAGE + "% cashback applied (Rs. " + cashbackAmount + ").");
        return discountedAmount;
    }

    @Override
    public String getStrategyName() {
        return "CASHBACK";
    }
}
