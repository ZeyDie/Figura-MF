package com.zeydie.figura.configuration;

import lombok.Getter;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

@Getter
public final class ConfigCommandSection {
    private final @NotNull Component dwarf = Component.literal("/figuragnome");
    private final @NotNull Component elf = Component.literal("/figuraelf");
    private final @NotNull Component ork = Component.literal("/figuraork");
    private final @NotNull Component human = Component.literal("/figurachelovek");
    private final @NotNull Component child = Component.literal("/figurachild");
    private final @NotNull Component goblin = Component.literal("/figuragoblin");
}