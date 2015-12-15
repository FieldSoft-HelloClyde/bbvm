package me.wener.bbvm.vm;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.multibindings.OptionalBinder;
import me.wener.bbvm.BeBasicVirtualMachine;
import me.wener.bbvm.util.MoreModules;
import me.wener.bbvm.vm.res.StringManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * @author wener
 * @since 15/12/13
 */
public class VirtualMachineModule extends AbstractModule {
    private final static Logger log = LoggerFactory.getLogger(VirtualMachineModule.class);
    private final VMConfig config;

    public VirtualMachineModule(VMConfig config) {
        this.config = config;
    }

    @Override
    protected void configure() {
        OptionalBinder.newOptionalBinder(binder(), SystemInvokeManager.class)
                .setDefault().to(SystemInvokeManagerImpl.class).in(Singleton.class);
//        OptionalBinder.newOptionalBinder(binder(), StringManager.class)
//                .setDefault().to(StringManager.class).in(Singleton.class);
        bind(StringManager.class).in(Singleton.class);
        bind(VM.class).in(Singleton.class);
        install(MoreModules.pluggingModule(BeBasicVirtualMachine.class.getPackage().getName(), getClass().getClassLoader(), input -> {
            log.info("Module {} {}", input.getKey(), config.isModuleEnabled(input.getKey()) ? "enabled" : "disabled");
            return config.isModuleEnabled(input.getKey());
        }));

    }

    @Provides
    @Singleton
    public VMConfig config() {
        return config;
    }

    // region Generated Register
    @Named("RP")
    @Provides
    @Singleton
    public Register RP(VM vm) {
        return vm.getRegister(RegisterType.RP);
    }

    @Named("RF")
    @Provides
    @Singleton
    public Register RF(VM vm) {
        return vm.getRegister(RegisterType.RF);
    }

    @Named("RS")
    @Provides
    @Singleton
    public Register RS(VM vm) {
        return vm.getRegister(RegisterType.RS);
    }

    @Named("RB")
    @Provides
    @Singleton
    public Register RB(VM vm) {
        return vm.getRegister(RegisterType.RB);
    }

    @Named("R0")
    @Provides
    @Singleton
    public Register R0(VM vm) {
        return vm.getRegister(RegisterType.R0);
    }

    @Named("R1")
    @Provides
    @Singleton
    public Register R1(VM vm) {
        return vm.getRegister(RegisterType.R1);
    }

    @Named("R2")
    @Provides
    @Singleton
    public Register R2(VM vm) {
        return vm.getRegister(RegisterType.R2);
    }

    @Named("R3")
    @Provides
    @Singleton
    public Register R3(VM vm) {
        return vm.getRegister(RegisterType.R3);
    }
    // endregion
}