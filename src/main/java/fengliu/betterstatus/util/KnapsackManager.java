package fengliu.betterstatus.util;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class KnapsackManager {
    protected static final MinecraftClient client = MinecraftClient.getInstance();
    protected PlayerEntity player;
    private final Map<Item, Integer> knapsack = new HashMap<>();
    protected int knapsackEmptyStack = 0;

    public KnapsackManager(PlayerEntity player) {
        this.player = player;

        this.readPlayerKnapsack();
    }

    protected void addItemCount(ItemStack itemStack){
        Item item = itemStack.getItem();
        if (this.knapsack.containsKey(item)){
            this.knapsack.put(item, this.knapsack.get(item) + itemStack.getCount());
            return;
        }

        this.knapsack.put(item, itemStack.getCount());
    }

    protected void readBoxItemsCount(ItemStack itemStack, NbtCompound nbt){
        if (!(itemStack.getItem() instanceof BlockItem) || !nbt.contains("BlockEntityTag", NbtCompound.COMPOUND_TYPE)){
            return;
        }

        nbt = nbt.getCompound("BlockEntityTag");
        if (!nbt.contains("Items", NbtCompound.LIST_TYPE)){
            return;
        }

        NbtList boxItems = nbt.getList("Items", NbtCompound.COMPOUND_TYPE);
        boxItems.forEach(itemNbt -> {
            Item item = Registries.ITEM.get(new Identifier(((NbtCompound) itemNbt).getString("id")));
            int count = ((NbtCompound) itemNbt).getInt("Count");

            if (this.knapsack.containsKey(item)){
                this.knapsack.put(item, this.knapsack.get(item) + count);
                return;
            }

            this.knapsack.put(item, count);
        });
    }

    protected void readPlayerKnapsack(){
        this.knapsack.clear();
        this.knapsackEmptyStack = 0;

        PlayerInventory inventory = player.getInventory();
        for(int slot = 0; slot < inventory.size(); slot++){
            ItemStack itemStack = inventory.getStack(slot);
            if (itemStack.isEmpty() || itemStack.isOf(Items.AIR)){
                this.knapsackEmptyStack += 1;
                continue;
            }

            this.addItemCount(itemStack);
            NbtCompound nbt = itemStack.getNbt();
            if (!itemStack.hasNbt() || nbt == null){
                continue;
            }

            this.readBoxItemsCount(itemStack, nbt);
        }
    }

    public int getKnapsackItemAllCount(ItemStack itemStack){
        Item item = itemStack.getItem();
        if (!this.knapsack.containsKey(item)){
            return 0;
        }

        return this.knapsack.get(item);
    }

    public void drawKnapsackEmptyStackCount(ItemRenderer itemRenderer, MatrixStack matrices, int x, int y){
        itemRenderer.renderInGui(new ItemStack(Items.CHEST), x, y);

        String emptyStackCountString = String.valueOf(this.knapsackEmptyStack);
        client.textRenderer.draw(matrices, emptyStackCountString, x + 20, y + 4,  0xffffff);
    }

    public void drawMainHandStackStatus(ItemRenderer itemRenderer, MatrixStack matrices, int x, int y){
        ItemStack mainHandStack = this.player.getMainHandStack();

        int itemAllCount = this.getKnapsackItemAllCount(mainHandStack);
        if (itemAllCount == 0){
            return;
        }

        itemRenderer.renderInGui(mainHandStack, x, y);
        if (mainHandStack.isDamageable()){
            int maxDamage = mainHandStack.getMaxDamage();
            client.textRenderer.draw(matrices, maxDamage - mainHandStack.getDamage() + " / " + maxDamage, x + 20, y + 4,  0xffffff);
            return;
        }

        String itemAllCountString = String.valueOf(itemAllCount);
        client.textRenderer.draw(matrices, itemAllCountString, x + 20, y + 4,  0xffffff);
    }

    public void drawKnapsackStatus(MatrixStack matrices, int x, int y){
        ItemRenderer itemRenderer = client.getItemRenderer();
        this.drawKnapsackEmptyStackCount(itemRenderer, matrices, x, y - 18);
        this.drawMainHandStackStatus(itemRenderer, matrices, x, y);
    }

}
