package theExile.relics;

import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.mod.stslib.relics.RelicWithButton;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import theExile.TheExile;

import java.util.ArrayList;

import static theExile.ExileMod.makeID;
import static theExile.util.Wiz.atb;
import static theExile.util.Wiz.discard;

public class VestOfManyPockets extends AbstractExileRelic implements RelicWithButton {
    public static final String ID = makeID(VestOfManyPockets.class.getSimpleName());
    private static final String textureString = "exilemodResources/images/ui/CycleButton.png";
    private static final int CYCLE_AMOUNT = 1;

    public VestOfManyPockets() {
        super(ID, AbstractRelic.RelicTier.COMMON, AbstractRelic.LandingSound.FLAT, TheExile.Enums.EXILE_BLARPLE_COLOR);
        amount = CYCLE_AMOUNT;
        setUpdatedDescription();
    }

    @Override
    public void atPreBattle() {
        grayscale = false;
    }

    @Override
    public void onVictory() {
        grayscale = false;
    }

    @Override
    public Texture getTexture() {
        return ImageMaster.loadImage(textureString);
    }

    @Override
    public boolean isButtonDisabled() {
        return grayscale;
    }

    @Override
    public ArrayList<PowerTip> getHoverTips() {
        return tips;
    }

    @Override
    public void onButtonPress() {
        CardCrawlGame.sound.play("UI_CLICK_1");
        flash();
        atb(new DrawCardAction(amount));
        discard(amount);
        grayscale = true;
    }
}
