import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;

public class ACOPanel extends JPanel {

    int cityNum = 10;
    int time = 100;

    public ACOPanel(){
        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                super.keyTyped(e);
                char c = e.getKeyChar();
                if (c == 'q'){
                    time+=100;
                    repaint();
                }else if (c == 'z'){
                    ACOPanel.super.setVisible(false);
                    new mainPanel();
                }
            }
        });
        this.setFocusable(true);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        try {
            g.setFont(new Font("Consolas", Font.BOLD, 20));
            g.drawString("Press 'Q' to next step", 30, 500);
            g.drawString("Press 'Z' to return", 30, 540);
            g.drawString("(See parameters in source code)", 30, 580);

            ACO aco = new ACO(cityNum, 10, time, 1.f, 5.f, 0.5f);
            g.drawString("Current Iteration(default +100):"+ time, 600, 500);
            aco.init("data.txt");
            aco.solve();
            int[] bestTour = aco.getBestTour();
            int bestLength= aco.getBestLength();
            g.drawString("Best Length:"+bestLength, 600, 540);

            StringBuilder sb=new StringBuilder();
            for (int i = 0; i < cityNum; i++) {
                sb.append(bestTour[i]+"->");
            }
            sb.append("0");
            sb.delete(sb.length(), sb.length());
            g.drawString("Best Route:"+sb.toString(), 600, 570);

            g.setColor(Color.RED);
            // 读取数据
            int[] x;
            int[] y;

            String strbuff;
            BufferedReader data = new BufferedReader(new InputStreamReader(
                    new FileInputStream("data.txt")));

            x = new int[cityNum];
            y = new int[cityNum];
            for (int i = 0; i < cityNum; i++) {
                strbuff = data.readLine();
                String[] strcol = strbuff.split(" ");
                x[i] = Integer.valueOf(strcol[1]);// x
                y[i] = Integer.valueOf(strcol[2]);// y
                g.fillOval(x[i] / 10, y[i] / 10, 10, 10);
                g.drawString(String.valueOf(i), x[i] / 10, y[i] / 10);
            }
            data.close();

            g.setColor(Color.BLACK);
            for(int j=0;j<cityNum-1;j++)
            {
                g.drawLine(x[bestTour[j]]/ 10, y[bestTour[j]]/ 10,
                        x[bestTour[j+1]]/ 10, y[bestTour[j+1]]/ 10);
            }

            g.setColor(Color.BLUE);
            g.fillOval(x[bestTour[0]]/ 10, y[bestTour[0]]/ 10, 10, 10);
            g.fillOval(x[bestTour[cityNum-1]]/ 10, y[bestTour[cityNum-1]]/ 10, 10, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) throws {
//        JFrame f = new JFrame();
//        f.setResizable(false);
//        f.setTitle("ACO");
//        f.getContentPane().add(new ACOPanel());
//        f.setSize(1000, 640);
//        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        f.setVisible(true);
//    }
}
