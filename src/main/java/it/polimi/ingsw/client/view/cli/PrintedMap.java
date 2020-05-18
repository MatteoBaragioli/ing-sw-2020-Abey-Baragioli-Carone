package it.polimi.ingsw.client.view.cli;

public class PrintedMap {
        private static final int mapDim=5;
        private PrintedBlock printedMap[][]=new PrintedBlock[mapDim][mapDim];


        public PrintedMap() {
            for (int i = 0; i < mapDim; i++)
                for (int j = 0; j < mapDim; j++) {
                    this.printedMap[i][j] = new PrintedBlock(i, j);
                }
        }


        /**
         * This method returns a printedblock depending on the given coordinates
         * @param x longitude
         * @param y latitude
         * @return printedBlock
         */
        public PrintedBlock position(int x, int y) {
            if (x>=0 && x<mapDim && y>=0 && y<mapDim)
                return printedMap[x][y];
            return null;
        }

    }

