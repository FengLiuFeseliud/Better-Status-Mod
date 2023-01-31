package fengliu.betterstatus.bar;

import net.minecraft.entity.player.PlayerEntity;

public class TwinkleBarOffset extends OffsetItem {
    private int twinkleShowTime = 0;
    private boolean twinkleShow = false;

    public TwinkleBarOffset(int offsetX, int offsetY) {
        super(offsetX, offsetY);
    }

    @Override
    public boolean canDraw(float value, float maxValue, PlayerEntity player) {
        if(twinkleShowTime == 0){
            twinkleShowTime = 100;
        }

        if (twinkleShowTime % 20 == 0){
            twinkleShow = !twinkleShow;
        }

        twinkleShowTime -= 1;
        return twinkleShow;
    }
}
