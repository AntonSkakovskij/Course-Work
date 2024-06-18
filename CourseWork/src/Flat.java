import java.time.Year;
import java.util.Arrays;

public class Flat {
    //змінні класу Flat
    private String sSurname;
    private int iFlatNumber;
    private  ResourcesUse[][] ruFlatExpenses;



    //конструктор за замовчуванням класу Flat
    Flat(){
        sSurname = "Unknown";
        this.iFlatNumber = 0;
    }
    //конструктор з параметрами класу Flat
    Flat(int FlatNumber,int StartYear){
        sSurname = "Unknown";
        this.iFlatNumber = FlatNumber;
        ruFlatExpenses = new ResourcesUse[Year.now().getValue() - StartYear + 1][12];
        for (int i = 0; i < Year.now().getValue() - StartYear + 1; i++) {
            for (int j = 0; j < 12; j++) {
                ruFlatExpenses[i][j] = new ResourcesUse();
            }
        }
    }
    //конструктор копій класу Flat
    public Flat(Flat fFlatInfo) {
        this.iFlatNumber = fFlatInfo.getFlatNumber();
        this.sSurname = fFlatInfo.sSurname;

        this.ruFlatExpenses = new ResourcesUse[fFlatInfo.ruFlatExpenses.length][];
        for (int i = 0; i < fFlatInfo.ruFlatExpenses.length; i++) {
            this.ruFlatExpenses[i] = new ResourcesUse[fFlatInfo.ruFlatExpenses[i].length];
            for (int j = 0; j < fFlatInfo.ruFlatExpenses[i].length; j++) {
                this.ruFlatExpenses[i][j] = new ResourcesUse(fFlatInfo.ruFlatExpenses[i][j]);
            }
        }
    }


    //геттери класу Flat
    public String getSurname(){
        return this.sSurname;
    }
    public int getFlatNumber() {
        return this.iFlatNumber;
    }
    public ResourcesUse getFlatExpenses(int iYear, int iMonth){
        return this.ruFlatExpenses[iYear][iMonth];
    }

    //сеттери класу Flat
    public void setSurname(String Surname) {
        this.sSurname = Surname;
    }
    public void setFlatExpenses(int iYear, int iMonth, double WaterUse, double LightUse) {
        this.ruFlatExpenses[iYear][iMonth].setWaterUse(WaterUse);
        this.ruFlatExpenses[iYear][iMonth].setLightUse(LightUse);
    }




    //функція для підрахунку витрат за місяць
    public double[] calculateMonthExpenses(int iYear,int iMonth,  ResourcesPrice rpPriceForYear) {
        if(rpPriceForYear.getLightPrice() == 0 || rpPriceForYear.getWaterPrice() == 0){
            throw new RuntimeException("You haven't entered prices for year");
        }
        double waterExpenses = -1;
        double lightExpenses = -1;

        if (ruFlatExpenses[iYear][iMonth].getWaterUse() != -1 && ruFlatExpenses[iYear][iMonth].getLightUse() != -1) {
            waterExpenses = ruFlatExpenses[iYear][iMonth].getWaterUse() * rpPriceForYear.getWaterPrice();
            lightExpenses = ruFlatExpenses[iYear][iMonth].getLightUse() * rpPriceForYear.getLightPrice();
        }

        return new double[]{waterExpenses, lightExpenses};
    }
    //функція для підрахунку витрат за рік
    public double[] calculateYearExpenses(int iYear, ResourcesPrice rpPriceForYear) {
        double waterYearExpenses = 0;
        double lightYearExpenses = 0;
        int iSkipCount = 0;

        for (int i = 0; i < 12; i++) {
            double[] monthExpenses = calculateMonthExpenses(iYear, i, rpPriceForYear);
            if(monthExpenses[0] == -1 && monthExpenses[1] == -1){
                iSkipCount++;
                continue;
            }
            waterYearExpenses += monthExpenses[0];
            lightYearExpenses += monthExpenses[1];
        }
        if (iSkipCount == 12){
            return new double[]{-1, -1};
        }

        return new double[]{waterYearExpenses, lightYearExpenses};
    }
    //функція для підрахунку витрат за всі роки
    public double[] calculateTotalExpenses(ResourcesPrice[] rpYearsPrices) {
        double totalWaterExpenses = 0;
        double totalLightExpenses = 0;
        int iSkipCount = 0;

        for (int i = 0; i < ruFlatExpenses.length; i++) {
            double[] yearExpenses = calculateYearExpenses(i, rpYearsPrices[i]);

            if(yearExpenses[0]==-1 && yearExpenses[1] == -1){
                iSkipCount++;
                continue;
            }
            totalWaterExpenses += yearExpenses[0];
            totalLightExpenses += yearExpenses[1];
        }
        if (iSkipCount == ruFlatExpenses.length){
            return new double[]{-1, -1};
        }
        return new double[]{totalWaterExpenses, totalLightExpenses};

    }

    @Override
    public String toString() {
        return "Flat {" +
                "sSurname='" + sSurname + '\'' +
                ", iFlatNumber=" + iFlatNumber +
                ", ruFlatExpenses=" + Arrays.toString(ruFlatExpenses) +
                '}';
    }

    //функція для перевірки чи заповнена інформація для даного місяця
    public boolean isMonthFilled(int iYear, int iMonth) {

        ResourcesUse flatExpenses = this.getFlatExpenses(iYear, iMonth);
        return flatExpenses.getWaterUse() != -1 && flatExpenses.getLightUse() != -1;
    }



}