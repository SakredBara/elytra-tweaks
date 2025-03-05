package dev.sakred.elytratweaks;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
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
		ServerTickEvents.END_WORLD_TICK.register(world -> {
			for (PlayerEntity player : world.getPlayers()) {
				if (player.isSpectator() || player.isCreative()) continue;

				ItemStack chestStack = player.getEquippedStack(EquipmentSlot.CHEST);
				if (!player.isOnGround() && !player.isTouchingWater()) {
					if (!chestStack.isOf(Items.ELYTRA)) {
						equipElytra(player);
					}
				}

				if (chestStack.isOf(Items.ELYTRA)) {
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

				if (player.isOnGround()) {
					equipFirstChestplate(player);
				}
			}
		});
	}

	private void equipElytra(PlayerEntity player) {
		for (int i = 0; i < player.getInventory().size(); i++) {
			ItemStack stack = player.getInventory().getStack(i);
			if (stack.isOf(Items.ELYTRA)) {
				ItemStack currentChestplate = player.getEquippedStack(EquipmentSlot.CHEST);

				if (!currentChestplate.isEmpty()) {
					boolean placed = false;
					for (int j = 0; j < player.getInventory().size(); j++) {
						ItemStack invStack = player.getInventory().getStack(j);
						if (invStack.isEmpty()) {
							player.getInventory().setStack(j, currentChestplate.copy());
							placed = true;
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
		if (chestStack.isOf(Items.ELYTRA)) {
			return;
		}

		for (int i = 0; i < player.getInventory().size(); i++) {
			ItemStack stack = player.getInventory().getStack(i);
			if (stack.isOf(Items.IRON_CHESTPLATE) || stack.isOf(Items.DIAMOND_CHESTPLATE) || stack.isOf(Items.GOLDEN_CHESTPLATE)) {
				ItemStack currentChestplate = player.getEquippedStack(EquipmentSlot.CHEST);
				player.getInventory().removeStack(i);
				player.equipStack(EquipmentSlot.CHEST, stack);

				for (int j = 0; j < player.getInventory().size(); j++) {
					ItemStack invStack = player.getInventory().getStack(j);
					if (invStack.isEmpty()) {
						player.getInventory().setStack(j, currentChestplate);
						break;
					}
				}
				break;
			}
		}
	}

	private void displayLowDurabilityWarning(PlayerEntity player, int remainingDurability, Formatting color) {
		String message = "Remaining Elytra Durability: " + remainingDurability;
		player.sendMessage(Text.literal(message).formatted(color), true);
	}

	private Formatting getDurabilityColor(int remainingDurability) {
		if (remainingDurability <= 5) {
			return Formatting.RED;
		} else if (remainingDurability <= 10) {
			return Formatting.GOLD;
		} else if (remainingDurability <= 20) {
			return Formatting.YELLOW;
		}
		return Formatting.WHITE;
	}
}
