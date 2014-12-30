package me.wener.bbvm.def;

import me.wener.bbvm.utils.val.IsInteger;

public enum FontType implements IsInteger
{
    FONT_12SONG(0),
    FONT_12KAI(1),
    FONT_12HEI(2),
    FONT_16SONG(3),
    FONT_16KAI(4),
    FONT_16HEI(5),
    FONT_24SONG(6),
    FONT_24KAI(7),
    FONT_24HEI(8);
    private final int value;

    FontType(int value)
    {
        this.value = value;
    }

    public Integer get()
    {
        return value;
    }
}
