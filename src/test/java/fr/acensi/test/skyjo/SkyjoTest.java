package fr.acensi.test.skyjo;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.button.Button;
import fr.acensi.skyjo.business.SkyjoLogic;
import fr.acensi.skyjo.model.SkyjoCard;
import fr.acensi.skyjo.ui.views.SkyjoView;
import org.junit.Assert;
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

        SkyjoLogic.initBoard(2);
        SkyjoLogic.initFields(view, true);
        SkyjoLogic.initDeck();
        SkyjoLogic.initDiscardPile();
    }

    /**
     * Test l'échange de cartes lors d'un Shift + Click
     */
    @Test
    public void whenSwapping_thenCardHasChangedPlace() {
        int expected = SkyjoLogic.getBoard().getPlayersField().get(0).getField()[0][0].getValue();

        Button button1 = SkyjoLogic.getBoard().getPlayersField().get(0).getField()[0][0].getButton();
        Button button2 = SkyjoLogic.getBoard().getPlayersField().get(0).getField()[0][1].getButton();

        // Le premier Shift + click sur un bouton enregistre l'index de colonne et de ligne du premier bouton
        ComponentUtil.fireEvent(button1, new ClickEvent<>(button1, false, 0, 0, 0, 0, 0, 0, false, true, false, false));
        // Le deuxième Shift + click swap les deux cartes
        ComponentUtil.fireEvent(button2, new ClickEvent<>(button2, false, 0, 0, 0, 0, 0, 0, false, true, false, false));

        // Après avoir échangé les cartes, la valeur qui était en [0, 0] doit maintenant se trouver en [0, 1]
        Assert.assertEquals(expected, SkyjoLogic.getBoard().getPlayersField().get(0).getField()[0][1].getValue());
    }

    /**
     * Test la suppression d'une colonne
     */
    @Test
    public void whenRemoveColumn_thenFieldHasOneLessColumn() {
        int expected = SkyjoLogic.getBoard().getPlayersField().get(0).getField().length;

        // Suppression de la colonne 0
        SkyjoLogic.clearColumn(SkyjoLogic.getBoard().getPlayersField().get(0), 0);

        Assert.assertEquals(expected - 1, SkyjoLogic.getBoard().getPlayersField().get(0).getField().length);
    }

    /**
     * Test le clique sur une carte
     */
    @Test
    public void whenClickingCard_thenCardIsVisible() {
        SkyjoLogic.getBoard().getPlayersField().get(0).getField()[0][0].getButton().click();

        Assert.assertTrue(SkyjoLogic.getBoard().getPlayersField().get(0).getField()[0][0].isVisible());
    }

    /**
     * Test le calcul de score
     */
    @Test
    public void whenCalculatingScore_thenScoreIsCorrect() {
        // Au début de la partie, aucune carte n'est retourné donc le score est de 0
        Assert.assertEquals(0, SkyjoLogic.getBoard().getPlayersField().get(0).calculateScore());

        int expected = SkyjoLogic.getBoard().getPlayersField().get(0).getField()[0][0].getValue();
        SkyjoLogic.getBoard().getPlayersField().get(0).getField()[0][0].getButton().click();

        // Après avoir retourné une carte, le score est forcément égale à la valeur de la carte
        Assert.assertEquals(expected, SkyjoLogic.getBoard().getPlayersField().get(0).calculateScore());
    }

    /**
     * Test l'ajout dans la défausse
     */
    @Test
    public void whenDiscardingCard_thenCardIsAddedInDiscardPile() {
        // Au début de la partie, une seule carte est présente dans la défausse
        Assert.assertEquals(1, SkyjoLogic.getBoard().getDiscardPile().getCards().size());

        // On pioche une carte et on l'ajoute à la défausse
        SkyjoCard card = SkyjoLogic.getBoard().getDeck().draw();
        SkyjoLogic.getBoard().getDiscardPile().discard(card);

        // Il y a maintenant deux cartes dans la défausse
        Assert.assertEquals(2, SkyjoLogic.getBoard().getDiscardPile().getCards().size());
    }

    /**
     * Test la recyclage de la défausse en pioche
     */
    @Test
    public void whenRecyclingDiscardPile_thenDiscardPileIsEmptied() {
        // On ajoute une carte à la défausse pour tester la fonctionnalité de recyclage de la défausse
        SkyjoCard card = SkyjoLogic.getBoard().getDeck().draw();
        SkyjoLogic.getBoard().getDiscardPile().discard(card);

        // Il y a maintenant deux cartes dans la défausse
        Assert.assertEquals(2, SkyjoLogic.getBoard().getDiscardPile().getCards().size());

        int expected = card.getValue();

        // On retoure la défausse mais on laisse la dernière carte ajoutée
        SkyjoLogic.recycleDiscardPile();

        // Il n'y a plus qu'une seule carte dans la défausse
        Assert.assertEquals(1, SkyjoLogic.getBoard().getDiscardPile().getCards().size());
        // La seule carte restante est la dernière ajoutée
        Assert.assertEquals(expected, SkyjoLogic.getBoard().getDiscardPile().getTopCard().getValue());
    }

    /**
     * Test la création du board et des fields
     */
    @Test
    public void whenCreatingBoard_thenFieldIsCreatedForEachPlayers() {
        // On créé un board pour deux personnes
        SkyjoLogic.initBoard(2);
        // On a donc deux playerFields
        Assert.assertEquals(2, SkyjoLogic.getBoard().getPlayersField().size());

        // On créé un board pour 10 personnes
        SkyjoLogic.initBoard(10);
        // On a donc 10 playerFields
        Assert.assertEquals(10, SkyjoLogic.getBoard().getPlayersField().size());

        // On ne peut pas créer de board pour 99 personnes car il n'y a que 150 cartes disponibles
        Assert.assertThrows(IndexOutOfBoundsException.class, () -> SkyjoLogic.initBoard(99));
    }
}
