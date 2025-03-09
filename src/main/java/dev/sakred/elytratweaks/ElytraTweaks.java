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
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.UUID;

public class ElytraTweaks implements ModInitializer {
	public static final String MOD_ID = "elytra-tweaks";

	private final HashMap<UUID, Integer> lastReportedDurability = new HashMap<>();
	private final HashMap<UUID, Vec3d> lastPositions = new HashMap<>();

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

				if (ElytraTweaksConfigManager.config.enableAutoElytraReplace && chestStack.isOf(Items.ELYTRA)) {
					int damage = chestStack.getDamage();
					int maxDurability = chestStack.getMaxDamage();
					int remainingDurability = maxDurability - damage;

					if (remainingDurability == 1 && hasAnotherElytra(player)) {
						replaceElytra(player);
					}
				}

				if (ElytraTweaksConfigManager.config.enableLowDurabilityWarning && chestStack.isOf(Items.ELYTRA)) {
					int damage = chestStack.getDamage();
					int maxDurability = chestStack.getMaxDamage();
					int remainingDurability = maxDurability - damage;

					int lastDurability = lastReportedDurability.getOrDefault(player.getUuid(), Integer.MAX_VALUE);

					int warn1 = ElytraTweaksConfigManager.config.enableWarningCustomization
							? ElytraTweaksConfigManager.config.warnDurability1
							: 20;
					int warn2 = ElytraTweaksConfigManager.config.enableWarningCustomization
							? ElytraTweaksConfigManager.config.warnDurability2
							: 10;
					int warn3 = ElytraTweaksConfigManager.config.enableWarningCustomization
							? ElytraTweaksConfigManager.config.warnDurability3
							: 5;

					if ((remainingDurability <= warn3 || remainingDurability == warn2 || remainingDurability == warn1)
							&& remainingDurability != lastDurability) {
						Formatting color = getDurabilityColor(remainingDurability);
						displayLowDurabilityWarning(player, remainingDurability, color);
						lastReportedDurability.put(player.getUuid(), remainingDurability);
					}
				}

				Vec3d currentPos = player.getPos();
				lastPositions.put(player.getUuid(), currentPos);
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
<<<<<<< Updated upstream
=======

	private boolean isChestplate(ArmorItem armorItem) {
		return armorItem == Items.NETHERITE_CHESTPLATE ||
				armorItem == Items.DIAMOND_CHESTPLATE ||
				armorItem == Items.IRON_CHESTPLATE ||
				armorItem == Items.GOLDEN_CHESTPLATE ||
				armorItem == Items.LEATHER_CHESTPLATE;
	}
>>>>>>> Stashed changes

	private void displayLowDurabilityWarning(PlayerEntity player, int remainingDurability, Formatting color) {
		int cordX = (int) player.getX();
		int cordY = (int) player.getY();
		int cordZ = (int) player.getZ();

		Vec3d currentPos = player.getPos();
		Vec3d lastPos = lastPositions.getOrDefault(player.getUuid(), currentPos);
		double speed = currentPos.distanceTo(lastPos) * 20;

		String message;
		if (ElytraTweaksConfigManager.config.enableWarningCustomization) {
			message = ElytraTweaksConfigManager.config.customDurabilityWarningMessage
					.replace("$durability", String.valueOf(remainingDurability))
					.replace("$cordX", String.valueOf(cordX))
					.replace("$cordY", String.valueOf(cordY))
					.replace("$cordZ", String.valueOf(cordZ))
					.replace("$speed", String.format("%.2f", speed));
		} else {
			message = String.format("Remaining Elytra Durability: %d", remainingDurability);
		}

		player.sendMessage(Text.literal(message).formatted(color), true);
	}

	private Formatting getDurabilityColor(int remainingDurability) {
		if (ElytraTweaksConfigManager.config.enableWarningCustomization) {
			if (remainingDurability <= ElytraTweaksConfigManager.config.warnDurability3) return Formatting.RED;
			if (remainingDurability <= ElytraTweaksConfigManager.config.warnDurability2 && remainingDurability >= ElytraTweaksConfigManager.config.warnDurability3) return Formatting.GOLD;
			if (remainingDurability <= ElytraTweaksConfigManager.config.warnDurability1 && remainingDurability >= ElytraTweaksConfigManager.config.warnDurability2) return Formatting.YELLOW;
		} else {
			if (remainingDurability <= 5) return Formatting.RED;
			if (remainingDurability <= 10) return Formatting.GOLD;
			if (remainingDurability <= 20) return Formatting.YELLOW;
		}
		return Formatting.WHITE;
	}

	private boolean hasAnotherElytra(PlayerEntity player) {
		ItemStack chestStack = player.getEquippedStack(EquipmentSlot.CHEST);
		for (int i = 0; i < player.getInventory().size(); i++) {
			ItemStack stack = player.getInventory().getStack(i);
			if (stack.isOf(Items.ELYTRA) && !stack.equals(chestStack) && stack.getDamage() < stack.getMaxDamage() - 1) {
				return true;
			}
		}
		return false;
	}

	private void replaceElytra(PlayerEntity player) {
		ItemStack chestStack = player.getEquippedStack(EquipmentSlot.CHEST);
		for (int i = 0; i < player.getInventory().size(); i++) {
			ItemStack stack = player.getInventory().getStack(i);
			if (stack.isOf(Items.ELYTRA) && !stack.equals(chestStack) && stack.getDamage() < stack.getMaxDamage() - 1) {
				player.getInventory().removeStack(EquipmentSlot.CHEST.getEntitySlotId());
				player.equipStack(EquipmentSlot.CHEST, stack.copy());
				player.getInventory().removeStack(i);
				player.getInventory().insertStack(chestStack);
				break;
			}
		}
	}
}