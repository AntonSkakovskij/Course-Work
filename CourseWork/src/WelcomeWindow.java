import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.Year;

public class WelcomeWindow extends JFrame {
    //елементи інтерфейсу (змінні класу WelcomeWindow)
    private JTextField tfFlatsNumber;
    private JTextField tfStartYear;
    private JButton jbStart;
    private JPanel WelcomePanel;

    //конструктор класу WelcomeWindow
    WelcomeWindow(){
        //відцентрування вікна при виводі на екран
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        this.setSize(330, 300);
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);

        // дефолтні налаштування вікна
        this.setContentPane(this.WelcomePanel);
        this.setTitle("Welcome");
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);


        // listener для початку роботи
        jbStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    int iFlatsNumber = Integer.parseInt(tfFlatsNumber.getText());

                    if(!(iFlatsNumber > 1)){
                        throw new RuntimeException("Number of flats must be > 1");
                    }

                    int iStartYear = Integer.parseInt(tfStartYear.getText());

                    if(!(iStartYear > 0 && iStartYear <= Year.now().getValue())){
                        throw new RuntimeException("Start year must be between the first and current year");
                    }

                    new MainApplicationWindow(iFlatsNumber, iStartYear);
                    dispose();
                }
                catch (NumberFormatException nfe) {
                    JOptionPane.showMessageDialog(null,"Enter correct values","Warning:\\",JOptionPane.WARNING_MESSAGE);
                }
                catch (RuntimeException exc){
                    JOptionPane.showMessageDialog(null,exc.getMessage(),"Warning:\\",JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
    public static void main(String[] args){
        new WelcomeWindow();
    }
}

