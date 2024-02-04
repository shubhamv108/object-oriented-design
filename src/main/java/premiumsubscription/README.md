IProductService
+ getProductPrice(product: String): double
+ getShippingTime(product: String): int
+ getPremiumAnnualFee(): int

NormalProductService(IProductService)

AbstractPremiumDecorator(IProductService)
- WrappedService: IProductService
+ AbstractPremiumDecorator(IProductService)

PlatinumPremiumDecorator(AbstractPremiumDecorator)
GoldPremiumDecorator(AbstractPremiumDecorator)
SilverPremiumDecorator(AbstractPremiumDecorator)
BronzePremiumDecorator(AbstractPremiumDecorator)
