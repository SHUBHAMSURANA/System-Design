package com.company.discount.service;

public interface DiscountStrategy {
  
    double applyDiscount(double orderAmount);
  
    String getStrategyName();
}
