package theExile.cards;

import basemod.ReflectionHacks;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;
import com.megacrit.cardcrawl.vfx.combat.MindblastEffect;
import theExile.ExileMod;

import static theExile.ExileMod.makeID;
import static theExile.util.Wiz.vfx;

public class ArcaneBeam extends AbstractExileCard {
    public final static String ID = makeID(ArcaneBeam.class.getSimpleName());
    private final static int DAMAGE = 20;
    private final static int UPGRADE_DAMAGE = 5;
    private final static int COST = 2;

    public ArcaneBeam() {
        super(ID, COST, CardType.ATTACK, ExileMod.Enums.UNIQUE, CardTarget.ENEMY);
    }

    @Override
    protected void applyAttributes() {
        baseDamage = DAMAGE;
    }

    public void onUse(AbstractPlayer p, AbstractMonster m) {
        MindblastEffect effect = new MindblastEffect(p.hb.cX, p.hb.cY, p.flipHorizontal);
        ReflectionHacks.setPrivate(effect, AbstractGameEffect.class, "color", Color.MAGENTA.cpy());
        vfx(effect, 0.1F);
        dmg(m, AbstractGameAction.AttackEffect.NONE);
    }

    public void upp() {
        upgradeDamage(UPGRADE_DAMAGE);
    }
}