import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.File;
import java.time.Year;
import java.util.ArrayList;
import java.util.Vector;

public class MainApplicationWindow extends JFrame {

    //функціональні елементи
    final private int iStartYear;
    final private int iFlatsNumber;
    private final Building bApartment;

    //елементи інтерфейсу (змінні класу MainApplicationWindow)
    private JTable jtFlatShowInfo;
    private final DefaultTableModel dtbTableInfo;
    private JPanel MainApplicationWindow;
    private JPanel jpControlItem;
    private AddFlatInfo AddInfoItem;
    private ChooseFunction FunctionsManager;
    private JComboBox cmbTableYear;
    private JComboBox cmbTableMonth;
    private JComboBox cmbFunctionMenu;
    private final String sFuncOutput = "Q:\\Anton\\Education\\Kyrsova\\CourseWork\\FuncOutput.txt";
    private JPanel ControlBlock;

    //конструктор класу MainApplicationWindow
    MainApplicationWindow(int FlatsNumber, int StartYear){


        //Перевизначення методу toString() для основного класу
        Flat flat = new Flat();
        System.out.println(flat);

        this.iFlatsNumber= FlatsNumber;
        this.iStartYear= StartYear;
        bApartment = new Building(iFlatsNumber,iStartYear);

        //відцентрування вікна
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(900, 625);
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);


