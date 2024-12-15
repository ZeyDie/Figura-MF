package com.zeydie.figura.configuration;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

@Getter
public final class ConfigButtonSection {
    @Getter
    private final @NotNull String shopLink = "https://mirtan.minefite.net";
    private final @NotNull List<String> hiddenEntry = new ArrayList<>();
}