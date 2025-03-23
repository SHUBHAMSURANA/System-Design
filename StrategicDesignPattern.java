// Based upon the rest api the Corresponding discount strategic will be applied

// Common interface for all discount strategies with 2 methods.
interface DiscountStrategy {

    double applyDiscount(double orderAmount);    
    String getStrategyName();
}

// Now lets create the 2 implementing class
//*****************Interface 1********************
@Component
class FlatDiscountStrategy implements DiscountStrategy {

    private static final double FLAT_AMOUNT = 100.0;

    @Override
    public double applyDiscount(double orderAmount) {
        double discountedAmount = Math.max(orderAmount - FLAT_AMOUNT, 0);
        System.out.println("[FlatDiscountStrategy] Rs. " + FLAT_AMOUNT + " flat discount applied.");
        return discountedAmount;
    }

    @Override
    public String getStrategyName() {
        return "FLAT";
    }
}
//*****************Interface 2********************
@Component
class CashbackDiscountStrategy implements DiscountStrategy {

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

// Common class where both above bean will be injected for use
@Component
class DiscountStrategyFactory {
    private final Map<String, DiscountStrategy> strategyMap = new HashMap<>();

/* *******IMPORTANT***CONCEPT*********
It says:
"Hey, I have two (or more) beans implementing DiscountStrategy"
It injects both beans into the List<DiscountStrategy>
The list will look something like:
*/	
	
    @Autowired
    public DiscountStrategyFactory(List<DiscountStrategy> strategies) {
        for (DiscountStrategy strategy : strategies) {
            strategyMap.put(strategy.getStrategyName(), strategy);
        }
    }

    public DiscountStrategy getStrategy(String strategyName) {
        DiscountStrategy strategy = strategyMap.get(strategyName.toUpperCase());
        if (strategy == null) {
            throw new IllegalArgumentException("No strategy found for: " + strategyName);
        }
        return strategy;
    }
}

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

//Based on api path discount will be applied
// http://localhost:8080/api/orders/discount?strategyName=FLAT&orderAmount=1000

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping("/discount")
    public double getDiscountedAmount(@RequestParam String strategyName,
                                      @RequestParam double orderAmount) {
        return orderService.calculateFinalAmount(strategyName, orderAmount);
    }
}



