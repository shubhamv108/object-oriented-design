IProductService
+ getProductPrice(product: String): double
+ getShippingTime(product: String): int
+ getPremiumAnnualFee(): int

NormalProductService(IProductService)

AbstractPremiumDecorator(IProductService)
- wrappedService: IProductService
+ AbstractPremiumDecorator(productService: IProductService)

PlatinumPremiumDecorator(AbstractPremiumDecorator)
GoldPremiumDecorator(AbstractPremiumDecorator)
SilverPremiumDecorator(AbstractPremiumDecorator)
BronzePremiumDecorator(AbstractPremiumDecorator)
