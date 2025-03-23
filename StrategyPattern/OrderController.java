/**
 * REST Controller to expose discount calculation API.
 */
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * http://localhost:8080/api/orders/discount?strategyName=FLAT&orderAmount=1000
     * http://localhost:8080/api/orders/discount?strategyName=CASHBACK&orderAmount=1000
     */
    @GetMapping("/discount")
    public double getDiscountedAmount(@RequestParam String strategyName,
                                      @RequestParam double orderAmount) {
        return orderService.calculateFinalAmount(strategyName, orderAmount);
    }
}

