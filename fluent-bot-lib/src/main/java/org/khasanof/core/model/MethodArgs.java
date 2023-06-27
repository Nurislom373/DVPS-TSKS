package org.khasanof.core.model;

import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;

/**
 * @author Nurislom
 * @see org.khasanof.core.model
 * @since 25.06.2023 23:19
 */
public record MethodArgs(Update update, AbsSender sender) {}
