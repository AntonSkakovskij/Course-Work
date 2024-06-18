import javax.swing.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;


//абстрактний клас ApplicationFunc, що містить методи-функції необхідні для роботи програми
public abstract class ApplicationFunc {
    //функція для зчитування даних з файлу
    public static void ReadFlatsInfoFromFile(Building bApartment,int iYearIndex, File selFile){

        try {
            BufferedReader br = new BufferedReader(new FileReader(selFile));

            String firstLine = br.readLine();
            String[] prices = firstLine.split(" ");

            double waterPrice = Double.parseDouble(prices[0]);
            double lightPrice = Double.parseDouble(prices[1]);

            bApartment.setYearPrices(iYearIndex,waterPrice,lightPrice);

            for (int i = 0; i < bApartment.getNumberOfFlats() ; i++) {

                if(br.readLine() == null){
                    bApartment.ClearInfo();
                    throw new RuntimeException("Your file don`t contain enough info");
                }

                bApartment.setSurname(i, br.readLine());


                for (int j = 0; j < 12; j++) {

                    String line = br.readLine();
                    String[] values = line.split(" ");

                    double waterUse = Double.parseDouble(values[1]);
                    double lightUse = Double.parseDouble(values[2]);

                    bApartment.setFlatExpences(i, iYearIndex, j, waterUse, lightUse);
                }
            }

            /*if(br.readLine() != null){
                throw new RuntimeException("Your file contain more info that needed");
            }*/

            br.close();
            JOptionPane.showMessageDialog(null,"Information successfully read");
        }
        catch (RuntimeException exc){
            JOptionPane.showMessageDialog(null,exc.getMessage(),"Warning:\\",JOptionPane.WARNING_MESSAGE);
        }
        catch (Exception exc){
            JOptionPane.showMessageDialog(null, exc.getMessage(),"Error:(",JOptionPane.ERROR_MESSAGE);
        }
    }
    // функція для виводу в лог файл
    public static void OutputToLogFile(String sFilePath, String sFuncName, String sLogBody) {
        try (FileWriter fw = new FileWriter(sFilePath, true);
             PrintWriter pw = new PrintWriter(fw)) {

            // Отримуємо поточну дату та час
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            String dateTime = dateFormat.format(new Date());

            // Записуємо лог у файл
            pw.println("[" + dateTime + "] Назва функції: " + sFuncName + " {");
            pw.println("\n" + sLogBody);
            pw.println("}\n\n");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //функція для виводу даних у файл
    public static void OutputFlatsInfoToFile(Building bApartment,int iCurOutputYear, int iStartYear){
        try{
            String filePath = "Q:/Anton/Education/Kyrsova/CourseWork/" + iCurOutputYear + ".txt";
            File file = new File(filePath);

            FileWriter fwWrite = new FileWriter(file, false);


            ResourcesPrice rpYearPrice = bApartment.getYearPrices(iCurOutputYear - iStartYear);
            fwWrite.write(rpYearPrice.getWaterPrice() + " " +
                                rpYearPrice.getLightPrice() + "\n");


            for (int i = 0; i < bApartment.getNumberOfFlats(); i++) {

                fwWrite.write((i + 1) + "\n");
                fwWrite.write(bApartment.getSurname(i)+"\n");

                for (int j = 0; j < 12; j++) {

                    ResourcesUse ruFlatExpences = bApartment.getFlatExpences(i,iCurOutputYear - iStartYear,j);
                    fwWrite.write((j + 1) + " " +
                            ruFlatExpences.getWaterUse() + " " +
                            ruFlatExpences.getLightUse() + "\n");
                }
            }
            fwWrite.close();
            JOptionPane.showMessageDialog(null,"Information successfully saved");
        }
        catch (RuntimeException exc) {
            JOptionPane.showMessageDialog(null, exc.getMessage(), "Warning:\\", JOptionPane.WARNING_MESSAGE);
        }
        catch (Exception exc){
            JOptionPane.showMessageDialog(null, exc.getMessage(),"Error:(",JOptionPane.ERROR_MESSAGE);
        }
    }
    //функція для обновлення текстових полів при додаванні інформації
    public static void UpdateTextFields(Building bApartment,int flat, int year, int month,JTextField tfWaterUse, JTextField tfLightUse){

        ResourcesUse ruFlatUse = bApartment.getFlatExpences(flat,year,month);

        if(!(ruFlatUse.getLightUse() ==-1 || ruFlatUse.getWaterUse() == -1)){
            tfLightUse.setText(String.valueOf(ruFlatUse.getLightUse()));
            tfWaterUse.setText(String.valueOf(ruFlatUse.getWaterUse()));
        }
        else {
            tfLightUse.setText("");
            tfWaterUse.setText("");
        }
    }
    //функція для обновлення головної таблиці
    public static Object[][] SetTableInfo(Building bApartment, int year, int month){

        Object[][] oTableData = new Object[bApartment.getNumberOfFlats()][4];

        for (int i = 0; i < oTableData.length; i++) {

            oTableData[i][0] = i + 1;

            oTableData[i][1] = bApartment.getSurname(i);

            ResourcesUse FlatUse = bApartment.getFlatExpences(i, year, month);

            if(FlatUse.getWaterUse() == -1){
                oTableData[i][2] = "not filled";
            }
            else{
                oTableData[i][2] = FlatUse.getWaterUse();
            }

            if(FlatUse.getLightUse() == -1){
                oTableData[i][3] = "not filled";
            }
            else{
                oTableData[i][3] =  FlatUse.getLightUse();
            }
        }
        return oTableData;
    }
    //функція для порівняння стрічок
    private static int stringCompare(String sFirstString, String sSecondString) {
        int iMaxIteration = Math.min(sFirstString.length(), sSecondString.length());

        for (int i = 0; i < iMaxIteration; i++) {
            if (sFirstString.charAt(i) < sSecondString.charAt(i)) {
                return -1;
            } else if (sFirstString.charAt(i) > sSecondString.charAt(i)) {
                return 1;
            }
        }
        return Integer.compare(sFirstString.length(), sSecondString.length());
    }
    //функція для сортування прізвищ
    public static void shellSort(String[] sSurnames, int[] iFlatNumbers) {
        int n = sSurnames.length;

        for (int gap = n / 2; gap > 0; gap /= 2) {
            for (int i = gap; i < n; i++) {
                String tempSurname = sSurnames[i];
                int tempFlatNumber = iFlatNumbers[i];
                int j;

                for (j = i; j >= gap && stringCompare(sSurnames[j - gap], tempSurname) > 0; j -= gap) {
                    sSurnames[j] = sSurnames[j - gap];
                    iFlatNumbers[j] = iFlatNumbers[j - gap];
                }

                sSurnames[j] = tempSurname;
                iFlatNumbers[j] = tempFlatNumber;
            }
        }
    }
}
