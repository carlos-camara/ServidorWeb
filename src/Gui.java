import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
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
	private ButtonGroup group;
	private JTextArea display;
	

	public Gui() {
		initUI();
	}

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
		frame.setBounds(200, 200, 500, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(0, 2, 0, 0));

		// JPanel interaction is inside the JFrame
		JPanel interaction = new JPanel();
		frame.getContentPane().add(interaction);
		interaction.setLayout(new BoxLayout(interaction, BoxLayout.Y_AXIS));

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
		// To make space
		panelPort.add(Box.createRigidArea(new Dimension(26, 0)));
		JLabel laberPort = new JLabel("Port:");
		panelPort.add(laberPort);

		textFieldPort = new JTextField();
		textFieldPort.setText("8080");
		panelPort.add(textFieldPort);
		textFieldPort.setColumns(10);

		// JPanel panelchoice is inside the interaction Jpanel
		JPanel panelchoice = new JPanel();
		interaction.add(panelchoice);

		JRadioButton JRadioButtonHost = new JRadioButton("Host");
		JRadioButtonHost.setActionCommand("Host");
		JRadioButtonHost.setSelected(true);
		panelchoice.add(JRadioButtonHost);

		panelchoice.add(Box.createRigidArea(new Dimension(10, 0)));

		JRadioButton JRadioButtonGuest = new JRadioButton("Guest");
		JRadioButtonGuest.setActionCommand("Guest");
		panelchoice.add(JRadioButtonGuest);

		// Group the radio buttons.
		group = new ButtonGroup();
		group.add(JRadioButtonHost);
		group.add(JRadioButtonGuest);

		// JPanel panelConnect is inside the interaction Jpanel
		JPanel panelConnect = new JPanel();
		interaction.add(panelConnect);

		btnConnect = new JButton("Connect");
		btnConnect.addActionListener(this);
		panelConnect.add(btnConnect);

		btnDisconnect = new JButton("Disconnect");
		btnDisconnect.addActionListener(this);
		panelConnect.add(btnDisconnect);

		// JLabel lblonline is inside the interaction Jpanel
		JLabel lblonline = new JLabel("offline");
		interaction.add(lblonline);

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

		 display = new JTextArea(16, 58);
		display.setEditable(false); // set textArea non-editable
		JScrollPane scroll = new JScrollPane(display);
		scroll.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panelAreaText.add(scroll);
		panelAreaText.add(Box.createRigidArea(new Dimension(5, 0)));
		Receiver.add(Box.createRigidArea(new Dimension(0, 16)));

		frame.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnConnect && group.getSelection().getActionCommand().equals("Host")) {
			Server server = new Server(
					(textFieldPort.getText().equals("")) ? Integer.parseInt(textFieldPort.getText()) : 8080,
					(textFieldPort.getText().equals("")) ? textFieldHost.getText() : "localhost",this);
			server.start();
		
		} else if (e.getSource() == btnConnect && group.getSelection().getActionCommand().equals("Host")) {
			System.out.println("Modo guest");
		} else {
			// Close the app
			System.exit(0);
		}
	}

	public JTextArea getDisplay() {
		return display;
	}
	
	
}
