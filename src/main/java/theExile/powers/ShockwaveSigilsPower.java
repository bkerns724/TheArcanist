package theExile.powers;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import theExile.ExileMod;
import theExile.cards.AbstractExileCard;

import static theExile.util.Wiz.applyToEnemy;
import static theExile.util.Wiz.forAllMonstersLiving;

public class ShockwaveSigilsPower extends AbstractExilePower {
    public static String POWER_ID = ExileMod.makeID(ShockwaveSigilsPower.class.getSimpleName());

    public ShockwaveSigilsPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.BUFF, false, owner, amount);
    }

    @Override
    public void onAfterCardPlayed(AbstractCard usedCard) {
        if (usedCard instanceof AbstractExileCard && ((AbstractExileCard) usedCard).sigil)
            forAllMonstersLiving(m -> applyToEnemy(m, new CrushedPower(m, amount)));
    }
}