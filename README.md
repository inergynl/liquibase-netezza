# liquibase-netezza
A liquibase extension for Netezza.

## Installation
Add the liquibase-netezza jar and netezza jdbc driver to the lib folder of liquibase.

## Usage
	liquibase --username=user --password=password --url=jdbc:netezza://[host]:5480/[database] --changeLogFile=changelog.xml update

## Changelog example
**SQL / DDL example**
```xml
<databaseChangeLog
		xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
		 http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.6.xsd">
		
	<changeSet id="201801171058" author="John Doe">
		<comment>An example changeset</comment>
		<sql>
CREATE TABLE EXAMPLE (
    EXAMPLE_COL INTEGER not null
);
		</sql>
		<rollback>
DROP TABLE EXAMPLE;
		</rollback>
	</changeSet>
</databaseChangeLog>
```

**Stored procedure example**

If you want to create a stored procedure, use `splitStatements=false` and enclose it within a `CDATA` section.
```xml
<databaseChangeLog
		xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
		 http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.6.xsd">
		
	<changeSet id="201801171058" author="John Doe">
		<comment>An example changeset</comment>
		<sql splitStatements="false">
<![CDATA[
--YOUR PROCEDURE HERE
]]>
		</sql>
		<rollback>
			<sql splitStatements="false">
<![CDATA[
--YOUR ROLLBACK HERE
]]>			
			</sql>
		</rollback>
	</changeSet>
</databaseChangeLog>
```

## Transactions
By default all statements are run within a transaction block.  
Some SQL commands are prohibited within the BEGIN/COMMIT transaction block. For example:
* BEGIN
* [CREATE | DROP] DATABASE
* ALTER TABLE [ADD | DROP] COLUMN operations
* SET AUTHENTICATION
* [SET | DROP] CONNECTION
* GROOM TABLE
* GENERATE STATISTICS
* SET SYSTEM DEFAULT HOSTKEY
* [CREATE | ALTER|DROP] KEYSTORE
* [CREATE | DROP] CRYPTO KEY
* SET CATALOG
* SET SCHEMA dbname.schemaname, where dbname is not the current database

If you want to execute any of the SQL commands that are prohibited within the transaction block, you must use `runInTransaction="false"`

Example changelog:
```xml
<databaseChangeLog
		xmlns="http://www.liquibase.org/xml/ns/dbchangelog"
		xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
		xsi:schemaLocation="http://www.liquibase.org/xml/ns/dbchangelog
		 http://www.liquibase.org/xml/ns/dbchangelog/dbchangelog-3.6.xsd">
		
	<changeSet id="201801171060" author="John Doe" runInTransaction="false">
		<comment>A SQL command that is run outside a transaction block.</comment>
		<sql>
<![CDATA[
ALTER TABLE TEST ADD COLUMN TEST_COL INTEGER;
]]>
		</sql>
		<rollback>
			<sql>
<![CDATA[
ALTER TABLE TEST DROP COLUMN TEST_COL CASCADE;
]]>
			</sql>
		</rollback>
	</changeSet>
</databaseChangeLog>
```

## Limitations
This extension has only been tested with the `SQL` refactoring command.
Other refactoring commands may or may not work as expected.

## Upgrading from older liquibase versions
Newer versions of liquibase might add additional columns to the databasechangelog / databasechangeloglock tables.  
With Netezza most DDL statements can run in a transaction, but there are some exceptions.  
For example `ALTER TABLE [ADD | DROP] COLUMN` operations cannot run in a transaction.  
When upgrading Liquibase, Netezza will complain about not being able to add columns to the metadata tables because of this.  
Any new columns in the metadata tables must be added manually.  

