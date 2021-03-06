package theExile.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.powers.DexterityPower;
import theExile.TheExile;

import static theExile.ExileMod.makeID;
import static theExile.util.Wiz.*;

public class CeremonialRobe extends AbstractExileRelic {
    public static final String ID = makeID(CeremonialRobe.class.getSimpleName());
    private static final int DEX_LOSS = 1;
    private static final int DRAW_AMOUNT = 1;

    public CeremonialRobe() {
        super(ID, RelicTier.SHOP, LandingSound.FLAT, TheExile.Enums.EXILE_BLARPLE_COLOR);
        amount = DRAW_AMOUNT;
        amount2 = DEX_LOSS;
        setUpdatedDescription();
    }

    public void onEquip() {
        adp().masterHandSize += DRAW_AMOUNT;
    }

    public void onUnequip() {
        adp().masterHandSize -= DRAW_AMOUNT;
    }

    @Override
    public void atBattleStart() {
        flash();
        applyToSelfTop(new DexterityPower(adp(), -DEX_LOSS));
        att(new RelicAboveCreatureAction(adp(), this));
    }
}