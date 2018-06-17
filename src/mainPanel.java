import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class mainPanel extends JFrame{
    JPanel splitePane = new JPanel();

    public mainPanel(){
        this.setSize(400, 400);

        initPane();
        this.setTitle("Algorithm Display");
        this.add(splitePane);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void initPane() {
        splitePane.setLayout(null);

        JTextArea text = new JTextArea();
        JLabel label = new JLabel("See/edit data in data.txt");
        label.setFont(new java.awt.Font("Consolas", 1, 20));
        label.setBounds(100,50,500,40);
        JButton button_ga = new JButton("GA");
        button_ga.setBounds(150,100,150,40);
        JButton button_aco = new JButton("ACO");
        button_aco.setBounds(150,150,150,40);
        JButton button_tabu = new JButton("TABU");
        button_tabu.setBounds(150,210,150,40);
        this.setVisible(true);

        splitePane.add(text);
        splitePane.add(label);
        splitePane.add(button_aco);
        splitePane.add(button_ga);
        splitePane.add(button_tabu);

//        text.append("GA:遗传算法求解TSP\n+"
//                + "ACO:蚁群算法求解TSP\n"
//                + "Tabu:禁忌搜索算法求解TSP\n");

        button_aco.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f = new JFrame();
                f.setResizable(false);
                f.setTitle("ACO to TSP");
                f.getContentPane().add(new ACOPanel());
                f.setSize(1200, 640);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setVisible(true);
            }
        });

        button_ga.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f = new JFrame();
                f.setResizable(false);
                f.setTitle("GA to TSP");
                f.getContentPane().add(new GAPanel());
                f.setSize(1200, 640);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setVisible(true);
            }
        });

        button_tabu.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame f = new JFrame();

                f.setResizable(false);
                f.setTitle("Tabu to TSP");
                f.getContentPane().add(new TabuPanel());
                f.setSize(1200, 640);
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setVisible(true);
            }
        });
    }

    public static void main(String args[]){
        new mainPanel();
    }
}
