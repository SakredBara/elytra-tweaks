package dev.sakred.elytratweaks.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ElytraTweaksConfigScreen {
    public static Screen create(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.of("Elytra Tweaks v1.1.0+1.21"))
                .category(ConfigCategory.createBuilder()
                        .name(Text.of("General"))
                        .option(Option.<Boolean>createBuilder()
                                .name(Text.of("Should Elytra Tweaks be enabled?"))
                                .description(OptionDescription.of(Text.of("Fully enables or disables the mod")))
                                .binding(true, () -> ElytraTweaksConfigManager.config.modEnabled, newValue -> ElytraTweaksConfigManager.config.modEnabled = newValue)
                                .controller(TickBoxControllerBuilder::create)
                                .build())
                        .group(OptionGroup.createBuilder()
                                .name(Text.of("Functionality settings"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Elytra Swap"))
                                        .description(OptionDescription.of(Text.of("Automatically replaces chestplate with elytra when you fly and elytra with chestplate when you land.")))
                                        .binding(true, () -> ElytraTweaksConfigManager.config.enableElytraSwap, newValue -> ElytraTweaksConfigManager.config.enableElytraSwap = newValue)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Low durability warnings"))
                                        .description(OptionDescription.of(Text.of("Shows warnings when elytra durability becomes low.")))
                                        .binding(true, () -> ElytraTweaksConfigManager.config.enableLowDurabilityWarning, newValue -> ElytraTweaksConfigManager.config.enableLowDurabilityWarning = newValue)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Elytra Swap on Mace"))
                                        .description(OptionDescription.of(Text.of("Automatically replaces elytra with chestplate when holding a mace and disables swap.")))
                                        .binding(true, () -> ElytraTweaksConfigManager.config.enableElytraSwapOnMace, newValue -> ElytraTweaksConfigManager.config.enableElytraSwapOnMace = newValue)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .build())
                .save(ElytraTweaksConfigScreen::save)
                .build()
                .generateScreen(parent);
    }

    private static void save() {
        ElytraTweaksConfigManager.saveConfig();
    }
}