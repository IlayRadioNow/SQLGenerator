package queries;

import java.util.HashMap;


public class SqlServerQuery extends Query
{
    public SqlServerQuery(String table)
    {
        super(table, new HashMap<>());
    }

    @Override
    protected char quotationMark()
    {
        return '\'';
    }

    @Override
    protected boolean isDataCaseSensitive()
    {
        return true;
    }
    
    @Override
    protected String aliasForColumnsAndTables()
    {
        return "AS";
    }
    
    @Override
    protected boolean needColumnBorder()
    {
        return true;
    }
}
