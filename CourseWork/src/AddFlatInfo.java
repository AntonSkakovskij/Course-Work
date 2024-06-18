import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;


public class AddFlatInfo extends JPanel{
    //елементи інтерфейсу (змінні класу AddFlatInfo)
    private JComboBox cmbYear;
    private JComboBox cmbMonth;
    private JTextField tfSurname;
    private JComboBox cmbFlatNumber;
    private JTextField tfWaterUse;
    private JTextField tfLightUse;
    private JButton jbAddInfo;
    private JButton jbReadFromFile;
    private JPanel AddInfo;


    //геттери класу AddFlatInfo
    public JButton getJbReadFromFile() {
        return jbReadFromFile;
    }
    public JButton getJbAddInfo() {
        return jbAddInfo;
    }
    public JComboBox getCmbYear() {
        return cmbYear;
    }
    public JComboBox getCmbMonth() {
        return cmbMonth;
    }
    public JTextField getTfSurname() {
        return tfSurname;
    }
    public JComboBox getCmbFlatNumber() {
        return cmbFlatNumber;
    }
    public JTextField getTfWaterUse() {
        return tfWaterUse;
    }
    public JTextField getTfLightUse() {
        return tfLightUse;
    }


    //сеттери класу AddFlatInfo
    public void setCmbYear(int[] iYears) {
        for (int i = 0; i < iYears.length; i++) {
            this.cmbYear.addItem(iYears[i]);
        }
    }
    public void setCmbFlatNumber(int iFlatsNumber) {
        for (int i = 0; i < iFlatsNumber; i++) {
            cmbFlatNumber.addItem("Flat №"+(i+1));
        }
    }

}
