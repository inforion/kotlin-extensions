package ru.inforion.lab403.common.spark;

import java.util.HashMap;

public class ModulePorts extends HashMap<String, ModulePorts.APort> {
    class APort {
        public String name;
        public Long size;

        APort(String name, Long size) {
            this.name = name;
            this.size = size;
        }

        @Override
        public String toString() {
            return "APort{" +
                    "name='" + name + '\'' +
                    ", size=" + size +
                    '}';
        }
    }

    JavaModule module;

    ModulePorts(JavaModule module) {
        this.module = module;
    }
}