        //дефолтні налаштування вікна
        this.setContentPane(this.MainApplicationWindow);
        this.setTitle("Flat consumption manager");
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);


        //Заповнення комбобоксів
        int[] iYears = new int[Year.now().getValue() - iStartYear + 1];
        for (int i = 0; i < iYears.length; i++) {
            iYears[i] = iStartYear + i;
        }

        for (int iYear : iYears) {
            cmbTableYear.addItem(iYear);
        }


        //Вказання змісту comboBox для блоку AddInfoItem

        AddInfoItem.setCmbYear(iYears);

        AddInfoItem.setCmbFlatNumber(iFlatsNumber);

        //Вказання змісту comboBox для блоку FunctionManager

        FunctionsManager.setCmbYear(iYears);

        //створення основної таблиці
        String[] sColumnsNames = {"№","Surname","Water use","Light use"};
        dtbTableInfo = new DefaultTableModel(ApplicationFunc.SetTableInfo(bApartment,cmbTableYear.getSelectedIndex(), cmbTableMonth.getSelectedIndex()),sColumnsNames){
            @Override
            public boolean isCellEditable(int row, int column) {
                //all cells false
                return false;
            }
        };
        jtFlatShowInfo.setModel(dtbTableInfo);

        //listener для обрання типу меню
        cmbFunctionMenu.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                CardLayout cl = (CardLayout) jpControlItem.getLayout();
                if(cmbFunctionMenu.getSelectedIndex() == 0) {
                    cl.show(jpControlItem, "Card1");
                } else if (cmbFunctionMenu.getSelectedIndex() == 1) {
                    cl.show(jpControlItem, "Card2");
                }
            }
        });

        //listener для cканування з файлу
        AddInfoItem.getJbReadFromFile().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try{
                    final JFileChooser fc = new JFileChooser("Q:\\Anton\\Education\\Kyrsova\\CourseWork\\files\\");
                    fc.showOpenDialog(null);

                    File selFile = fc.getSelectedFile();
                    if (selFile == null) {
                        throw new RuntimeException("Сhoose file");
                    }

                    String sFileName = selFile.getName();
                    String[] fileNameParts = sFileName.split("\\.");

                    int iCurInputYear = Integer.parseInt(fileNameParts[0]);

                    if (iCurInputYear < iStartYear || iCurInputYear >Year.now().getValue()){
                        throw new RuntimeException("Selected file isn`t included to accounting period");
                    }

                    ApplicationFunc.ReadFlatsInfoFromFile(bApartment,iCurInputYear - iStartYear, selFile);

                    AddInfoItem.getTfSurname().setText(bApartment.getSurname(AddInfoItem.getCmbFlatNumber().getSelectedIndex()));
                    if(AddInfoItem.getCmbMonth().getSelectedIndex() == cmbTableMonth.getSelectedIndex() &&
                        AddInfoItem.getCmbYear().getSelectedIndex() == cmbTableYear.getSelectedIndex()){
                        ApplicationFunc.UpdateTextFields(bApartment,
                                                            AddInfoItem.getCmbFlatNumber().getSelectedIndex(),
                                                            cmbTableYear.getSelectedIndex(),
                                                            cmbTableMonth.getSelectedIndex(),
                                                            AddInfoItem.getTfWaterUse(),
                                                            AddInfoItem.getTfLightUse()
                                                            );
                    }

                    //заповнення полів з ціною та перемикання комбобоксу на рік зчитування
                    ResourcesPrice rpYearPrice = bApartment.getYearPrices(iCurInputYear - iStartYear);
                    FunctionsManager.getCmbYear().setSelectedItem(iCurInputYear);
                    FunctionsManager.getTfWaterPrice().setText(String.valueOf(rpYearPrice.getWaterPrice()));
                    FunctionsManager.getTfLightPrice().setText(String.valueOf(rpYearPrice.getLightPrice()));


                    //оновлення таблиці
                    cmbTableYear.setSelectedItem(iCurInputYear);
                    dtbTableInfo.setDataVector(ApplicationFunc.SetTableInfo(bApartment,cmbTableYear.getSelectedIndex(),cmbTableMonth.getSelectedIndex()),sColumnsNames);

                    //вивід у файл

                    String sLogBody = "File Selected: " + selFile.getName() + "\n" +
                            "Input Year: " + iCurInputYear + "\n" +
                            "Data Successfully Read and Updated\n";

                    ApplicationFunc.OutputToLogFile(sFuncOutput,"ReadFromFile",sLogBody);

                }
                catch (NumberFormatException exc) {
                    JOptionPane.showMessageDialog(null,"Selected file is incorrect or contains incorrect data","Warning:\\",JOptionPane.WARNING_MESSAGE);
                }
                catch (RuntimeException exc){
                    JOptionPane.showMessageDialog(null,exc.getMessage(),"Warning:\\",JOptionPane.WARNING_MESSAGE);
                }
                catch(Exception exc){
                    JOptionPane.showMessageDialog(null,exc.getMessage(),"Error:(",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        //listener для додавання витрат
        AddInfoItem.getJbAddInfo().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{

                    // перевірки на коректність вводу
                    if(AddInfoItem.getTfSurname().getText().equals("")){
                        throw  new RuntimeException("Enter surname");
                    }
                    if(AddInfoItem.getTfWaterUse().getText().equals("")){
                        throw  new RuntimeException("Enter water use value");
                    }
                    if(AddInfoItem.getTfLightUse().getText().equals("")){
                        throw  new RuntimeException("Enter light use value");
                    }
                    String surname = AddInfoItem.getTfSurname().getText();

                    surname = surname.replaceAll("\\s", "");
                    String regex = "^[a-zA-Z]+$";

                    if (!surname.matches(regex)) {
                        throw new RuntimeException("Enter a valid surname\nEnglish letters only");
                    }

                    double waterUse = Double.parseDouble(AddInfoItem.getTfWaterUse().getText());

                    double lightUse = Double.parseDouble(AddInfoItem.getTfLightUse().getText());

                    // збереження даних
                    bApartment.setFlatExpences(AddInfoItem.getCmbFlatNumber().getSelectedIndex(),
                                                AddInfoItem.getCmbYear().getSelectedIndex(),
                                                AddInfoItem.getCmbMonth().getSelectedIndex(),
                                                waterUse,
                                                lightUse);

                    bApartment.setSurname(AddInfoItem.getCmbFlatNumber().getSelectedIndex(),
                                            surname);

                    //перемикання комбобоксів року та місяця  на зміну інформації
                    cmbTableYear.setSelectedItem(AddInfoItem.getCmbYear().getSelectedItem());
                    cmbTableMonth.setSelectedItem(AddInfoItem.getCmbMonth().getSelectedItem());

                    //оновлення таблиці
                    dtbTableInfo.setDataVector(ApplicationFunc.SetTableInfo(bApartment,cmbTableYear.getSelectedIndex(),cmbTableMonth.getSelectedIndex()),sColumnsNames);


                    //вивід у файл

                    String sLogBody = "Surname: " + AddInfoItem.getTfSurname().getText() + "\n" +
                            "Water Use: " + AddInfoItem.getTfWaterUse().getText() + "\n" +
                            "Light Use: " + AddInfoItem.getTfLightUse().getText() + "\n" +
                            "Flat Number: " + (AddInfoItem.getCmbFlatNumber().getSelectedIndex()+1) + "\n" +
                            "Year: " + AddInfoItem.getCmbYear().getSelectedItem() + "\n" +
                            "Month: " + AddInfoItem.getCmbMonth().getSelectedItem() + "\n" +
                            "Processed: Data successfully added\n";
                    ApplicationFunc.OutputToLogFile(sFuncOutput,"AddInformation",sLogBody);

                }
                catch (NumberFormatException exc) {
                    JOptionPane.showMessageDialog(null,"You entered wrong value of water or light use","Warning:\\",JOptionPane.WARNING_MESSAGE);
                }
                catch (RuntimeException exc){
                    JOptionPane.showMessageDialog(null,exc.getMessage(),"Warning:\\",JOptionPane.WARNING_MESSAGE);
                }
                catch(Exception exc){
                    JOptionPane.showMessageDialog(null,exc.getMessage(),"Error:(",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        //listener для оновлення полів вводу витрат та прізвища при перемиканні комбобоксу номера квартири
        AddInfoItem.getCmbFlatNumber().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int iSelectedFlat = AddInfoItem.getCmbFlatNumber().getSelectedIndex();
                String SurnameOfSelectedFlat = bApartment.getSurname(iSelectedFlat);
                if(!SurnameOfSelectedFlat.equals("Unknown")){
                    AddInfoItem.getTfSurname().setText(SurnameOfSelectedFlat);
                }
                else{
                    AddInfoItem.getTfSurname().setText("");
                }
                int iSelectedMonth = AddInfoItem.getCmbMonth().getSelectedIndex();
                int iSelectedYear = AddInfoItem.getCmbYear().getSelectedIndex();

                ApplicationFunc.UpdateTextFields(bApartment,iSelectedFlat,iSelectedYear, iSelectedMonth, AddInfoItem.getTfWaterUse(),AddInfoItem.getTfLightUse());
            }
        });
        //listener для оновлення полів вводу витрат та прізвища при перемиканні комбобоксу року
        AddInfoItem.getCmbYear().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int iSelectedFlat = AddInfoItem.getCmbFlatNumber().getSelectedIndex();
                int iSelectedMonth = AddInfoItem.getCmbMonth().getSelectedIndex();
                int iSelectedYear = AddInfoItem.getCmbYear().getSelectedIndex();

                ApplicationFunc.UpdateTextFields(bApartment,iSelectedFlat,iSelectedYear, iSelectedMonth, AddInfoItem.getTfWaterUse(),AddInfoItem.getTfLightUse());
            }
        });
        //listener для оновлення полів вводу витрат та прізвища при перемиканні комбобоксу місяця
        AddInfoItem.getCmbMonth().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int iSelectedFlat = AddInfoItem.getCmbFlatNumber().getSelectedIndex();
                int iSelectedMonth = AddInfoItem.getCmbMonth().getSelectedIndex();
                int iSelectedYear = AddInfoItem.getCmbYear().getSelectedIndex();

                ApplicationFunc.UpdateTextFields(bApartment,iSelectedFlat,iSelectedYear, iSelectedMonth, AddInfoItem.getTfWaterUse(),AddInfoItem.getTfLightUse());
            }
        });

        //listener для виводу номеру квартири, яка спожила найбільше послуг в грошовому еквіваленті
        FunctionsManager.getJbDisplayRichPerson().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    ArrayList<Object[]> maxFlatInfoList = bApartment.findFlatsWithHighestExpenses();

                    if (!maxFlatInfoList.isEmpty()) {
                        StringBuilder message = new StringBuilder("Flats with the highest expenses:\n");

                        double waterExpenses = 0;
                        double lightExpenses = 0;

                        for (Object[] flatInfo : maxFlatInfoList) {
                            int flatNumber = (int) flatInfo[0];
                            waterExpenses = (double) flatInfo[1];
                            lightExpenses = (double) flatInfo[2];

                            message.append("Flat ").append(flatNumber)
                                    .append(String.format(", Water Expenses: %.2f",waterExpenses))
                                    .append(String.format(", Light Expenses: %.2f",lightExpenses)).append("\n");
                        }
                        message.append(String.format("Total expences: %.2f\n",waterExpenses + lightExpenses));

                        JOptionPane.showMessageDialog(null, message.toString(), "Highest Expenses", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(null, "No flats found with expenses.", "Highest Expenses", JOptionPane.INFORMATION_MESSAGE);
                    }
                    //вивід у файл

                    String sLogBody = "Flats with the highest expenses:\n";

                    double totalWaterExpenses = 0;
                    double totalLightExpenses = 0;

                    for (Object[] flatInfo : maxFlatInfoList) {
                        int flatNumber = (int) flatInfo[0];
                        double waterExpenses = (double) flatInfo[1];
                        double lightExpenses = (double) flatInfo[2];

                        sLogBody += String.format("Flat %d, Water Expenses: %.2f, Light Expenses: %.2f\n", flatNumber, waterExpenses, lightExpenses);

                        totalWaterExpenses += waterExpenses;
                        totalLightExpenses += lightExpenses;
                    }

                    sLogBody += String.format("Total expenses: %.2f\n", totalWaterExpenses + totalLightExpenses);

                    ApplicationFunc.OutputToLogFile(sFuncOutput,"DisplayRichPerson",sLogBody);
                }
                catch (RuntimeException exc){
                    JOptionPane.showMessageDialog(null,exc.getMessage(),"Warning:\\",JOptionPane.WARNING_MESSAGE);
                }
                catch(Exception exc){
                    JOptionPane.showMessageDialog(null,exc.getMessage(),"Error:(",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        //listener для виводу інформації за обраний рік у файл
        FunctionsManager.getJbOutputFile().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int iSelectedYear = (int)FunctionsManager.getCmbYear().getSelectedItem();
                ApplicationFunc.OutputFlatsInfoToFile(bApartment,iSelectedYear,iStartYear);

                //вивід у файл

                String sLogBody = "Flat information successfully output to file";

                ApplicationFunc.OutputToLogFile(sFuncOutput,"OutputToFile",sLogBody);

            }
        });
        //listener для встановлення ціни в межах року
        FunctionsManager.getJbSetPrice().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try{
                    if(FunctionsManager.getTfWaterPrice().getText().equals("")){
                        throw  new RuntimeException("Enter water price value");
                    }
                    if(FunctionsManager.getTfLightPrice().getText().equals("")){
                        throw  new RuntimeException("Enter light price value");
                    }
                    double waterPrice = Double.parseDouble(FunctionsManager.getTfWaterPrice().getText());
                    double lightPrice = Double.parseDouble(FunctionsManager.getTfLightPrice().getText());


                    bApartment.setYearPrices(FunctionsManager.getCmbYear().getSelectedIndex(), waterPrice, lightPrice);
                    JOptionPane.showMessageDialog(null,"Price successfully set");

                    //вивід у файл
                    String sLogBody = String.format("Water price set to %.2f, Light price set to %.2f", waterPrice, lightPrice);

                    ApplicationFunc.OutputToLogFile(sFuncOutput, "SetPrice", sLogBody);
                }
                catch (NumberFormatException exc) {
                    JOptionPane.showMessageDialog(null,"You entered wrong value of water or light price","Warning:\\",JOptionPane.WARNING_MESSAGE);
                }
                catch (RuntimeException exc){
                    JOptionPane.showMessageDialog(null,exc.getMessage(),"Warning:\\",JOptionPane.WARNING_MESSAGE);
                }
                catch(Exception exc){
                    JOptionPane.showMessageDialog(null,exc.getMessage(),"Error:(",JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        //listener для оновлення полів для вказання ціни
        FunctionsManager.getCmbYear().addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int iSelectedYear = FunctionsManager.getCmbYear().getSelectedIndex();
                ResourcesPrice rpWLPrise = bApartment.getYearPrices(iSelectedYear);

                if(!(rpWLPrise.getLightPrice() == 0 || rpWLPrise.getWaterPrice() == 0)){
                    FunctionsManager.getTfLightPrice().setText(String.valueOf(rpWLPrise.getLightPrice()));
                    FunctionsManager.getTfWaterPrice().setText(String.valueOf(rpWLPrise.getWaterPrice()));
                }
                else {
                    FunctionsManager.getTfLightPrice().setText("");
                    FunctionsManager.getTfWaterPrice().setText("");
                }
            }
        });
        //listener для відображення місяців для яких інформація не заповнена
        FunctionsManager.getJbShowNotFilledInfo().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int iSelectedYear = FunctionsManager.getCmbYear().getSelectedIndex();

                    Vector result[] = bApartment.OutputNotFilledMonths(iSelectedYear);

                    String sLogBody = "Not filled info for selected year:\n";
                    String[] ShowInfoColumns= {"Month","Not filled info"};
                    Object[][] oTableData = new Object[12][2];

                    //заповнення інформації для таблиці
                    for (int i = 0; i < oTableData.length; i++) {
                        sLogBody += String.format("Month: %s, Not filled info:", cmbTableMonth.getItemAt(i));

                        oTableData[i][0] = cmbTableMonth.getItemAt(i);
                        oTableData[i][1] = "";
                        if (-1 == (int)result[i].get(0)) {
                            sLogBody +="for each flats";
                            oTableData[i][1] = "for each flats";
                        }
                        else if(0 == (int)result[i].get(0)) {
                            sLogBody +="for any flats";
                            oTableData[i][1] = "for any flats";
                        }
                        else{
                            for (int j = 0; j < result[i].size(); j++) {
                                oTableData[i][1] += String.valueOf(result[i].get(j))+" ";
                                sLogBody += String.valueOf(result[i].get(j))+" " ;
                            }
                        }
                        sLogBody += "\n";
                    }

                    DefaultTableModel dtbShowInfo = new DefaultTableModel(oTableData,ShowInfoColumns){
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            //all cells false
                            return false;
                        }
                    };
                    ShowInfo showInfo = new ShowInfo(dtbShowInfo,1);


                    //зміна кольорів елементів табиці відповідно до їх вмісту
                    DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
                        @Override
                        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                            Object column1Value = table.getValueAt(row, 1);

                            // Змінити фон та текст в залежності від значень
                            if (column1Value instanceof String) {
                                String notFilledInfo = (String) column1Value;

                                // Змінити колір фону в залежності від значень
                                if ("for each flats".equals(notFilledInfo)) {
                                    c.setBackground(new Color(205, 92, 92));
                                    c.setForeground(Color.BLACK);
                                } else if ("for any flats".equals(notFilledInfo)) {
                                    c.setBackground(new Color(60, 179, 113));
                                    c.setForeground(Color.BLACK);
                                } else {
                                    c.setBackground(new Color(240, 230, 140));
                                    c.setForeground(Color.BLACK);
                                }
                            }
                            return c;

                        }
                    };
                    // Задаємо рендерер для стовпця
                    showInfo.getJtInfo().getColumnModel().getColumn(1).setCellRenderer(renderer);

                    //вивід у файл


                    ApplicationFunc.OutputToLogFile(sFuncOutput, "ShowNotFilledInfo", sLogBody);

                }
                catch(Exception exc){
                    JOptionPane.showMessageDialog(null,exc.getMessage(),"Error:(",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        //listener для відображення жителів , які спожили найменше послуг за рік відсортованих за прізвищем
        FunctionsManager.getJbDisplaySavers().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    int iSelectedYear = FunctionsManager.getCmbYear().getSelectedIndex();


                    //вивід вікна для вибору к-сті людей для відображення
                    String[] sChoosePeople = new String[iFlatsNumber];
                    for (int i = 0; i < iFlatsNumber; i++) {
                        sChoosePeople[i] = "" + (i + 1) + " people";
                    }
                    JComboBox cmbChoosePeople = new JComboBox(sChoosePeople);

                    int iAnswer = JOptionPane.showConfirmDialog(null, cmbChoosePeople, "How many people you want to display: ", JOptionPane.DEFAULT_OPTION);


                    int iSeversCount = cmbChoosePeople.getSelectedIndex() + 1;




                    if (iAnswer == JOptionPane.OK_OPTION) {

                        String sLogBody = "Displayed savers information:\n";
                        Vector <Integer> vflatNumbers = bApartment.getSavers(iSeversCount, iSelectedYear);
                        int[] iFlatNumbers = new int[vflatNumbers.size()];
                        String[] sSurname = new String[iSeversCount];
                        Object[][] oTableData = new Object[vflatNumbers.size()][4];
                        for (int i = 0; i < iSeversCount; i++) {
                            iFlatNumbers[i] = vflatNumbers.elementAt(i);
                            sSurname[i] = bApartment.getSurname(vflatNumbers.elementAt(i));
                        }


                        ApplicationFunc.shellSort(sSurname, iFlatNumbers);


                        //заповнення даних таблиці
                        for (int i = 0; i < vflatNumbers.size(); i++) {

                            oTableData[i][0] = sSurname[i];
                            double[] FlatExpences = bApartment.getFlatYearExpences(iFlatNumbers[i], iSelectedYear);
                            sLogBody += String.format("Surname: %s, Water price: %.2f, Light price: %.2f, Total price: %.2f\n", sSurname[i], FlatExpences[0], FlatExpences[1], FlatExpences[0] + FlatExpences[1]);
                            oTableData[i][1] = String.format("%.2f", FlatExpences[0]);
                            oTableData[i][2] = String.format("%.2f", FlatExpences[1]);
                            oTableData[i][3] = String.format("%.2f", FlatExpences[0] + FlatExpences[1]);
                        }
                        String[] ShowInfoColumns = {"Surname", "Water price", "Light price", "Total price"};

                        DefaultTableModel dtbShowInfo = new DefaultTableModel(oTableData, ShowInfoColumns){
                            @Override
                            public boolean isCellEditable(int row, int column) {
                                //all cells false
                                return false;
                            }
                        };
                        new ShowInfo(dtbShowInfo, 2);
                        if(vflatNumbers.size() < iSeversCount){
                            throw new RuntimeException(String.format("You have only %d entered flat usage, not %d",vflatNumbers.size(), iSeversCount));
                        }
                        //вивід у файл


                        ApplicationFunc.OutputToLogFile(sFuncOutput, "DisplaySavers", sLogBody);
                    }

                }
                catch (RuntimeException exc){
                    JOptionPane.showMessageDialog(null,exc.getMessage(),"Warning:\\",JOptionPane.WARNING_MESSAGE);
                }
                catch(Exception exc){
                    JOptionPane.showMessageDialog(null,exc.getMessage(),"Error:(",JOptionPane.ERROR_MESSAGE);
                }

            }
        });
        //listener для відображення агрегованих витрат помісячно
        FunctionsManager.getJbMonthsExpences().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                try{
                    int iSelectedYear = FunctionsManager.getCmbYear().getSelectedIndex();

                    String sLogBody = "Calculated monthly expenses for the selected year:\n";

                    double[][] MonthsExpences = bApartment.calculateMonthExpences(iSelectedYear);

                    String[] ShowInfoColumns = {"Month", "Water Expences", "Light Expences", "Total Expences"};
                    Object[][] oTableData = new Object[12][4];

                    //заповнення інформації для таблиці
                    for (int i = 0; i < oTableData.length; i++) {

                        oTableData[i][0] = cmbTableMonth.getItemAt(i);
                        if(MonthsExpences[i][0] != -1 || MonthsExpences[i][1] != -1) {
                            oTableData[i][1] = String.format("%.2f", MonthsExpences[i][0]);
                            oTableData[i][2] = String.format("%.2f", MonthsExpences[i][1]);
                            oTableData[i][3] = String.format("%.2f", MonthsExpences[i][0] + MonthsExpences[i][1]);
                            sLogBody += String.format("Month: %s, Water Expenses: %.2f, Light Expenses: %.2f, Total Expenses: %.2f\n",
                                    cmbTableMonth.getItemAt(i), MonthsExpences[i][0], MonthsExpences[i][1], MonthsExpences[i][0] + MonthsExpences[i][1]);
                        }
                        else{
                            oTableData[i][1] = "-";
                            oTableData[i][2] = "-";
                            oTableData[i][3] = "not filled usage";
                            sLogBody += String.format("Month: %s, Water Expenses: -, Light Expenses: -, Total Expenses: Not filled usage\n",cmbTableMonth.getItemAt(i));
                        }
                    }

                    DefaultTableModel dtbShowInfo = new DefaultTableModel(oTableData, ShowInfoColumns){
                        @Override
                        public boolean isCellEditable(int row, int column) {
                            //all cells false
                            return false;
                        }
                    };
                    ShowInfo showInfo = new ShowInfo(dtbShowInfo, 3);
                    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
                    centerRenderer.setHorizontalAlignment( JLabel.CENTER );
                    showInfo.getJtInfo().getColumnModel().getColumn(1).setCellRenderer( centerRenderer );
                    showInfo.getJtInfo().getColumnModel().getColumn(2).setCellRenderer( centerRenderer );

                    //вивід у файл

                    ApplicationFunc.OutputToLogFile(sFuncOutput, "MonthsExpences", sLogBody);
                }
                catch (RuntimeException exc){
                    JOptionPane.showMessageDialog(null,exc.getMessage(),"Warning:\\",JOptionPane.WARNING_MESSAGE);
                }
                catch(Exception exc){
                    JOptionPane.showMessageDialog(null,exc.getMessage(),"Error:(",JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        //listener для очищення всіх даних
        FunctionsManager.getJbClearInfo().addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bApartment.ClearInfo();

                //оновлення таблиці
                dtbTableInfo.setDataVector(ApplicationFunc.SetTableInfo(bApartment,cmbTableYear.getSelectedIndex(),cmbTableMonth.getSelectedIndex()),sColumnsNames);

                //оновлення полів вводу
                FunctionsManager.getTfWaterPrice().setText("");
                FunctionsManager.getTfLightPrice().setText("");
                AddInfoItem.getTfSurname().setText("");
                AddInfoItem.getTfWaterUse().setText("");
                AddInfoItem.getTfLightUse().setText("");

                JOptionPane.showMessageDialog(null,"Data successfully cleared");
                //вивід у файл
                String sLogBody = "Data successfully cleared\n";

                ApplicationFunc.OutputToLogFile(sFuncOutput, "MonthsExpences", sLogBody);
            }
        });
        //listener для зміни таблиці відповідно до вибору комбобоксу року
        cmbTableYear.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int iSelectedYear= cmbTableYear.getSelectedIndex();
                int iSelectedMonth = cmbTableMonth.getSelectedIndex();

                dtbTableInfo.setDataVector(ApplicationFunc.SetTableInfo(bApartment,iSelectedYear,iSelectedMonth),sColumnsNames);
            }
        });
        //listener для зміни таблиці відповідно до вибору комбобоксу місяця
        cmbTableMonth.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                int iSelectedYear= cmbTableYear.getSelectedIndex();
                int iSelectedMonth = cmbTableMonth.getSelectedIndex();

                dtbTableInfo.setDataVector(ApplicationFunc.SetTableInfo(bApartment,iSelectedYear,iSelectedMonth),sColumnsNames);
            }
        });
    }
}
