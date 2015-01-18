package me.wener.bbvm.neo;

import static java.nio.charset.StandardCharsets.UTF_8;

import io.netty.buffer.ByteBuf;
import me.wener.bbvm.neo.inst.CAL;
import me.wener.bbvm.neo.inst.Inst;
import me.wener.bbvm.neo.inst.JPC;
import me.wener.bbvm.neo.inst.OneOperandInst;
import me.wener.bbvm.neo.inst.TowOperandInst;
import me.wener.bbvm.neo.inst.def.Flags;
import me.wener.bbvm.neo.inst.def.InstructionType;

/**
 * 将相应类型转换为字符串
 */
public class Stringer
{
    public static String string(byte[] bytes)
    {
        return new String(bytes, UTF_8);
    }

    public static String string(ByteBuf buf)
    {
        return buf == null ? null : buf.toString(UTF_8);
    }

    public static String string(Inst inst)
    {
        InstructionType type = Types.instructionType(inst);
        OneOperandInst one = null;
        TowOperandInst tow = null;
        if (inst instanceof OneOperandInst)
        {
            one = (OneOperandInst) inst;
        } else if (inst instanceof TowOperandInst)
        {
            tow = (TowOperandInst) inst;
        }

        switch (type.get())
        {
            case Flags.CAL:
            {
                CAL cal = (CAL) inst;
                return String .format("%s %s %s, %s", type, Types.calculateType(cal.operator), cal.a,cal.b);
            }
            case Flags.JPC:
                return String.format("%s %s %s", type, Types.compareType(((JPC)one).compare), one.a);
            case Flags.POP:
            case Flags.PUSH:
            case Flags.CALL:
            case Flags.JMP:
                return String.format("%s %s", type, one.a);
            case Flags.IN:
            case Flags.OUT:
            {
                String format = "%s %s, %s";
                return String.format(format, type, tow.a, tow.b);
            }

            case Flags.CMP:


            case Flags.RET:
            case Flags.NOP:
            case Flags.EXIT:
                return String.valueOf(type);
            case Flags.LD:
            {
                String format = "%s %s %s, %s";
                return String.format(format, type, Types.dataType(tow.dataType), tow.a, tow.b);
            }
            default:
                throw new AssertionError();
        }
    }
}
