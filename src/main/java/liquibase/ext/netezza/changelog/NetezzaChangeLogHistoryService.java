package liquibase.ext.netezza.changelog;

import liquibase.changelog.StandardChangeLogHistoryService;
import liquibase.database.Database;
import liquibase.ext.netezza.database.NetezzaDatabase;

public class NetezzaChangeLogHistoryService extends StandardChangeLogHistoryService {

	@Override
	public int getPriority() {
		return PRIORITY_DEFAULT;
	}

	@Override
	public boolean supports(Database database) {
		return database instanceof NetezzaDatabase;
	}
}
