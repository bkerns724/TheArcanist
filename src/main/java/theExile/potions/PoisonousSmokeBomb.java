package theExile.potions;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.relics.SacredBark;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import theExile.ExileMod;
import theExile.actions.PoisonBombAction;

import static theExile.ExileMod.makeID;
import static theExile.util.Wiz.*;

public class PoisonousSmokeBomb extends AbstractExilePotion {
    public static final String POTION_ID = makeID(PoisonousSmokeBomb.class.getSimpleName());
    public static final int DEFAULT_POTENCY = 1;
    public static final PotionRarity RARITY = ExileMod.Enums.EVENT;
    public static final PotionSize SIZE = PotionSize.SPHERE;
    public static final boolean IS_THROWN = false;
    public static final boolean TARGET_REQUIRED = false;

    public PoisonousSmokeBomb() {
        super(POTION_ID, RARITY, SIZE, PotionColor.ENERGY,
                IS_THROWN, TARGET_REQUIRED, DEFAULT_POTENCY);
    }

    @Override
    public void setKeywordStrings() { }

    public void use(AbstractCreature target) {
        if (adRoom().phase == AbstractRoom.RoomPhase.COMBAT)
            atb(new PoisonBombAction());
    }


    public boolean canUse() {
        if (super.canUse()) {
            for (AbstractMonster m : getEnemies()) {
                if (m.hasPower("BackAttack"))
                    return false;
                if (m.type == AbstractMonster.EnemyType.BOSS)
                    return false;
                if (adRoom().eliteTrigger && !adp().hasRelic(SacredBark.ID))
                    return false;
            }
            return true;
        }
        return false;
    }
}