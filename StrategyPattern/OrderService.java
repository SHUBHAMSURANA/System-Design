// Sevice class to call corresponding discount method.
@Service
public class OrderService {

    private final DiscountStrategyFactory discountStrategyFactory;

    @Autowired
    public OrderService(DiscountStrategyFactory discountStrategyFactory) {
        this.discountStrategyFactory = discountStrategyFactory;
    }

  
    public double calculateFinalAmount(String strategyName, double orderAmount) {
        // Below Factory Pattern helps create the right object.
        DiscountStrategy strategy = discountStrategyFactory.getStrategy(strategyName);
        //Strategy allows different behaviors (algorithms) in those objects.
        return strategy.applyDiscount(orderAmount);
    }
}
