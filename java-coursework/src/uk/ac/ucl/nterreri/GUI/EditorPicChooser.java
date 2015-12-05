package uk.ac.ucl.nterreri.GUI;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Event;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.charset.Charset;
import java.util.EventObject;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.SwingConstants;

import uk.ac.ucl.nterreri.task3.Patient;

public class EditorPicChooser extends JDialog implements ActionListener {

	private Patient patient;
	private PatientEditorFrame parentEditorFrame;
	private Component sourceComponent;
	private JPanel contentPane;
	private final JLabel lblHowWouldYou = new JLabel("How would you like to choose the picture?");
	private JButton btnCancel;
	private JButton btnFromUrl;
	private JButton btnFromFile;

	//experimented with apache http api to check if content was picture
	/*void method(String url) {
		CloseableHttpClient client = HttpClientBuilder.create().build();
		//HttpGet httpget = new HttpGet("http://localhost/");
		HttpHead head = new HttpHead(url);
		CloseableHttpResponse response;
		try {
			response = client.execute(head);
			System.out.println(response);
			HttpEntity entity = response.getEntity();
			ContentType contentType = ContentType.getOrDefault(entity);
	        String mimeType = contentType.getMimeType();
	        Charset charset = contentType.getCharset();

	        System.out.println("\nMimeType = " + mimeType);
	        System.out.println("Charset  = " + charset);

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		}


	}*/

	public EditorPicChooser(PatientEditorFrame parentEditorFrame, Component sourceComponent, Patient patient) {
		super(parentEditorFrame);
		this.patient = patient;
		this.sourceComponent = sourceComponent;
		this.parentEditorFrame = parentEditorFrame;

		setBounds(parentEditorFrame.tabbedPane.getX(), parentEditorFrame.tabbedPane.getY() + 150, 
				378, parentEditorFrame.tabbedPane.getY() + 80);
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
		btnCancel.addActionListener(this);
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

					String urlBuffer = JOptionPane.showInputDialog(this, "Enter the URL:"); /*<-returns user-input string*/

					if(urlBuffer != null) {
						checkExtensionType(urlBuffer);
						setPictureURL(urlBuffer);
					}

					//break loop if no exception is thrown or if user cancels input
					break;
				} catch (MalformedURLException excp) {
					//if exception is thrown, feedback is displayed and user asked for input again, user can cancel input insertion at any time
					JOptionPane.showMessageDialog(this, "Invalid URL entered", "Invalid URL", JOptionPane.ERROR_MESSAGE);
				} catch (Exception excp) {
					//generic exception is thrown by drawPicture() method, only if something goes wrong with the rendering
					JOptionPane.showMessageDialog(this, "Error rendering picture", "Error", JOptionPane.ERROR_MESSAGE);
					excp.printStackTrace();
				}
			}
			//dispose of dialog when done
			this.dispose();
		} else if(e.getSource() == btnFromFile) {
			JFileChooser fc = new JFileChooser(/*add param for current directory?*/);
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"Images", "gif", "jpg", "jpeg" ,"jpe" ,"ico" ,"pbm" ,"png" ,"pnm" ,"ppm" ,"sgi" ,"rgb" ,"rgba"
				+ "bw","tga","tiff","tif","bmp","xbm","icon","bitmap","xpm","pcc","pcx");
			fc.setFileFilter(filter);
			fc.showOpenDialog(this);

			try {
				setPictureFilepath("file://" + fc.getSelectedFile());
				//patient.setPatientPicture("file://" + fc.getSelectedFile()); //need to precede absolute path with file protocol:
				this.dispose();


			} catch (IOException excp) {
				JOptionPane.showMessageDialog(this, "Failed to open picture file", "Error", JOptionPane.ERROR_MESSAGE);

			} catch (Exception excp) {
				JOptionPane.showMessageDialog(this, "Error rendering picture", "Error", JOptionPane.ERROR_MESSAGE);

			}

		}

	}

	//TODO: http://stackoverflow.com/questions/3571223/how-do-i-get-the-file-extension-of-a-file-in-java
	private void checkExtensionType(String url) {
		String extension = "";
		int i = url.lastIndexOf('.');

		if (i > 0) 
			extension = url.substring(i+1);

		if(!extension.matches("gif|jpg|jpeg|jpe|ico|pbm|png|pnm|ppm|sgi|rgb|rgba"
				+ "|bw|tga|tiff|tif|bmp|xbm|icon|bitmap|xpm|pcc|pcx")) {
			this.dispose();
			JOptionPane.showMessageDialog(null, "Warning: file extension not recognized",
					"Warning", JOptionPane.WARNING_MESSAGE);

		}

	}

	private void setPictureURL(String urlBuffer) throws MalformedURLException, Exception {

		if(sourceComponent == parentEditorFrame.lblPatientPic) {
			patient.setPatientPictureURL(urlBuffer);
			parentEditorFrame.drawPicture(patient.getPatientPicture(), parentEditorFrame.lblPatientPic);
		} else if (sourceComponent  == parentEditorFrame.lblConditionPic) {
			patient.addConditionPicture(" " + urlBuffer);	//whitespace added for safety, see Patient.getConditionPictures()
			parentEditorFrame.drawPicture(patient.getConditionPictures()[patient.getConditionPictures().length - 1], 
					parentEditorFrame.lblConditionPic);
			//^ draw the picture at the last location specified in the condition pics string
			parentEditorFrame.comboBox.setModel(parentEditorFrame.fetchComboBoxModel());
			//^ refresh the combobox model:
			parentEditorFrame.comboBox.setSelectedIndex(parentEditorFrame.comboBox.getItemCount() - 1);
			//^ force selection on newly added element
		}
	}	
	private void setPictureFilepath(String filepath) throws IOException, Exception {

		if(sourceComponent == parentEditorFrame.lblPatientPic) {
			patient.setPatientPictureFilepath(filepath);
			parentEditorFrame.drawPicture(patient.getPatientPicture(), parentEditorFrame.lblPatientPic);
		} else if (sourceComponent == parentEditorFrame.lblConditionPic) {
			patient.addConditionPicture(" " + filepath);
			parentEditorFrame.drawPicture(patient.getConditionPictures()[patient.getConditionPictures().length - 1], 
					parentEditorFrame.lblConditionPic);
			parentEditorFrame.comboBox.setModel(parentEditorFrame.fetchComboBoxModel());
			parentEditorFrame.comboBox.setSelectedIndex(parentEditorFrame.comboBox.getItemCount() - 1);
		}
	}



}
