package theExile.events;

import basemod.eventUtil.AddEventParams;
import basemod.eventUtil.EventUtils;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import com.megacrit.cardcrawl.vfx.UpgradeShineEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import theExile.ExileMod;
import theExile.TheExile;
import theExile.cards.AbstractExileCard;
import theExile.cards.NullElement;
import theExile.cards.cardUtil.Resonance;
import theExile.damagemods.*;
import theExile.patches.TipsInDialogPatch;

import java.util.ArrayList;

import static theExile.ExileMod.makeID;
import static theExile.util.Wiz.adp;

public class SpellForge extends AbstractExileEvent {
    public static final String ID = makeID(SpellForge.class.getSimpleName());
    private static final EventStrings eventStrings;
    private static final String IMAGE_PATH;
    private static final EventUtils.EventType TYPE = EventUtils.EventType.SHRINE;
    private static final int GOLD_COST_A0 = 75;
    private static final int GOLD_COST_A15 = 100;
    private static final int GOLD_GAIN_A0 = 300;
    private static final int GOLD_GAIN_A15 = 250;
    private static final int GOLD_INCREASE = 25;
    private boolean isRemoving = false;
    private AbstractExileCard.elenum element;

    private CUR_SCREEN screen = CUR_SCREEN.INTRO;

    static {
        eventStrings = CardCrawlGame.languagePack.getEventString(ID);
        eventStrings.DESCRIPTIONS[0] = eventStrings.DESCRIPTIONS[0].replace("!E4!",
                String.valueOf(Resonance.ELEMENT_DAMAGE_PENALTY_PERCENT));
        IMAGE_PATH = ExileMod.makeImagePath("events/" + SpellForge.class.getSimpleName() + ".jpg");
    }

    public static AddEventParams getParams() {
        AddEventParams params = new AddEventParams();
        params.eventClass = SpellForge.class;
        params.eventID = ID;
        params.eventType = TYPE;
        params.dungeonIDs = new ArrayList<>();
        params.dungeonIDs.add(TheCity.ID);
        params.playerClass = TheExile.Enums.THE_EXILE;
        params.bonusCondition = () -> ((adp().gold >= getGoldCost() && checkForUpgradableCard())
                || checkForElementalCard());
        return params;
    }

    public SpellForge() {
        super(eventStrings, IMAGE_PATH, getGoldCost(), getGoldGain());
        setChoices();
    }

