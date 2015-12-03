package uk.ac.ucl.nterreri.GUI;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.swing.SwingConstants;

import uk.ac.ucl.nterreri.task3.Patient;

public class EditorPicChooser extends JDialog implements ActionListener {
	
	private Patient patient;
	private JPanel contentPane;
	private final JLabel lblHowWouldYou = new JLabel("How would you like to choose the picture?");
	private JButton btnCancel;
	private JButton btnFromUrl;
	private JButton btnFromFile;

	void method() {
		CloseableHttpClient client = HttpClients.createDefault();
		//HttpGet httpget = new HttpGet("http://localhost/");
		HttpHead head = new HttpHead("http://www.bigshocking.com/wp-content/uploads/2014/10/Stare-at-a-Random-Person.jpg");
		CloseableHttpResponse response;
		try {
			response = client.execute(head);
			HttpEntity entity = response.getEntity();
			System.out.println(entity.getContentType());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public EditorPicChooser(PatientEditorFrame editorFrame, Patient patient) {
		super(editorFrame);
		this.patient = patient;
		
		method();
		
		setBounds(editorFrame.tabbedPane.getX(), editorFrame.tabbedPane.getY() + 150, 
				378, editorFrame.tabbedPane.getY() + 80);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		
		lblHowWouldYou.setHorizontalAlignment(SwingConstants.CENTER);
		
		btnFromFile = new JButton("From file");
		btnFromFile.addActionListener(this);
		
		btnFromUrl = new JButton("From URL");
		btnFromUrl.addActionListener(this);
		
		btnCancel = new JButton("Cancel");
		GroupLayout groupLayout = new GroupLayout(getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblHowWouldYou, GroupLayout.DEFAULT_SIZE, 375, Short.MAX_VALUE))
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(30)
							.addComponent(btnFromFile)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(btnFromUrl)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnCancel)))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addComponent(lblHowWouldYou, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED, 70, Short.MAX_VALUE)
					.addGroup(groupLayout.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnFromFile)
						.addComponent(btnFromUrl)
						.addComponent(btnCancel))
					.addContainerGap())
		);
		getContentPane().setLayout(groupLayout);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnCancel) {
			this.dispose();
		} else if(e.getSource() == btnFromUrl) {
			
			while(true)
			{
				try {
					
					String buffer = JOptionPane.showInputDialog(this, "Enter the URL:"); /*<-returns string*/
					
					//this step is necessary because otherwise the program would loop where the user presses cancel
					if(buffer != null)
						patient.setPatientPicture(buffer);
					
					//break loop if no exception is thrown or if user cancels input
					break;
				} catch (MalformedURLException excp) {
					
					//if exception is thrown, feedback is displayed and user asked for input again, user can cancel input insertion at any time
					JOptionPane.showMessageDialog(this, "Invalid URL entered", "Invalid URL", JOptionPane.ERROR_MESSAGE);
				}
			}
			//dispose of dialog when done
			this.dispose();
		} else if(e.getSource() == btnFromFile) {
			JFileChooser fc = new JFileChooser(/*add param for current directory?*/);
			fc.showOpenDialog(this);
			
			try {
				patient.setPatientPicture("file://" + fc.getSelectedFile()); //need to precede absolute path with file protocol:
				this.dispose();
				
			} catch (IOException excp) {
				JOptionPane.showMessageDialog(this, "Failed to open picture file", "Error", JOptionPane.ERROR_MESSAGE);
				
			}
			
		}
		
	}
}
