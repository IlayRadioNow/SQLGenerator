package enums;

public enum InstructionType
{
    SELECT("SELECT "),
    FROM("FROM "),
    WHERE("WHERE "),
    JOIN(" JOIN"),
    ORDER_BY("ORDER BY ");
    
    private String value;
    InstructionType(String value)
    {
        this.value = value;
    }
    public String getValue()
    {
        return value;
    }
}
