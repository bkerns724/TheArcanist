package theAquaLance.cards;

import basemod.AutoAdd;
import basemod.abstracts.CustomCard;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import theAquaLance.TheAquaLance;
import theAquaLance.patches.AbstractCardPatch.AbstractCardField;
import theAquaLance.patches.GameActionManagerPatch.GameActionManagerField;
import theAquaLance.powers.IceMasteryPower;
import theAquaLance.powers.IntelligencePower;
import theAquaLance.powers.SoakedPower;
import theAquaLance.util.CardArtRoller;

import java.util.ArrayList;

import static theAquaLance.AquaLanceMod.*;
import static theAquaLance.util.Wiz.*;

@AutoAdd.Ignore
public abstract class AbstractEasyCard extends CustomCard {

    protected final CardStrings cardStrings;

    public int secondMagic;
    public int baseSecondMagic;
    public boolean upgradedSecondMagic;
    public boolean isSecondMagicModified;

    public int secondDamage;
    public int baseSecondDamage;
    public boolean upgradedSecondDamage;
    public boolean isSecondDamageModified;
    public int[] multiDamageTwo;

    private float rotationTimer = 0;
    protected int previewIndex;
    protected ArrayList<AbstractCard> cardToPreview = new ArrayList<>();

    private boolean needsArtRefresh = false;

    public AbstractEasyCard(final String cardID, final int cost, final CardType type, final CardRarity rarity, final CardTarget target) {
        this(cardID, cost, type, rarity, target, TheAquaLance.Enums.AQUALANCE_TURQUOISE_COLOR);
    }

    public AbstractEasyCard(final String cardID, final int cost, final CardType type, final CardRarity rarity, final CardTarget target, final CardColor color) {
        super(cardID, "", getCardTextureString(cardID.replace(modID + ":", ""), type),
                cost, "", type, color, rarity, target);
        cardStrings = CardCrawlGame.languagePack.getCardStrings(this.cardID);
        rawDescription = cardStrings.DESCRIPTION;
        name = originalName = cardStrings.NAME;
        initializeTitle();
        initializeDescription();

        if (textureImg.contains("ui/missing.png")) {
            if (CardLibrary.getAllCards() != null && !CardLibrary.getAllCards().isEmpty()) {
                CardArtRoller.computeCard(this);
            } else
                needsArtRefresh = true;
        }
    }

    public void onManualDiscard() {}

    @Override
    public void triggerOnManualDiscard() {
        if (AbstractCardField.sigil.get(this)) {
            Integer x = GameActionManagerField.sigilsThisCombat.get(AbstractDungeon.actionManager);
            GameActionManagerField.sigilsThisCombat.set(AbstractDungeon.actionManager, x+1);
        }
        onManualDiscard();
    }

    @Override
    protected Texture getPortraitImage() {
        return CardArtRoller.getPortraitTexture(this);
    }

    public static String getCardTextureString(final String cardName, final AbstractCard.CardType cardType) {
        String textureString;

        switch (cardType) {
            case ATTACK:
            case POWER:
            case SKILL:
                textureString = makeImagePath("cards/" + cardName + ".png");
                break;
            default:
                textureString = makeImagePath("ui/missing.png");
                break;
        }

        FileHandle h = Gdx.files.internal(textureString);
        if (!h.exists()) {
            textureString = makeImagePath("ui/missing.png");
        }
        return textureString;
    }

    @Override
    public void applyPowers() {
        if (baseSecondDamage > -1) {
            secondDamage = baseSecondDamage;

            float secDamage = secondDamage;
            AbstractPower pow = adp().getPower(IntelligencePower.POWER_ID);
            if (pow != null)
                secDamage = ((IntelligencePower)pow).cardCalcHelper(secDamage);

            secondDamage = (int)Math.floor(secDamage);
            isSecondDamageModified = (secondDamage != baseSecondDamage);
        }
        super.applyPowers();
    }

