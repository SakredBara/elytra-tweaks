package dev.sakred.elytratweaks.config;

import dev.isxander.yacl3.api.*;
import dev.isxander.yacl3.api.controller.IntegerSliderControllerBuilder;
import dev.isxander.yacl3.api.controller.StringControllerBuilder;
import dev.isxander.yacl3.api.controller.TickBoxControllerBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ElytraTweaksConfigScreen {
    public static Screen create(Screen parent) {
        return YetAnotherConfigLib.createBuilder()
                .title(Text.of("Elytra Tweaks v1.1.2"))
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
<<<<<<< Updated upstream
=======
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Elytra Swap on Mace (1.21+ only)"))
                                        .description(OptionDescription.of(Text.of("Automatically replaces elytra with chestplate when holding a mace and disables swap.")))
                                        .binding(true, () -> ElytraTweaksConfigManager.config.enableElytraSwapOnMace, newValue -> ElytraTweaksConfigManager.config.enableElytraSwapOnMace = newValue)
                                        .controller(TickBoxControllerBuilder::create)
                                        .available(false)
                                        .build())
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Auto Elytra Replaces"))
                                        .description(OptionDescription.of(Text.of("Automatically replaces elytra with other elytra when they are broken")))
                                        .binding(true, () -> ElytraTweaksConfigManager.config.enableAutoElytraReplace, newValue -> ElytraTweaksConfigManager.config.enableAutoElytraReplace = newValue)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .build())
                        .build())
                .category(ConfigCategory.createBuilder()
                        .name(Text.of("Customization"))
                        .group(OptionGroup.createBuilder()
                                .name(Text.of("Warning Customization"))
                                .option(Option.<Boolean>createBuilder()
                                        .name(Text.of("Enable Warning Customization"))
                                        .description(OptionDescription.of(Text.of("Allows customization of low durability warnings.")))
                                        .binding(false, () -> ElytraTweaksConfigManager.config.enableWarningCustomization, newValue -> ElytraTweaksConfigManager.config.enableWarningCustomization = newValue)
                                        .controller(TickBoxControllerBuilder::create)
                                        .build())
                                .option(Option.<String>createBuilder()
                                        .name(Text.of("Custom Warning Message"))
                                        .description(OptionDescription.of(Text.of("Custom message to display when elytra durability is low.")))
                                        .binding("", () -> ElytraTweaksConfigManager.config.customDurabilityWarningMessage, newValue -> ElytraTweaksConfigManager.config.customDurabilityWarningMessage = newValue)
                                        .controller(StringControllerBuilder::create)
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.of("First Warning Durability"))
                                        .description(OptionDescription.of(Text.of("Setting the value when the first warning is displayed")))
                                        .binding(20, () -> ElytraTweaksConfigManager.config.warnDurability1, newValue -> ElytraTweaksConfigManager.config.warnDurability1 = newValue)
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(1, 100) // Устанавливаем диапазон от 1 до 100
                                                .step(1)) // Шаг изменения значения
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.of("Second Warning Durability"))
                                        .description(OptionDescription.of(Text.of("Setting the value when the second warning is displayed")))
                                        .binding(10, () -> ElytraTweaksConfigManager.config.warnDurability2, newValue -> ElytraTweaksConfigManager.config.warnDurability2 = newValue)
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(1, 100) // Устанавливаем диапазон от 1 до 100
                                                .step(1)) // Шаг изменения значения
                                        .build())
                                .option(Option.<Integer>createBuilder()
                                        .name(Text.of("Third Warning Durability"))
                                        .description(OptionDescription.of(Text.of("Setting the value when the third warning is displayed")))
                                        .binding(5, () -> ElytraTweaksConfigManager.config.warnDurability3, newValue -> ElytraTweaksConfigManager.config.warnDurability3 = newValue)
                                        .controller(opt -> IntegerSliderControllerBuilder.create(opt)
                                                .range(1, 100) // Устанавливаем диапазон от 1 до 100
                                                .step(1)) // Шаг изменения значения
                                        .build())
>>>>>>> Stashed changes
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