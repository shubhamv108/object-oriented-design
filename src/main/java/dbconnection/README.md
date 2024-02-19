MySQLConnectionAdaptee
+ openConnection()
+ closeConnection()
+ execute(query: String): Object
+ executeUpdateQuery(query: String): int

PostgreSQLConnectionAdaptee
+ openDBConnection()
+ closeDBConnection()
+ performQuery(query: String): Object
+ performUpdate(query: String): int

OracleConnectionAdaptee
+ startConnection()
+ endConnection()
+ getResult(query: String): Object
+ getUpdatedCount(query: String): int

<<ConnectionTarget>>
+ open()
+ close()
+ executeQuery(query: String): Object
+ executeUpdate(query: String): int

MySQlConnectionAdapter(ConnectionTarget)
- connection: MySQLConnectionAdaptee

PostgreSQlConnectionAdapter(ConnectionTarget)
- connection: PostgreSQLConnectionAdaptee

OracleConnectionAdapter(ConnectionTarget)
- connection: OracleConnectionAdaptee