import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.rmi.server.UID;
import javax.swing.*;

public class Main extends JFrame implements ActionListener{
    int pixel[][] = new int[50][50];
    int sample[][] = new int [50][50];



    void piexlReset(int arrayLength)
    {
        for (int i = 0;i<arrayLength;i++)
        {
            for(int j = 0;j<arrayLength;j++)
            {
                pixel[i][j] = 0;
            }
        }
    }

    class HandWriteCanvas extends Canvas implements MouseListener,MouseMotionListener
    {
        Point now;
        public Graphics g;
        Main mm;
        public HandWriteCanvas(Main mm)
        {
            super();
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
            this.mm = mm;
        }

        @Override
        public void paint(Graphics g) {
            super.paint(g);
        }

        @Override
        public void update(Graphics g) {
            super.update(g);
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            now = new Point(e.getX(),e.getY());
            piexlReset(50);
        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

        @Override
        public void mouseDragged(MouseEvent e) {
            now = new Point(e.getX(),e.getY());
            System.out.println(now.x);
            g = this.getGraphics();
            g.setColor(Color.WHITE);
            System.out.println(g);
            if(now!=null)
            {
                g.fillOval(now.x,now.y,20,20);
                g.setColor(Color.WHITE);
                int x = now.x,y = now.y;
                for (int i = x;i<x+20;i++)
                {
                    for(int j = y;j<y+20;j++)
                    {
                        pixel[i/15][j/15] = 1;
                    }
                }
            }


        }

        @Override
        public void mouseMoved(MouseEvent e) {

        }
    }

    public static void main(String[] args) {
        Main mm = new Main();
    }

    JTextField jtx;
    KNN knn;
    HandWriteCanvas canvas;
    JColorChooser colorChooser;

    public Main()
    {
        super("HCI");
        this.setSize(1000,800);
        this.setLayout(new FlowLayout());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        int width = this.getWidth();
        int height = this.getHeight();

        JMenuBar menuBar = new JMenuBar();
        JMenu menu1 = new JMenu("开始");
        JMenu menu2 = new JMenu("选择颜色");
        JMenu menu3 = new JMenu("橡皮");
        JMenuItem  menuItem1 = new JMenuItem("新建");
        JMenuItem  menuItem2 = new JMenuItem("保存");
        JMenuItem  menuItem3 = new JMenuItem("退出");
        JMenuItem  menuItem4 = new JMenuItem("选择颜色");
        menuItem1.addActionListener(this);
        menuItem2.addActionListener(this);
        menuItem3.addActionListener(this);
        menuItem4.addActionListener(this);
        menu1.add(menuItem1);
        menu1.add(menuItem2);
        menu1.addSeparator();
        menu1.add(menuItem3);
        menu2.add(menuItem4);
        menuBar.add(menu1);
        menuBar.add(menu2);
        menuBar.add(menu3);

//        JToolBar toolBar = new JToolBar();


        canvas = new HandWriteCanvas(this);
        canvas.setBackground(Color.BLACK);
        canvas.setBounds(0,50,1000,650);

        JButton btn1 = new JButton("保存");
        JButton btn2 = new JButton("识别");
        JButton btn3 = new JButton("清除");

        btn1.addActionListener(this);
        btn2.addActionListener(this);
        btn3.addActionListener(this);

        btn1.setSize(200,20);
        btn2.setSize(200,20);
        btn3.setSize(200,20);

        jtx = new JTextField(10);

        this.add(menuBar);
        this.add(canvas);
        this.add(btn1);
        this.add(btn2);
        this.add(btn3);
        this.add(jtx);
        this.setVisible(true);
        this.setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("保存")) {
            String selectedNumber=jtx.getText();
            UID id=new UID();
            String rootPath="D:\\save\\";
            String fileName=selectedNumber+"-"+id.hashCode();
            String absoluteFile=rootPath+fileName+".txt";
            File file=new File(absoluteFile);
            try {
                if(!file.exists())
                    file.createNewFile();
                FileWriter out = new FileWriter(file);
                for(int i=0;i<40;i++) {
                    for(int j=0;j<40;j++) {
                        out.write(pixel[i][j]+"");
                    }
                }
                out.flush();
                out.close();
            }catch(Exception e1) {
                e1.printStackTrace();
            }
            canvas.repaint();
        } else if (e.getActionCommand().equals("识别")) {
            // 识别结果；
            knn=new KNN(3);
            File fileDir=new File("D:\\save\\");
            String[] fileList=fileDir.list();
            for(int i=0;i<fileList.length;i++) {
                File file=new File("D:\\save\\"+fileList[i]);
                String name=file.getName().split("-")[0];
                FileReader in;
                try {
                    in = new FileReader(file);
                    for(int j=0;j<40;j++){
                        for(int k=0;k<40;k++) {
                            sample[j][k]=in.read()-'0';  //逐单位汉字/字母/数字读取
                        }
                    }
                    in.close();
                } catch (FileNotFoundException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                //开始进行 欧拉距离和KNN 运算；
                knn.sort(sample, pixel, 40, name);
            }
            JOptionPane.showMessageDialog(canvas,"预测结果为："+knn.predict());
            canvas.repaint();
        }
        else if (e.getActionCommand().equals("清除") ||e.getActionCommand().equals("新建"))
        {
            canvas.repaint();
        }
        else if (e.getActionCommand().equals("退出"))
        {
            System.out.println(e.getActionCommand());
            System.exit(0);
        }
        else if(e.getActionCommand().equals("选择颜色"))
        {
            colorChooser = new JColorChooser();
            Color color = JColorChooser.showDialog(this,"选择颜色",Color.black);
            System.out.println(this);
            canvas.setBackground(color);
        }
    }
}
