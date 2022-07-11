package enums;

public enum ComparisonType
{
	IS_NOT_NULL("IS NOT NULL "),
	IN("IN "),
	LIKE("LIKE "),
	EQUAL("="),
	GREATER(">"),
	LESS("<"),
	GREATER_OR_EQUAL(">="),
	NOT_EQUAL("<>"),
	LESS_OR_EQUAL("<=");
	
	private final String value;
	ComparisonType(String value)
	{
		this.value = value;
	}
	public String getValue()
	{
		return value;
	}
}
