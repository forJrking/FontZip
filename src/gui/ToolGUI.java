package gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import util.FontGenerateManager;

public class ToolGUI implements MouseListener {

	public static void main(String[] args) {
		new ToolGUI();
	}

	private String sourceFontFile;

	private String generateFontFile;

	private String endWith, path;

	private JFrame frame;

	private JTextArea viewArea;

	private JLabel fontField, outField, result;

	private JButton button;

	private JComboBox<String> outName;

	private JTextField fontName;

	public ToolGUI() {
		// 窗体
		frame = new JFrame("字体压缩神器2.0 By forjrking");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 文字区域
		viewArea = new JTextArea(12, 35);// 可提取的字数
		viewArea.setFont(new Font("微软雅黑", Font.BOLD, 15));
		viewArea.setText("需要提取的文字");

		fontField = new JLabel("选择源字体:");
		fontName = new JTextField();
		fontName.setColumns(15);
		fontName.setEditable(false);
		fontName.setBackground(Color.WHITE);

		outField = new JLabel("输出文件类型:");
		String[] item = { ".TTF", ".TTC", ".OTF", ".WOFF", ".EOT" };
		outName = new JComboBox<String>(item);
		button = new JButton("OK");
		result = new JLabel("提醒:请勿使用fontzipMin做字体文件名", JLabel.CENTER);
		result.setFont(new Font("微软雅黑", Font.BOLD, 16));

		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));
		panel.add(fontField);
		panel.add(fontName);
		panel.add(outField);
		panel.add(outName);
		panel.add(button);

		// 文字区域的拉动条
		JScrollPane sp = new JScrollPane(viewArea);
		sp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

		frame.add("South", result);
		panel.add(sp);
		frame.add("Center", panel);
		frame.setSize(565, 365);
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
			if (isNotEmpty(viewArea.getText())
					&& isNotEmpty(fontName.getText())) {
				// System.out.println("源文件" + sourceFontFile + "  生成文件名" +
				// generateFontFile);
				createGenerateFontFile("fontzipMin");
				try {
					FontGenerateManager.generateFontByContent(
							viewArea.getText(), sourceFontFile,
							generateFontFile);

					result.setText("结果：提取成功，请在字体同级目录查看");
					result.setForeground(Color.BLUE);
				} catch (Exception e) {
					result.setText("结果：提取失败");
					result.setForeground(Color.RED);
					e.printStackTrace();
				}

			} else {
				result.setText("提示：请选择源字体并且添加提取的字");
				result.setForeground(Color.RED);
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
		FileNameExtensionFilter filter = new FileNameExtensionFilter("字体文件",
				"TTC", "TTF", "OTF", "WOFF", "EOT");
		// 设置文件类型
		chooser.setFileFilter(filter);
		// 打开选择器面板
		int returnVal = chooser.showOpenDialog(new JPanel());
		// 保存文件从这里入手，输出的是文件名
		if (returnVal == JFileChooser.APPROVE_OPTION) {

			fontName.setText(chooser.getSelectedFile().getName());
			// 绝对路径
			sourceFontFile = chooser.getSelectedFile().getAbsolutePath();
			endWith = getExtName(chooser.getSelectedFile().getName(), '.');
			path = sourceFontFile.replace(chooser.getSelectedFile().getName(),
					"");
			// 切换列表
			outName.setSelectedItem(endWith.toUpperCase());
		}
	}

	private void createGenerateFontFile(String name) {
		generateFontFile = path + name
				+ outName.getSelectedItem().toString().toLowerCase();
		;
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
