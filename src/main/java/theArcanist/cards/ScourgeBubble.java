package theArcanist.cards;

import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theArcanist.ArcanistMod;
import theArcanist.damagemods.ScourgeType;
import theArcanist.powers.JinxThornsPower;

import static theArcanist.ArcanistMod.makeID;
import static theArcanist.util.Wiz.*;

public class ScourgeBubble extends AbstractArcanistCard {
    public final static String ID = makeID(ScourgeBubble.class.getSimpleName());
    private final static int BLOCK = 8;
    private final static int UPGRADE_BLOCK = 4;
    private final static int MAGIC = 1;
    private final static int COST = 1;

    public ScourgeBubble() {
        super(ID, COST, CardType.SKILL, ArcanistMod.Enums.UNIQUE, CardTarget.SELF);
        baseBlock = BLOCK;
        baseMagicNumber = magicNumber = MAGIC;
        DamageModifierManager.addModifier(this, new ScourgeType());
    }

    public void onUse(AbstractPlayer p, AbstractMonster m) {
        blck();
        applyToSelf(new JinxThornsPower(p, magicNumber));
    }

    public void upp() {
        upgradeBlock(UPGRADE_BLOCK);
    }
}