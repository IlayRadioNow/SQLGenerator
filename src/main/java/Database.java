import enums.QueryType;
import queries.Query;
import queries.QueryFactory;

public class Database
{
    private final QueryType type;

    public Database(QueryType type)
    {
        this.type = type;
    }


    public Query query(String tableName)
    {
        return QueryFactory.createQuery(type, tableName);
    }
}
