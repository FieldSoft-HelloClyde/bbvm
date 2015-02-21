package me.wener.bbvm.system;

import com.google.common.collect.Lists;
import java.io.Serializable;
import java.util.EventListener;
import java.util.List;
import lombok.Getter;
import lombok.experimental.Accessors;
import me.wener.bbvm.system.api.Register;
import me.wener.bbvm.system.api.RegisterType;

@Accessors(chain = true, fluent = true)
public class RegisterImpl implements Register, Serializable
{
    @Getter
    private String name;
    @Getter
    private RegisterType type;
    private Integer value;

    public RegisterImpl()
    {
    }

    public RegisterImpl(String name)
    {
        this.name = name;
        type = RegisterType.valueOf(name);
    }

    public RegisterImpl(RegisterType type)
    {
        this.type = type;
        name = type.toString();
    }

    public static MonitoredRegister monitor(Register register)
    {
        MonitoredRegister monitored;
        if (register instanceof MonitoredRegister)
        {
            monitored = (MonitoredRegister) register;
        } else
        {
            monitored = new MonitoredRegister(register);
        }
        return monitored;
    }

    @Override
    public Integer get()
    {
        return value;
    }

    @Override
    public void set(Integer v)
    {
        value = v;
    }

    public interface RegisterChangeListener extends EventListener
    {
        void onChange(Register register, Integer val);
    }

    public static class MonitoredRegister extends RegisterImpl
    {
        @Getter
        @Accessors(fluent = true)
        private final List<RegisterChangeListener> listeners = Lists.newArrayList();
        @Getter
        private final Register internal;

        public MonitoredRegister(Register register)
        {
            internal = register;
        }

        @Override
        public void set(Integer v)
        {
            for (RegisterChangeListener listener : listeners)
            {
                listener.onChange(this, v);
            }
            internal.set(v);
        }

        @Override
        public Integer get()
        {
            return internal.get();
        }

        @Override
        public String name()
        {
            return internal.name();
        }

        @Override
        public RegisterType type()
        {
            return internal.type();
        }
    }
}
