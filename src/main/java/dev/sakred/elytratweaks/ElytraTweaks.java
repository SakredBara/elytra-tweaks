package dev.sakred.elytratweaks;

import dev.sakred.elytratweaks.config.ElytraTweaksConfigManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.UUID;

public class ElytraTweaks implements ModInitializer {
	public static final String MOD_ID = "elytra-tweaks";

	private final HashMap<UUID, Integer> lastReportedDurability = new HashMap<>();

	@Override
	public void onInitialize() {
		ElytraTweaksConfigManager.loadConfig();

		ServerTickEvents.END_WORLD_TICK.register(world -> {
			for (PlayerEntity player : world.getPlayers()) {
				if (player.isSpectator() || player.isCreative()) continue;

				ItemStack chestStack = player.getEquippedStack(EquipmentSlot.CHEST);

				if (!ElytraTweaksConfigManager.config.modEnabled) {
					return;
				}

				if (ElytraTweaksConfigManager.config.enableElytraSwap) {
					if (!player.isOnGround() && !player.isTouchingWater()) {
						if (!chestStack.isOf(Items.ELYTRA)) {
							equipElytra(player);
						}
					} else {
						equipFirstChestplate(player);
					}
				}

				if (ElytraTweaksConfigManager.config.enableLowDurabilityWarning && chestStack.isOf(Items.ELYTRA)) {
					int damage = chestStack.getDamage();
					int maxDurability = chestStack.getMaxDamage();
					int remainingDurability = maxDurability - damage;

					int lastDurability = lastReportedDurability.getOrDefault(player.getUuid(), Integer.MAX_VALUE);

					if ((remainingDurability <= 5 || remainingDurability == 10 || remainingDurability == 20)
							&& remainingDurability != lastDurability) {
						displayLowDurabilityWarning(player, remainingDurability, getDurabilityColor(remainingDurability));
						lastReportedDurability.put(player.getUuid(), remainingDurability);
					}
				}
			}
		});
	}

	private void equipElytra(PlayerEntity player) {
		for (int i = 0; i < player.getInventory().size(); i++) {
			ItemStack stack = player.getInventory().getStack(i);

			if (stack.isOf(Items.ELYTRA) && stack.getDamage() < stack.getMaxDamage() - 1) {
				ItemStack currentChestplate = player.getEquippedStack(EquipmentSlot.CHEST);

				if (!currentChestplate.isEmpty()) {
					for (int j = 0; j < player.getInventory().size(); j++) {
						if (player.getInventory().getStack(j).isEmpty()) {
							player.getInventory().setStack(j, currentChestplate.copy());
							break;
						}
					}
				}

				player.getInventory().removeStack(i);
				player.equipStack(EquipmentSlot.CHEST, stack);
				break;
			}
		}
	}

	private void equipFirstChestplate(PlayerEntity player) {
		ItemStack chestStack = player.getEquippedStack(EquipmentSlot.CHEST);
		if (!chestStack.isOf(Items.ELYTRA)) {
			return;
		}

		for (int i = 0; i < player.getInventory().size(); i++) {
			ItemStack stack = player.getInventory().getStack(i);
			if (stack.getItem() instanceof ArmorItem armorItem && armorItem.getSlotType() == EquipmentSlot.CHEST) {
				player.getInventory().removeStack(i);
				player.equipStack(EquipmentSlot.CHEST, stack);

				for (int j = 0; j < player.getInventory().size(); j++) {
					if (player.getInventory().getStack(j).isEmpty()) {
						player.getInventory().setStack(j, chestStack);
						break;
					}
				}
				break;
			}
		}
	}

	private void displayLowDurabilityWarning(PlayerEntity player, int remainingDurability, Formatting color) {
		player.sendMessage(Text.literal("Remaining Elytra Durability: " + remainingDurability).formatted(color), true);
	}

	private Formatting getDurabilityColor(int remainingDurability) {
		if (remainingDurability <= 5) return Formatting.RED;
		if (remainingDurability <= 10) return Formatting.GOLD;
		if (remainingDurability <= 20) return Formatting.YELLOW;
		return Formatting.WHITE;
	}
}