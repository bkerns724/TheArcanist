package theArcanist.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import theArcanist.ArcanistMod;
import theArcanist.powers.CorrodedPower;

import static theArcanist.ArcanistMod.makeID;
import static theArcanist.util.Wiz.*;

public class AcidRain extends AbstractArcanistCard {
    public final static String ID = makeID("AcidRain");
    private final static int MAGIC = 1;
    private final static int UPGRADE_MAGIC = 1;
    private final static int COST = 1;
    private final static int SECOND_MAGIC = 2;

    public AcidRain() {
        super(ID, COST, CardType.SKILL, ArcanistMod.Enums.UNIQUE, CardTarget.ALL_ENEMY);
        baseMagicNumber = magicNumber = MAGIC;
        baseSecondMagic = secondMagic = SECOND_MAGIC;
        isMultiDamage = true;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        forAllMonstersLiving(mon -> {
            applyToEnemy(mon, new CorrodedPower(mon, secondMagic));
            applyToEnemy(mon, new VulnerablePower(mon, magicNumber, false));
            vfx(new FlashAtkImgEffect(m.hb.cX, m.hb.cY, ArcanistMod.Enums.ACID));
        });
    }

    public void upp() {
        upgradeMagicNumber(UPGRADE_MAGIC);
    }
}