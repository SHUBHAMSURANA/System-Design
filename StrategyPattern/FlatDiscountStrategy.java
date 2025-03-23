package com.company.discount.service;

@Component
public class FlatDiscountStrategy implements DiscountStrategy {

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
