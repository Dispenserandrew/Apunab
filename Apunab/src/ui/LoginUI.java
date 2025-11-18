package ui;

import javax.swing.*;
import java.awt.*;
import service.UsuarioService;
import model.Usuario;

public class LoginUI extends JFrame {
    private UsuarioService usuarioService = new UsuarioService();

    public LoginUI() {
        setTitle("APUNAB - Login");
        setSize(480, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        getContentPane().setBackground(Color.WHITE);
        setLayout(null);

        // logo (data/logo_unab.png)
        ImageIcon logo = new ImageIcon("data/logo_unab.png");
        JLabel logoLabel = new JLabel(logo);
        logoLabel.setBounds(140, 20, 200, 200);
        add(logoLabel);

        JLabel title = new JLabel("Bienvenido a APUNAB", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setBounds(60, 230, 360, 30);
        add(title);

        JLabel lblUser = new JLabel("Usuario o correo:");
        lblUser.setBounds(60, 280, 360, 20);
        add(lblUser);

        JTextField txtUser = new JTextField();
        txtUser.setBounds(60, 305, 360, 34);
        add(txtUser);

        JLabel lblPass = new JLabel("Contraseña:");
        lblPass.setBounds(60, 350, 360, 20);
        add(lblPass);

        JPasswordField txtPass = new JPasswordField();
        txtPass.setBounds(60, 375, 360, 34);
        add(txtPass);

        JButton btnLogin = new JButton("Ingresar");
        btnLogin.setBounds(60, 430, 360, 40);
        btnLogin.setBackground(new Color(84, 167, 255));
        btnLogin.setForeground(Color.white);
        btnLogin.setFocusPainted(false);
        btnLogin.addActionListener(e -> {
            String u = txtUser.getText().trim();
            String p = new String(txtPass.getPassword());
            if (u.isEmpty() || p.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Completa usuario y contraseña");
                return;
            }
            Usuario usuario = usuarioService.login(u, p);
            if (usuario != null) {
                new MenuPrincipalUI(usuario).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Credenciales inválidas");
            }
        });
        add(btnLogin);

        JButton btnReg = new JButton("Crear cuenta");
        btnReg.setBounds(60, 480, 360, 34);
        btnReg.addActionListener(e -> {
            new RegistroUI().setVisible(true);
            dispose();
        });
        add(btnReg);
    }
}
