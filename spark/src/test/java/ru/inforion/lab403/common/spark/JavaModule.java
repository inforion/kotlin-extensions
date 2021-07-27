package ru.inforion.lab403.common.spark;

public class JavaModule {
    static class Ports extends ModulePorts {
        APort irq = new APort("irq", 0x10L);

        Ports(JavaModule module) {
           super(module);
        }
    }

    int ord;

    Ports ports = new Ports(this);

    JavaModule(int ord) {
        this.ord = ord;
    }
}