    @Override
    public void calculateCardDamage(AbstractMonster mo) {
        if (baseSecondDamage > -1) {
            secondDamage = baseSecondDamage;

            float secDamage = secondDamage;
            AbstractPower pow = adp().getPower(IntelligencePower.POWER_ID);
            if (pow != null)
                secDamage = ((IntelligencePower)pow).cardCalcHelper(secDamage);

            pow = mo.getPower(SoakedPower.POWER_ID);
            if (pow != null)
                secDamage = ((SoakedPower)pow).cardCalcHelper(secDamage);

            secondDamage = (int)Math.floor(secDamage);
            isSecondDamageModified = (secondDamage != baseSecondDamage);
        }
        super.calculateCardDamage(mo);
    }

    public void resetAttributes() {
        super.resetAttributes();
        secondMagic = baseSecondMagic;
        isSecondMagicModified = false;
        secondDamage = baseSecondDamage;
        isSecondDamageModified = false;
    }

    public void displayUpgrades() {
        super.displayUpgrades();
        if (upgradedSecondMagic) {
            secondMagic = baseSecondMagic;
            isSecondMagicModified = true;
        }
        if (upgradedSecondDamage) {
            secondDamage = baseSecondDamage;
            isSecondDamageModified = true;
        }
    }

    protected void upgradeSecondMagic(int upgradeAmount) {
        baseSecondMagic += upgradeAmount;
        secondMagic = baseSecondMagic;
        upgradedSecondMagic = true;
    }

    protected void upgradeSecondDamage(int amount) {
        baseSecondDamage += amount;
        secondDamage = baseSecondDamage;
        upgradedSecondDamage = true;
    }

    protected void uDesc() {
        rawDescription = cardStrings.UPGRADE_DESCRIPTION;
        initializeDescription();
    }

    protected void upgradeCardToPreview() {
        for (AbstractCard q : cardToPreview) {
            q.upgrade();
        }
    }

    public void upgrade() {
        if (!upgraded) {
            upgradeName();
            upp();
        }
    }

    public abstract void upp();

    public void update() {
        super.update();
        if (needsArtRefresh) {
            CardArtRoller.computeCard(this);
        }
        if (!cardToPreview.isEmpty()) {
            if (hb.hovered) {
                if (rotationTimer <= 0F) {
                    rotationTimer = getRotationTimeNeeded();
                    if (previewIndex >= cardToPreview.size())
                        previewIndex = 0;
                    cardsToPreview = cardToPreview.get(previewIndex);
                    if (previewIndex == cardToPreview.size() - 1) {
                        previewIndex = 0;
                    } else {
                        previewIndex++;
                    }
                } else {
                    rotationTimer -= Gdx.graphics.getDeltaTime();
                }
            }
        }
    }

    protected float getRotationTimeNeeded() {
        return 1f;
    }

    // These shortcuts are specifically for cards. All other shortcuts that aren't specifically for cards can go in Wiz.
    protected void dmg(AbstractCreature m, AbstractGameAction.AttackEffect fx) {
        atb(new DamageAction(m, new DamageInfo(AbstractDungeon.player, damage, DamageInfo.DamageType.NORMAL), fx));
    }

    protected void dmgTwo(AbstractCreature m, AbstractGameAction.AttackEffect fx) {
        atb(new DamageAction(m, new DamageInfo(AbstractDungeon.player, baseSecondDamage, DamageInfo.DamageType.HP_LOSS), fx));
    }

    protected void dmgTop(AbstractMonster m, AbstractGameAction.AttackEffect fx) {
        att(new DamageAction(m, new DamageInfo(AbstractDungeon.player, damage, damageTypeForTurn), fx));
    }

    protected void allDmg(AbstractGameAction.AttackEffect fx) {
        atb(new DamageAllEnemiesAction(AbstractDungeon.player, multiDamage, damageTypeForTurn, fx));
    }

    protected void blck() {
        atb(new GainBlockAction(AbstractDungeon.player, AbstractDungeon.player, block));
    }

    protected void onPopAction(AbstractCreature target) {
        AbstractPower pow = adp().getPower(IceMasteryPower.POWER_ID);
        int count = 1;
        if (pow != null)
            count += pow.amount;
        for (int i = 0; i < count; i++)
            popBonus(target);
    }

    protected void popBonus(AbstractCreature target) {}
}
