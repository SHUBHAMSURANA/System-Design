// Sevice class to call corresponding discount method.
@Service
public class OrderService {

    private final DiscountStrategyFactory discountStrategyFactory;

    @Autowired
    public OrderService(DiscountStrategyFactory discountStrategyFactory) {
        this.discountStrategyFactory = discountStrategyFactory;
    }

  
    public double calculateFinalAmount(String strategyName, double orderAmount) {
        DiscountStrategy strategy = discountStrategyFactory.getStrategy(strategyName);
        return strategy.applyDiscount(orderAmount);
    }
}
