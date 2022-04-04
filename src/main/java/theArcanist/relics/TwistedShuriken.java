package theArcanist.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import theArcanist.TheArcanist;

import static theArcanist.ArcanistMod.makeID;
import static theArcanist.util.Wiz.*;

public class TwistedShuriken extends AbstractArcanistRelic {
    public static final String ID = makeID("TwistedShuriken");
    public static final int LOSE_STR_AMT = 1;
    public static final int NUM_CARDS = 3;

    public TwistedShuriken() {
        super(ID, AbstractRelic.RelicTier.UNCOMMON,
                AbstractRelic.LandingSound.CLINK,
                TheArcanist.Enums.ARCANIST_BLARPLE_COLOR);
        amount = LOSE_STR_AMT;
        amount2 = NUM_CARDS;
        setUpdatedDescription();
    }

    public void atTurnStart() {
        counter = 0;
    }

    public void onUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) {
            ++counter;
            if (counter % 3 == 0) {
                counter = 0;
                flash();

                AbstractMonster strongestMonster = null;

                for (AbstractMonster m : getEnemies()) {
                    if (strongestMonster == null)
                        strongestMonster = m;
                    else if (m.currentHealth > strongestMonster.currentHealth)
                        strongestMonster = m;
                }
                if (strongestMonster == null)
                    return;

                atb(new RelicAboveCreatureAction(strongestMonster, this));
                applyToEnemy(strongestMonster, new StrengthPower(strongestMonster, -1));
            }
        }
    }

    public void onVictory() {
        counter = -1;
    }
}