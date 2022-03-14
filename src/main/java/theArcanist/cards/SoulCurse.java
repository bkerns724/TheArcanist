package theArcanist.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theArcanist.powers.JinxPower;

import static theArcanist.ArcanistMod.makeID;
import static theArcanist.util.Wiz.*;

public class SoulCurse extends AbstractArcanistCard {
    public final static String ID = makeID("SoulCurse");
    private final static int COST = 1;

    public SoulCurse() {
        super(ID, COST, CardType.SKILL, CardRarity.RARE, CardTarget.ENEMY);
        exhaust = true;
    }

    public void onUse(AbstractPlayer p, AbstractMonster m) {
        int count = getJinxAmount(m);
        if (count > 0)
            applyToEnemy(m, new JinxPower(m, count));
    }

    public void upp() {
        exhaust = false;
    }
}