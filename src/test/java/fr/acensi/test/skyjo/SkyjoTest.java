package fr.acensi.test.skyjo;

import fr.acensi.skyjo.business.SkyjoLogic;
import fr.acensi.views.SkyjoView;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class SkyjoTest {

    @Mock
    SkyjoView view;

    public SkyjoTest() {
        view = Mockito.mock(SkyjoView.class);

        SkyjoLogic.createBoard(2);
        SkyjoLogic.initBoard(view, true);
        SkyjoLogic.initDeck();
        SkyjoLogic.initDiscardPile();
    }

    /**
     * https://stackoverflow.com/questions/74490232/how-can-i-simulate-a-shift-click-on-a-vaadin-button
     */
    @Test
    @Ignore
    public void whenSwapping_thenFieldHasChanged() {
        int expected = SkyjoLogic.getBoard().getPlayersField().get(0).getField()[0][0].getValue();

        // Echange des cartes [0, 0] et [0, 1]
        SkyjoLogic.swapCards(SkyjoLogic.getBoard().getPlayersField().get(0), 0, 1);

        Assert.assertEquals(expected, SkyjoLogic.getBoard().getPlayersField().get(0).getField()[0][1].getValue());
    }

    @Test
    public void whenRemoveColumn_thenFieldHasChanged() {
        int expected = SkyjoLogic.getBoard().getPlayersField().get(0).getField().length;

        // Suppression de la colonne 0
        SkyjoLogic.clearColumn(SkyjoLogic.getBoard().getPlayersField().get(0), 0);

        Assert.assertEquals(expected - 1, SkyjoLogic.getBoard().getPlayersField().get(0).getField().length);
    }

    @Test
    public void whenClickingCard_thenCardIsVisible() {
        SkyjoLogic.getBoard().getPlayersField().get(0).getField()[0][0].getButton().click();

        Assert.assertTrue(SkyjoLogic.getBoard().getPlayersField().get(0).getField()[0][0].isVisible());
    }

    @Test
    public void whenCalculatingScore_thenScoreIsCorrect() {
        // Au début de la partie, aucune carte n'est retourné donc le score est de 0
        Assert.assertEquals(0, SkyjoLogic.getBoard().getPlayersField().get(0).calculateScore());

        int cardValue = SkyjoLogic.getBoard().getPlayersField().get(0).getField()[0][0].getValue();
        SkyjoLogic.getBoard().getPlayersField().get(0).getField()[0][0].getButton().click();

        // Après avoir retourné une carte, le score est forcément égale à la valeur de la carte
        Assert.assertEquals(cardValue, SkyjoLogic.getBoard().getPlayersField().get(0).calculateScore());
    }
}
