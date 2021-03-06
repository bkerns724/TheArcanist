package theExile.potions;

import com.megacrit.cardcrawl.core.AbstractCreature;
import theExile.actions.SigilDiscoveryAction;

import static theExile.ExileMod.makeID;
import static theExile.util.Wiz.atb;

public class SigilPotion extends AbstractExilePotion {
    public static final String POTION_ID = makeID(SigilPotion.class.getSimpleName());
    public static final int DEFAULT_POTENCY = 1;
    public static final PotionRarity RARITY = PotionRarity.COMMON;
    public static final PotionSize SIZE = PotionSize.CARD;
    public static final boolean IS_THROWN = false;
    public static final boolean TARGET_REQUIRED = false;

    public SigilPotion() {
        super(POTION_ID, RARITY, SIZE, PotionColor.ENERGY,
                IS_THROWN, TARGET_REQUIRED, DEFAULT_POTENCY);

    }

    @Override
    public void setKeywordStrings() {
        keywordStrings.add(makeID("Sigil"));
    }

    public void use(AbstractCreature target) {
        atb(new SigilDiscoveryAction(potency));
    }
}