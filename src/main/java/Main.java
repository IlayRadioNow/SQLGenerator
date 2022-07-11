import enums.JoinType;
import enums.OrderType;
import enums.QueryType;
import queries.Query;

public class Main
{
    public static void main(String[] args)
    {
        Database db = new Database(QueryType.SQL_SERVER);
        Query query = db.query("Books")
                .select("id")
                .whereIn("name", "onegin", "sapiens")
                .join("Author", "id", "book_id")
                .join("Shelf", "id", "book_id", JoinType.LEFT)
                .orderBy(OrderType.ASC, "id", "name", "smth")
                .whereNotNull("name")
                .whereIn("id", "odin", "dva", "tri");
    
        System.out.println(query.generate());
    }
}
