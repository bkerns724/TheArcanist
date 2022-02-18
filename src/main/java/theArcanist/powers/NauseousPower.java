package theArcanist.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import theArcanist.ArcanistMod;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;

import static theArcanist.util.Wiz.*;

// Code is in WeakPowerPatch
public class NauseousPower extends AbstractArcanistPower {
    public static String POWER_ID = ArcanistMod.makeID("Nauseous");
    private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
    public static final String NAME = powerStrings.NAME;
    public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;
    public static final float WEAK_DECREASE = 0.25f;

    public NauseousPower(AbstractCreature owner, int amount) {
        super(POWER_ID, PowerType.DEBUFF, true, owner, amount);
        this.name = NAME;
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (amount == 1)
            atb(new RemoveSpecificPowerAction(owner, owner, this));
        else
            atb(new ReducePowerAction(owner, owner, POWER_ID, 1));
    }

    @Override
    public void updateDescription() {
        description = DESCRIPTIONS[0];
    }
}