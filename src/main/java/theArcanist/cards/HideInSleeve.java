package theArcanist.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import theArcanist.actions.MakeAndDiscardSigilAction;

import java.util.ArrayList;
import java.util.Iterator;

import static com.megacrit.cardcrawl.dungeons.AbstractDungeon.*;
import static theArcanist.ArcanistMod.makeID;
import static theArcanist.util.Wiz.*;

public class HideInSleeve extends AbstractArcanistCard {
    public final static String ID = makeID("HideInSleeve");
    private final static int UPGRADED_COST = 0;
    private final static int COST = 1;

    public HideInSleeve() {
        super(ID, COST, CardType.SKILL, CardRarity.UNCOMMON, CardTarget.SELF);
    }

    public void use(AbstractPlayer p, AbstractMonster m) {
        ArrayList<AbstractSigilCard> list = new ArrayList<>();
        AbstractCard c;

        Iterator<AbstractCard> var2 = srcCommonCardPool.group.iterator();
        while(var2.hasNext()) {
            c = var2.next();
            if (c instanceof AbstractSigilCard && !c.hasTag(CardTags.HEALING))
                list.add((AbstractSigilCard) c);
        }

        var2 = srcUncommonCardPool.group.iterator();
        while(var2.hasNext()) {
            c = var2.next();
            if (c instanceof AbstractSigilCard && !c.hasTag(CardTags.HEALING))
                list.add((AbstractSigilCard) c);
        }

        var2 = srcRareCardPool.group.iterator();
        while(var2.hasNext()) {
            c = var2.next();
            if (c instanceof AbstractSigilCard && !c.hasTag(CardTags.HEALING))
                list.add((AbstractSigilCard) c);
        }

        c = list.get(cardRandomRng.random(list.size() - 1));

        atb(new MakeAndDiscardSigilAction((AbstractSigilCard) c));
    }

    public void upp() {
        upgradeBaseCost(UPGRADED_COST);
    }
}