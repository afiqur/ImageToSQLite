import java.awt.EventQueue;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JDesktopPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.awt.event.ActionEvent;

public class Image extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JLabel label;
	JDesktopPane desktopPane;

	String ss;
	byte[] image_detail = null;
	private ImageIcon format = null;
	String filename = null;
	int s = 0;
	byte[] image = null;
	AbstractButton path;

	/**
	 * Launch the application.
	 */

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Image frame = new Image();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */

	Connection conn = null;
	private JButton btnAdd;
	private JButton btnShowImage;

	public Image() {

		conn = SqliteConnection.dbConnector();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		desktopPane = new JDesktopPane();
		desktopPane.setBounds(20, 11, 200, 140);
		contentPane.add(desktopPane);

		label = new JLabel("");
		label.setBounds(0, 0, 200, 140);
		desktopPane.add(label);

		JButton btnSelectImage = new JButton("Select Image");
		btnSelectImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
				FileNameExtensionFilter filter = new FileNameExtensionFilter("*.IMAGE", "jpg", "gif", "png");
				fileChooser.addChoosableFileFilter(filter);
				int result = fileChooser.showSaveDialog(null);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					try {
						@SuppressWarnings("resource")
						FileInputStream fis = new FileInputStream(selectedFile);
						ByteArrayOutputStream bos = new ByteArrayOutputStream();

						byte[] buf = new byte[1024];
						for (int readNum; (readNum = fis.read(buf)) != -1;) {
							bos.write(buf, 0, readNum);
						}
						image_detail = bos.toByteArray();
					} catch (FileNotFoundException e1) {
						e1.printStackTrace();
					} catch (IOException e1) {
						e1.printStackTrace();
					}

				} else if (result == JFileChooser.CANCEL_OPTION) {
					System.out.println("No Data");
				}

				btnSelectImage.setText(filename);
			}
		});
		btnSelectImage.setForeground(Color.DARK_GRAY);
		btnSelectImage.setFont(new Font("Tahoma", Font.ITALIC, 14));
		btnSelectImage.setBounds(256, 23, 130, 43);
		contentPane.add(btnSelectImage);

		btnAdd = new JButton("Insert");
		btnAdd.setFont(new Font("Tahoma", Font.PLAIN, 13));
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					String query = " insert into ImageTable (Image) values (?)";
					PreparedStatement pst = conn.prepareStatement(query);

					pst.setBytes(1, image_detail);

					pst.execute();

					JOptionPane.showMessageDialog(null, "Image Inserted");

					pst.close();

				} catch (Exception es) {
					es.printStackTrace();
				}
			}
		});
		btnAdd.setBounds(277, 77, 89, 23);
		contentPane.add(btnAdd);

		btnShowImage = new JButton("Show Image");
		btnShowImage.setFont(new Font("Sylfaen", Font.PLAIN, 14));
		btnShowImage.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				try {
					String query = "select * from ImageTable Where rowid=1";
					PreparedStatement pst = conn.prepareStatement(query);
					ResultSet rs = pst.executeQuery();

					while (rs.next()) {
						byte[] imagedata = rs.getBytes("Image");
						format = new ImageIcon(imagedata);
						label.setIcon(format);
					}
					pst.close();
				} catch (Exception ef) {
					ef.printStackTrace();
				}
			}
		});

		btnShowImage.setBounds(256, 111, 130, 25);
		contentPane.add(btnShowImage);

		JLabel lblImageSize = new JLabel("Image Size : 200*140");
		lblImageSize.setFont(new Font("Tahoma", Font.PLAIN, 13));
		lblImageSize.setBounds(30, 159, 139, 23);
		contentPane.add(lblImageSize);
	}
}
