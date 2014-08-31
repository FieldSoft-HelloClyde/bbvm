package me.wener.bbvm.core.asm;

import java.util.EnumMap;
import java.util.Map;
import me.wener.bbvm.core.IsValue;

/**
 * 数据类型<pre>
 * dword    | 0x0
 * word   | 0x1
 * byte   | 0x2
 * float  | 0x3
 * int    | 0x4
 * </pre>
 */
public enum DataType implements IsValue<Integer>
{
    T_DWORD (0x0),
    T_WORD  (0x1),
    T_BYTE  (0x2),
    T_FLOAT (0x3),
    T_INT   (0x4);
    private final int value;
    private final static Map<DataType, String> strings = new EnumMap<DataType, String>(DataType.class);
    static
    {
        strings.put(T_DWORD, "DWORD");
        strings.put(T_WORD, "WORD");
        strings.put(T_BYTE, "BYTE");
        strings.put(T_FLOAT, "FLOAT");
        strings.put(T_INT, "INT");
    }
    DataType(int value)
    {
        this.value = value;
    }

    public Integer asValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return strings.get(this);
    }
}
