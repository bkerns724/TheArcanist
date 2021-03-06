package theExile.events;

import basemod.eventUtil.AddEventParams;
import basemod.eventUtil.EventUtils;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.curses.Clumsy;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import theExile.ExileMod;
import theExile.TheExile;
import theExile.cards.AbstractExileCard;
import theExile.patches.TipsInDialogPatch;

import java.util.ArrayList;

import static theExile.ExileMod.makeID;
import static theExile.util.Wiz.adp;

public class ResearchCenter extends AbstractExileEvent {
    public static final String ID = makeID(ResearchCenter.class.getSimpleName());
    private static final EventStrings eventStrings;
    private static final String IMAGE_PATH;
    private static final EventUtils.EventType TYPE = EventUtils.EventType.SHRINE;
    private static final int GOLD_A0 = 100;
    private static final int GOLD_A15 = 125;
    private static final float HEALTH_A0 = 0.15f;
    private static final float HEALTH_A15 = 0.2f;

    private boolean pickedRetain = false;

    private CUR_SCREEN screen = CUR_SCREEN.INTRO;

    static {
        eventStrings = CardCrawlGame.languagePack.getEventString(ID);
        IMAGE_PATH = ExileMod.makeImagePath("events/" + ResearchCenter.class.getSimpleName() + ".jpg");
    }

    public static AddEventParams getParams() {
        AddEventParams params = new AddEventParams();
        params.eventClass = ResearchCenter.class;
        params.eventID = ID;
        params.eventType = TYPE;
        params.dungeonIDs = new ArrayList<>();
        params.dungeonIDs.add(TheCity.ID);
        params.playerClass = TheExile.Enums.THE_EXILE;
        params.bonusCondition = () -> (adp().gold >= getGoldCost() && hasSigil() || hasCardForSigil());
        return params;
    }

    public ResearchCenter() {
        super(eventStrings, IMAGE_PATH, getGoldCost(), getHealthCost());

        if (adp().gold >= getGoldCost() && hasSigil())
            imageEventText.setDialogOption(options[0]);
        else
            imageEventText.setDialogOption(options[1], true);
        if (hasCardForSigil()) {
            imageEventText.setDialogOption(options[2], new Clumsy());
            LargeDialogOptionButton but = imageEventText.optionList.get(1);
            TipsInDialogPatch.ButtonPreviewField.previewTips.set(but, getTipsFull());
        }
        else {
            imageEventText.setDialogOption(options[3], true);
            LargeDialogOptionButton but = imageEventText.optionList.get(1);
            TipsInDialogPatch.ButtonPreviewField.previewTips.set(but, getTipsShort());
        }
        imageEventText.setDialogOption(options[4]);
    }

