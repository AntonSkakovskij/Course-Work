import javax.swing.*;

public class ChooseFunction extends JPanel{
    //елементи інтерфейсу (змінні класу ChooseFunction)
    private JButton jbOutputFile;
    private JButton jbDisplaySavers;
    private JButton jbDisplayRichPerson;
    private JTextField tfWaterPrice;
    private JTextField tfLightPrice;
    private JButton jbSetPrice;
    private JButton jbShowNotFilledInfo;
    private JButton jbMonthsExpences;
    private JComboBox cmbYear;
    private JButton jbClearInfo;
    private JPanel Function;


    //геттери класу ChooseFunction

    public JButton getJbMonthsExpences() {
        return jbMonthsExpences;
    }
    public JButton getJbClearInfo() {
        return jbClearInfo;
    }
    public JTextField getTfWaterPrice() {
        return tfWaterPrice;
    }
    public JTextField getTfLightPrice() {
        return tfLightPrice;
    }
    public JButton getJbDisplaySavers() {
        return jbDisplaySavers;
    }
    public JButton getJbDisplayRichPerson() {
        return jbDisplayRichPerson;
    }
    public JButton getJbSetPrice() {
        return jbSetPrice;
    }
    public JButton getJbShowNotFilledInfo() {
        return jbShowNotFilledInfo;
    }
    public JButton getJbOutputFile() {return jbOutputFile;}
    public JComboBox getCmbYear() {
        return cmbYear;
    }


    //сеттери класу ChooseFunction
    public void setCmbYear(int[] iYears) {
        for (int i = 0; i < iYears.length; i++) {
            this.cmbYear.addItem(iYears[i]);
        }
    }

}
