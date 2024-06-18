import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class ShowInfo extends JFrame{
    //елементи інтерфейсу (змінні класу ShowInfo)
    private JTable jtInfo;
    private JPanel panel1;



    //геттери класу ShowInfo
    public JTable getJtInfo() {
        return jtInfo;
    }



    //конструктор класу ShowInfo
    ShowInfo(DefaultTableModel tableModel,int iExecType){
        //відцентрування вікна при виводі на екран
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(600, 460);
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);

        // дефолтні налаштування вікна
        if(iExecType == 1) {
            this.setTitle("Months info");
        }
        else if (iExecType == 2) {
            this.setTitle("Severs info");
        }
        else if (iExecType == 3) {
            this.setTitle("Month expences");
            this.setSize(700, 460);
        }
        this.setContentPane(panel1);
        this.setVisible(true);

        jtInfo.setModel(tableModel);
    }
}
