package View;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import model.DAO;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Login extends JFrame {

	private JPanel contentPane;
	private JTextField txtUsuario;
	private JPasswordField txtSenha;
	private JLabel lblStatus;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login frame = new Login();
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
	public Login() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowActivated(WindowEvent e) {
				status();
			}
		});
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("Usu\u00E1rio :");
		lblNewLabel.setBounds(50, 35, 60, 14);
		contentPane.add(lblNewLabel);
		
		txtUsuario = new JTextField();
		txtUsuario.setBounds(142, 32, 175, 20);
		contentPane.add(txtUsuario);
		txtUsuario.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Senha :");
		lblNewLabel_1.setBounds(50, 88, 46, 14);
		contentPane.add(lblNewLabel_1);
		
		txtSenha = new JPasswordField();
		txtSenha.setBounds(142, 85, 175, 20);
		contentPane.add(txtSenha);
		
		JButton btnEntrar = new JButton("Entrar");
		btnEntrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				logar();
			}
		});
		btnEntrar.setBounds(49, 171, 89, 23);
		contentPane.add(btnEntrar);
		
		lblStatus = new JLabel("New label");
		lblStatus.setIcon(new ImageIcon(Login.class.getResource("/icones/dbof.png")));
		lblStatus.setBounds(323, 175, 32, 32);
		contentPane.add(lblStatus);
	}// Fim do construtor >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	
	/**
	 * M�todo respons�vel pela exibi��o do status de conex�o
	 */
	private void status() {
		// Criar um objeto de nome DAO para acessar o m�tido na classe DAO
		DAO dao = new DAO();
		try {
			// Abrir a conex�o com o banco
			Connection con = dao.conectar();
			// A linha abaixo exibe o retorno da conex�o
			System.out.print(con);
			// Mudando o icone do rodap� no caso do banco de dados estar dispon�vel
			if (con == null) {
				lblStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/dbof.png")));
			} else {
				lblStatus.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icones/dbon.png")));
			}
			// IMPORTANTE! Sempre encerrar a conex�o
			con.close();
		} catch (Exception e) {
			System.out.print(e);
		}
	} //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
	DAO dao = new DAO();
	/**
	 * M�tido responsavel pela autentica��o do usu�rio/cliente
	 */
	private void logar() {
		if (txtUsuario.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Preenchar o Login", "Aten��o", JOptionPane.WARNING_MESSAGE);
			txtUsuario.requestFocus();
		} else if (txtSenha.getText().isEmpty()) {
			JOptionPane.showMessageDialog(null, "Preenchar a Senha", "Aten��o", JOptionPane.WARNING_MESSAGE);
			txtSenha.requestFocus();
		} else {
			try {
				String read = "select * from usuarios where login=? and senha=md5(?)";
				Connection con = dao.conectar();
				PreparedStatement pst = con.prepareStatement(read);
				pst.setString(1, txtUsuario.getText());
				pst.setString(2, txtSenha.getText());
				// A linha abaixo executa a Query(instru��o sql) armazenando o resultado no
				// objeto rs
				ResultSet rs = pst.executeQuery();
				// Se existir o login e senha correspondente entrar na tela principal
				if (rs.next()) {
					// Capturar o perfil do usu�rio
					String perfil = rs.getString(5);
					System.out.println(perfil);
									
					// Tratamento de perfil do usu�rio
					if (perfil.equals("Administrador")) {
						Assistencia assistencia = new Assistencia();
						assistencia.setVisible(true);
						// Liberar os bot�es
						assistencia.btnRelatorios.setEnabled(true);
						assistencia.btnUsuarios.setEnabled(true);
						// Ap�s o login, finalizar o JFrame
						this.dispose();
					} else {
						Assistencia assistencia = new Assistencia();
						assistencia.setVisible(true);
						// Ap�s o login, finalizar o JFrame
						this.dispose();												
					}
					} else {
					JOptionPane.showMessageDialog(null, "Login e/ou senha inv�lida", "Aten��o",
							JOptionPane.WARNING_MESSAGE);
					txtSenha.requestFocus();
					}
				con.close();
			} catch (Exception e) {
				System.out.println(e);
			}
		}
	}
}

