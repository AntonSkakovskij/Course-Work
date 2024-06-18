import java.time.Year;
import java.util.*;

public class Building{
    //змінні класу Building
    private final Flat[] fFlats;
    private final ResourcesPrice[] rpYearsPrices;


    // конструктор з параметрами класу Building
    public Building(int iFlatCount, int iStartYear){

        fFlats = new Flat[iFlatCount];

        for (int i = 0; i < iFlatCount; i++) {
            fFlats[i] = new Flat(i + 1, iStartYear);
        }


        rpYearsPrices = new ResourcesPrice[Year.now().getValue() - iStartYear + 1];

        for (int i = 0; i < rpYearsPrices.length; i++) {
            rpYearsPrices[i] = new ResourcesPrice();
        }
    }
    // коструктор копіювання класу Building
    public Building(Building buildingToCopy) {
        this.fFlats = new Flat[buildingToCopy.fFlats.length];
        for (int i = 0; i < buildingToCopy.fFlats.length; i++) {
            this.fFlats[i] = new Flat(buildingToCopy.fFlats[i]);
        }

        this.rpYearsPrices = new ResourcesPrice[buildingToCopy.rpYearsPrices.length];
        for (int i = 0; i < buildingToCopy.rpYearsPrices.length; i++) {
            this.rpYearsPrices[i] = new ResourcesPrice(buildingToCopy.rpYearsPrices[i]);
        }
    }
    //сеттери класу Building
    public void setFlatExpences(int iFlatNumber, int iYear, int iMonth, double waterUse, double lightUse){
        fFlats[iFlatNumber].setFlatExpenses(iYear, iMonth, waterUse,lightUse);
    }
    public void setYearPrices(int iYear, double waterPrice, double lightPrice){
        rpYearsPrices[iYear].setWaterPrice(waterPrice);
        rpYearsPrices[iYear].setLightPrice(lightPrice);
    }
    void setSurname(int iFlatNumber, String sSurname){
        fFlats[iFlatNumber].setSurname(sSurname);
    }

    //геттери класу Building
    public ResourcesUse getFlatExpences(int iFlatNumber,int iYear, int iMonth){
        return fFlats[iFlatNumber].getFlatExpenses(iYear,iMonth);
    }
    public ResourcesPrice getYearPrices(int iYear){
        return rpYearsPrices[iYear].getYearPrices();
    }
    public int getNumberOfFlats(){
        return fFlats.length;
    }
    String getSurname(int iFlatNumber){
        return fFlats[iFlatNumber].getSurname();
    }


    //функція для визначення витрат квартири за певний рік
    public double[] getFlatYearExpences(int iFlatNumber,int iYear){
        double[] FlatYearExpenses = fFlats[iFlatNumber].calculateYearExpenses(iYear,rpYearsPrices[iYear]);
        return new double[]{FlatYearExpenses[0],FlatYearExpenses[1]};
    }

