package theExile.relics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import theExile.TheExile;
import theExile.cards.AbstractExileCard;
import theExile.cards.Wrath;

import static theExile.ExileMod.makeID;

// Code in SoulFireDamage and LightningDamage
public class HexedStaff extends AbstractExileRelic {
    public static final String ID = makeID(HexedStaff.class.getSimpleName());
    public static final float BONUS_MULT = 0.25f;
    private static final int BONUS_PERCENT = (int)(BONUS_MULT*100);

    public HexedStaff() {
        super(ID, RelicTier.SPECIAL, LandingSound.HEAVY, TheExile.Enums.EXILE_BLARPLE_COLOR);
        cardToPreview = new Wrath();
        amount = BONUS_PERCENT;
        setUpdatedDescription();
    }

    @Override
    public void onEquip() {
        AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Wrath(), (float) Settings.WIDTH / 2.0F,
                (float) Settings.HEIGHT / 2.0F));

        countCurses();
    }

    @Override
    public float atDamageModify(float damage, AbstractCard c) {
        if (c instanceof AbstractExileCard) {
            AbstractExileCard card = (AbstractExileCard) c;
            if (card.damageModList.contains(AbstractExileCard.elenum.LIGHTNING))
                damage *= (1f + BONUS_MULT*counter);
            if (card.damageModList.contains(AbstractExileCard.elenum.FIRE))
                damage *= (1f + BONUS_MULT*counter);
        }
        return damage;
    }

    public void onMasterDeckChange() {
        countCurses();
    }

    private void countCurses() {
        counter = 0;
        for (AbstractCard c : AbstractDungeon.player.masterDeck.group) {
            if (c.type == AbstractCard.CardType.CURSE)
                ++counter;
        }
    }
}
