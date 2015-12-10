package me.wener.bbvm.vm;

import com.google.common.base.MoreObjects;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * @author wener
 * @since 15/12/10
 */
public class Memory {
    public static final int DEFAULT_MEMORY = 1024 * 1024 * 4;
    ByteBuf mem;
    Register rs;
    Register rb;
    private int memorySize;
    private int stackSize;
    private VM vm;

    public Memory() {
        this(DEFAULT_MEMORY, 1024);
    }

    public Memory(int memorySize, int stackSize) {
        mem = Unpooled.buffer(memorySize, memorySize);
        this.stackSize = stackSize;
        this.memorySize = memorySize;
    }

    public void reset() {
        mem.clear();
        byte[] bytes = mem.array();
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = 0;
        }
        rb.setValue(memorySize);
        rs.setValue(memorySize - stackSize);
    }

    public VM getVm() {
        return vm;
    }

    public Memory setVm(VM vm) {
        this.vm = vm;
        this.rs = vm.rs;
        this.rb = vm.rb;
        return this;
    }

    public int pop() {
        rs.add(4);
        return mem.getInt(rs.intValue());
    }

    public void push(int v) {
        // TODO
//        if (rb.intValue() - rs.intValue() < 4) {
//            throw new RuntimeException("Stack overflow");
//        }
        mem.setInt(rs.intValue(), v);
        rs.subtract(4);
    }

    public ByteBuf getMemory() {
        return mem;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("mem", mem)
                .add("rs", rs)
                .add("rb", rb)
                .add("memorySize", memorySize)
                .add("stackSize", stackSize)
                .toString();
    }

    public int read(int addr) {
        return mem.getInt(addr);
    }

    public Memory write(int addr, int v) {
        mem.setInt(addr, v);
        return this;
    }
}
