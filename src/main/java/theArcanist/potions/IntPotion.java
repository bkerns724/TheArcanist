package theArcanist.potions;

import basemod.BaseMod;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import theArcanist.ArcanistMod;
// import theArcanist.powers.IntelligencePower;

import static theArcanist.util.Wiz.*;

public class IntPotion extends AbstractEasyPotion {
    public static final String POTION_ID = ArcanistMod.makeID("IntPotion");
    private static final int INT_AMOUNT = 2;

    public IntPotion() {
        super(POTION_ID, PotionRarity.COMMON, PotionSize.EYE, PotionColor.ENERGY, false, false);
        PotionStrings potionStrings = CardCrawlGame.languagePack.getPotionString(POTION_ID);
        String[] potionDesc = potionStrings.DESCRIPTIONS;
        description = potionDesc[0] + potency + potionDesc[1];
        tips.clear();
        tips.add(new PowerTip(name, description));
        tips.add(new PowerTip(BaseMod.getKeywordTitle("aqualancemod:Intelligence"),
                BaseMod.getKeywordDescription("aqualancemod:Intelligence")));
    }

    public void use(AbstractCreature target) {
        //applyToSelf(new IntelligencePower(adp(), potency));
    }

    public int getPotency(int ascensionLevel) { return INT_AMOUNT; }
}