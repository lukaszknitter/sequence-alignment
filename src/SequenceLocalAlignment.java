import java.util.ArrayList;


public class SequenceLocalAlignment extends SequenceGlobalAlignment{
    private int max;

    public SequenceLocalAlignment(String filePath) {
       super(filePath);
    }

    @Override
    public void countTable(){
        mainTable = new MainTableElement [firstSequenceLength+1][secondSequenceLength+1];
        mainTable[0][0]=new MainTableElement(0,false,false,false);

        for (int i =1; i<secondSequenceLength+1; i++){
            mainTable[0][i] = new MainTableElement(0,false,false,false);
        }

        for (int i =1; i<firstSequenceLength+1; i++){
            mainTable[i][0] =new MainTableElement( 0,false,false,false);
        }

        MainTableElement max;
        for (int i =1; i<firstSequenceLength+1; i++){
            for (int j =1; j<secondSequenceLength+1; j++){
                max = findMax(i,j);
                mainTable[i][j]= max;
                if(max.getValue() >= this.max) this.max = max.getValue();
            }
        }
    }

    private int SimilarBetweenElements(String a, String b){
        int cost = similarityTable[alphabet.indexOf(a)][alphabet.indexOf(b)];
        return cost;
    }

    private MainTableElement findMax(int i, int j){
        String actualCharFirstSq = Character.toString(firstSequence.charAt(i-1));
        String actualCharSecondSq = Character.toString(secondSequence.charAt(j-1));

        int costBtwChars = CostBetweenElements(actualCharFirstSq,actualCharSecondSq);
        int leftCost= CostBetweenElements(actualCharFirstSq," ");
        int upCost = CostBetweenElements(" ",actualCharSecondSq);

        int diagonalCost = costBtwChars + mainTable[i-1][j-1].getValue();
        int left = leftCost + mainTable[i-1][j].getValue();
        int up = upCost + mainTable[i][j-1].getValue();
        int value =  Math.max(Math.max(0,diagonalCost), Math.max(left, up));
        if(value != 0) {
            return new MainTableElement(value, left == value, up == value, diagonalCost == value);
        }
        else{
            return new MainTableElement(value, false, false, false);
        }
    }

    @Override
    public void countResults(MainTableElement [][] table, ArrayList<ArrayList<PointInTable>> list){
        ArrayList<PointInTable> localResult;
        for(int i=firstSequenceLength; i >= 0; i--){
            for(int j=secondSequenceLength; j >= 0; j--){
                if(table[i][j].getValue() == this.max){
                    PointInTable actualPoint = new PointInTable(i, j);
                    localResult = new ArrayList<>();
                    localResult.add(actualPoint);
                    list.add(localResult);
                }
            }
        }
        PointInTable actualPoint = list.get(0).get(0);
        int counter = 0;
        while(continueCondition(list)){
            localResult = list.get(counter);
            int x = localResult.get(localResult.size()-1).x;
            int y = localResult.get(localResult.size()-1).y;
            while(mainTable[x][y].getValue() != 0){
                int edgesCounter = 0;
                if( table[x][y].isUpEdge()){
                    actualPoint = new PointInTable(x, y-1);
                    edgesCounter++;
                }
                if( table[x][y].isLeftEdge()){
                    if(edgesCounter != 0){
                        ArrayList<PointInTable> clone = new  ArrayList<>();
                        for (PointInTable item : localResult) clone.add(item);
                        clone.add(new PointInTable(x-1, y));
                        list.add(clone);

                    } else {
                        actualPoint = new PointInTable(x-1, y);
                    }
                    edgesCounter++;
                }
                if( table[x][y].isDiagonalEdge()){
                    if(edgesCounter != 0){
                        ArrayList<PointInTable> clone = new  ArrayList<>();
                        for (PointInTable item : localResult) clone.add(item);
                        clone.add(new PointInTable(x-1, y-1));
                        list.add(clone);

                    } else {
                        actualPoint = new PointInTable(x-1, y-1);
                    }
                    edgesCounter++;
                }
                if(actualPoint.x == (x-1) && actualPoint.y == (y-1)){
                    x--;
                    y--;
                } else  if(actualPoint.x == x && actualPoint.y == (y-1)){
                    y--;
                } else {
                    x--;
                }
                localResult.add(actualPoint);
                if(edgesCounter==0){
                    list.remove(localResult);
                    counter--;
                    break;
                }
            }
            counter++;
        }
    }

    @Override
    public ArrayList<String> getResultsList(ArrayList<ArrayList<PointInTable>> lists) {
        ArrayList<String> resultsList = new ArrayList<>();

        int actual_x = 0, actual_y = 0;
        int resent_x = 0;
        int resent_y = 0;
        for (ArrayList<PointInTable> list : lists) {
            String firstOutput = "";
            String secondOutput = "";
            resent_x = list.get(0).x;
            resent_y = list.get(0).y;
            for (PointInTable point : list) {
                actual_x = point.x;
                actual_y = point.y;
                if(actual_x!=firstSequenceLength ||  actual_y!=secondSequenceLength) {

                    if (resent_x == actual_x) {
                        firstOutput = firstOutput + (" ");
                    } else {
                        firstOutput = firstOutput + (firstSequence.charAt(actual_x ));
                    }
                    if (resent_y == actual_y) {
                        secondOutput = secondOutput + (" ");
                    } else {
                        secondOutput = secondOutput + (secondSequence.charAt(actual_y ));
                    }
                }
                resent_x = actual_x;
                resent_y = actual_y;
            }
            firstOutput= new StringBuilder(firstOutput).reverse().toString();
            secondOutput= new StringBuilder(secondOutput).reverse().toString();
            if(rnaToAminoAcids){
                firstOutput = reTranslateSequence(firstOutput);
                secondOutput = reTranslateSequence(secondOutput);
            }
            resultsList.add(firstOutput);
            resultsList.add(secondOutput);
        }
        return resultsList;
    }

    private boolean continueCondition(ArrayList<ArrayList<PointInTable>> lists){
        if(lists.isEmpty()) return true;
        for(ArrayList<PointInTable> list : lists){
            PointInTable lastPoint = list.get(list.size()-1);
            if(mainTable[lastPoint.x][lastPoint.y].getValue() != 0){
                return true;
            }
        }
        return false;
    }
}

