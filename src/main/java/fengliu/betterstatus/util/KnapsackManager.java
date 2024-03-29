package fengliu.betterstatus.util;

import com.mojang.blaze3d.systems.RenderSystem;
import fengliu.betterstatus.BetterStatusClient;
import fengliu.betterstatus.config.Configs;
import fi.dy.masa.malilib.config.options.ConfigColor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class KnapsackManager {
    protected static final MinecraftClient client = MinecraftClient.getInstance();
    protected static final Identifier BARS_TEXTURE = new Identifier(
        BetterStatusClient.MOD_ID, "textures/gui/bars.png"
    );
    protected static final int TEXTURE_Y = 240;
    protected static final int TEXTURE_X = 81;

    protected final int itemStatusFontColor = Configs.GUI.ITEM_STATUS_COUNT_FONT_COLOR.getIntegerValue();
    protected final PlayerInventory inventory;
    protected final ItemRenderer itemRenderer;
    protected PlayerEntity player;
    private final Map<Item, Integer> knapsack = new HashMap<>();
    protected int knapsackEmptyStack = 0;
    protected boolean haveDangerItem = false;

    public KnapsackManager(PlayerEntity player) {
        this.itemRenderer = client.getItemRenderer();
        this.inventory = player.getInventory();
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

    protected void readItemStack(ItemStack stack){
        if (stack.isEmpty() || stack.isOf(Items.AIR)){
            this.knapsackEmptyStack += 1;
            return;
        }

        this.addItemCount(stack);
        new BoxHelper(stack).statistics(this.knapsack);
    }

    /**
     * 统计背包物品数量
     */
    protected void readPlayerKnapsack(){
        this.knapsack.clear();
        this.knapsackEmptyStack = 0;

        for(int index = 0; index < this.inventory.size(); index++){
            this.readItemStack(this.inventory.getStack(index));
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

    private enum Ponderance {
        SAFE(0.80f, Configs.GUI.ITEM_STATUS_SAFE_FONT_COLOR),
        SLIGHTLY(0.60f, Configs.GUI.ITEM_STATUS_SLIGHTLY_FONT_COLOR),
        ATTENTION(0.40f, Configs.GUI.ITEM_STATUS_ATTENTION_FONT_COLOR),
        WARNING(0.15f, Configs.GUI.ITEM_STATUS_WARNING_FONT_COLOR),
        DANGER(0, Configs.GUI.ITEM_STATUS_DANGER_FONT_COLOR);

        private final float percent;
        private final ConfigColor color;

        public static Ponderance getPonderance(int value, int maxValue){
            for (Ponderance ponderance: Ponderance.values()) {
                if (!ponderance.canUse(value, maxValue)){
                    continue;
                }

                return ponderance;
            }

            return SAFE;
        }

        Ponderance(float percent, ConfigColor color){
            this.percent = percent;
            this.color = color;
        }

        public boolean canUse(int value, int maxValue){
            return value >= maxValue * this.percent;
        }

        public int getColor() {
            return this.color.getIntegerValue();
        }
    }

    protected Ponderance getPonderance(int value, int maxValue){
        Ponderance ponderance = Ponderance.getPonderance(value, maxValue);
        if (ponderance == Ponderance.DANGER){
            this.haveDangerItem = true;
        }

        return ponderance;
    }

    /**
     * 绘制背包空格状态
     * @param matrices matrices
     * @param x 绘制所在 X 轴
     * @param y 绘制所在 Y 轴
     */
    public void drawKnapsackEmptyStackCount(MatrixStack matrices, int x, int y){
        this.itemRenderer.renderInGui(matrices, new ItemStack(Items.CHEST), x, y);

        String emptyStackCountString = String.valueOf(this.knapsackEmptyStack);
        client.textRenderer.draw(matrices, emptyStackCountString, x + 20, y + 4, Ponderance.getPonderance(this.knapsackEmptyStack, PlayerInventory.MAIN_SIZE).getColor());
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
        client.textRenderer.draw(matrices, itemAllCountString, x, y + 4,  this.itemStatusFontColor);
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
        client.textRenderer.draw(matrices, itemAllCountString, x - itemAllCountString.length() * 5, y + 4,  this.itemStatusFontColor);
    }

    /**
     * 获取显示耐久字符
     * @param damage 耐久
     * @param maxDamage 最大耐久
     * @return 耐久字符
     */
    public String getDamageString(int damage, int maxDamage){
        if (Configs.ENABLE.DRAW_ITEM_DAMAGE_PERCENTAGE.getBooleanValue()){
            return new DecimalFormat(Configs.GUI.ITEM_DAMAGE_PERCENTAGE_PLACES.getStringValue()).format(damage / (float) maxDamage );
        }

        return damage + " / " + maxDamage;
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
        int damage = maxDamage - itemStack.getDamage();

        Ponderance ponderance = Ponderance.getPonderance(damage, maxDamage);
        if (ponderance == Ponderance.DANGER){
            this.haveDangerItem = true;
        }

        client.textRenderer.draw(matrices, this.getDamageString(damage, maxDamage), x, y + 4,  this.getPonderance(damage, maxDamage).getColor());
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
        int damage = maxDamage - itemStack.getDamage();
        String damageString = this.getDamageString(damage, maxDamage);
        client.textRenderer.draw(matrices, damageString, x - damageString.length() * 5, y + 4,  this.getPonderance(damage, maxDamage).getColor());
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

        this.itemRenderer.renderGuiItemIcon(matrices, itemStack, x, y);
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

        this.itemRenderer.renderGuiItemIcon(matrices, itemStack, x, y);
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
        for (ItemStack armorItem: this.inventory.armor) {
            if (armorItem.isEmpty()){
                continue;
            }

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
        ItemStack mainHandStack = this.player.getMainHandStack();
        ItemStack offHandStack = this.player.getOffHandStack();
        if (mainHandStack.isEmpty() && offHandStack.isEmpty()){
            this.drawKnapsackEmptyStackCount(matrices, x, y);
            return;
        }

        if (mainHandStack.isEmpty()){
            this.drawKnapsackEmptyStackCount(matrices, x, y - 18);
            this.drawItemStackStatus(offHandStack, matrices, x, y);
            return;

        }

        this.drawItemStackStatus(mainHandStack, matrices, x, y);
        if (offHandStack.isEmpty()){
            this.drawKnapsackEmptyStackCount(matrices, x, y - 18);
            return;
        }

        this.drawItemStackStatus(offHandStack, matrices, x, y - 18);
        this.drawKnapsackEmptyStackCount(matrices, x, y - 36);
    }

    /**
     * 绘制物品附魔信息
     * @param itemStack 物品格
     * @param matrices matrices
     * @param x 绘制所在 X 轴
     * @param y 绘制所在 Y 轴
     */
    public void drawItemEnchantments(ItemStack itemStack, MatrixStack matrices, int x, int y){
        if (itemStack.isEmpty()){
            return;
        }

        for(Entry<Enchantment, Integer> enchantment :EnchantmentHelper.get(itemStack).entrySet()){
            client.textRenderer.draw(matrices,
                enchantment.getKey().getName(enchantment.getValue()), x, y, this.itemStatusFontColor
            );
            y -= 18;
        }
    }

    /**
     * 绘制主手物品, 副手物品 耐久警告
     * @param matrices matrices
     * @param x 绘制所在 X 轴
     * @param y 绘制所在 Y 轴
     */
    public void drawItemDangerStatusInfo(MatrixStack matrices, int x, int y){
        if (!this.haveDangerItem){
            return;
        }

        RenderSystem.setShaderTexture(0, BARS_TEXTURE);
        DrawableHelper.drawTexture(matrices, x - 3, y, 0, 180, 6, 12, TEXTURE_X, TEXTURE_Y);
    }

    /**
     * 绘制快捷背包
     * @param matrices matrices
     * @param x 绘制所在 X 轴
     * @param y 绘制所在 Y 轴
     */
    public void drawKnapsack(MatrixStack matrices, int x, int y){
        if (this.knapsack.isEmpty()){
            return;
        }

        int offsetY = 0, offsetX = -90;
        List<Item> items = new ArrayList<>();
        for (int index = 9; index < PlayerInventory.MAIN_SIZE; index++){
            ItemStack itemStack = this.inventory.main.get(index);
            Item item = itemStack.getItem();
            if (items.contains(item) || itemStack.isEmpty()){
                continue;
            }

            if (items.size() % 9 == 0){
                offsetX += 85;
                offsetY = 0;
            }

            items.add(item);
            this.drawItemStackStatus(itemStack, matrices, x + offsetX, y + offsetY);
            offsetY -= 18;
        }

    }
}