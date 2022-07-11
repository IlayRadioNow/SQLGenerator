package queries;

import enums.QueryType;

public class QueryFactory
{
    public static Query createQuery(QueryType type, String tableName)
    {
        Query query;

        switch (type)
        {
            case SQL_SERVER: query = new SqlServerQuery(tableName); break;
            default: query = null;
        }

        return query;
    }
}
