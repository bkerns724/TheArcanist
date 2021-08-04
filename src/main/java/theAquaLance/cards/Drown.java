package theAquaLance.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import theAquaLance.powers.TempNegStrengthPower;

import static theAquaLance.AquaLanceMod.makeID;
import static theAquaLance.util.Wiz.*;

public class Drown extends AbstractEasyCard {
    public final static String ID = makeID("Drown");
    private final static int MAGIC = 6;
    private final static int UPGRADE_MAGIC = 2;
    private final static int SECOND_MAGIC = 2;
    private final static int UPGRADE_SECOND = 1;
    private final static int COST = 2;

    public Drown() {
        super(ID, COST, CardType.SKILL, CardRarity.RARE, CardTarget.ENEMY);
        baseMagicNumber = magicNumber = MAGIC;
        baseSecondMagic = secondMagic = SECOND_MAGIC;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        applyToEnemy(m, new TempNegStrengthPower(m, magicNumber));
        applyToEnemy(m, new WeakPower(m, secondMagic, false));
    }

    public void upp() {
        upgradeMagicNumber(UPGRADE_MAGIC);
        upgradeSecondMagic(UPGRADE_SECOND);
    }
}