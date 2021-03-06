package theExile.damagemods;

import basemod.AutoAdd;
import basemod.helpers.TooltipInfo;
import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.icons.AbstractCustomIcon;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import theExile.ExileMod;
import theExile.icons.Lightning;
import theExile.patches.DamageModsIDPatch;

import java.util.ArrayList;

@AutoAdd.Ignore
public class LightningDamage extends AbstractDamageModifier {
    public static final String ID = ExileMod.makeID(LightningDamage.class.getSimpleName());
    public static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
    public TooltipInfo lightningTooltip;
    private boolean visibleTips = true;

    public LightningDamage() {
        lightningTooltip = null;
        DamageModsIDPatch.ID.set(this, ID);
    }

    public LightningDamage(boolean visTips) {
        this();
        visibleTips = visTips;
    }

    @Override
    public boolean shouldPushIconToCard(AbstractCard card) {
        return true;
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCreature target, AbstractCard card) {
        return target.hasPower(VulnerablePower.POWER_ID) ? damage * 1.5f : damage;
    }

    @Override
    public ArrayList<TooltipInfo> getCustomTooltips() {
        if (!visibleTips)
            return new ArrayList<>();
        if (lightningTooltip == null)
            lightningTooltip = new TooltipInfo(cardStrings.DESCRIPTION, cardStrings.EXTENDED_DESCRIPTION[0]);

        return new ArrayList<TooltipInfo>() { { add(lightningTooltip); } };
    }

    public static ArrayList<PowerTip> getPowerTips() {
        ArrayList<PowerTip> list = new ArrayList<>();
        list.add(new PowerTip(cardStrings.DESCRIPTION, cardStrings.EXTENDED_DESCRIPTION[0]));
        return list;
    }

    //Overriding this to true tells us that this damage mod is considered part of the card and not just something added on to the card later.
    //If you ever add a damage modifier during the initialization of a card, it should be inherent.
    public boolean isInherent() {
        return true;
    }

    @Override
    public AbstractDamageModifier makeCopy() {
        LightningDamage output = new LightningDamage(visibleTips);
        output.lightningTooltip = this.lightningTooltip;
        return output;
    }

    @Override
    public AbstractCustomIcon getAccompanyingIcon() {
        return new Lightning();
    }
}
