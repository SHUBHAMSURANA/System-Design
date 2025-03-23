
@Component
public class DiscountStrategyFactory {

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
