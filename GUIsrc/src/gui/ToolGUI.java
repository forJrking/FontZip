package gui;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import util.FontGenerateManager;

public class ToolGUI extends JFrame implements MouseListener {

    /**
         * 
         */
    private static final long serialVersionUID = 1L;

    public static void main(String[] args) {
        new ToolGUI();
    }

    private String sourceFontFile;

    private String generateFontFile;

    private String endWith;

    private JFrame frame;

    private JTextArea viewArea;

    private JLabel viewField, result;

    private JButton button;

    private JTextField fontName;

    public ToolGUI() {
        // 窗体
        frame = new JFrame("字体压缩神器 By forjrking");
        frame.setResizable(false);
        // 文字区域
        viewArea = new JTextArea(15, 35);// 可提取的字数

        viewArea.setText("需要提取的文字");

        viewArea.setFont(new Font("微软雅黑", Font.BOLD, 14));

        fontName = new JTextField();
        button = new JButton("OK");
        viewField = new JLabel("选择源字体");
        result = new JLabel("提醒请勿使用fontmix做字体文件名");
        result.setFont(new Font("微软雅黑", Font.BOLD | Font.ITALIC, 16));
        fontName.setColumns(25);

        JPanel panel = new JPanel();

        panel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
        panel.add(viewField);
        panel.add(fontName);
        panel.add(button);

        // 文字区域的拉动条
        JScrollPane sp = new JScrollPane(viewArea);
        sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        frame.add("South", result);
        panel.add(sp);
        frame.add("Center", panel);
        frame.setSize(580, 430);
        center(frame);
        frame.setVisible(true);

        button.addMouseListener((MouseListener) this);
        fontName.addMouseListener((MouseListener) this);

    }

    void center(Window w) {
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension d = toolkit.getScreenSize();
        int x = (d.width - w.getWidth()) / 2;
        int y = (d.height - w.getHeight()) / 2;
        w.setLocation(x, y);
    }

    public void mouseClicked(MouseEvent evt) {

        if (evt.getSource() == button) {
            if (isNotEmpty(viewArea.getText()) && isNotEmpty(fontName.getText())) {
                // System.out.println("源文件" + sourceFontFile + "  生成文件名" +
                // generateFontFile);
                FontGenerateManager.generateFontByContent(viewArea.getText(), sourceFontFile, generateFontFile);
                result.setText("结果：提取成功，请在字体统计目录查看");
            } else {
                result.setText("结果：请选择源字体并且添加提取的字");
            }
        } else if (evt.getSource() == fontName) {
            fileChooser();
        }
    }

    public boolean isNotEmpty(String src) {
        return src != null && !src.equals("");
    }

    public void fileChooser() {
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(".ttf&.ttc", "TTC", "TTF");
        // 设置文件类型
        chooser.setFileFilter(filter);
        // 打开选择器面板
        int returnVal = chooser.showOpenDialog(new JPanel());
        // 保存文件从这里入手，输出的是文件名
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            // chooser.getSelectedFile().getAbsolutePath());
            fontName.setText(chooser.getSelectedFile().getName());
            // 绝对路径
            sourceFontFile = chooser.getSelectedFile().getAbsolutePath();
            endWith = getExtName(chooser.getSelectedFile().getName(), '.');

            String string = sourceFontFile.replace(chooser.getSelectedFile().getName(), "");
            createGenerateFontFile(string);
        }
    }

    private void createGenerateFontFile(String string) {
        string = string.toLowerCase();
        generateFontFile = string + "fontmix" + endWith;
    }

    private String getExtName(String s, char split) {
        int i = s.indexOf(split);
        int leg = s.length();
        return (i > 0 ? (i + 1) == leg ? " " : s.substring(i, s.length()) : " ");
    }

    public void mousePressed(MouseEvent evt) {
    }

    public void mouseReleased(MouseEvent evt) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

}
