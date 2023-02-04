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
    protected final ItemRenderer itemRenderer;
    protected PlayerEntity player;
    private final Map<Item, Integer> knapsack = new HashMap<>();
    protected int knapsackEmptyStack = 0;

    public KnapsackManager(PlayerEntity player) {
        this.itemRenderer = client.getItemRenderer();
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

    /**
     * 统计潜影盒 Nbt 物品数量
     * @param itemStack 潜影盒
     * @param nbt 潜影盒 nbt
     */
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

    /**
     * 统计背包物品数量
     */
    protected void readPlayerKnapsack(){
        this.knapsack.clear();
        this.knapsackEmptyStack = 0;

        PlayerInventory inventory = player.getInventory();
        for(int slot = 0; slot < inventory.main.size(); slot++){
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

    /**
     * 获取物品总数
     * @param itemStack 物品格
     * @return 物品总数
     */
    public int getKnapsackItemAllCount(ItemStack itemStack){
        Item item = itemStack.getItem();
        if (!this.knapsack.containsKey(item)){
            return 0;
        }

        return this.knapsack.get(item);
    }

    /**
     * 绘制背包空格状态
     * @param matrices matrices
     * @param x 绘制所在 X 轴
     * @param y 绘制所在 Y 轴
     */
    public void drawKnapsackEmptyStackCount(MatrixStack matrices, int x, int y){
        this.itemRenderer.renderInGui(new ItemStack(Items.CHEST), x, y);

        String emptyStackCountString = String.valueOf(this.knapsackEmptyStack);
        client.textRenderer.draw(matrices, emptyStackCountString, x + 20, y + 4,  0xffffff);
    }

    /**
     * 绘制该物品在背包的所有数量
     * @param matrices matrices
     * @param itemStack 物品格
     * @param x 绘制所在 X 轴
     * @param y 绘制所在 Y 轴
     */
    public void drawItemAllCount(ItemStack itemStack, MatrixStack matrices, int x, int y){
        String itemAllCountString = String.valueOf(this.getKnapsackItemAllCount(itemStack));
        client.textRenderer.draw(matrices, itemAllCountString, x, y + 4,  0xffffff);
    }

    /**
     * 绘制该物品在背包的所有数量 (反向绘制)
     * @param matrices matrices
     * @param itemStack 物品格
     * @param x 绘制所在 X 轴
     * @param y 绘制所在 Y 轴
     */
    public void drawBackItemAllCount(ItemStack itemStack, MatrixStack matrices, int x, int y){
        String itemAllCountString = String.valueOf(this.getKnapsackItemAllCount(itemStack));
        client.textRenderer.draw(matrices, itemAllCountString, x - itemAllCountString.length() * 5, y + 4,  0xffffff);
    }

    /**
     * 绘制该物品在背包的耐久状态
     * @param matrices matrices
     * @param itemStack 物品格
     * @param x 绘制所在 X 轴
     * @param y 绘制所在 Y 轴
     */
    public void drawItemDamage(ItemStack itemStack, MatrixStack matrices, int x, int y){
        int maxDamage = itemStack.getMaxDamage();
        client.textRenderer.draw(matrices, maxDamage - itemStack.getDamage() + " / " + maxDamage, x, y + 4,  0xffffff);
    }

    /**
     * 绘制该物品在背包的耐久状态 (反向绘制)
     * @param matrices matrices
     * @param itemStack 物品格
     * @param x 绘制所在 X 轴
     * @param y 绘制所在 Y 轴
     */
    public void drawBackItemDamage(ItemStack itemStack, MatrixStack matrices, int x, int y){
        int maxDamage = itemStack.getMaxDamage();
        String damageString = maxDamage - itemStack.getDamage() + " / " + maxDamage;
        client.textRenderer.draw(matrices, damageString, x - damageString.length() * 5, y + 4,  0xffffff);
    }

    /**
     * 绘制该物品在背包的状态, 物品绘制数量, 工具绘制耐久  (反向绘制)
     * @param matrices matrices
     * @param itemStack 物品格
     * @param x 绘制所在 X 轴
     * @param y 绘制所在 Y 轴
     */
    public void drawBackItemStackStatus(ItemStack itemStack, MatrixStack matrices, int x, int y){
        if (itemStack.isEmpty()){
            return;
        }

        this.itemRenderer.renderInGuiWithOverrides(itemStack,  x, y, 0);
        if (itemStack.isDamageable()){
            this.drawBackItemDamage(itemStack, matrices, x - 8, y);
            return;
        }

        this.drawBackItemAllCount(itemStack, matrices, x - 8, y);
    }

    /**
     * 绘制该物品在背包的状态, 物品绘制数量, 工具绘制耐久
     * @param matrices matrices
     * @param itemStack 物品格
     * @param x 绘制所在 X 轴
     * @param y 绘制所在 Y 轴
     */
    public void drawItemStackStatus(ItemStack itemStack, MatrixStack matrices, int x, int y){
        if (itemStack.isEmpty()){
            return;
        }

        this.itemRenderer.renderGuiItemIcon(itemStack, x, y);
        if (itemStack.isDamageable()){
            this.drawItemDamage(itemStack, matrices, x + 20, y);
            return;
        }

        this.drawItemAllCount(itemStack, matrices, x + 20, y);
    }

    /**
     * 绘制盔甲状态
     * @param matrices matrices
     * @param x 绘制所在 X 轴
     * @param y 绘制所在 Y 轴
     */
    public void drawArmorStackStatus(MatrixStack matrices, int x, int y){
        PlayerInventory inventory = this.player.getInventory();

        for (ItemStack armorItem: inventory.armor) {
            this.drawBackItemStackStatus(armorItem, matrices, x, y);
            y -= 18;
        }
    }

    /**
     * 绘制主手物品状态, 副手物品状态, 背包空格状态
     * @param matrices matrices
     * @param x 绘制所在 X 轴
     * @param y 绘制所在 Y 轴
     */
    public void drawItemsStatus(MatrixStack matrices, int x, int y){
        this.drawItemStackStatus(this.player.getMainHandStack(), matrices, x, y);

        ItemStack offHandStack = this.player.getOffHandStack();
        if (offHandStack.isEmpty()){
            this.drawKnapsackEmptyStackCount(matrices, x, y - 18);
            return;
        }

        this.drawKnapsackEmptyStackCount(matrices, x, y - 36);
        this.drawItemStackStatus(offHandStack, matrices, x, y - 18);
    }

}