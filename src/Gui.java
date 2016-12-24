import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

/**
 * Author: Carlos Cámara
 */
public class Gui implements ActionListener {

	private JTextField textFieldHost, textFieldPort;
	private JFrame frame;
	private JButton btnConnect, btnDisconnect;
	private JTextArea display;
	private JLabel lblonline;
	private Server server;

	public Gui() {
		initUI();
	}

	/**
	 * Create the gui of the app
	 */
	void initUI() {
		// LookAndFeel
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
			// If Nimbus is not available, you can set the GUI to another look
			// and feel.
		}

		// Jframe
		frame = new JFrame();
		frame.setTitle("OTH Control Panel");
		frame.getContentPane().setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		frame.setBounds(200, 200, 400, 400);
		// This line will center the window on the screen
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// The ImageIcon is used to create the icon
		ImageIcon webIcon = new ImageIcon("github-octocat.png");
		frame.setIconImage(webIcon.getImage());
		frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

		// JPanel interaction is inside the JFrame
		JPanel interaction = new JPanel();
		frame.getContentPane().add(interaction);
		interaction.setLayout(new BoxLayout(interaction, BoxLayout.X_AXIS));

		// JPanel panelHost is inside the interaction Jpanel
		JPanel panelHost = new JPanel();
		interaction.add(panelHost);
		// To make space
		panelHost.add(Box.createRigidArea(new Dimension(5, 0)));

		JLabel labelHost = new JLabel("Host Ip:");
		panelHost.add(labelHost);

		textFieldHost = new JTextField();
		textFieldHost.setText("localhost");
		panelHost.add(textFieldHost);
		textFieldHost.setColumns(10);

		// JPanel panelPort is inside the interaction Jpanel
		JPanel panelPort = new JPanel();
		interaction.add(panelPort);
		JLabel laberPort = new JLabel("Port:");
		panelPort.add(laberPort);

		textFieldPort = new JTextField();
		textFieldPort.setText("8080");
		panelPort.add(textFieldPort);
		textFieldPort.setColumns(10);

		// JPanel Receiver is inside the JFrame
		JPanel Receiver = new JPanel();
		frame.getContentPane().add(Receiver);
		Receiver.setLayout(new BoxLayout(Receiver, BoxLayout.Y_AXIS));
		// To make space
		Receiver.add(Box.createRigidArea(new Dimension(0, 6)));
		// JPanel Receiver is inside the Receiver Jpanel
		JPanel panelAreaText = new JPanel();
		Receiver.add(panelAreaText);
		panelAreaText.setLayout(new BoxLayout(panelAreaText, BoxLayout.X_AXIS));

		panelAreaText.add(Box.createRigidArea(new Dimension(10, 0)));
		display = new JTextArea(16, 58);
		display.setEditable(false); // set textArea non-editable
		JScrollPane scroll = new JScrollPane(display);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panelAreaText.add(scroll);
		panelAreaText.add(Box.createRigidArea(new Dimension(10, 0)));
		Receiver.add(Box.createRigidArea(new Dimension(0, 16)));

		// JPanel panelConnect is inside the interaction Jpanel
		JPanel panelConnect = new JPanel();
		Receiver.add(panelConnect);

		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(this);
		panelConnect.add(btnConnect);

		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(this);
		panelConnect.add(btnDisconnect);
		// JPanel panelLabelConnection is inside the interaction Jpanel
		JPanel panelLabelConnection = new JPanel();
		Receiver.add(panelLabelConnection);
		panelLabelConnection.setLayout(new BoxLayout(panelLabelConnection, BoxLayout.X_AXIS));

		Component rigidArea = Box.createRigidArea(new Dimension(340, 0));
		panelLabelConnection.add(rigidArea);

		// JLabel lblonline is inside the interaction Jpanel
		lblonline = new JLabel("Offline");
		panelLabelConnection.add(lblonline);
		lblonline.setForeground(Color.red);

		// Put the frame visible
		frame.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnConnect) {
			String auxiliaryHost = (textFieldPort.getText().equals("")) ? textFieldHost.getText() : "localhost";
			display.append("Starting server in " + auxiliaryHost + "\n\n");
			// Start the server
			server = new Server((textFieldPort.getText().equals("")) ? Integer.parseInt(textFieldPort.getText()) : 8080,
					auxiliaryHost, this);
			server.start();
			lblonline.setText("Online");
			lblonline.setForeground(Color.green);
		} else {
			lblonline.setText("Offline");
			lblonline.setForeground(Color.RED);
			display.setText("");
			server.interrupt();
		}
	}

	public JTextArea getDisplay() {
		return display;
	}

}
