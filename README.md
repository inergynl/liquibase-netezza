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

## Limitations
This extension has only been tested with the `SQL` refactoring command.
Other refactoring commands may or may not work as expected.