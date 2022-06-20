package liquibase.ext.fixnumtype.datatype;

import liquibase.database.Database;
import liquibase.database.core.*;
import liquibase.datatype.DataTypeInfo;
import liquibase.datatype.DatabaseDataType;
import liquibase.datatype.LiquibaseDataType;
import liquibase.datatype.core.NumberType;
import liquibase.servicelocator.PrioritizedService;
import liquibase.GlobalConfiguration;

@DataTypeInfo(name="number", aliases = {"numeric", "java.sql.Types.NUMERIC"}, minParameters = 0, maxParameters = 2, priority = LiquibaseDataType.PRIORITY_DEFAULT + 100)
public class NumberTypeExt extends NumberType {
    
    @Override
    public int getPriority() {
        return PrioritizedService.PRIORITY_DEFAULT + 100;
    }
    
    @Override
    public boolean supports(Database database) {
        return database instanceof PostgresDatabase;
    }
    
    @Override
    public DatabaseDataType toDatabaseDataType(Database database) {
        if (GlobalConfiguration.CONVERT_DATA_TYPES.getCurrentValue()){
            if ((getParameters().length > 1) && "1".equals(getParameters()[0]) && "0".equals(getParameters()[1])) {
                return new DatabaseDataType("BOOLEAN");
            }
        } else if ((getParameters().length > 0) && (Integer.parseInt(getParameters()[0].toString()) > 1000)) {
            return new DatabaseDataType("numeric");
        }
        return new DatabaseDataType("numeric", getParameters());
    }
}
