# Using prototype design pattern define configuration management
    - Application configuration
    - Network configuration
    - Profile configuration
Use them by modifying

ConfigurationType
+ APPLICATION
+ NETWORK
+ PROFILE

<<Configuration>>
+ clone(): Configuration

ApplicationConfiguration(Configuration)
- properties: Properties
+ ApplicationConfiguration()
+ clone(): ApplicationConfiguration

NetworkConfiguration(Configuration)
- properties: Properties
+ NetworkConfiguration()
+ clone(): NetworkConfiguration

ProfileConfiguration(Configuration)
- properties: Properties
+ ProfileConfiguration()
+ clone(): ProfileConfiguration

ConfigurationManager
- baseApplicationConfiguration: ApplicationConfiguration
- baseNetworkConfiguration: NetworkConfiguration
- baseProfileConfiguration: ProfileConfiguration
+ ConfigurationManager(baseApplicationConfiguration: ApplicationConfiguration, baseNetworkConfiguration: NetworkConfiguration, baseProfileConfiguration: ProfileConfiguration)
+ getApplicationConfiguration(): ApplicationConfiguration
+ getNetworkConfiguration(): NetworkConfiguration
+ getProfileConfiguration(): ProfileConfiguration
