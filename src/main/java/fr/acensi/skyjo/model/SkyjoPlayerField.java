package fr.acensi.skyjo.model;

public class SkyjoPlayerField {
    private final int playerNum;
    private String playerName;
    private boolean hisTurn;
    private SkyjoCard[][] field;

    public SkyjoPlayerField(int playerNum, String playerName, SkyjoCard[][] field) {
        this.playerNum = playerNum;
        this.field = field;
        this.playerName = playerName;
    }

    public int getPlayerNum() {
        return playerNum;
    }

    public String getPlayerName() {
        return playerName;
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
