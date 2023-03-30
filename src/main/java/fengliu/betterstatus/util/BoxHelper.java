package fengliu.betterstatus.util;

import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class BoxHelper {
    protected final ItemStack boxStack;
    protected boolean allItemSame;
    protected Map<Item, Integer> items = new HashMap<>();

    public static boolean isBox(ItemStack stack){
        NbtCompound nbt = stack.getNbt();
        if (!stack.hasNbt() || nbt == null){
            return false;
        }

        if (!(stack.getItem() instanceof BlockItem)|| !nbt.contains("BlockEntityTag", NbtCompound.COMPOUND_TYPE)){
            return false;
        }

        return nbt.getCompound("BlockEntityTag").contains("Items", NbtCompound.LIST_TYPE);
    }

    public BoxHelper(ItemStack stack){
        this.boxStack = stack;
        if (!isBox(stack)){
            this.allItemSame = false;
            return;
        }

        this.readBoxItemsCount(stack);
    }

    /**
     * 统计潜影盒 Nbt 物品数量
     * @param stack 潜影盒
     */
    protected void readBoxItemsCount(ItemStack stack){
        NbtCompound nbt = stack.getNbt();

        assert nbt != null;
        NbtList boxItems = nbt.getCompound("BlockEntityTag").getList("Items", NbtCompound.COMPOUND_TYPE);

        NbtCompound oldItem = boxItems.getCompound(0);
        this.allItemSame = true;
        for (NbtElement itemNbt: boxItems){
            if (!((NbtCompound) itemNbt).getString("id").equals(oldItem.getString("id"))){
                oldItem = (NbtCompound) itemNbt;
                this.allItemSame = false;
            }

            Item item = Registries.ITEM.get(new Identifier(((NbtCompound) itemNbt).getString("id")));
            int count = ((NbtCompound) itemNbt).getInt("Count");

            if (this.items.containsKey(item)){
                this.items.put(item, this.items.get(item) + count);
                continue;
            }

            this.items.put(item, count);
        }
    }

    public void statistics(Map<Item, Integer> knapsack){
        if (!isBox(this.boxStack)){
            return;
        }

        for (Entry<Item, Integer> count: this.items.entrySet()){
            Item item = count.getKey();
            if (knapsack.containsKey(item)){
                knapsack.put(item, knapsack.get(item) + count.getValue());
                continue;
            }

            knapsack.put(item, count.getValue());
        }
    }

    public boolean isAllItemSame() {
        return allItemSame;
    }

    public int getItemCount(Item item){
        return this.items.get(item);
    }

    public Item getSameItem(){
        if (!this.allItemSame){
            return Items.AIR;
        }

        Object[] array = this.items.keySet().toArray();
        if (array.length == 0){
            return Items.AIR;
        }

        return (Item) array[0];
    }
}
