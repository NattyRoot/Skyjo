package fr.acensi.skyjo.model;

import fr.acensi.skyjo.ui.components.SkyjoPlayerFieldComponent;

public class SkyjoPlayerField {
    private final int playerNum;
    private boolean hisTurn;
    private SkyjoCard[][] field;

    public SkyjoPlayerField(int playerNum, SkyjoCard[][] field) {
        this.playerNum = playerNum;
        this.field = field;
    }

    public int getPlayerNum() {
        return playerNum;
    }
    public boolean isHisTurn() {
        return hisTurn;
    }

    public void setHisTurn(int player, int max) {
        if (player == max) {
            player = 0;
        }
        hisTurn = playerNum == player;
    }

    public SkyjoCard[][] getField() {
        return field;
    }

    public SkyjoPlayerFieldComponent getFieldComponent() {
        return new SkyjoPlayerFieldComponent(this);
    }


    public void changeCard(int col, int row, SkyjoCard card, boolean showCard) {
        if (showCard) {
            card.setVisible(true);
        }
        field[col][row] = card;
    }

    public boolean checkForClearColumn(int column) {
        for (int row = 0; row < 3; row++) {
            if (field[column][row].getValue() != field[column][0].getValue()) {
                return false;
            } else if (!field[column][row].isVisible()) {
                return false;
            }
        }
        return true;
    }

    public SkyjoCard[] discardCol(int col) {
        SkyjoCard[][] newField = new SkyjoCard[field.length - 1][3];
        for (int newRow = 0; newRow < 3; newRow++) {
            int diff = 0;
            for (int newCol = 0; newCol < field.length; newCol++) {
                if (newCol == col) {
                    diff = 1;
                    continue;
                }
                newField[newCol - diff][newRow] = field[newCol][newRow];
            }
        }

        SkyjoCard[] discarded = field[col];
        this.field = newField;

        return discarded;
    }

    public int calculateScore() {
        int result = 0;
        for (int row = 0; row < 3; row++) {
            for (SkyjoCard[] skyjoCards : field) {
                SkyjoCard card = skyjoCards[row];
                if (card.isVisible()) {
                    result += card.getValue();
                }
            }
        }

        return result;
    }

    public String getPlayerName() {
        return switch (playerNum) {
            case 0 -> "Félix";
            case 1 -> "Alexandre";
            case 2 -> "Romain";
            case 3 -> "Thibault";
            case 4 -> "Maël";
            case 5 -> "Mathieu";
            case 6 -> "Yann";
            default -> "Player " + (playerNum + 1);
        };
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getPlayerName()).append(": \n");
        for (int row = 0; row < 3; row++) {
            for (SkyjoCard[] skyjoCards : field) {
                builder.append(skyjoCards[row]).append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