    //функція для знаходження квартири з найбільшими витратами за всі роки
    public ArrayList<Object[]> findFlatsWithHighestExpenses() {

        for (int i = 0; i < rpYearsPrices.length; i++) {
            if (rpYearsPrices[i].getWaterPrice() == 0 || rpYearsPrices[i].getLightPrice() == 0) {
                throw new RuntimeException("You haven't entered prices for all years");
            }
        }

        double maxExpenses = -1;
        ArrayList<Object[]> maxFlatInfo = new ArrayList<>();

        for (int i = 0; i < fFlats.length; i++) {
            double[] totalExpenses = fFlats[i].calculateTotalExpenses(rpYearsPrices);
            if(totalExpenses[0] == -1 && totalExpenses[1] == -1){
                throw new RuntimeException("You have not filled information");
            }
            double waterExpenses = totalExpenses[0];
            double lightExpenses = totalExpenses[1];

            if (waterExpenses + lightExpenses > maxExpenses) {
                maxExpenses = waterExpenses + lightExpenses;
                maxFlatInfo.clear();
                maxFlatInfo.add(new Object[]{fFlats[i].getFlatNumber(), waterExpenses, lightExpenses});
            } else if (waterExpenses + lightExpenses == maxExpenses) {
                maxFlatInfo.add(new Object[]{fFlats[i].getFlatNumber(), waterExpenses, lightExpenses});
            }
        }
        return maxFlatInfo;
    }
    //функція для виводу інформація про заповненість інфрмацією місяців
    public Vector<Integer>[] OutputNotFilledMonths(int year) {
        Vector<Integer>[] vMonthInfo = new Vector[12];

        for (int i = 0; i < 12; i++) {
            Vector<Integer> vUnfilledFlats = new Vector<>();

            for (int j = 0; j < fFlats.length; j++) {
                if (!fFlats[j].isMonthFilled(year, i)) {
                    vUnfilledFlats.add(j + 1);
                }
            }

            // Інформація заповнена для всіх квартир
            if (vUnfilledFlats.isEmpty()) {
                vUnfilledFlats.add(0);
            }
            // Інформація не заповнена для всіх квартир
            else if (vUnfilledFlats.size() == fFlats.length) {
                vUnfilledFlats.clear();
                vUnfilledFlats.add(-1);
            }

            vMonthInfo[i] = vUnfilledFlats;
        }
        return vMonthInfo;
    }
    //функція для знаходження квартир з найменшими витратами
    public Vector<Integer> getSavers(int iFlatCount, int iYear) {
        int len = fFlats.length;
        Vector<Double> iFlatsExpenses = new Vector<>();

        Vector<Integer> iSortedFlatNumberList = new Vector<>();
        for (int i = 0; i < len; i++) {

            double[] totalExpenses = fFlats[i].calculateYearExpenses(iYear, rpYearsPrices[iYear]);
            if(totalExpenses[0] != -1 && totalExpenses[1] !=-1) {
                iSortedFlatNumberList.add(i);
                iFlatsExpenses.add(totalExpenses[0] + totalExpenses[1]);
            }
        }
        if (iSortedFlatNumberList.isEmpty()){
            throw new RuntimeException("You haven`t entered usage for all flats");
        }
        Vector<Integer> result;

        if (iSortedFlatNumberList.size()< iFlatCount){
            result = new Vector<>(iSortedFlatNumberList.subList(0, iSortedFlatNumberList.size()));
        }
        else {
            result = new Vector<>(iSortedFlatNumberList.subList(0, iFlatCount));
        }

        int iResultLen = result.size();
        for (int i = 0; i < iResultLen - 1; i++) {
            for (int j = i + 1; j < iResultLen; j++) {
                if (iFlatsExpenses.elementAt(i) > iFlatsExpenses.elementAt(j)) {

                    double tempExpenses = iFlatsExpenses.elementAt(i);
                    iFlatsExpenses.setElementAt(iFlatsExpenses.elementAt(j), i);
                    iFlatsExpenses.setElementAt(tempExpenses, j);

                    int tempFlatIndex = iSortedFlatNumberList.elementAt(i);
                    iSortedFlatNumberList.setElementAt(iSortedFlatNumberList.elementAt(j), i);
                    iSortedFlatNumberList.setElementAt(tempFlatIndex, j);
                }
            }
        }

        return result;
    }
    //функція для очищення всіх даних
    public void ClearInfo(){

        for (int i = 0; i < rpYearsPrices.length; i++) {
            rpYearsPrices[i].setYearPrices(0,0);
        }
        for (int i = 0; i <fFlats.length ; i++) {
            fFlats[i].setSurname("Unknown");
            for (int j = 0; j < rpYearsPrices.length; j++) {
                for (int k = 0; k < 12; k++) {
                    fFlats[i].setFlatExpenses(j, k, -1 ,-1);
                }
            }
        }

    }
    //функція для підрахунку  агрегованих витрат помісячно
    public double[][] calculateMonthExpences(int iYear){
        double[][] result = new double[12][2];

        double[] totalMonthExpences = new double[2];
        totalMonthExpences[0]= -1;
        totalMonthExpences[1]= -1;
        int iFlag =0;
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < fFlats.length; j++) {
                double[] FlatExpences =  fFlats[j].calculateMonthExpenses(iYear, i, rpYearsPrices[iYear]);
                if(FlatExpences[0] != -1 && FlatExpences[1] != -1){
                    iFlag++;
                    if(iFlag == 1){
                        totalMonthExpences[0] = 0;
                        totalMonthExpences[1] = 0;
                    }
                    totalMonthExpences[0] += FlatExpences[0];
                    totalMonthExpences[1] += FlatExpences[1];
                }
            }
            result[i][0] = totalMonthExpences[0];
            result[i][1] = totalMonthExpences[1];
            totalMonthExpences[0]= -1;
            totalMonthExpences[1]= -1;
        }

        return result;
    }
}
