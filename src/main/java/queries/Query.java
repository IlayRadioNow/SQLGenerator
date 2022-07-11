package queries;

import enums.InstructionType;
import enums.JoinType;
import enums.OrderType;
import enums.ComparisonType;

import java.util.Map;

public abstract class Query
{
    private static char OPEN_COLUMN_BORDER;
    private static char CLOSE_COLUMN_BORDER;
    private static final char DEFAULT_COLUMN_BORDER = '\u0000';
    private static final char VALUES_SEPARATOR = ',';

    private final String tableName;
    private final char quotationMark;
    private final Map<InstructionType, String> instructions;

    public Query(String tableName, Map<InstructionType, String> instructions)
    {
        this.tableName = tableName;
        this.instructions = instructions;
        this.quotationMark = quotationMark();
        
        OPEN_COLUMN_BORDER = getOpenColumnBorder();
        CLOSE_COLUMN_BORDER = getCloseColumnBorder();
        
        instructions.put(InstructionType.FROM, "FROM " + tableName + " ");
    }

    // Interface
    public Query select(String ... columns)
    {
        StringBuilder builder = new StringBuilder("SELECT ");
        
        if (!buildInstruction(builder, columns))
        {
            instructions.put(InstructionType.SELECT, "SELECT * ");
            return this;
        }
        builder.append(" ");
        instructions.put(InstructionType.SELECT, builder.toString());
        return this;
    }
    public Query select()
    {
        return select(null);
    }
    
    public Query whereIn(String column, String ... args)
    {
        if (!isArgsExist(args))
            return null;
        
        StringBuilder builder = prepareBuilderForInstruction(InstructionType.WHERE);
        
        appendAndDecorateColumn(builder, OPEN_COLUMN_BORDER, column, CLOSE_COLUMN_BORDER);
        
        builder.append(" ")
                .append(ComparisonType.IN.getValue())
                .append("(")
                .append(quotationMark)
                .append(args[0])
                .append(quotationMark);
        
        if (args.length == 1)
        {
            builder.append(") ");
            return this;
        }
        
        for (int i=1; i<args.length; i++)
        {
            builder.append(VALUES_SEPARATOR)
                    .append(quotationMark)
                    .append(args[i])
                    .append(quotationMark);
        }
        
        builder.append(") ");
        
        instructions.put(InstructionType.WHERE, builder.toString());
        return this;
    }
    public Query whereNotNull(String column)
    {
        StringBuilder builder = prepareBuilderForInstruction(InstructionType.WHERE);
        
        appendAndDecorateColumn(builder, OPEN_COLUMN_BORDER, column, CLOSE_COLUMN_BORDER);
        
        builder.append(" ")
                .append(ComparisonType.IS_NOT_NULL.getValue());
        
        instructions.put(InstructionType.WHERE, builder.toString());
        return this;
    }
    
    public Query orderBy(OrderType type, String ... columns)
    {
        StringBuilder builder = new StringBuilder("ORDER BY ");
        
        if (!buildInstruction(builder, columns))
            return this;
        
        builder.append(" ")
                .append(type)
                .append(" ");
        
        instructions.put(InstructionType.ORDER_BY, builder.toString());
        return this;
    }
    
    public Query join(String tableName, String leftParam, String rightParam, JoinType type)
    {
        StringBuilder builder = new StringBuilder(type + " JOIN ");
        
        builder.append(tableName)
                .append(" ON ");
        appendCondition(builder, leftParam, ComparisonType.EQUAL, rightParam);
        
        String oldInstruction = instructions.get(InstructionType.JOIN);
        if (oldInstruction == null)
            instructions.put(InstructionType.JOIN, builder.toString());
        else
            instructions.put(InstructionType.JOIN, oldInstruction  + builder);
        
        return this;
    }
    public Query join(String tableName, String leftParam, String rightParam)
    {
        return join(tableName, leftParam, rightParam, JoinType.INNER);
    }

    
    protected abstract boolean needColumnBorder();
    protected abstract char quotationMark();
    protected abstract boolean isDataCaseSensitive();
    protected abstract String aliasForColumnsAndTables();
    protected char getOpenColumnBorder()
    {
        return DEFAULT_COLUMN_BORDER;
    }
    protected char getCloseColumnBorder()
    {
        return DEFAULT_COLUMN_BORDER;
    }

    public String generate()
    {
        StringBuilder builder = new StringBuilder();

        for (InstructionType type : InstructionType.values())
        {
            String instruction = instructions.get(type);
            if (instruction == null)
                continue;
            builder.append(instruction);
        }

        return builder.toString();
    }


    public String getTableName()
    {
        return tableName;
    }
    public Map<InstructionType, String> getInstructions()
    {
        return instructions;
    }
    
    // routines
    private boolean buildInstruction(StringBuilder builder, String[] columns)
    {
        if (!isArgsExist(columns))
            return false;
    
        if (needColumnBorder())
            appendColumns(builder, OPEN_COLUMN_BORDER, CLOSE_COLUMN_BORDER, columns);
        else
            appendColumns(builder, columns);
        
        return true;
    }
    private void appendColumns(StringBuilder builder, char open, char close, String[] columns)
    {
        appendAndDecorateColumn(builder, open, columns[0], close);
        
        for(int i=1; i<columns.length; i++)
        {
            builder.append(VALUES_SEPARATOR);
            appendAndDecorateColumn(builder, open, columns[i], close);
        }
    }
    private void appendColumns(StringBuilder builder, String[] columns)
    {
        appendColumns(builder, DEFAULT_COLUMN_BORDER, DEFAULT_COLUMN_BORDER, columns);
    }
    private void appendAndDecorateColumn(StringBuilder builder, char open, String column, char close)
    {
        builder.append(open)
                .append(column)
                .append(close);
    }
    private StringBuilder prepareBuilderForInstruction(InstructionType type)
    {
        String instruction = instructions.get(type);
        if (instruction != null)
            return new StringBuilder(instruction + "AND ");
        else
            return new StringBuilder(type + " ");
    }
    private void appendCondition(StringBuilder builder, String leftParam, ComparisonType type, String rightParam)
    {
        if (needColumnBorder())
        {
            appendAndDecorateColumn(builder, OPEN_COLUMN_BORDER, leftParam, CLOSE_COLUMN_BORDER);
            builder.append(ComparisonType.EQUAL.getValue());
            appendAndDecorateColumn(builder, OPEN_COLUMN_BORDER, rightParam, CLOSE_COLUMN_BORDER);
        }
        else
        {
            builder.append(leftParam)
                    .append(ComparisonType.EQUAL.getValue())
                    .append(rightParam);
        }
        builder.append(" ");
    }
    private boolean isArgsExist(String[] args)
    {
        return args!=null && args.length>0;
    }

}