    public void update() {
        super.update();
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            if (isRemoving) {
                AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(0);
                AbstractDungeon.player.masterDeck.removeCard(c);
                AbstractDungeon.effectList.add(new ShowCardAndObtainEffect(new NullElement(), c.current_x, c.current_y));
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                adp().gainGold(getGoldGain());
                imageEventText.updateBodyText(descriptions[5]);
            }
            else if (element != null) {
                AbstractExileCard c = (AbstractExileCard) AbstractDungeon.gridSelectScreen.selectedCards.get(0);
                c.addModifier(element);
                AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(c.makeStatEquivalentCopy(),
                        Settings.WIDTH/2.0f, Settings.HEIGHT/2.0f));
                AbstractDungeon.topLevelEffects.add(new UpgradeShineEffect(Settings.WIDTH/2.0f, (float)Settings.HEIGHT/2.0F));
                AbstractDungeon.gridSelectScreen.selectedCards.clear();
                adp().loseGold(amount);
                amount += GOLD_INCREASE;
                imageEventText.updateBodyText(descriptions[4]);
            }
            screen = CUR_SCREEN.INTRO;
            setChoices();
        }
    }

    protected void buttonEffect(int buttonPressed) {
        if (screen == CUR_SCREEN.INTRO) {
            switch (buttonPressed) {
                case 0:
                    isRemoving = false;
                    screen = CUR_SCREEN.ELE_CHOICE;
                    imageEventText.updateBodyText(descriptions[1]);
                    imageEventText.clearAllDialogs();

                    if (checkForUpgradableCard(AbstractExileCard.elenum.ICE)) {
                        imageEventText.setDialogOption(options[5]);
                        LargeDialogOptionButton but = imageEventText.optionList.get(0);
                        TipsInDialogPatch.ButtonPreviewField.previewTips.set(but, IceDamage.getPowerTips());
                    } else
                        imageEventText.setDialogOption(options[5], true);
                    if (checkForUpgradableCard(AbstractExileCard.elenum.FORCE)) {
                        imageEventText.setDialogOption(options[6]);
                        LargeDialogOptionButton but = imageEventText.optionList.get(1);
                        TipsInDialogPatch.ButtonPreviewField.previewTips.set(but, ForceDamage.getPowerTips());
                    } else
                        imageEventText.setDialogOption(options[6], true);
                    if (checkForUpgradableCard(AbstractExileCard.elenum.DARK)) {
                        imageEventText.setDialogOption(options[7]);
                        LargeDialogOptionButton but = imageEventText.optionList.get(2);
                        TipsInDialogPatch.ButtonPreviewField.previewTips.set(but, EldritchDamage.getPowerTips());
                    } else
                        imageEventText.setDialogOption(options[7], true);
                    if (checkForUpgradableCard(AbstractExileCard.elenum.FIRE)) {
                        imageEventText.setDialogOption(options[8]);
                        LargeDialogOptionButton but = imageEventText.optionList.get(3);
                        TipsInDialogPatch.ButtonPreviewField.previewTips.set(but, SoulFireDamage.getPowerTips());
                    } else
                        imageEventText.setDialogOption(options[8], true);
                    if (checkForUpgradableCard(AbstractExileCard.elenum.LIGHTNING)) {
                        imageEventText.setDialogOption(options[9]);
                        LargeDialogOptionButton but = imageEventText.optionList.get(4);
                        TipsInDialogPatch.ButtonPreviewField.previewTips.set(but, LightningDamage.getPowerTips());
                    } else
                        imageEventText.setDialogOption(options[9], true);
                    break;
                case 1:
                    isRemoving = true;
                    CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    for (AbstractCard c :  CardGroup.getGroupWithoutBottledCards(adp().masterDeck).group) {
                        if (c instanceof AbstractExileCard && !((AbstractExileCard) c).damageModList.isEmpty())
                            group.addToTop(c);
                    }
                    AbstractDungeon.gridSelectScreen.open(group, 1, descriptions[3], false,
                            false, false, false);
                    break;
                case 2:
                    openMap();
                    break;
            }
        } else if (screen == CUR_SCREEN.ELE_CHOICE) {
            CardGroup eleGroup = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            if (buttonPressed == 0)
                element = AbstractExileCard.elenum.ICE;
            else if (buttonPressed == 1)
                element = AbstractExileCard.elenum.FORCE;
            else if (buttonPressed == 2)
                element = AbstractExileCard.elenum.DARK;
            else if (buttonPressed == 3)
                element = AbstractExileCard.elenum.FIRE;
            else if (buttonPressed == 4)
                element = AbstractExileCard.elenum.LIGHTNING;
            for (LargeDialogOptionButton x : imageEventText.optionList)
                TipsInDialogPatch.ButtonPreviewField.previewTips.set(x, null);
            for (AbstractCard c :  adp().masterDeck.group)
                if (c instanceof AbstractExileCard && c.type == AbstractCard.CardType.ATTACK
                        && !((AbstractExileCard) c).damageModList.contains(element)
                        && ((AbstractExileCard)c).damageModList.size() < 2)
                    eleGroup.addToTop(c);
            AbstractDungeon.gridSelectScreen.open(eleGroup, 1, descriptions[2], false);
        }
    }

    private enum CUR_SCREEN {
        INTRO,
        ELE_CHOICE
    }

    private static int getGoldCost() {
        if (AbstractDungeon.ascensionLevel < 15)
            return GOLD_COST_A0;
        return GOLD_COST_A15;
    }

    private static int getGoldGain() {
        if (AbstractDungeon.ascensionLevel < 15)
            return GOLD_GAIN_A0;
        return GOLD_GAIN_A15;
    }

    private static boolean checkForElementalCard() {
        for (AbstractCard card : CardGroup.getGroupWithoutBottledCards(adp().masterDeck).group)
            if (card instanceof AbstractExileCard && !((AbstractExileCard) card).damageModList.isEmpty())
                return true;
        return false;
    }

    private static boolean checkForUpgradableCard() {
        for (AbstractCard card : adp().masterDeck.group)
            if (card instanceof AbstractExileCard && card.type == AbstractCard.CardType.ATTACK &&
                    ((AbstractExileCard) card).damageModList.size() < 2)
                return true;
        return false;
    }

    private static boolean checkForUpgradableCard(AbstractExileCard.elenum ele) {
        for (AbstractCard card : adp().masterDeck.group)
            if (card instanceof AbstractExileCard && card.type == AbstractCard.CardType.ATTACK &&
                    !((AbstractExileCard) card).damageModList.contains(ele) &&
                    ((AbstractExileCard) card).damageModList.size() < 2)
                return true;
        return false;
    }

    private void setChoices() {
        imageEventText.clearAllDialogs();
        NullElement curse = new NullElement();
        if (adp().gold >= amount && checkForUpgradableCard())
            imageEventText.setDialogOption(options[0].replace("!E3!", String.valueOf(amount)));
        else
            imageEventText.setDialogOption(options[1].replace("!E3!", String.valueOf(amount)), true);
        if (checkForElementalCard())
            imageEventText.setDialogOption(options[2].replace("!CardString!",
                    FontHelper.colorString(curse.name, "r")), curse);
        else
            imageEventText.setDialogOption(options[3], true);

        LargeDialogOptionButton but = imageEventText.optionList.get(0);
        TipsInDialogPatch.ButtonPreviewField.previewTips.set(but, getTipsElements());

        but = imageEventText.optionList.get(1);
        TipsInDialogPatch.ButtonPreviewField.previewTips.set(but, getTipsVoid());

        imageEventText.setDialogOption(options[4]);
    }

    private ArrayList<PowerTip> getTipsElements() {
        ArrayList<PowerTip> list = new ArrayList<>();
        list.add(new PowerTip(descriptions[6], descriptions[7]));
        return list;
    }

    private ArrayList<PowerTip> getTipsVoid() {
        ArrayList<PowerTip> list = new ArrayList<>();
        list.add(new PowerTip(descriptions[6], descriptions[8]));
        return list;
    }
}