    public void update() {
        super.update();
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            if (pickedRetain) {
                AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                c.selfRetain = true;
                c.initializeDescription();
                AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(),
                        Settings.WIDTH/2.0f, Settings.HEIGHT/2.0f));
                AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(Settings.WIDTH/2.0f, (float)Settings.HEIGHT/2.0F));
                imageEventText.updateBodyText(descriptions[1]);
                imageEventText.clearAllDialogs();
                imageEventText.setDialogOption(options[6]);
                screen = CUR_SCREEN.COMPLETE;
            }
            else {
                AbstractExileCard c = (AbstractExileCard) AbstractDungeon.gridSelectScreen.selectedCards.get(0);
                c.sigil = true;
                c.cost = -2;
                if (c.target == AbstractCard.CardTarget.ENEMY)
                    c.target = AbstractCard.CardTarget.ALL_ENEMY;
                c.initializeDescription();
                AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(),
                        Settings.WIDTH/2.0f, Settings.HEIGHT/2.0f));
                AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(Settings.WIDTH/2.0f, (float)Settings.HEIGHT/2.0F));
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                imageEventText.updateBodyText(descriptions[3]);
                imageEventText.clearAllDialogs();
                imageEventText.setDialogOption(options[5]);
                screen = CUR_SCREEN.POTION;
            }
        }
    }

    protected void buttonEffect(int buttonPressed) {
        if (screen == CUR_SCREEN.INTRO) {
            switch (buttonPressed) {
                case 0:
                    pickedRetain = true;
                    adp().loseGold(amount);
                    CardGroup retainGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    for (AbstractCard c :  adp().masterDeck.group) {
                        if (c instanceof AbstractExileCard && ((AbstractExileCard) c).sigil)
                            retainGroup.addToTop(c);
                        for (LargeDialogOptionButton but : imageEventText.optionList)
                            TipsInDialogPatch.ButtonPreviewField.previewTips.set(but, new ArrayList<>());
                    }
                    AbstractDungeon.gridSelectScreen.open(retainGroup, 1, descriptions[2], false,
                            false, false, false);
                    break;
                case 1:
                    pickedRetain = false;
                    CardGroup sigilGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    for (AbstractCard c :  adp().masterDeck.group) {
                        if (c instanceof AbstractExileCard && c.cost < 3 && c.cost >= 0)
                            sigilGroup.addToTop(c);
                    }
                    AbstractDungeon.gridSelectScreen.open(sigilGroup, 1, descriptions[4], false,
                            false, false, false);
                    for (LargeDialogOptionButton but : imageEventText.optionList)
                        TipsInDialogPatch.ButtonPreviewField.previewTips.set(but, new ArrayList<>());
                    break;
                case 2:
                    screen = CUR_SCREEN.COMPLETE;
                    imageEventText.updateBodyText(descriptions[6]);
                    imageEventText.clearAllDialogs();
                    imageEventText.setDialogOption(options[6]);
                    drinkPotion();
                    break;
            }
        } else if (screen == CUR_SCREEN.POTION) {
            drinkPotion();
            imageEventText.updateBodyText(descriptions[5]);
            imageEventText.clearAllDialogs();
            imageEventText.setDialogOption(options[6]);
            screen = CUR_SCREEN.COMPLETE;

        } else
            openMap();
    }

    private enum CUR_SCREEN {
        INTRO,
        POTION,
        COMPLETE;
    }

    private ArrayList<PowerTip> getTipsFull() {
        ArrayList<PowerTip> list = new ArrayList<>();
        list.add(new PowerTip(descriptions[7], descriptions[8]));
        list.add(new PowerTip(descriptions[9], descriptions[10].replace("!HealthString!", "" + amount2)));
        return list;
    }

    private ArrayList<PowerTip> getTipsShort() {
        ArrayList<PowerTip> list = new ArrayList<>();
        list.add(new PowerTip(descriptions[7], descriptions[8]));
        return list;
    }

    private void drinkPotion() {
        int x = AbstractDungeon.miscRng.random(0, 2);
        ExileMod.logger.info("potion");
        ExileMod.logger.info(x);
        if (x == 0)
            return;
        if (x == 1) {
            AbstractDungeon.player.damage(new DamageInfo(adp(), amount2, DamageInfo.DamageType.HP_LOSS));
            AbstractDungeon.effectList.add(new FlashAtkImgEffect(adp().hb.cX, adp().hb.cY, AbstractGameAction.AttackEffect.POISON));
            return;
        }
        if (x == 2)
            AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new Clumsy(),Settings.WIDTH/2.0f,
                    (float)Settings.HEIGHT/2.0F ));
    }

    private static int getGoldCost() {
        if (AbstractDungeon.ascensionLevel < 15)
            return GOLD_A0;
        return GOLD_A15;
    }

    private static int getHealthCost() {
        if (AbstractDungeon.ascensionLevel < 15)
            return (int)(HEALTH_A0*adp().maxHealth);
        return (int)(HEALTH_A15*adp().maxHealth);
    }

    private static boolean hasSigil() {
        for (AbstractCard c : adp().masterDeck.group)
            if (c instanceof AbstractExileCard && ((AbstractExileCard) c).sigil)
                return true;
        return false;
    }

    private static boolean hasCardForSigil() {
        for (AbstractCard c : adp().masterDeck.group)
            if (c instanceof AbstractExileCard && c.cost < 3 && c.cost >= 0)
                return true;
        return false;
    }
}