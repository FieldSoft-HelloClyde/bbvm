package me.wener.bbvm.core;

import me.wener.bbvm.utils.Bins;

public abstract class BBVm
{
    public void load(byte[] bytes)
    {
        memory = bytes;
    }

    private final Reg rp = new Reg();
    private final Reg rf = new Reg();
    private final Reg rs = new Reg();
    private final Reg rb = new Reg();
    private final Reg r0 = new Reg();
    private final Reg r1 = new Reg();
    private final Reg r2 = new Reg();
    private final Reg r3 = new Reg();

    private byte[] memory;
    private int pc = 0;

    private void loop()
    {
        /*
           指令码 + 数据类型 + 特殊用途字节 + 寻址方式 + 第一个操作数 + 第二个操作数
        0x 0        0          0             0          0000           0000
        */
        int code = Bins.uint16(memory, pc);
        int op = code >> 12;// 指令码
        int dataType = (code & 0x0F00) >> 8;// 数据类型
        int special = (code & 0x00F0) >> 4;// 特殊用途字节
        int addressType = code & 0x000F;// 寻址方式

        Instruction instruction = Values.fromValue(Instruction.class, op);
        switch (instruction)
        {
            case NOP:
                break;
            case LD:
                break;
            case PUSH:
                break;
            case POP:
                break;
            case IN:
                break;
            case OUT:
                break;
            case JMP:
                break;
            case JPC:
                break;
            case CALL:
                break;
            case RET:
                break;
            case CMP:
                break;
            case CAL:
                break;
            case EXIT:
                exit();
                break;
            default:
                System.out.println("未预料的结果");
                break;
        }
    }

    private void exit()
    {
        System.exit(0);
    }
}
