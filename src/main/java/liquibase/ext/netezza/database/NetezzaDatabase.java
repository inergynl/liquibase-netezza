package liquibase.ext.netezza.database;

import liquibase.database.AbstractJdbcDatabase;
import liquibase.database.DatabaseConnection;
import liquibase.exception.DatabaseException;
import liquibase.statement.SqlStatement;
import liquibase.statement.core.RawCallStatement;
import liquibase.structure.DatabaseObject;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class NetezzaDatabase extends AbstractJdbcDatabase {
	public static final String PRODUCT_NAME = "Netezza NPS";
	private Set<String> reservedWords = new HashSet<String>();
	private Set<String> systemTablesAndViews = new HashSet<String>();

	public NetezzaDatabase() {
		super.setCurrentDateTimeFunction("CURRENT_TIMESTAMP");

		reservedWords.addAll(Arrays.asList("ABORT", "DEC", "LEADING", "RESET", "ADMIN", "DECIMAL", "LEFT", "REUSE", "AGGREGATE",
				"DECODE", "LIKE", "RIGHT", "ALIGN", "DEFAULT", "LIMIT", "ROWS", "ALL", "DEFERRABLE", "LISTEN", "ROWSETLIMIT",
				"ALLOCATE", "DESC", "LOAD", "RULE", "ANALYSE", "DISTINCT", "LOCAL", "SEARCH", "ANALYZE", "DISTRIBUTE", "LOCK",
				"SELECT", "AND", "DO", "MATERIALIZED", "SEQUENCE", "ANY", "ELSE", "MINUS", "SESSION_USER", "AS", "END", "MOVE",
				"SETOF", "ASC", "EXCEPT", "NATURAL", "SHOW", "BETWEEN", "EXCLUDE", "NCHAR", "SOME", "BINARY", "EXISTS", "NEW",
				"SUBSTRING", "BIT", "EXPLAIN", "NOT", "SYSTEM", "BOTH", "EXPRESS", "NOTNULL", "TABLE", "CASE", "EXTEND", "NULL",
				"THEN", "CAST", "EXTERNAL", "NULLIF", "TIES", "CHAR", "EXTRACT", "NULLS", "TIME", "CHARACTER", "FALSE", "NUMERIC",
				"TIMESTAMP", "CHECK", "FIRST", "NVL", "TO", "CLUSTER", "FLOAT", "NVL2", "TRAILING", "COALESCE", "FOLLOWING",
				"OFF", "TRANSACTION", "COLLATE", "FOR", "OFFSET", "TRIGGER", "COLLATION", "FOREIGN", "OLD", "TRIM", "COLUMN",
				"FROM", "ON", "TRUE", "CONSTRAINT", "FULL", "ONLINE", "UNBOUNDED", "COPY", "FUNCTION", "ONLY", "UNION", "CROSS",
				"GENSTATS", "OR", "UNIQUE", "CURRENT", "GLOBAL", "ORDER", "USER", "CURRENT_CATALOG", "GROUP", "OTHERS", "USING",
				"CURRENT_DATE", "HAVING", "OUT", "VACUUM", "CURRENT_DB", "IDENTIFIER_CASE", "OUTER", "VARCHAR", "CURRENT_SCHEMA",
				"ILIKE", "OVER", "VERBOSE", "CURRENT_SID", "IN", "OVERLAPS", "VERSION", "CURRENT_TIME", "INDEX", "PARTITION",
				"VIEW", "CURRENT_TIMESTAMP", "INITIALLY", "POSITION", "WHEN", "CURRENT_USER", "INNER", "PRECEDING", "WHERE",
				"CURRENT_USERID", "INOUT", "PRECISION", "WITH", "CURRENT_USEROID", "INTERSECT", "PRESERVE", "WRITE", "DEALLOCATE",
				"INTERVAL", "PRIMARY", "RESET", "INTO", "REUSE", "CTID", "OID", "XMIN", "CMIN", "XMAX", "CMAX", "TABLEOID",
				"ROWID", "DATASLICEID", "CREATEXID", "DELETEXID"));

		systemTablesAndViews.addAll(Arrays.asList("_v_procedure", "_v_relation_column", "_v_relation_column_def", "_v_sequence",
				"_v_session", "_v_table", "_v_table_dist_map", "_v_table_index", "_v_user", "_v_usergroups", "_v_view",
				"_v_sys_group_priv", "_v_sys_index", "_v_sys_priv", "_v_sys_table", "_v_sys_user_priv", "_v_sys_view"));

		super.sequenceNextValueFunction = "%s.NEXTVAL";
		super.sequenceCurrentValueFunction = "%s.CURRVAL";
		super.unmodifiableDataTypes.addAll(Arrays.asList("boolean", "char", "varchar", "nchar", "nvarchar", "date", "timestamp",
				"time", "interval", "time with time zone", "real", "double precision", "integer", "byteint", "smallint", "bigint",
				"bool", "datetime", "time without time zone", "timespan", "timetz", "double", "float", "float8", "int", "int4",
				"int1", "int2", "int8"));
		super.unquotedObjectsAreUppercased=true;
	}

	/**
	 * Although Netezza supports DDL in a transaction. It has it's limitations. For example ALTER TABLE [ADD | DROP] COLUMN operations cannot run in a transaction.
	 * @return true
	 */
	@Override
	public boolean supportsDDLInTransaction() {
		return true;
	}

	@Override
	public String getShortName() {
		return PRODUCT_NAME;
	}

	@Override
	public int getPriority() {
		return PRIORITY_DEFAULT;
	}

	@Override
	protected String getDefaultDatabaseProductName() {
		return PRODUCT_NAME;
	}

	@Override
	public Integer getDefaultPort() {
		return 5480;
	}

	@Override
	public boolean supportsInitiallyDeferrableColumns() {
		return true;
	}

	@Override
	public boolean isCorrectDatabaseImplementation(DatabaseConnection conn) throws DatabaseException {
		String databaseProductName = conn.getDatabaseProductName();
		return PRODUCT_NAME.equalsIgnoreCase(databaseProductName);
	}

	@Override
	public boolean isReservedWord(String tableName) {
		return reservedWords.contains(tableName.toUpperCase());
	}

	@Override
	protected SqlStatement getConnectionSchemaNameCallStatement() {
		return new RawCallStatement("select current_schema");
	}

	@Override
	public String getDefaultDriver(String url) {
		if (url.startsWith("jdbc:netezza:")) {
			return "org.netezza.Driver";
		}
		return null;
	}

	@Override
	public boolean supportsTablespaces() {
		return false;
	}

	@Override
	public Set<String> getSystemViews() {
		return systemTablesAndViews;
	}

	@Override
	public boolean supportsSequences() {
		return true;
	}

	@Override
	public boolean supportsRestrictForeignKeys() {
		return false;
	}

	@Override
	public boolean supportsDropTableCascadeConstraints() {
		return false;
	}

	@Override
	public boolean supportsAutoIncrement() {
		return false;
	}

	@Override
	public boolean isCaseSensitive() {
		return false;
	}

	@Override
	public boolean supportsCatalogInObjectName(final Class<? extends DatabaseObject> type) {
		return true;
	}
}