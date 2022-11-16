package fr.acensi.skyjo.model;

import fr.acensi.views.components.SkyjoPlayerFieldComponent;

public class SkyjoPlayerField {
    private int playerNum;
    private SkyjoCard[][] field;


    public SkyjoPlayerField(int playerNum, SkyjoCard[][] field) {
        this.playerNum = playerNum;
        this.field = field;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public SkyjoCard[][] getField() {
        return field;
    }

    public SkyjoPlayerFieldComponent getFieldComponent() {
        return new SkyjoPlayerFieldComponent(this);
    }

    public void changeCard(int col, int row, SkyjoCard card) {
        card.setVisible(true);
        field[col][row] = card;
    }

    public boolean checkForClearColumn(int column) {
        for (int row = 0; row < 3; row++) {
            if (field[column][row].getValue() != field[column][0].getValue()) {
                System.out.println(field[column][row] + " does not equal " + field[column][0]);
                return false;
            } else if (!field[column][row].isVisible()) {
                return false;
            }
        }
        System.out.println("SKYJO !");
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
            for (int col = 0; col < field.length; col++) {
                SkyjoCard card = field[col][row];
                if (card.isVisible()) {
                    result += card.getValue();
                }
            }
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("----------------------------------------------------------\nPlayer ").append(playerNum).append(": \n");
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < field.length; col++) {
                builder.append(" ").append(field[col][row]).append(" ");
            }
            builder.append("\n");
        }
        return builder.toString();
    }
}
