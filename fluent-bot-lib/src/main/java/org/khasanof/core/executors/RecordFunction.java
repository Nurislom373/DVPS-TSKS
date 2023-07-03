package org.khasanof.core.executors;

import org.khasanof.core.enums.HandleType;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author Nurislom
 * @see org.khasanof.core.executors
 * @since 27.06.2023 22:50
 */
public record RecordFunction(Map.Entry<Boolean, Supplier<Map.Entry<HandleType, Object>>> supplierEntry) {}
