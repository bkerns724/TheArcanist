package theArcanist.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;
import com.megacrit.cardcrawl.vfx.combat.LightningEffect;

import static theArcanist.ArcanistMod.makeID;
import static theArcanist.util.Wiz.*;

public class Zap extends AbstractArcanistCard {
    public final static String ID = makeID("Zap");
    private final static int DAMAGE = 5;
    private final static int UPGRADE_DAMAGE = 3;
    private final static int COST = 1;

    public Zap() {
        super(ID, COST, CardType.ATTACK, CardRarity.UNCOMMON, CardTarget.ENEMY);
        baseDamage = DAMAGE;
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        vfx(new LightningEffect(m.drawX, m.drawY));
        dmg(m, AbstractGameAction.AttackEffect.NONE);
        applyToEnemy(m, new WeakPower(m, getJinxAmount(m), false));
    }

    public void upp() {
        upgradeDamage(UPGRADE_DAMAGE);
    }
}