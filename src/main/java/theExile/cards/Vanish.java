package theExile.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theExile.actions.ShadowCloakBlockAction;
import theExile.powers.ShadowcloakPower;

import static theExile.ExileMod.makeID;
import static theExile.util.Wiz.*;

public class Vanish extends AbstractExileCard {
    public final static String ID = makeID(Vanish.class.getSimpleName());
    private final static int BLOCK = 0;
    private final static int MAGIC = 2;
    private final static int COST = 2;
    private final static int SECOND_MAGIC = 3;
    private final static int UPGRADE_SECOND = 1;

    public Vanish() {
        super(ID, COST, CardType.SKILL, CardRarity.RARE, CardTarget.SELF);
    }

    @Override
    protected void applyAttributes() {
        baseBlock = BLOCK;
        baseMagicNumber = magicNumber = MAGIC;
        secondMagic = baseSecondMagic = SECOND_MAGIC;
    }

    public void onUse(AbstractPlayer p, AbstractMonster m) {
        applyToSelf(new ShadowcloakPower(p, magicNumber));
        atb(new ShadowCloakBlockAction(this));
    }

    @Override
    public void applyPowers() {
        if (adp().hasPower(ShadowcloakPower.POWER_ID))
            baseBlock = secondMagic*adp().getPower(ShadowcloakPower.POWER_ID).amount;
        super.applyPowers();
        baseBlock = BLOCK;
    }

    public void upp() {
        upMagic2(UPGRADE_SECOND);
    }
}