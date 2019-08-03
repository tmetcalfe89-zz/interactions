package us.timinc.interactions.recipe;

import com.google.gson.annotations.SerializedName;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import us.timinc.interactions.util.RandUtil;

public class InteractRecipe {
	@SerializedName("targetBlock")
	public String targetBlockId = "minecraft:dirt:0";
	@SerializedName("handItem")
	public String handItemId = "minecraft:air:0";
	@SerializedName("changeBlock")
	public String changeBlockId = "";
	public String changeChance = "";
	@SerializedName("dropItem")
	public String dropItemId = "";
	public String dropChance = "";
	public String damage = "";
	public String damageChance = "";

	public boolean changesBlock() {
		return !changeBlockId.isEmpty();
	}

	public IBlockState getIntoBlock() {
		String[] splitIntoBlockId = changeBlockId.split(":");
		return Block.getBlockFromName(splitIntoBlockId[0] + ":" + splitIntoBlockId[1])
				.getStateFromMeta(Integer.parseInt(splitIntoBlockId[2]));
	}

	public boolean matches(String targetBlock, String handItem) {
		return this.targetBlockId.equals(targetBlock) && itemsMatch(handItemId, handItem);
	}

	public boolean rollFor(String chance) {
		String[] splitChance = chance.split(":");
		return RandUtil.rollSuccess(Integer.parseInt(splitChance[1]), Integer.parseInt(splitChance[0]) + 1);
	}

	public boolean rollForChangeBlock() {
		return changeChance.isEmpty() || rollFor(changeChance);
	}

	public boolean dropsItem() {
		return !dropItemId.isEmpty();
	}

	public boolean rollForDropItem() {
		return dropChance.isEmpty() || rollFor(dropChance);
	}

	public boolean itemsMatch(String itemId1, String itemId2) {
		if (createItemStackFrom(itemId1).isItemStackDamageable()) {
			String[] split1 = itemId1.split(":");
			String[] split2 = itemId2.split(":");
			return split1[0].equals(split2[0]) && split1[1].equals(split2[1]);
		} else {
			return itemId1.equals(itemId2);
		}
	}

	public ItemStack createItemStackFrom(String itemId) {
		String[] splitDropItemId = itemId.split(":");
		ItemStack newItemStack = new ItemStack(Item.getByNameOrId(splitDropItemId[0] + ":" + splitDropItemId[1]));
		newItemStack.setItemDamage(Integer.parseInt(splitDropItemId[2]));
		return newItemStack;
	}

	public ItemStack createDrop() {
		ItemStack dropped = createItemStackFrom(dropItemId);
		return dropped;
	}

	public boolean damagesItem() {
		return !damage.isEmpty();
	}

	public boolean rollForDamageItem() {
		return damageChance.isEmpty() || rollFor(damageChance);
	}

	public int getDamage() {
		return Integer.parseInt(damage);
	}
}
